<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <!DOCTYPE html>
    <html>

    <head>
      <%@ include file="parts/head.jsp" %>
        <link rel="stylesheet" href="styles/cropper.min.css">
        <link rel="stylesheet" href="styles/crop-banner.css">
    </head>

    <body>
      <%@ include file="parts/ieReload.jsp" %>
        <div class="navbar navbar-fixed-top navbar-inverse" role="navigation">
          <div class="container">
            <%@ include file="parts/navLeft.jsp" %>
              <div class="collapse navbar-collapse">
                <%@ include file="parts/communityDropDown.jsp" %>

                  <%@ include file="parts/navRight.jsp" %>
              </div>
              <!-- /.nav-collapse -->
          </div>
          <!-- /.container -->
        </div>
        <!-- /.navbar -->
        <div class="activitySideBar">
          <div class="peopleType">
            <a href="activity.jsp?nav=myactivity" class="peopeleType peopeleTypeHover myActivity">我的活动</a>  <a href="activity.jsp?nav=discovery" class="peopeleType discoveryActivity">近期活动</a>  <a href="activity.jsp?nav=hit" class="peopeleType hitActivity">热门活动</a>
            <a href="activity.jsp?nav=entertainment" class="peopeleType peopeleTypeS entertainmentA">娱乐类</a>  <a href="activity.jsp?nav=athletic" class="peopeleType peopeleTypeS athleticA">体育类</a>  <a href="activity.jsp?nav=academic" class="peopeleType peopeleTypeS academicA">学术类</a>  <a href="activity.jsp?nav=others" class="peopeleType peopeleTypeS othersA">其他类</a>

          </div>
        </div>
        <div class="container container_activityA">
          <div class="activityBody">
            <div class="activityBord"></div>
          </div>
        </div>
        <%@ include file="parts/activityCommunityModel.jsp" %>
          <!-- CHATROOM -->
          <%@ include file="parts/chatRoom.jsp" %>
            <!-- Bootstrap core JavaScript
    ================================================== -->

            <%@ include file="parts/activityJavaScript.jsp" %>
              <script src="js/cropper.min.js"></script>
              <script src="js/crop-banner.js"></script>
              <%@ include file="parts/contentScroll.jsp" %>
                <%@ include file="parts/baidu.jsp" %>
    </body>

    </html>
