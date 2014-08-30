//function Msnry
function Msnry(selectContain, item, width) {
	var container = document.querySelector(selectContain);
	imagesLoaded(container, function() {
		msnry = new Masonry(container, {
			columnWidth : width,
			itemSelector : item,
			gutter : 20
		});
	});
}

function post(ownerID, ownerNickName, publishDate, content, postID, likers,
		collecters, srcImage, ownerImage) {
	var likeClass = "glyphicon glyphicon-heart-empty";

	if ($.inArray(USERID, likers) != -1) {
		likeClass = "glyphicon glyphicon-heart";
	}
	/*
	 * var collectClass = "glyphicon glyphicon-star-empty"; if
	 * ($.inArray(USERID, collecters) != -1) { collectClass = "glyphicon
	 * glyphicon-star"; }
	 */
	var pRemoveBtn = "";
	if (USERID == ownerID) {
		pRemoveBtn = "<div class='deletePostBtn'><input id='deleteID' type='hidden' value="
				+ postID
				+ " /><span class='glyphicon glyphicon-remove'></span></div>";
	}
	var postImgDiv = "<div class='post_img' id='postImg" + postID + "'>";
	var imageDiv = "";
	if (srcImage.length != 0) {
		$
				.each(
						srcImage,
						function(n, image) {
							imageDiv = imageDiv
									+ "<img class='postimg' onload='javascript:auto_resize(400, 250, this)' onclick='showPost("
									+ postID + ")' src='" + image + "'/>";
						});
		postImgDiv = postImgDiv + imageDiv + "</div>";
	} else {
		postImgDiv = "";
	}
	var readmore = "";
	var contentD = content;
	if (content.length > 100) {
		readmore = "<div class='post_more' id='" + postID
				+ "' ><a style='cursor:pointer'>read more</a></div>";
		contentD = content.substr(0, 100) + "......";
	}
	var boarddiv = "<div class='post "
			+ postID
			+ "'><div class='post_body'><div class='row'><div class='col-md-2'><div class='user_img'><img class='img-circle userImg' onload='javascript:auto_resize(50, 50, this)' src='"
			+ ownerImage
			+ "' style='display: none'/><input type='hidden' value='"
			+ ownerID
			+ "' name='userID'/></div></div><div class='col-md-6'><div class='user_name'><strong>"
			+ ownerNickName
			+ "</strong></div><div class='user_info'>"
			+ publishDate
			+ "</div></div><div class='col-md-4'>"
			+ pRemoveBtn
			+ "</div></div><div class='post_info'><span class='postContent'>"
			+ contentD
			+ "</span>"
			+ readmore
			+ "</div>"
			+ postImgDiv
			+ "<div class='row'><div class='col-md-1'><div class='post_like' style='cursor:pointer'><a><p id='ownerID' style='display:none;' value="
			+ ownerID
			+ "></p><input id='likeID' type='hidden' value="
			+ postID
			+ ">"
			+ "<span id='likeShow' class='"
			+ likeClass
			+ "' style='font-size:20px'>"
			+ likers.length
			+ "</span></a></div></div><div class='col-md-1'></div><div class='col-md-1'></div></div></div></div>";
	/*
	 * <div class='post_collect' style='cursor:pointer'><a><input
	 * id='collectID' type='hidden' value=" + postID + "><span class='" +
	 * collectClass + "' style='font-size:20px'></span></a></div>
	 */
	$("#commentText" + postID).blur(function() {
		$(this).attr("placeholder", "add a comment");
	});
	$('.act_content').find('a').hide();
	$('.act_content').hover(function() {

		$(this).find('a').fadeIn(300);
	}, function() {
		$(this).find('a').fadeOut(300);
	});

	return boarddiv;
}
// function addDiv
function addPost(ownerID, ownerNickName, publishDate, content, postID, likers,
		collecters, srcImage, ownerImage) {
	var boarddiv = post(ownerID, ownerNickName, publishDate, content, postID,
			likers, collecters, srcImage, ownerImage);
	$(".share").after(boarddiv);
	$('img.userImg').userTips();
	Msnry('.pro_body', '.post', 435);

}
// function hovercommentDeleteBtn

$('.act_content').hover(function() {
	$('.changeBtnGroup').hide();
	$('.changeBtnGroup').fadeIn(300);
}, function() {
	$('.changeBtnGroup').fadeOut(300, function() {
		$(this).remove();
	});
});

// fetchPostByIDs
function fetchPostByIDs(container) {
	var response = FetchPostByIDs(container);
	$.each(response, function(n, dataString) {
		addPost(dataString.owner.ID, dataString.owner.attributes.name,
				dataString.publishDate, dataString.attributes.content,
				dataString.ID, dataString.likerIDs, dataString.collectorIDs,
				dataString.imageLinks, dataString.owner.attributes.avatarLink);
	});
}

// function clickFuntion
function clickEvent() {
	// function collectPost and cancelCollcet
	$('body')
			.on(
					'click',
					'.post_collect',
					function() {
						var id = $(this).find("input").attr("value");
						if ($(this).find("span").attr("class") == "glyphicon glyphicon-star-empty") {
							CollectPost("2011052406", id);
							var inputID = $("input[value='" + id
									+ "'][id='collectID']");
							inputID.next().attr("class",
									"glyphicon glyphicon-star");
							return 0;
						}
						if ($(this).find("span").attr("class") == "glyphicon glyphicon-star") {
							CancelCollectPost("2011052406", id);
							var inputID = $("input[value='" + id
									+ "'][id='collectID']");
							inputID.next().attr("class",
									"glyphicon glyphicon-star-empty");
							return 0;
						}
					});
	$(document).ready(
			function() {
				$('.act_content').find('a').hide();
				$('.act_content').hover(function() {

					$(this).find('a').fadeIn(300);
				}, function() {
					$(this).find('a').fadeOut(300);
				});
				$('body').on(
						"click",
						".tipUser",
						function() {
							window.location.href = 'profile.jsp?nav=post&'
									+ sessionStorage.getItem("otherUserID");
						});
			});
	// function likePost and cancelLike
	$('body')
			.on(
					'click',
					'.post_like',
					function() {
						var id = $(this).find("input").attr("value");
						if ($(this).find("span").attr("class") == "glyphicon glyphicon-heart-empty") {
							LikePost(USERID, id);
							var inputID = $("input[value='" + id
									+ "'][id='likeID']");
							inputID.next().attr("class",
									"glyphicon glyphicon-heart");
							return 0;
						}
						if ($(this).find("span").attr("class") == "glyphicon glyphicon-heart") {
							CancelLikePost("2011052406", id);
							var inputID = $("input[value='" + id
									+ "'][id='likeID']");
							inputID.next().attr("class",
									"glyphicon glyphicon-heart-empty");
							return 0;
						}
					});
	// reply comment
	$('body').on("click", ".comment_reply", function() {
		var postID = $(this).attr("id");
		var commmentName = $(this).find("input[id='replyName']").attr("value");
		var commentID = $(this).find("input[id='replyID']").attr("value");
		sessionStorage.setItem("commentOwnerName", commmentName);
		sessionStorage.setItem("commentID", commentID);
		var inputID = $("input[id='commentText" + postID + "']");
		inputID.attr("placeholder", "@" + commmentName);
		inputID.focus();
	});
	// function likecomment and cancelLike
	$('body').on('click', '.comment_like', function() {
		var id = $(this).find("input").attr("value");
		if ($(this).find("a").css("color") == "rgb(90, 90, 90)") {
			LikeComment(USERID, id);
			var inputID = $("input[value='" + id + "'][id='likeID']");
			inputID.parents("a").css("color", "rgb(255, 255, 255)");
			inputID.parents("a").css("background-color", "rgb(66,139,202)");
			return 0;
		}
		if ($(this).find("a").css("color") == "rgb(255, 255, 255)") {
			CancelLikeComment(USERID, id);
			var inputID = $("input[value='" + id + "'][id='likeID']");

			inputID.parents("a").css("color", "rgb(90, 90, 90)");
			inputID.parents("a").css("background-color", "rgb(255, 255, 255)");
			return 0;
		}
	});
	// function Activity
	$('body').on('click', '.activityJoin', function() {
		var activityID = $(this).attr("id");
		var response = JoinActivity(USERID, activityID);
		if (response == 'success') {
			alert("参加成功！");
		}
	});
	$('body').on('click', '.leaveactivityJoin', function() {
		var activityID = $(this).attr("id");
		LeaveActivity(USERID, activityID);
	});

	// function addComment
	$('body').on('click', '#addComment', function() {
		var id = this.getAttribute("value");
		var commentID = "";
		var commentOwnerName = "";
		if (sessionStorage.getItem("commentID") != null) {
			commentID = sessionStorage.getItem("commentID");
		}
		if (sessionStorage.getItem("commentOwnerName") != null) {
			commentOwnerName = sessionStorage.getItem("commentOwnerName");
		}
		var inputComm = $("input[id='commentText" + id + "']");
		var comment = {
			attributes : {
				content : inputComm.val(),
				toCommentID : commentID,
				commentToComment : commentOwnerName,
				postID : id + ""
			}
		};
		var commentJson = $.toJSON(comment);
		AddComment(USERID, id, commentJson);
		sessionStorage.setItem("commentOwnerName", "");
		sessionStorage.setItem("commentID", "");
		inputComm.val("");
	});
	// function follow cancelfollow
	$('body').on('click', '#followBtn', function() {
		// get post owner
		var id = $('.popTip').attr('id');
		if ($(this).text() == "Follow") {
			Follow(USERID, id);
		}
		if ($(this).text() == "Following") {
			CancelFollow(USERID, id);
			var followBtn = $("button[id='followBtn']");
			followBtn.text("Follow");
		}
	});
	$('body').on('click', '.deleteCommBtn', function() {
		var commentID = $(this).find("input").attr("value");
		var postID = $(this).find("input").attr("id");
		DeleteComment(postID, commentID);
	});
	$('body').on("click", ".editCommunity", function() {
		$('#communityName').val(community.attributes.name);
		$('#communityIntro').val(community.attributes.introduct);

	});
	$('body').on("click", "#leaveCommunityBtn", function() {
		LeaveCommunity(USERID, communityID);
	});
	$('body').on("click", "#deleteCommunityBtn", function() {
		DeleteCommunity(communityID);
	});

	$('body').on("click", "#saveCommunity", function() {
		var card = community.attributes.communityCard;
		if ($('#fileuploadEdit').val() != "") {
			card = FileUpload(new FormData($('.editCommunityForm')[0]))[0];
		} else {

		}
		var attributes = {
			name : $('#communityName').val(),
			introduct : $('#communityIntro').val(),
			communityCard : card
		};
		var json = $.toJSON(attributes);
		var c = UpdateCommunity(community.ID, json);
		$('#editCommunity').modal('hide');
		$('.cName').html(c.attributes.name);
		$('.cIntro').html(c.attributes.introduct);
		$('.communityPic').find('img').attr("src", card);
		$('.editCommunityForm').get(0).reset();
	});
	if ($.parseJSON(sessionStorage.getItem("user")).userType == 'COMMUNITYOWNER') {
		$('#editCommunityBtn').css("display", "inline");
		$('#editMembersBtn').css("display", "inline");
		$('#deleteCommunityBtn').css("display", "inline");
		$('.editActivity').css("display", "inline");
	}

	$(document)
			.click(
					function(e) {
						var drag = $(".mentionBody"), dragel = $(".mentionBody")[0], target = e.target, arrow = $("#arrowBack")[0];
						if (dragel != target && !$.contains(dragel, target)
								&& arrow != target) {
							drag.fadeOut(300);
						}
					});
	$('body').on("click", "#editMembersBtn", function() {
		window.location.href = 'communityMember.jsp?' + community.ID;
	});

}
function clickOffEvent() {
	$('.Btnshare').click(function(e) {
		e.preventDefault();
		$(this).attr("data-toggle", "");
		alert("Sign In");
	});
	$('.share_txt').click(function(e) {
		e.preventDefault();
		$(this).attr("data-toggle", "");
		alert("Sign In");
	});
	$('.post_collect').click(function(e) {
		e.preventDefault();
		alert("Sign In");
	});
	$('.post_like').click(function(e) {
		e.preventDefault();
		alert("Sign In");
	});
	$('.comment_like').click(function(e) {
		e.preventDefault();
		alert("Sign In");
	});
	$('.activityJoin').click(function(e) {
		e.preventDefault();
		alert("Sign In");
	});
	$('.leaveactivityJoin').click(function(e) {
		e.preventDefault();
		alert("Sign In");
	});
	$('#addComment').click(function(e) {
		e.preventDefault();
		alert("Sign In");
	});
	$('#followBtn').click(function(e) {
		e.preventDefault();
		alert("Sign In");
	});
	$('#deleteCommBtn').click(function(e) {
		e.preventDefault();
		alert("Sign In");
	});
	$('#communityCreate').click(function(e) {
		e.preventDefault();
		alert("Sign In");
	});
	$('.content_join').click(function(e) {
		e.preventDefault();
		alert("Sign In");
	});
	$('#activityCreate').click(function(e) {
		e.preventDefault();
		alert("Sign In");
	});
	$('.pinCommon').click(function(e) {
		e.preventDefault();
		alert("Sign In");
	});
	$('.pCampus').click(function(e) {
		e.preventDefault();
		alert("Sign In");
	});
	$('.pSeason').click(function(e) {
		e.preventDefault();
		alert("Sign In");
	});
	$('.pMajor').click(function(e) {
		e.preventDefault();
		alert("Sign In");
	});
	$('。pClass').click(function(e) {
		e.preventDefault();
		alert("Sign In");
	});
	$('.addphotoBtn').click(function(e) {
		$(this).attr("data-toggle", "");
		alert("Sign In");
	});
	$('.aSavebtn').css("display", "none");
	$('.aEditbtn').css("display", "none");
	$('.createCom').click(function(e) {
		$(this).attr("data-toggle", "");
		alert("Sign In");
	});
	$('.share_txt').attr("readonly", "readonly");

}
(function($) {
	$.fn.userTips = function() {
		// Speed of the animations in milliseconds - 1000 =
		// 1 second.
		var animSpeed = 300;
		var tinyTip;
		// When we hover over the element that we want the
		// tooltip applied to
		$(this)
				.hover(
						function() {
							var pos = $(this).offset();
							var nPos = pos;
							nPos.top = pos.top + 20;
							nPos.left = pos.left + 40;
							var userid = $(this).next().val();
							var data = FetchUserByID(userid);
							var followTxt = "Follow";
							if ($.inArray(USERID, data.followerIDs) != -1) {
								followTxt = "Following";
							}
							if (USERID == userid) {
								followTxt = "Yourself";
							}
							sessionStorage.setItem("otherUserID", data.ID);
							var chatSpan = '<span  class="glyphicon glyphicon-comment" id="chatCreate" style="font-size:24px;color:#d7d7d7;"></span>';
							if (data.ID == USERID) {
								chatSpan = "";
							}
							if (data != "") {
								var tipFrame = '<div id="'
										+ data.ID
										+ '" class="popTip"><div class="content"><div class="urserBgShort"><img onload="javascript:auto_resize(240, 135, this)" src="'
										+ data.attributes.profileImageLink
										+ '" style="display: none"/></div><div class="urserInfShort"><div class="userInImg"><img onload="javascript:auto_resize(120, 120, this)"  src="'
										+ data.attributes.avatarLink
										+ '" style="display: none"/></div><p><h1><a class="tipUser">'
										+ data.attributes.name
										+ '</a></h1></p><p>'
										+ data.attributes.lookingFor
										+ '</p><button class="btn btn-danger" id="followBtn">'
										+ followTxt + '</button></div>'
										+ chatSpan + '</div></div>';
								$('body').append(tipFrame);
								var divTip = 'div.popTip';
								tinyTip = $(divTip);
								tinyTip.hide();
								tinyTip.css('position', 'absolute').css(
										'z-index', '1000');
								tinyTip.css(nPos).fadeIn(animSpeed);
								tinyTip.hover(function() {
									clearTimeout(window.timer);
								}, function() {
									tinyTip.fadeOut(animSpeed, function() {
										$(this).remove();
									});
								});
								var chat = '';
								$('.userInImg').after(chat);
								if (USERID != null && USERID != "") {
									$("span#chatCreate")
											.click(
													function() {
														open_chatroom(
																USERID,
																sessionStorage
																		.getItem("otherUserID"),
																data.attributes.name);
													});
								}
								/*
								 * $('.userInImg').hover(function(){
								 * $('#chatCreate').fadeIn(300);
								 * $('#chatCreate').css("display","inline");
								 * },function(){ $('#chatCreate').fadeOut(300);
								 * });
								 */
							}
						}, function() {
							window.timer = setTimeout(function() {
								tinyTip.fadeOut(300, function() {
									$(this).remove();
								});
							}, 200);

						});

	};
})(jQuery);

function showPost(postID) {
	var response = FetchCommentByPost(postID, "0", "20");
	var comment = "";
	$
			.each(
					response,
					function(index, jsonComment) {
						var atComment = "";
						if (jsonComment.attributes.commentToComment != "") {
							atComment = "@"
									+ jsonComment.attributes.commentToComment;
						}
						var removeBtn = "";
						var commentReply = "<div class='comment_reply' id="
								+ postID
								+ " style='cursor: pointer'><a><input id='replyName' type='hidden' value='"
								+ jsonComment.owner.attributes.name
								+ "' /><input id='replyID' type='hidden' value='"
								+ jsonComment.ID
								+ "' />reply<span style='font-size: 8px'></span></a></div>";
						if (USERID == jsonComment.owner.ID) {
							removeBtn = "<div class='deleteCommBtn' style='cursor:pointer'><a><input id='"
									+ postID
									+ "' type='hidden' value='"
									+ jsonComment.ID
									+ "' /><span class='glyphicon glyphicon-remove' style='font-size: 8px'></span></a></div>";
							commentReply = "";
						}
						comment = comment
								+ "<div class='act_content' id='"
								+ jsonComment.ID
								+ "'><div class='row'><div class='col-lg-1'><img onload='javascript:auto_resize(30, 30, this)' src='"
								+ jsonComment.owner.attributes.avatarLink
								+ "' style='display: none'/></div><div class='col-lg-10 cus-lg-10'><div class='row'><div class='col-lg-5 custom_lg-6'><div class='user_name'><strong>"
								+ jsonComment.owner.attributes.name
								+ "</strong></div></div><div class='col-lg-6 custom_lg-6'>"
								+ removeBtn
								+ "</div></div><div class='row'><div class='col-lg-7 custom_lg-6'><div class='user_info'>"
								+ jsonComment.publishDate
								+ "</div></div><div class='col-lg-2 custom_lg-6'><div class='comment_like' style='cursor: pointer'><div class='likeComment likeCommentN"
								+ jsonComment.ID
								+ "'>+<span>"
								+ jsonComment.likerIDs.length
								+ "</span></div><a><input id='likeID' type='hidden' value='"
								+ jsonComment.ID
								+ "' />+1<span style='font-size: 8px'></span></a></div></div><div class='col-lg-2'>"
								+ commentReply
								+ "</div></div></div></div><div class='act_comment'><span class='commentHead'>"
								+ atComment + "</span>" + "&nbsp;"
								+ jsonComment.attributes.content
								+ "﻿</div></div>";
					});

	var dataString = FetchPostByID(postID);
	var likeClass = "glyphicon glyphicon-heart-empty";

	if ($.inArray(USERID, dataString.likerIDs) != -1) {
		likeClass = "glyphicon glyphicon-heart";
	}

	$('.act_content').find('a').hide();
	$('.act_content').hover(function() {

		$(this).find('a').fadeIn(300);
	}, function() {
		$(this).find('a').fadeOut(300);
	});
	layer
			.photos({
				html : "<div class='showPost'><div class='row'><div class='col-md-3'><div class='user_img'><img class='userImg' onload='javascript:auto_resize(50, 50, this)' src='"
						+ dataString.owner.attributes.avatarLink
						+ "' style='display: none'/><input type='hidden' value='"
						+ dataString.owner.ID
						+ "' name='userID'/></div></div><div class='col-md-8'><div class='user_name'><strong>"
						+ dataString.owner.attributes.name
						+ "</strong></div><div class='user_info'>"
						+ dataString.publishDate
						+ "</div></div></div><div class='post_info'><span class='postContent'>"
						+ dataString.attributes.content
						+ "</span></div><div class='row'><div class='col-md-1'><div class='post_like' style='cursor:pointer'><a><p id='ownerID' style='display:none;' value="
						+ dataString.owner.ID
						+ "></p><input id='likeID' type='hidden' value="
						+ postID
						+ ">"
						+ "<span id='likeShow' class='"
						+ likeClass
						+ "' style='font-size:20px'>"
						+ dataString.likerIDs.length
						+ "</span></a></div></div><div class='col-md-1'></div><div class='col-md-1'></div></div><div class='media_comm'><div class='row addCommentBtn'><div class='col-lg-8'><div class='form-group'><input type='text' placeholder='Add a comment' class='form-control  commentTxt' id='commentText"
						+ postID
						+ "' maxLength='20'></div></div><div class='col-lg-4'><button type='submit' class='btn btn-success' id='addComment' value="
						+ postID
						+ ">Submit</button></div></div>"
						+ comment
						+ "</div></div>",
				page : {
					parent : '#postImg' + postID,
					title : '',
					start : 0
				}
			});
}
// funtion sessionID
$('body').on("click", ".activityHref", function() {
	window.location.href = 'activity.jsp?' + community.ID;
});
$('body').on("click", ".memberHref", function() {
	window.location.href = 'communityMember.jsp?' + community.ID;
});

/**
 * auto_resize
 */
function auto_resize(maxWidth, maxHeight, srcImage) {
	var image = new Image();
	image.src = srcImage.src;
	if (image.width > maxWidth && image.height <= maxHeight) {
		image.width = maxWidth;
		image.height = (maxHeight / maxWidth) * image.width;
	} else if (image.height > maxHeight && image.width <= maxWidth) {
		image.height = maxHeight;
		image.width = (maxWidth / maxHeight) * image.height;
	} else if (image.height > maxHeight && image.width > maxWidth) {
		var intervalWidth = image.width - maxWidth;
		var intervalHeight = image.height - maxHeight;
		if (intervalWidth >= intervalHeight) {
			image.width = maxWidth;
			image.height = (maxHeight / maxWidth) * image.width;
		} else {
			image.height = maxHeight;
			image.width = (maxWidth / maxHeight) * image.height;
		}
	}

	srcImage.width = image.width;
	srcImage.height = image.height;
	$(srcImage).fadeIn("fast");
}

function fixed_width_auto_resize(width, srcImage) {
	var image = new Image();
	image.src = srcImage.src;
	var originalWidth = srcImage.width;
	var originalHeight = srcImage.height;
	srcImage.width = width;
	srcImage.height = (width / originalWidth) * originalHeight;
	$(srcImage).fadeIn("fast");
}