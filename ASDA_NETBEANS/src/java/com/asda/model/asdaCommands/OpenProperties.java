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
public class OpenProperties implements Command {
    
    private static final String TYPE = "type";
    private static final String DIRECTORY = "/WEB-INF/view/";
    
    private CommandResponse aResponse;
    
    @Override
    public CommandResponse execute(HttpServletRequest req, HttpServletResponse res)
            throws CommandException {
        String type = req.getParameter(TYPE);
        
        switch (type) {
            case "server":
                aResponse = new CommandResponse();
                aResponse.setForward(true);
                aResponse.setPage(DIRECTORY + "propsServer.jsp");
                break;
            case "source":
                aResponse = new CommandResponse();
                aResponse.setForward(true);
                aResponse.setPage(DIRECTORY + "propsArrival.jsp");
                break;
            case "multiServer":
                aResponse = new CommandResponse();
                aResponse.setForward(true);
                aResponse.setPage(DIRECTORY + "propsMultiServer.jsp");
                break;
        }
                
        return aResponse;
    }
}
