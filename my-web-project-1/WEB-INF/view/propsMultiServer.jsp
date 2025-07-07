<%-- 
    Document   : multiServerProperties
    Created on : 11/02/2014, 19:19:32
    Author     : Felipe Osorio Thomé
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <div id="multiServerProperties" class="standardForm">
            <form>
                <input type="hidden" id="callerId">
                <input type="hidden" id="callerType">

                <fieldset>
                    <legend>Server type</legend>
                    <div class="field">
                        <label for="multiServer_nbrServers" class="labelLeft">Number of servers</label>
                        <input id="multiServer_nbrServers"type="text" class="inputText small" value="1">
                    </div>

                    <%--<div class="field">
                        <label for="multiServer_nbrQueues" class="labelLeft">Number of queues</label>
                        <input id="multiServer_nbrQueues"type="text" class="inputText small">
                    </div>--%>
                </fieldset>

                <fieldset>
                    <legend>Queue statistics</legend>
                    <div class="field">
                        <label for="multiServer_emptyQueues" class="labelLeft">Empty queue</label>
                        <input id="multiServer_emptyQueue" type="checkbox">
                    </div>

                    <div class="field">
                        <label for="multiServer_length" class="labelLeft">Length (max / min)</label>
                        <input id="multiServer_length" type="checkbox">
                    </div>
                </fieldset>

                <%--<fieldset>
                    <legend>Choice next resource</legend>
                    <div class="field">
                        <label for="multiServer_probability" class="labelLeft">Probability</label>
                        <input id="multiServer_probability" type="radio" name="probability">
                    </div>

                    <div class="field">
                        <label for="multiServer_cycle" class="labelLeft">Cycle</label>
                        <input id="multiServer_cycle" type="radio" name="probability">
                    </div>
                </fieldset>--%>
                <fieldset>
                    <legend>Arrival distribution</legend>
                    <div class="field">
                        <select id="multiServer_arrival_distribution" class="inputText">
                            <option>Exponential</option>
                            <%--<option>HyperExponential</option>--%>
                            <option>Normal</option>
                            <option>Uniform</option>
                            <%--<option>Erlang</option>--%>
                        </select>
                    </div>

                    <div class="field">
                        <label for="arrival_average" class="labelLeft">Average</label>
                        <input id="arrival_average" type="text" class="inputText small">
                    </div>

                    <%--<div class="field">
                        <label for="server_stdDeviation" class="labelLeft">Standard deviation</label>
                        <input id="server_stdDeviation" type="text" class="inputText small">
                    </div>

                    <div class="field">
                        <label for="server_sequence" class="labelLeft">Sequence</label>
                        <input id="server_sequence" type="text" class="inputText small">
                    </div>--%>
                </fieldset>
                
                <fieldset>
                    <legend>Service distribution</legend>
                    <div class="field">
                        <select id="multiServer_distribution" class="inputText">
                            <option>Exponential</option>
                            <option>HyperExponential</option>
                            <option>Normal</option>
                            <option>Uniform</option>
                            <option>Erlang</option>
                        </select>
                    </div>

                    <div class="field">
                        <label for="multiServer_average" class="labelLeft">Average</label>
                        <input id="multiServer_average" type="text" class="inputText small">
                    </div>

                    <div class="field">
                        <label for="multiServer_stdDeviation" class="labelLeft">Standard deviation</label>
                        <input id="multiServer_stdDeviation" type="text" class="inputText small">
                    </div>

                    <div class="field">
                        <label for="multiServer_sequence" class="labelLeft">Sequence</label>
                        <input id="multiServer_sequence" type="text" class="inputText small">
                    </div>
                </fieldset>

                <input id="btCancel" type="button" value="Cancel" class="button">
                <input id ="btSubmit" type="button" value="Ok" class="button">
            </form>
        </div>
    </body>
</html>