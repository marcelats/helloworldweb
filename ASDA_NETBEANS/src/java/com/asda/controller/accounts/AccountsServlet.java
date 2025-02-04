package com.asda.controller.accounts;

import com.asda.model.accountsCommands.SignInCommand;
import com.asda.model.accountsCommands.LogInCommand;
import com.asda.model.accountsCommands.ForgotPassCommand;
import com.asda.Command;
import com.asda.CommandException;
import com.asda.CommandResponse;
import com.asda.CommandToken;
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
public class AccountsServlet extends HttpServlet {

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

        commands.put("signin", new SignInCommand());
        commands.put("login", new LogInCommand());
        commands.put("forgot-pass", new ForgotPassCommand());
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