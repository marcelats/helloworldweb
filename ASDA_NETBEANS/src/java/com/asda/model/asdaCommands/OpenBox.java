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
public class OpenBox implements Command {

    private static final String TYPE = "type";
    private static final String DIRECTORY = "/WEB-INF/view/";
    private CommandResponse aResponse;

    @Override
    public CommandResponse execute(HttpServletRequest req, HttpServletResponse res)
            throws CommandException {
        String type = req.getParameter(TYPE);

        switch (type) {
            case "new":
                aResponse = new CommandResponse();
                aResponse.setForward(true);
                aResponse.setPage(DIRECTORY + "opNew.jsp");
                break;
            case "save":
                aResponse = new CommandResponse();
                aResponse.setForward(true);
                aResponse.setPage(DIRECTORY + "opSave.jsp");
                break;
            case "open":
                aResponse = new CommandResponse();
                aResponse.setForward(true);
                aResponse.setPage(DIRECTORY + "opOpen.jsp");
                break;
            case "parameters":
                aResponse = new CommandResponse();
                aResponse.setForward(true);
                aResponse.setPage(DIRECTORY + "opParam.jsp");
                break;
        }

        return aResponse;
    }
}