function communityClickEvent() {
	$('body').on('click', '#communityCreate', function() {
		var communityC;
		if ($('#fileupload').val() != "") {
			communityC = FileUpload(new FormData($('.communityForm')[0]))[0];
		} else {
			communityC = "";
		}
		var community = {
			tags : [],
			attributes : {
				name : $('#communityName').val(),
				introduct : $('#communityIntro').val(),
				communityCard : communityC,
				userID : USERID
			},
			communityType : $('#communityType').val()
		};
		var json = $.toJSON(community);
		var community = AddCommunity(USERID, json);
		fetchCommunityByID(community.ID);
		$('#createCommunity').modal('hide');
	});

	// function joinCommunity
	$('body').on('click', '.content_join', function() {
		var id = $(this).find("input").attr("value");
		if ($(this).css("background-color") == "rgb(255, 255, 255)") {
			JoinCommunity(USERID, id);
			$(this).css("color", "rgb(255, 255, 255)");
			$(this).css("background-color", "rgb(66,139,202)");
			return 0;
		} else {
			LeaveCommunity(USERID, id);
			$(this).css("color", "rgb(66,139,202)");
			$(this).css("background-color", "rgb(255, 255, 255)");
			return 0;
		}
	});
}
// fetchCommunityByID
function fetchCommunityByID(communityID) {
	var community = FetchCommunityByID(communityID);
	addCommunity(community.ID, community.attributes.name,
			community.memberIDs.length, "myCommunity",community.attributes.communityCard);
}
function fetchCommunityByOwner() {
	var communities = FetchCommunityByOwner(USERID, "0", "5");
	$.each(communities, function(n, community) {
		addCommunity(community.ID, community.attributes.name,
				community.memberIDs.length, "myCommunity",community.attributes.communityCard);
	});
}
// fetchCommunity()
function fetchHotCommunity() {
	var communities = FetchCommunity("0", "5");
	$.each(communities, function(n, community) {
		addCommunity(community.ID, community.attributes.name,
				community.memberIDs.length, "discoverCommunity",community.attributes.communityCard);
	});
}
function fetchCommunityByType(communityType) {
	var communities = FetchCommunityByType(communityType, "0", "5");
	$.each(communities, function(n, community) {
		addCommunity(community.ID, community.attributes.name,
				community.memberIDs.length, community.communityType,community.attributes.communityCard);
	});
}
function fetchCommunitys() {
	fetchHotCommunity();
	fetchCommunityByType("FOLK");
	fetchCommunityByType("SCHOOLUNION");
	fetchCommunityByType("OFFICIAL");

}
// 增加社区
function addCommunity(id, name, memberNum, communityType,communityImg) {
	var boarddiv = "<div class='content_container'><a><div class='img_container'><input type='hidden' value='"
			+ id
			+ "'><img src='"+communityImg+"' onload='javascript:auto_resize(267, 267, this)' /></div></a><div class='content_info'><div class='conten_head'>"
			+ name
			+ "</div><div class='content_count'>"
			+ memberNum
			+ " members</div><a><div class='content_join style='cursor:pointer;'><input type='hidden' value='"
			+ id + "'>Join</div></a></div></div>";
	switch (communityType) {
	case "discoverCommunity":
		$(".communityDiscovery").after(boarddiv);
		Msnry('.containerDiscovery', '.content_container', 265);
		break;
	case "myCommunity":
		$(".myCommunity").after(boarddiv);
		Msnry('.containerMy', '.content_container', 265);
		break;
	case "OFFICIAL":
		$(".officalCommunity").after(boarddiv);
		Msnry('.containerOffical', '.content_container', 265);
		break;
	case "SCHOOLUNION":
		$(".schoolUnionCommunity").after(boarddiv);
		Msnry('.containerSchool', '.content_container', 265);
		break;
	case "FOLK":
		$(".folkCommunity").after(boarddiv);
		Msnry('.containerFolk', '.content_container', 265);
		break;
	}
}
function fetchByType(communityType, communityTypeName,containerName) {
	$('.container_community').remove();
	var communityContainer = '<div class="container container_community"><div class="communityGroupTitle"><h3>'
			+ communityTypeName
			+ '</h3></div><div class="container '+containerName+'"><div class="'
			+ communityType
			+ '"></div></div></div>';
	$('.communitySideBar').after(communityContainer);
}
$(document)
		.ready(
				function() {
					// funtion sessionID
					$('body').on("click", ".img_container", function() {
						var comm = $(this).find("input").attr("value");
						window.location.href = 'communityShow.jsp?' + comm;
					});
					if ($.parseJSON(sessionStorage.getItem("user")) != null
							&& $.parseJSON(sessionStorage.getItem("user")).userType == 'COMMUNITYOWNER') {
						$('#createCommunityBtn').css("display", "inline");
						$('.appCom').css("display", "none");
						$('.titleMy').css("display", "block");
						$('.containerMy').css("display", "block");
						$('#myCommunityBtn').css("display", "block");
						
					}
					$('body').on("click", ".myCommunityBtn", function() {
						fetchByType("myCommunity","我的社区","containerMy");
						$('.titleMy').css("display", "block");
						$('.containerMy').css("display", "block");
						fetchCommunityByOwner();
					});
					$('body').on("click", ".officalCommunityBtn", function() {
						fetchByType("officalCommunity","Offical Community","containerOffical");
						fetchCommunityByType("OFFICIAL");	
					});
					$('body').on("click", ".studentUnionCommunityBtn", function() {
						fetchByType("schoolUnionCommunity","StudentUnion Community","containerSchool");
						fetchCommunityByType("SCHOOLUNION");	
					});
					$('body').on("click", ".folkCommunityBtn", function() {
						fetchByType("folkCommunity","Folk Community","containerFolk");
						fetchCommunityByType("FOLK");	
					});
					$('body').on("click", ".discoverCommunityBtn", function() {
						fetchByType("communityDiscovery","Divercovery Community","containerDiscovery");
						fetchHotCommunity();	
					});
				});
