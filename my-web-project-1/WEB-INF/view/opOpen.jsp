<%-- 
    Document   : opOpen
    Created on : 21/05/2014, 21:18:58
    Author     : Felipe Osorio Thomé
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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

            <div id="opOpen-top" class="filesWindow"><c:forEach var="arquivo" items="${arquivos}">
        <div>${arquivo}</div>
    </c:forEach></div>

            <div id="opOpen-bottom" class="standardForm">
                <form>
                    <label>
                        <span>File name:</span></label>
                        <input id="opOpen-filename" type="text" class="inputText large">
                        <input id ="opOpen-btSubmit" type="button" value="Edit" class="button">
                        <input id ="opCopy-btSubmit" type="button" value="Copy" class="button">
                        <input id ="opPublic-btSubmit" type="button" value="Public" class="button">
                        <input id ="opPrivate-btSubmit" type="button" value="Private" class="button">
                        <input id ="opRename-btSubmit" type="button" value="Rename" class="button">
                        <input id ="opDelete-btSubmit" type="button" value="Delete" class="button"><br>
                        <span>Rename to:</span></label>
                        <input id="opOpen-newname" type="text" class="inputText large">
                </form>
            </div>
        </div>
    </body>
</html>
