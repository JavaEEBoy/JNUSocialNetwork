/*global define, jQuery,console,window,FileReader,alert,FormData */
(function (factory) {
  if (typeof define === "function" && define.amd) {
    define(["jquery"], factory);
  } else {
    factory(jQuery);
  }
})(function ($) {

  "use strict";

  var log = function (o) {
    try {
      console.log(o);
    } catch (e) {}
  };

  // 构造CropAvatar
  function CropAvatar($element, options) {
    // 默认参数
    this.defaults = {
      aspectRatio: 1,
      // unit=MB
      imgPreferredSize: 2,
      imgUrlAttrName: 'avatarLink',
      targetView: ".profile_user_img"
    };

    this.setDefaults(options);

    this.$container = $element;

    this.$avatarView = this.$container.find(this.defaults.targetView); // to be modified
    this.$avatar = this.$avatarView.find("img");
    this.$avatarModal = this.$container.find(".modal"); // to be modified
    this.$loading = this.$container.find(".loading");

    this.$avatarForm = this.$avatarModal.find(".avatar-form");
    this.$avatarUpload = this.$avatarForm.find(".avatar-upload");
    this.$avatarSrc = this.$avatarForm.find(".avatar-src");
    this.$avatarData = this.$avatarForm.find(".avatar-data");
    this.$avatarInput = this.$avatarForm.find(".avatar-input");
    this.$avatarSave = this.$avatarForm.find(".avatar-save");

    this.$avatarWrapper = this.$avatarModal.find(".avatar-wrapper");
    this.$avatarPreview = this.$avatarModal.find(".avatar-preview");


    this.init();
    log(this);
  }


  CropAvatar.prototype = {
    constructor: CropAvatar,

    // 合并传入的参数
    setDefaults: function (options) {
      $.extend(this.defaults, options);
    },

    // 获取文件处理对象支持信息
    support: {
      fileList: !!$("<input type=\"file\">").prop("files"),
      fileReader: !!window.FileReader
    },

    // 总体初始化
    init: function () {
      this.support.datauri = this.support.fileList && this.support.fileReader;

      this.initModal();
      this.addListener();
    },

    // 初始化模态框
    initModal: function () {
      this.$avatarModal.modal("hide");
      this.initPreview();
    },

    // 初始化预览窗口
    initPreview: function () {
      var url = this.$avatar.attr("src");
      this.$avatarPreview.empty().html('<img src="' + url + '">');
    },

    // 添加监听器，并利用代理改变回调函数作用域
    addListener: function () {
      this.$avatarInput.on("change", $.proxy(this.change, this));
      this.$avatarForm.on("submit", $.proxy(this.submit, this));
    },

    // input[type=file]变更监听，主要验证图片是否符合规定，再读取文件信息
    change: function () {
      var files, file;

      if (this.support.datauri) {
        files = this.$avatarInput.prop("files");

        if (files.length > 0) {
          file = files[0];

          if (this.isImageFile(file) && this.validateImgSize(file)) {
            this.read(file);
          }
        }
      } else {
        this.alert("此浏览器不支持文件上传，请更换浏览器！推荐使用Chrome^_^！");
      }
    },

    // 检查图片类型
    isImageFile: function (file) {
      if (file.type) {
        return /^image\/\w+$/.test(file.type);
      } else {
        return /\.(jpg|jpeg|png|gif)$/.test(file);
      }
    },

    // 验证图片大小
    validateImgSize: function (file) {
      if (file.size > this.defaults.imgPreferredSize * 1024 * 1024) {
        alert("file size overflow");
        return false;
      } else {
        return true;
      }
    },

    // 读取图片，并开始crop
    read: function (file) {
      var _this = this,
        fileReader = new FileReader();

      fileReader.readAsDataURL(file);

      fileReader.onload = function () {
        _this.url = this.result;
        _this.startCropper();
      };
    },

    // 插入图像并为其开启cropper插件
    startCropper: function () {
      var _this = this;

      if (this.active) {
        this.$img.cropper("setImgSrc", this.url);
      } else {
        this.$img = $('<img src="' + this.url + '">');
        this.$avatarWrapper.empty().html(this.$img);
        this.$img.cropper({
          aspectRatio: this.defaults.aspectRatio,
          preview: this.$avatarPreview.selector,
          done: function (data) {
            log("crop-data: " + data);
            _this.$avatarData.val(JSON.stringify(data));
          }
        });

        this.active = true;
      }
    },

    // 关闭cropper
    stopCropper: function () {
      if (this.active) {
        this.$img.cropper("disable");
        this.$img.data("cropper", null).remove();
        this.active = false;
      }
    },

    // 上传成功后，重置表单数据,并关闭cropper
    cropDone: function () {
      this.$avatarSrc.val("");
      this.$avatarData.val("");
      this.$avatar.attr("src", this.url);
      this.stopCropper();
      this.$avatarModal.modal("hide");
    },

    // 上传处理
    ajaxUpload: function () {
      var url = this.$avatarForm.attr("action"),
        data = new FormData(this.$avatarForm[0]),
        _this = this;

      $.ajax(url, {
        type: "post",
        data: data,
        processData: false,
        contentType: false,

        beforeSend: function (request) {

          request.setRequestHeader("ID", USERID);

          _this.submitStart();
        },

        success: function (data) {
          _this.submitDone(data);
        },

        error: function (XMLHttpRequest, textStatus, errorThrown) {
          _this.submitFail(textStatus || errorThrown);
        },

        complete: function () {
          _this.submitEnd();
        }
      });
    },

    // 表格提交操作
    submit: function () {
      this.ajaxUpload();
      // 已使用异步提交，阻止表单提交
      return false;
    },

    // 开始提交，显示loading
    submitStart: function () {
      this.$loading.fadeIn();
    },

    // 提交完成，处理返回信息
    submitDone: function (data) {
      log(data);

      try {
        data = $.parseJSON(data);
      } catch (e) {}

      if (data) {
        if (data.croppedImage) {
          this.url = $.parseJSON(data.croppedImage).src;

          log("response url = " + this.url);

          if (this.support.datauri || this.uploaded) {
            this.uploaded = false;

            // 更新用户信息
            var datajson = {};
            datajson[this.defaults.imgUrlAttrName] = JSON.stringify(data);
            UpdateUserProfile(userID, JSON.stringify(datajson));

            this.cropDone();
          } else {
            this.uploaded = true;
            this.$avatarSrc.val(this.url);
            this.startCropper();
          }

          this.$avatarInput.val("");

        } else if (data.message) {
          this.alert(data.message);
        }
      } else {
        this.alert("Failed to response");
      }
    },

    // 提交失败
    submitFail: function (msg) {
      this.alert(msg);
    },

    // 提交结束，loading消失
    submitEnd: function () {
      this.$loading.fadeOut();
    },

    // 信息警报
    alert: function (msg) {
      var $alert = [
            '<div class="alert alert-danger avatar-alert">',
              '<button type="button" class="close" data-dismiss="alert">&times;</button>',
              msg,
            '</div>'
          ].join("");

      this.$avatarUpload.after($alert);
    }
  };

  window.CropAvatar = CropAvatar;

  //    $(function () {
  //        var example = new CropAvatar($("#crop-avatar")
  //            /*, {
  //            aspectRatio: 2.067,
  //            imgPreferredSize: 5
  //        }*/
  //        );
  //    });
});