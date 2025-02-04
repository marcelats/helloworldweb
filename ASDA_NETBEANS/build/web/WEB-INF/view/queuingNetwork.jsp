<%-- 
    Document   : queuingNetwork
    Created on : 29/01/2014, 21:20:05
    Author     : Felipe Osorio Thomé
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>ASDA</title>

        <link rel="icon" type="image/png" href="img/favicon.png" />
        
        <link rel="stylesheet" type="text/css" href="styles/queuingNetwork.css">
        <link rel="stylesheet" type="text/css" href="styles/formStd.css">
        <link rel="stylesheet" type="text/css" href="styles/lightBox.css">
        <link rel="stylesheet" type="text/css" href="styles/clearFixHack.css">

        <script data-main="js/queuingNetwork/main.js" src="js/libs/require.js"></script>
        <script src="js/queuingNetwork/require.cfg.js"></script>
    </head>

    <body>
        <div id="global">
            <div id="top">
                <div id="topTitle">
                    ASDA - Ambiente de Simulação Distribuída Automático<br>
                    (Automatic Distributed Simulation Environment)
                </div>
                <div id="topMenu">
                    <ul class="menu">
                        <li><div id="opNew"><img src="img/icon/new.gif"/></div>New Document</li>
                        <li><div id="opSave"><img src="img/icon/save.gif"/></div>Save</li>
                        <li><div id="opOpen"><img src="img/icon/open.gif"/></div>Open</li>
                        <li><div id="opParam"><img src="img/icon/param.gif"/></div>Parameters</li>
                        <li><div id="opGen"><img src="img/icon/exec.gif"/></div>Generate</li>
                        <li><div id="opReport"><img src="img/icon/report.gif"/></div>Report</li>
                        <li><div id="opCode"><img src="img/icon/code.gif"/></div>Source Code</li>
                        <li><div id="opManual"><img src="img/icon/manual.gif"/></div>Help</li>
                    </ul>
                </div>
            </div>

            <div id="center">
                <div id="leftMenu">
                    <ul class="menu menuBlock">
                        <li>
                            <div id="server">
                                <img src="img/icon/server.gif" title="One Server"/>
                            </div>
                        </li>
                        <li>
                            <div id="multiServer">
                                <img src="img/icon/multiServer.gif" title="Multiple Servers"/>
                            </div>
                        </li>
                        <li>
                            <div id="source">
                                <img src="img/icon/source.gif" title="Source"/>
                            </div>
                        </li>
                        <li>
                            <div id="out">
                                <img src="img/icon/out.gif" title="Out"/>
                            </div>
                        </li>
                        <li>
                            <div id="link">
                                <img src="img/icon/link.gif" title="Link"/>
                            </div>
                        </li>
                        <li>
                            <div id="erase">
                                <img src="img/icon/erase.gif" title="Erase"/>
                            </div>
                        </li>
                    </ul>
                </div>

                <div id="drawArea"></div>

                <div id="propertiesArea"></div>
            </div>

            <div id="bottom">
                <div id="bottomStatus">
                    <div id="statusLeft"></div>
                    <div id="statusMiddle">&nbsp;</div>
                    <div id="statusRight">&nbsp;</div>
                </div>
                <div id="bottomCredits">
                    Model - Sarita Mazzini Bruschi <br>
                    View and Controllers - Felipe Osorio Thomé
                </div>
            </div>
        </div>
        
        <div id="shadowing" class="shadowing">
            <div id="boxContainer" class="box"></div>
        </div>
    </body>
</html>