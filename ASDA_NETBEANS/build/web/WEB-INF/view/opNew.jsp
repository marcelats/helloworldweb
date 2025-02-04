<%-- 
    Document   : opNew
    Created on : 02/06/2014, 01:15:55
    Author     : Felipe Osorio Thomé
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <div id="opNewBox" class="boxContent">
            <div class="btCloseDiv cf">
                <span class="boxTitle">New</span>
                <span id="opNew-btClose"><img src="img/btClose.png"/></span>
            </div>

            <div id="opNew-form" class="standardForm">
                <form>
                    <span class="labelTop">Do you wanna save the changes?</span>
                    <input id ="opNew-btYes" type="button" value="Yes" class="button">
                    <input id="opNew-btNo" type="button" value="No" class="button">
                    <input id="opNew-btCancel" type="button" value="Cancel" class="button">
                </form>
            </div>
        </div>
    </body>
</html>
