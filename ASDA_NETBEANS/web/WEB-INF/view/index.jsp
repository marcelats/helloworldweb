<%-- 
    Document   : index
    Created on : 29/01/2014, 21:17:34
    Author     : Felipe Osorio Thomé
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>ASDA</title>

        <link rel="icon" type="image/png" href="img/favicon.png"/>
        <link rel="stylesheet" type="text/css" href="styles/indexStyles.css">
        <link rel="stylesheet" type="text/css" href="styles/lightBox.css">
        <link rel="stylesheet" type="text/css" href="styles/formStd.css">
        <link rel="stylesheet" type="text/css" href="styles/clearFixHack.css">

        <script type="text/javascript" src="js/libs/jquery-1.10.2.min.js"></script>
        <script type="text/javascript" src="js/libs/jquery.validate.min.js"></script>
        <script type="text/javascript" src="js/libs/jquery.form.min.js"></script>

        <script type="text/javascript" src="js/index/eventMap.js"></script>
        <script type="text/javascript" src="js/index/simpleLightBox.js"></script>
    </head>
    <body>
        <div id="wrapLogin">
            <div id="topLogin">
                <img src="img/logo.png"/>
            </div>

            <div id="loginFormDiv" class="cf">
                <form action="" method="post" id="login" class="standardForm">
                    <div class="field">
                        <label for="user" class="labelTop">User</label>
                        <input name="user" id="user" class="inputText dark relLarge"/>
                    </div>

                    <div class="field">
                        <label for="password" class="labelTop">Password</label>
                        <input type="password" name="password" id="password" class="inputText dark relLarge"/>
                    </div>

                    <h2>
                        <span id="forgotPassLink" class="link">Forgot your password?</span> | <span id="newUserLink" class="link">New user</span>
                    </h2>

                    <div class="right">
                        <input type="submit" value="Log in" class="button"/>
                    </div>
                </form>

            </div>

            <div id="bottomLogin">
                <p>Core - Sarita Mazzini Bruschi</p>
                <p>Interface - Felipe Osorio Thomé</p>
            </div>
        </div>

        <div id="shadowing" class="shadowing">
            <div id="wrapSignIn" class="box">

                <div class="boxContent cf">
                    <div class="btCloseDiv cf">
                        <span class="boxTitle">Sign in</span>

                        <span id="btSignInClose">
                            <img src="img/btClose.png"/>
                        </span>
                    </div>

                    <form action="" method="post" id="signin" class="standardForm">
                        <h1>The fields with * are mandatory.</h1>

                        <div class="field">
                            <label for="si_email" class="labelTop">Email *</label>
                            <input name="si_email" id="si_email" class="inputText dark large"/>
                        </div>

                        <div class="field">
                            <label for="si_password" class="labelTop">Password *</label>
                            <input name="si_password" type="password" id="si_password" class="inputText dark large"/>
                        </div>

                        <div class="field">
                            <label for="si_password" class="labelTop">Confirm Password *</label>
                            <input name="si_password2" type="password" id="si_password2" class="inputText dark large"/>
                        </div>

                        <div class="field">
                            <label for="si_name" class="labelTop">Name</label>
                            <input name="si_name" id="si_name" class="inputText dark large"/>
                        </div>

                        <div class="field">
                            <label for="si_course" class="labelTop">Course</label>
                            <input name="si_course" id="si_course" class="inputText dark large"/>
                        </div>

                        <div class="field">
                            <label for="si_class" class="labelTop">Class</label>
                            <input name="si_class" id="si_class" class="inputText dark large"/>
                        </div>

                        <div class="right">
                            <input type="reset" value="Clean" class="button"/>
                            <input type="submit" value="Sign in" class="button"/>
                        </div>
                    </form>
                </div>
            </div>

            <div id="wrapForgotPass" class="box">
                <div class="boxContent cf">
                    <div class="btCloseDiv cf">
                        <span class="boxTitle">Forgot password</span>

                        <span id="btForgotPassClose">
                            <img src="img/btClose.png"/>
                        </span>
                    </div>

                    <form action="" method="post" id="forgotPass" class="standardForm">
                        <div class="field">
                            <label for="fp_email" class="labelTop">Email</label>
                            <input name="fp_email" id="fp_email" class="inputText dark large" />
                        </div>

                        <div class="right">
                            <input type="submit" value="Send" class="button"/>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>  
</html>
