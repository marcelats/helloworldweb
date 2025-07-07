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
                <span class="boxTitle">Parametrize the model</span>
                <span id="opParam-btClose"><img src="img/btClose.png"/></span>
            </div>

            <div id="opParam-form" class="standardForm">
                <form class="right">
                    <fieldset>
                        <legend>Language</legend>
                        <select id="opParam_library" class="inputText">
                            <option value="R">R</option>
  <option value="Java">Java</option>
  <option value="Python">Python</option>
  <option value="C SMPL">C SMPL</option>
  <option value="C SMPLX">C SMPLX</option>
  <option value="C ParSMPL">C ParSMPL</option>
  <option value="C SIMPACK">C SIMPACK</option>
  <option value="C SIMPACK2">C SIMPACK2</option>
                        </select>
                    </fieldset>
                    
                    <fieldset>
                        <legend>General</legend>
                        <div class="field">
                            <label for="opParam_execTime" class="labelLeft">Execution time</label>
                            <input id="opParam_execTime" type="text" class="inputText small">
                        </div>
                        
                        <div class="field">
                            <label for="opParam_numCycles" class="labelLeft">Number of cycles</label>
                            <input id="opParam_numCycles" type="text" class="inputText small">
                        </div>

                        <div class="field">
                            <label for="opParam_batchSize" class="labelLeft">Batch size</label>
                            <input id="opParam_batchSize" type="text" class="inputText small">
                        </div>
                        
                        <div class="field">
                            <label for="opParam_maxEntities" class="labelLeft">Max number of entities</label>
                            <input id="opParam_maxEntities" type="text" class="inputText small">
                        </div>
                        <div class="field">
                            <label for="opParam_seed" class="labelLeft">Seed</label>
                            <input id="opParam_seed" type="text" class="inputText small">
                        </div>                       
                        
                    </fieldset>
                    
                    <fieldset>
                        <legend>Model type</legend>
                        
                        <input id="opParam_closed" type="radio" name="modelType">
                        <label for="opParam_closed" class="labelLeft">Closed</label>
                        
                        <input id="opParam_open" type="radio" name="modelType">
                        <label for="opParam_open" class="labelLeft">Open</label>
                        
                    </fieldset>

                    <fieldset>
                        <legend>Warmup Time</legend>
                        
                        <input id="opParam_timeAutomatic"type="radio" name="warmupTime">
                        <label for="opParam_timeAutomatic" class="labelLeft">Automatic</label>
                        
                        <input id="opParam_timeDefined" type="radio" name="warmupTime">
                        <label for="opParam_timeDefined" class="labelLeft">Defined</label>
                        
                        <input id="opParam_definedValue" type="text" class="inputText small">
                    </fieldset>

                    <input id ="opParam-btSubmit" type="button" value="Ok" class="button" onclick="checkModelType()">
                </form>
            </div>
        </div>
    </body>
</html>
