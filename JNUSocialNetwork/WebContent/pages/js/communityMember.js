
function fetchMembers() {
	$.each(community.members, function(n, member) {
		addMember(member.avatarLink,member.id,member.name);
	});
}
function addMember(avatarLink,id,name) {
	var memberDiv = "<div class='member'><img onload='javascript:auto_resize(80, 80, this)'  class='userMember' src='"
			+ avatarLink
			+ "' /><span class='glyphicon glyphicon-remove memberRemoveBtn' style='font-size:10px'></span><input type='hidden' value='"
			+ id + "' /><span class='memberName'>" + name
			+ "</span></div>";
	$('.membersBord').after(memberDiv);
	Msnry('.membersContainer', '.member', 215);
}
function showCommunityInfo() {
	$('.cName').html(community.attributes.name);
	$('.cIntro').html(community.attributes.introduct);
	$('.communityPic').find('img').attr("src",
			community.attributes.communityCard);
}