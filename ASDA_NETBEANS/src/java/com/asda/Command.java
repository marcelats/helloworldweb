package com.asda;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Felipe Osorio Thomé
 */
public interface Command {

    public CommandResponse execute(HttpServletRequest req, HttpServletResponse res)
            throws CommandException;
    
}
