package com.asda.controller.asda;

import com.asda.Command;
import com.asda.CommandException;
import com.asda.CommandResponse;
import com.asda.CommandToken;
import com.asda.model.asdaCommands.Init;
import com.asda.model.asdaCommands.OpenBox;
import com.asda.model.asdaCommands.OpenGraph;
import com.asda.model.asdaCommands.OpenProperties;
import com.asda.model.asdaCommands.SaveGraph;
import com.asda.utils.FlowControl;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Felipe Osorio Thomé
 */
public class QueuingNetworkServlet extends HttpServlet {

    private static final String REDIRECT_HEADER = "fot-redirect";
    private HashMap commands;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initCommands();
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        try {
            Command cmd = lookupCommand(req.getParameter("cmd"));
            CommandResponse next = cmd.execute(req, res);
            CommandToken.set(req);

            if (next != null) {
                if (next.isRedirect()) {
                    FlowControl.redirect(next.getPage(), req, res);
                } else if (next.isForward()) {
                    FlowControl.forward(next.getPage(), req, res);
                } else if (next.isAjax()) {
                    FlowControl.ajaxRedirect(REDIRECT_HEADER, next.getPage(), req, res);
                }
            }
        } catch (CommandException e) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, e.toString());
        }
    }


    /* --- Private Methods --- */
    /**
     * Initialize the HashMap whom is responsible to keep the command handlers.
     */
    private void initCommands() {
        commands = new HashMap();

        commands.put("init", new Init());
        commands.put("open-properties", new OpenProperties());
        commands.put("open-box", new OpenBox());
        commands.put("save", new SaveGraph());
        commands.put("open", new OpenGraph());
    }

    /**
     * Kind of a factory method that finds the right command handler in the
     * HashMap of commands.
     *
     * @param cmd
     * @return
     * @throws CommandException
     */
    private Command lookupCommand(String cmd)
            throws CommandException {
        if (cmd == null) {
            throw new CommandException("Content needed");
        }

        if (commands.containsKey(cmd.toLowerCase())) {
            return (Command) commands.get(cmd.toLowerCase());
        } else {
            throw new CommandException("Invalid Command Identifier");
        }
    }
}
