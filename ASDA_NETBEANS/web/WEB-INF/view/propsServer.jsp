<%-- 
    Document   : serverProperties
    Created on : 04/02/2014, 05:18:58
    Author     : Felipe Osorio Thomé
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <div id="serverProperties" class="standardForm">
            <form>
                <input type="hidden" id="callerId">
                <input type="hidden" id="callerType">

                <fieldset>
                    <legend>Queue statistics</legend>
                    <div class="field">
                        <label for="server-emptyQueue" class="labelLeft">Empty queue</label>
                        <input id="server-emptyQueue" type="checkbox">
                    </div>

                    <div class="field">
                        <label for="server-length" class="labelLeft">Length (max / min)</label>
                        <input id="server-length" type="checkbox">
                    </div>
                </fieldset>

                <fieldset>
                    <legend>Choice next resource</legend>
                    <div class="field">
                        <label for="server-probability" class="labelLeft">Probability</label>
                        <input id="server-probability" type="radio" name="probability">
                    </div>

                    <div class="field">
                        <label for="server-cycle" class="labelLeft">Cycle</label>
                        <input id="server-cycle" type="radio" name="probability">
                    </div>
                </fieldset>

                <fieldset>
                    <legend>Service distribution</legend>
                    <div class="field">
                        <select id="server-distribution" class="inputText">
                            <option>Exponential</option>
                            <option>HyperExponential</option>
                            <option>Normal</option>
                            <option>Uniform</option>
                            <option>Erlang</option>
                        </select>
                    </div>

                    <div class="field">
                        <label for="server-average" class="labelLeft">Average</label>
                        <input id="server-average" type="text" class="inputText small">
                    </div>

                    <div class="field">
                        <label for="server-stdDeviation" class="labelLeft">Standard deviation</label>
                        <input id="server-stdDeviation" type="text" class="inputText small">
                    </div>

                    <div class="field">
                        <label for="server-sequence" class="labelLeft">Sequence</label>
                        <input id="server-sequence" type="text" class="inputText small">
                    </div>
                </fieldset>

                <input id="btCancel" type="button" value="Cancel" class="button">
                <input id ="btSubmit" type="button" value="Ok" class="button">
            </form>
        </div>
    </body>
</html>
