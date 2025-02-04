<%-- 
    Document   : opOpen
    Created on : 21/05/2014, 21:18:58
    Author     : Felipe Osorio Thomé
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
     <body>
        <div id="opOpenBox" class="boxContent">
            <div class="btCloseDiv cf">
                <span class="boxTitle">Open</span>
                <span id="opOpen-btClose"><img src="img/btClose.png"/></span>
            </div>

            <div id="opOpen-top" class="filesWindow"></div>

            <div id="opOpen-bottom" class="standardForm">
                <form>
                    <label>
                        <span>File name:</span>
                        <input id="opOpen-filename" type="text" class="inputText large">
                        <input id ="opOpen-btSubmit" type="button" value="Open" class="button">
                    </label>
                </form>
            </div>
        </div>
    </body>
</html>
