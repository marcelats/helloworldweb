package com.asda.model.asdaCommands;

import com.asda.Command;
import com.asda.CommandException;
import com.asda.CommandResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Felipe Osorio Thomé
 */
public class Init implements Command {
    
    private static final String appPage = "/WEB-INF/view/queuingNetwork.jsp";
    private CommandResponse aResponse;

    @Override
    public CommandResponse execute(HttpServletRequest req, HttpServletResponse res)
            throws CommandException {
        aResponse = new CommandResponse();
        aResponse.setForward(true);
        aResponse.setPage(appPage);
        
        return aResponse;
    }
}