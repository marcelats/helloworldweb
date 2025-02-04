<%-- 
    Document   : opParameters
    Created on : 23/05/2014, 16:16:07
    Author     : Felipe Osorio Thomé
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <div id="opParamBox" class="boxContent">
            <div class="btCloseDiv cf">
                <span class="boxTitle">Parameterize the model</span>
                <span id="opParam-btClose"><img src="img/btClose.png"/></span>
            </div>

            <div id="opParam-form" class="standardForm">
                <form class="right">
                    <fieldset>
                        <legend>Library</legend>
                        <select id="opParam-library" class="inputText">
                            <option>SMPL</option>
                            <option>SMPLX</option>
                            <option>ParSMPL</option>
                            <option>SIMPACK</option>
                            <option>SIMPACK2</option>
                        </select>
                    </fieldset>
                    
                    <fieldset>
                        <legend>General</legend>
                        <div class="field">
                            <label for="opParam-execTime" class="labelLeft">Execution time</label>
                            <input id="opParam-execTime" type="text" class="inputText small">
                        </div>

                        <div class="field">
                            <label for="opParam-maxEntities" class="labelLeft">Max number of entities</label>
                            <input id="opParam-maxEntities" type="text" class="inputText small">
                        </div>

                        <div class="field">
                            <label for="opParam-batchSize" class="labelLeft">Batch size</label>
                            <input id="opParam-batchSize" type="text" class="inputText small">
                        </div>
                    </fieldset>

                    <fieldset>
                        <legend>Warmup Time</legend>
                        
                        <input id="opParam-timeAutomatic"type="radio" name="warmupTime">
                        <label for="opParam-timeAutomatic" class="labelLeft">Automatic</label>
                        
                        <input id="opParam-timeDefined" type="radio" name="warmupTime">
                        <label for="opParam-timeDefined" class="labelLeft">Defined</label>
                        
                        <input id="opParam-definedValue" type="text" class="inputText small">
                    </fieldset>

                    <input id ="opParam-btSubmit" type="button" value="Ok" class="button">
                </form>
            </div>
        </div>
    </body>
</html>
