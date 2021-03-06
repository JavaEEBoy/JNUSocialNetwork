﻿var banner1 ;
// 社区页面添加活动
function activityClickEvent() {
  $('#createActivityBtn').click(function () {
    $('.activityForm').get(0).reset();
  });
  $("#go_submit").click(function () {
	  if($('#table_activitySign').text()!="无需报名"){
		  if ($('#activityTime').val() !== "" && $('#activityRemind').val() !== "") {
		      // 提交原图
		      banner1.submit();
		      var post = {
		        postType: 'ACTIVITY',
		        attributes: {
		          activityName: $('#activityName').val(),
		          startDate: toTimeValue($('#activityTime')
		            .val() + "") + "",
		          remindDate: toTimeValue($(
		            '#activityRemind').val() + "") + "",
		          activityTime: $('#activityTime').val(),
		          activityRemindTime: $('#activityRemind')
		            .val(),
		          activityAddr: $('#activityAddr').val(),
		          activityMore: $('#activityMore').val(),
		          inquery: $('#inquery').val(),
		          limitation: $('#activityNum').val(),
		          communityName: community.attributes.name,
		          communityID: community.ID.toString(),
		          ifUpload: $('#table_activitySign').text(),
		        },
		        imageLinks: [],
		        activityTypeTags: [$('#activityType').val()]
		      };
		      // 上传海报图的js控制
		      if ($('#fileuploadA').val() !== "") {
		        var target = $(".activityForm .banner-src");
		        var result = target.val();
		        if (!result) return;
		        post.attributes["background"] = result;
		      }
		      if ($('#fileuploadB').val() !== "") {
		        post.attributes["registerTemplateAddr"] = RegisterFormUpload(new FormData(
		          $('.regForm')[0]));
		      }
		      var diffDate = toTimeValue($('#activityTime').val() + "") - toTimeValue($('#activityRemind').val() + "");
		      if ($('.activityForm')[0].checkValidity()) {
		        if (diffDate >= 0.021 * 24 * 60 * 60 * 1000) {
		          var json = $.toJSON(post);
		          $('.layer2').fadeIn(300);
		          $('#infinite_loader2').fadeIn(300);
		          AddPostToCommunity(USERID, community.ID,
		            json);
		          $('#newActivity').get(0).reset();
		          $('.banner-wrapper').css("display","none");
		          $('#go_page1').click();
		          $('#fileuploadB').val("");
		          $('#activityCommunity').modal('hide');
		        } else {
		          $('#fail_popover2').fadeIn("fast");
		          setTimeout(
		            '$("#fail_popover2").fadeOut("slow")',
		            3000);
		        }
		      }
		    } else {
		      $('#fail_popover').fadeIn("fast");
		      setTimeout('$("#fail_popover").fadeOut("slow")',
		        3000);
		    }
	  }else{
		  if ($('#activityTime').val() !== "") {
		      // 提交原图
		      banner1.submit();
		      var post = {
		        postType: 'ACTIVITY',
		        attributes: {
		          activityName: $('#activityName').val(),
		          startDate: toTimeValue($('#activityTime')
		            .val() + "") + "",  
		          remindDate: "0",
		          activityRemindTime: "0",
		          activityTime: $('#activityTime').val(),
		          activityAddr: $('#activityAddr').val(),
		          activityMore: $('#activityMore').val(),
		          inquery: $('#inquery').val(),
		          limitation: $('#activityNum').val(),
		          communityName: community.attributes.name,
		          communityID: community.ID.toString(),
		          ifUpload: $('#table_activitySign').text(),
		        },
		        imageLinks: [],
		        activityTypeTags: [$('#activityType').val()]
		      };
		      // 上传海报图的js控制
		      if ($('#fileuploadA').val() !== "") {
		        var target = $(".activityForm .banner-src");
		        var result = target.val();
		        if (!result) return;
		        post.attributes["background"] = result;
		      }
		      if ($('.activityForm')[0].checkValidity()) {
		          var json = $.toJSON(post);
		          $('.layer2').fadeIn(300);
		          $('#infinite_loader2').fadeIn(300);
		          AddPostToCommunity(USERID, community.ID,
		            json);
		          $('#newActivity').get(0).reset();
		          $('.banner-wrapper').css("display","none");
		          $('#go_page1').click();
		          $('#fileuploadB').val("");
		          $('#activityCommunity').modal('hide');
		      }
		    } else {
		      $('#fail_popover').fadeIn("fast");
		      setTimeout('$("#fail_popover").fadeOut("slow")',
		        3000);
		    }
	  }
   

  });
}
// 活动页面添加活动
function activityClickAEvent() {

  $('body').on("click", '#createActivityABtn', function () {
    $('.activityForm').get(0).reset();
  });
  $('body')
    .on(
      "click",
      "#go_submit",
      function () {
          // 提交原图
          banner1.submit();
        if($('#table_activitySign').text()!='无需报名'){
        	if ($('#activityTime').val() !== "" && $('#activityRemind').val() !== "") {
                var post = {
                  postType: 'ACTIVITY',
                  attributes: {
                    activityName: $('#activityName').val(),
                    startDate: toTimeValue($('#activityTime')
                      .val() + "") + "",
                    remindDate: toTimeValue($(
                      '#activityRemind').val() + "") + "",
                    activityTime: $('#activityTime').val(),
                    activityRemindTime: $('#activityRemind')
                      .val(),
                    activityAddr: $('#activityAddr').val(),
                    activityMore: $('#activityMore').val(),
                    inquery: $('#inquery').val(),
                    limitation: $('#activityNum').val(),
                    communityName: ($.parseJSON(sessionStorage.getItem("user")).communityIDNameTuples)[0].name,
                    communityID: ($.parseJSON(sessionStorage.getItem("user")).communityIDNameTuples)[0].ID,
                    ifUpload: $('#table_activitySign').text(),
                  },
                  imageLinks: [],
                  activityTypeTags: [$('#activityType').val()]
                };
                if ($('#fileuploadA').val() !== "") {
                    var target = $(".activityForm .banner-src");
                    var result = target.val();
                    if (!result) return;
                    post.attributes["background"] = result;
                }
                if ($('#fileuploadB').val() !== "") {
                	var oMyForm = new FormData();
                	var ft=$('#fileuploadB')[0].files[0];
                	oMyForm.append("fileuploadB", ft);
                  post.attributes["registerTemplateAddr"] = RegisterFormUpload(oMyForm);
                }
                var diffDate = toTimeValue($('#activityTime').val() + "") - toTimeValue($('#activityRemind').val() + "");
                if ($('.activityForm')[0].checkValidity()) {
                  if (diffDate >= 0.021 * 24 * 60 * 60 * 1000) {
                    var json = $.toJSON(post);
                    $('.layer2').fadeIn(300);
                    $('#infinite_loader2').fadeIn(300);
                    AddPostToCommunity(USERID, ($.parseJSON(sessionStorage.getItem("user")).communityIDNameTuples)[0].ID,
                      json);
                    $('#newActivity').get(0).reset();
                    $('.banner-wrapper').css("display","none");
                    $('#go_page1').click();
                    $('#fileuploadB').val("");
                    $('#activityCommunity').modal('hide');
                  } else {
                    $('#fail_popover2').fadeIn("fast");
                    setTimeout(
                      '$("#fail_popover2").fadeOut("slow")',
                      3000);
                  }
                }
              } else {
                $('#fail_popover').fadeIn("fast");
                setTimeout('$("#fail_popover").fadeOut("slow")',
                  3000);
              }
        }else{
        	if ($('#activityTime').val() !== "") {
                var post = {
                  postType: 'ACTIVITY',
                  attributes: {
                    activityName: $('#activityName').val(),
                    startDate: toTimeValue($('#activityTime')
                      .val() + "") + "",
                    remindDate: "0",
    		        activityRemindTime: "0",
                    activityTime: $('#activityTime').val(),
                    activityAddr: $('#activityAddr').val(),
                    activityMore: $('#activityMore').val(),
                    inquery: $('#inquery').val(),
                    limitation: $('#activityNum').val(),
                    communityName: ($.parseJSON(sessionStorage.getItem("user")).communityIDNameTuples)[0].name,
                    communityID: ($.parseJSON(sessionStorage.getItem("user")).communityIDNameTuples)[0].ID,
                    ifUpload: $('#table_activitySign').text(),
                  },
                  imageLinks: [],
                  activityTypeTags: [$('#activityType').val()]
                };
                if ($('#fileuploadA').val() !== "") {
                    var target = $(".activityForm .banner-src");
                    var result = target.val();
                    if (!result) return;
                    post.attributes["background"] = result;
                }
               
                if ($('.activityForm')[0].checkValidity()) {
                    var json = $.toJSON(post);
                    $('.layer2').fadeIn(300);
                    $('#infinite_loader2').fadeIn(300);
                    AddPostToCommunity(USERID, ($.parseJSON(sessionStorage.getItem("user")).communityIDNameTuples)[0].ID,
                      json);
                    $('#newActivity').get(0).reset();
                    $('.banner-wrapper').css("display","none");
                    $('#go_page1').click();
                    $('#fileuploadB').val("");
                    $('#activityCommunity').modal('hide');
                }
              } else {
                $('#fail_popover').fadeIn("fast");
                setTimeout('$("#fail_popover").fadeOut("slow")',
                  3000);
              }
        }
        

      });
}
// 时间控件初始化
var date1 = new Date();
date1.setTime(date1.getTime() + 0.25 * 24 * 60 * 60 * 1000);
$('.form_datetime1').datetimepicker({
  // language: 'fr',
  format: "MM dd,yyyy - hh:ii",
  startDate: date1,
  todayBtn: 0,
  autoclose: 1,
  startView: 2,
  Integer: 1,
  forceParse: 0,
  showMeridian: 1,
  pickerPosition: "bottom-left"
});
var date2 = new Date();
date2.setTime(date2.getTime());
$('.form_datetime2').datetimepicker({
  // language: 'fr',
  format: "MM dd,yyyy - hh:ii",
  startDate: date2,
  todayBtn: 0,
  autoclose: 1,
  startView: 2,
  Integer: 1,
  forceParse: 0,
  showMeridian: 1,
  pickerPosition: "bottom-left"
});

function toTimeValue(dateTime) {
  var patt = /([a-zA-Z]{3,9})\s(\d{2}),(\d{4})\s-\s(\d{2}):(\d{2})/;
  var matchers = patt.exec(dateTime);
  var month = "NaN";
  switch (matchers[1]) {
  case "January":
    month = 0;
    break;
  case "February":
    month = 1;
    break;
  case "March":
    month = 2;
    break;
  case "April":
    month = 3;
    break;
  case "May":
    month = 4;
    break;
  case "June":
    month = 5;
    break;
  case "July":
    month = 6;
    break;
  case "August":
    month = 7;
    break;
  case "September":
    month = 8;
    break;
  case "October":
    month = 9;
    break;
  case "November":
    month = 10;
    break;
  case "December":
    month = 11;
    break;
  }
  var d = new Date(matchers[3], month, matchers[2], matchers[4], matchers[5],
    0, 0);
  return d.valueOf();

}
// 创建活动“下一步按钮”
$(document).ready(function () {
  var easingtime = 300;
  var go_page = [$('#go_page1'), $('#go_page2'),
       $('#go_page3')];
  var page = [$('#page1'), $('#page2'), $('#page3')];
  var go_pre = $('#go_pre');
  var go_next = $('#go_next');
  var go_submit = $('#go_submit');
  var current_page = 0;
  go_page[0].click(function (event) {
    page[2].animate({
      left: '700px'
    }, easingtime);
    page[1].animate({
      left: '700px'
    }, easingtime);
    page[0].animate({
      left: '0px'
    }, easingtime);
    go_pre.css('display', 'none');
    go_next.css('display', '');
    go_submit.css('display', 'none');
    current_page = 0;
  });
  go_page[1].click(function (event) {
    page[0].animate({
      left: '-700px'
    }, easingtime);
    page[1].animate({
      left: '10px'
    }, easingtime);
    page[2].animate({
      left: '700px'
    }, easingtime);
    go_pre.css('display', '');
    go_next.css('display', '');
    go_submit.css('display', 'none');
    current_page = 1;
  });
  go_page[2].click(function (event) {
    page[0].animate({
      left: '-700px'
    }, easingtime);
    page[1].animate({
      left: '-700px'
    }, easingtime);
    page[2].animate({
      left: '0px'
    }, easingtime);
    go_pre.css('display', '');
    go_next.css('display', 'none');
    go_submit.css('display', '');
    current_page = 2;
  });
  go_pre.click(function (event) {
    if (current_page > 0) {
      go_page[--current_page].click();
    }
    if(current_page==0){
    	$('.modalBody').css({'overflow-y':'auto'});
    }
    
  });
  go_next.click(function (event) {
	  if (current_page == 0 && $("#optionsRadios2").is(':checked') && !/.*\.xls|.xlsx|.doc|.docx|.wps|.et$/.test($('#fileuploadB').val())) {
            $('#fail_popover3').css("display", "block");
            setTimeout('$("#fail_popover3").fadeOut("slow")',
              3000);
            return;
      } else if (current_page == 1 && (!$('.activityForm')[0].checkValidity() || ($('#div_activityRemind').css('display')=='block' && toTimeValue($('#activityTime').val() + "") - toTimeValue($(
          '#activityRemind')
        .val() + "") < 0.021 * 24 * 60 * 60 * 1000))) {
        go_submit.click();
        return;
      } else if (current_page < page.length - 1) {
    	if($('#optionsRadios3').is(':checked')){
    		$('#div_activityRemind').hide();
    	}
    	else{
    		$('#div_activityRemind').show();
    	}
    	  
        go_page[++current_page].click();
        if (current_page == 2) {
          $('#table_activityName').html(
            $('#activityName').val());
          $('#table_activityTime').html(
            $('#activityTime').val());
          $('#table_activityRemind').html(
        		  $('#optionsRadios3').is(
                  ':checked') ? '无需提醒' :
            $('#activityRemind').val());
          $('#table_activityAddr')
            .html(
              $('#activityAddr')
              .val().length > 30 ? $(
                '#activityAddr')
              .val()
              .substring(0,
                30) + '......' : $(
                '#activityAddr')
              .val());
          $('#table_activityMore')
            .html(
              $('#activityMore')
              .val().length > 30 ? $(
                '#activityMore')
              .val()
              .substring(0,
                30) + '......' : $(
                '#activityMore')
              .val());
          $('#table_activityNum').html(
            $('#activityNum').val());
          $('#table_activitySign').html(
            $('#optionsRadios1').is(
              ':checked') ? '默认方式' : $('#optionsRadios2').is(
              ':checked') ? '上传报名表' : '无需报名');
          $('#table_fileuploadA')
            .html(
              $('#fileuploadA').val() === '' ? '否' : $(
                '#fileuploadA')
              .val());
          $('#table_fileuploadB')
            .html(
              $('#fileuploadB').val() === '' ? '否' : $(
                '#fileuploadB')
              .val());
        }
      }
    });
  $('#optionsRadios1').click(function (event) {
    $('#div_fileuploadB').fadeOut(easingtime);
    $('.docI').fadeOut(easingtime);
  });
  $('#optionsRadios2').click(function (event) {
    $('#div_fileuploadB').fadeIn(easingtime);
    $('.docI').fadeIn(easingtime);
  });
  $('#optionsRadios3').click(function (event) {
	    $('#div_fileuploadB').fadeOut(easingtime);
	    $('.docI').fadeOut(easingtime);
	  });
  banner1 = new CropBanner($("#activityCommunity"), {
    aspectRatio: 3.8,
    imgPreferredSize: 5
  });
});