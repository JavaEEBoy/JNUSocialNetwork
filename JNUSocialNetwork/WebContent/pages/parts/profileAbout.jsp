<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="about_body">
	<div class="post about">
		<div class="aboutBlue">
			<div>
				<span class="aboutTitle">基本信息</span>
			</div>
			<div class="InforItem">
				<span class="Atitle">姓名</span><span class="Anickname"></span>
			</div>
			<div class="InforItem">
				<span class="Atitle">性别</span><span class="Agender"></span>
			</div>
			<div class="InforItem">
				<span class="Atitle">学院</span><span class="Ainstitution"></span>
			</div>
			<div class="InforItem">
				<span class="Atitle">专业</span><span class="Amajor"></span>
			</div>
			<div class="InforItem">
				<span class="Atitle">年级</span><span class="Aseason"></span><span>级</span>
			</div>
			<div class="InforItem">
				<span class="Atitle">校区</span><span class="Acampus"></span>
			</div>
		</div>
	</div>
	<div class="post about">
		<div class="aboutRed">
			<div>
				<span class="aboutTitle" id="afC">个人信息
					<button class="btn btn-primary aEditbtn">编辑</button>
				</span>
			</div>
			<div class="InforItem">
				<div class='iAt'>
					<span class="Atitle">生日</span><span class="Abirth"></span>
				</div>
				<select class="selectAbout selectBirth"><option
						value="private">私密</option>
					<option value="public">公开</option></select>
			</div>
			<div class="InforItem">
				<div class='iAt'>
					<span class="Atitle">宿舍号</span><span class="Aaddress"></span>
				</div>
				<select class="selectAbout selectAddre"><option
						value="private">私密</option>
					<option value="public">公开</option></select>
			</div>
			<div class="InforItem">
				<div class='iAt'>
					<span class="Atitle">电话</span><span class="Atelenum"></span>
				</div>
				<select class="selectAbout selectTele"><option
						class='private'>私密</option>
					<option class='public'>公开</option></select>
			</div>
			<div class="InforItem">
				<div class='iAt'>
					<span class="Atitle">邮箱</span><span class="Aemail"></span>
				</div>
				<select class="selectAbout selectEmail"><option
						class='private'>私密</option>
					<option class='public'>公开</option></select>
			</div>
			<div class="InforItem">
				<div class='iAt'>
					<span class="Atitle">微信</span><span class="Awechat"></span>
				</div>
				<select class="selectAbout selectWechat"><option
						class='private'>私密</option>
					<option class='public'>公开</option></select>
			</div>
		</div>
	</div>
	<div class="post about">
		<div class="aboutBlack">
			<div>
				<span class="aboutTitle">个人介绍
					<button class="btn btn-primary aEditbtn2">编辑</button>
				</span>
			</div>
			<div class="InforItem">
				<span class="Atitle">感情状态</span><span class="Arelationship"></span>
			</div>
			<div class="InforItem">
				<span class="Atitle">个人描述</span><span class="Alooking"></span>
			</div>
		</div>
	</div>
	<div class="post about">
		<div class="aboutGreen">
			<div>
				<span class="aboutTitle">个性标签</span>
			</div>
			<div class="InforItem" id="inforTag">
				<div class="tags">
					<div class="tagBoard"></div>
					<span class="defaultTags"></span>
				</div>
				<div class="selectTags">
					<div class="tagHead">
						<span class="tagName">热门标签:</span> <input maxlength="8" type="text"
							class="tagInput" />
						<div class="btn-group btn-group-xs tagButton">
							<button class="btn btn-xs btn-default addTag">贴上</button>
							<button class="btn btn-xs btn-default changeTag0">换一换</button>
						</div>
					</div>
					<div class="tagContainer">
						<div class="tagSB"></div>
						
						<br clear="all" /> 
					</div>
				</div>
			</div>
			<br clear="all" /> 
		</div>
	</div>
</div>
