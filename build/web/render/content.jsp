<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.io.*,java.util.*" %> 
<%@page import="tools.Controller" %> 
<%

    Boolean l = false;
    Cookie[] cookies = request.getCookies();
    if (cookies.length>2) {
        if (request.getParameter("exit") != null) {
            cookies[1].setMaxAge(0);
            response.addCookie(cookies[1]);
            response.setStatus(response.SC_MOVED_TEMPORARILY);
            response.setHeader("Location", "index.jsp");
            
        }
        l = true;

%>
<%@include file = "../includes/list.jsp" %>         
<%            } else {
        l = false;
    }

    if (request != null && request.getParameter("username") != null && request.getParameter("password") != null) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Boolean log = Controller.loginController(username, password);

        if (log) {
            Cookie username_ = new Cookie("username_", username);
            Cookie password_ = new Cookie("password_", password);
            username_.setMaxAge(3 * 60);
            password_.setMaxAge(3 * 60);
            response.addCookie(username_);
            response.addCookie(password_);


            response.setStatus(response.SC_MOVED_TEMPORARILY);
            response.setHeader("Location", "index.jsp");

        } else {
            l = false;
            out.write("<div class='alert alert-danger'><strong > Səhv!</strong > İstifadəçi adı və ya şifrə səhvdir!!!</div >");
        }
    }

    if (l == false) {

%>
<!--<div class="rerun"><a href="">Yenilə</a></div>-->
<div class="container">
    <div class="card"></div>
    <div class="card">
        <h1 class="title">Gİrİş</h1>
        <form method="post">
            <div class="input-container">
                <input type="text" id="Username" name="username" required="required"/>
                <label for="Username">İsitifadəçi adı</label>
                <div class="bar"></div>
            </div>
            <div class="input-container">
                <input type="password" id="Password" name="password" required="required"/>
                <label for="Password">Şifrə</label>
                <div class="bar"></div>
            </div>
            <div class="button-container">
                <button><span>Daxİl ol</span></button>
            </div>

        </form>
    </div>
    <!--  <div class="card alt">
        <div class="toggle" title='Qeydiyyat'></div>
        <h1 class="title">Qeydİyyat
          <div class="close"></div>
        </h1>
        <form>
          <div class="input-container">
            <input type="text" id="Username" required="required"/>
            <label for="Username">İstifadəçi adı</label>
            <div class="bar"></div>
          </div>
          <div class="input-container">
            <input type="password" id="Password" required="required"/>
            <label for="Password">Şifrə</label>
            <div class="bar"></div>
          </div>
          <div class="input-container">
            <input type="password" id="Repeat Password" required="required"/>
            <label for="Repeat Password">Təkrar şifrə</label>
            <div class="bar"></div>
          </div>
          <div class="button-container">
            <button><span>Daxİl et</span></button>
          </div>
        </form>
      </div>-->
</div>


<% }%>