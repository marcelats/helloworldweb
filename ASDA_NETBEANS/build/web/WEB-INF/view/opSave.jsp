<%-- 
    Document   : opSave
    Created on : 21/05/2014, 19:34:15
    Author     : Felipe Osorio Thomé
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <div id="opSaveBox" class="boxContent">
            <div class="btCloseDiv cf">
                <span class="boxTitle">Save</span>
                <span id="opSave-btClose"><img src="img/btClose.png"/></span>
            </div>

            <div id="opSave-top" class="filesWindow"></div>

            <div id="opSave-bottom" class="standardForm">
                <form>
                    <label>
                        <span>File name:</span>
                        <input id="opSave-filename" type="text" class="inputText large">
                        <input id ="opSave-btSubmit" type="button" value="Save" class="button">
                    </label>
                </form>
            </div>
        </div>
    </body>
</html>
