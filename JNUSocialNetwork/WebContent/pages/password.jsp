<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<jsp:scriptlet>synchronized (session) {
				session.setAttribute("hiddenCode", System.currentTimeMillis()
						+ "");
			}</jsp:scriptlet>
<%@ include file="parts/head.jsp"%>

<body>
	<div class="regBody">
		<div class="layer">
			<div class="regTop">
				<a href="home.jsp" class="btn btn-primary btn-sm rl-button-span"
					role="button"><span>回到首页</span></a> <a href="aboutUs.jsp"
					class="btn btn-primary btn-sm rl-button-text" role="button"><span>关于我们</span></a>
			</div>
			<div class="regTitle">修改密码</div>
			<div class="containerReg" style="display: block">
				<div class="regBox">
					<form class="form-signin" role="form" method="post"
						action="../security/Login">
						<p>
							<input type="text" class="form-control" placeholder="请输入ID"
								name="ID" data-errormessage-value-missing="请输入ID" required
								autofocus maxLength="20" />
						</p>
						<p>
							<input type="password" class="form-control" placeholder="请输入旧密码"
								name="oldPassword" id="md5Password"
								data-errormessage-value-missing="请输入旧密码" required autofocus
								maxLength="40" />
						</p>
						<p>
							<input type="password" class="form-control" placeholder="请输入新密码"
								name="newpassword" id="md5Password"
								data-errormessage-value-missing="请输入新密码" required autofocus
								maxLength="40" />
						</p>
						<input type="hidden" name="hiddenCode"
							value="${sessionScope.hiddenCode }" />
						<button class="btn btn-lg btn-success btn-block signInBtn"
							type="submit">确认</button>
						<h4>
							没有账号？<span class="btn signUp">注册</span>
						</h4>
					</form>
					<div id="login_popover" style="display: none"></div>
				</div>
			</div>
			<!-- /container -->
			<div class="containerSign" style="display: none"></div>
		</div>
	</div>
	<%@ include file="parts/footer.jsp"%>
	<!-- /container -->

	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="js/jquery-1.10.2.js"></script>
	<script src="styles/bootstrap-3.0.3-dist/dist/js/bootstrap.min.js"></script>
	<script src="http://cdn.bootcss.com/holder/2.0/holder.min.js"></script>
	<script src="js/md5.js"></script>
	<script>
		$('body').on("click", ".signInBtn", function() {
			var pass = $("#md5Password").val();
			if (pass.length > 0) {
				$("#md5Password").val(md5(pass));
			}
		});
		$("h4 span.signUp").click(function() {
			location.href = "register.jsp";
		});
	</script>
	<c:choose>
		<c:when test="${ param.success eq false}">
			<script type="text/javascript">
				$('#login_popover')
						.replaceWith(
								'<div id="login_popover" class="alert alert-danger" style="display: none">登录失败</div>');
				$('#login_popover').fadeIn("fast");
				setTimeout('$("#login_popover").fadeOut("slow")', 3000);
			</script>

		</c:when>
		<c:when test="${param.register}">

			<script type="text/javascript">
				$('#login_popover')
						.replaceWith(
								'<div id="login_popover" class="alert alert-success" style="display: none">注册成功，请登录</div>');
				$('#login_popover').fadeIn("fast");
				setTimeout('$("#login_popover").fadeOut("slow")', 3000);
			</script>

		</c:when>
		<c:when test="${param.registerExist}">

			<script type="text/javascript">
				$('#login_popover')
						.replaceWith(
								'<div id="login_popover" class="alert alert-danger" style="display: none">已有账号，请登录</div>');
				$('#login_popover').fadeIn("fast");
				setTimeout('$("#login_popover").fadeOut("slow")', 3000);
			</script>

		</c:when>
	</c:choose>
</body>
</html>