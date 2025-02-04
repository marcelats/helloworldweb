<%-- 
    Document   : arrivalProperties
    Created on : 04/02/2014, 06:53:04
    Author     : Felipe Osorio Thomé
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <div id="arrivalProperties" class="standardForm">
            <form>
                <input type="hidden" id="callerId">
                <input type="hidden" id="callerType">

                <fieldset>
                    <legend>Arrival</legend>
                    <div class="field">
                        <label for="arrival-nbrClients" class="labelLeft">Number of clients</label>
                        <input id="arrival-nbrClients" type="text" class="inputText small">
                    </div>

                    <div class="field">
                        <label for="arrival-arrivalTime" class="labelLeft">Arrival time</label>
                        <input id="arrival-arrivalTime" type="text" class="inputText small">
                    </div>
                </fieldset>

                <fieldset>
                    <legend>Source distribution</legend>
                    <div class="field">
                        <select id="arrival-distribution" class="inputText">
                            <option>Exponential</option>
                            <option>HyperExponential</option>
                            <option>Normal</option>
                            <option>Uniforme</option>
                            <option>Erlang</option>
                        </select>
                    </div>

                    <div class="field">
                        <label for="arrival-average" class="labelLeft">Average</label>
                        <input id="arrival-average" type="text" class="inputText small">
                    </div>

                    <div class="field">
                        <label for="arrival-stdDeviation" class="labelLeft">Standard deviation</label>
                        <input id="arrival-stdDeviation" type="text" class="inputText small">
                    </div>

                    <div class="field">
                        <label for="arrival-sequence" class="labelLeft">Sequence</label>
                        <input id="arrival-sequence" type="text" class="inputText small">
                    </div>
                </fieldset>

                <input id="btCancel" type="button" value="Cancel" class="button">
                <input id ="btSubmit" type="button" value="Ok" class="button">
            </form>
        </div>
    </body>
</html>
