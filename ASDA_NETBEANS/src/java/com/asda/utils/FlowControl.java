package com.asda.utils;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Felipe Osorio Thomé
 */
public class FlowControl {
    
    /**
     * The name of the response header under which ajax callback function
     * identify the page to be redirected.
     */
    private static final String REDIRECT_HEADER = "fot-redirect";
    
    /**
     * Uses sendRedirect to change the page.
     *
     * @param next Page to be transferred.
     * @param res The http response.
     * @throws IOException If an input or output exception occurs
     */
    public static void redirect(String next, HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        String urlWithSessionID = res.encodeRedirectURL(req.getContextPath() + next);
        res.sendRedirect(urlWithSessionID);
    }

    /**
     * Uses RequestDispatcher.forward to change the page.
     *
     * @param next Page to be transferred.
     * @param req The http request.
     * @param res The http response.
     * @throws ServletException If the target resource throws this exception.
     * @throws IOException If the target resource throws this exception.
     */
    public static void forward(String next, HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(next);
        dispatcher.forward(req, res);
    }

    /**
     * The ajax callback function must perform the redirection. When the server
     * receive an ajax call he must treat her different inserting a header in
     * the response.
     *
     * @param next Page to be transferred.
     * @param res The http response.
     */
    public static void ajaxRedirect(String next, HttpServletRequest req, HttpServletResponse res) {
        res.setHeader(REDIRECT_HEADER, res.encodeRedirectURL(req.getContextPath() + next));
    }
    
    /**
     * The ajax callback function must perform the redirection. When the server
     * receive an ajax call he must treat her different inserting a header in
     * the response.
     * 
     * @param attribute Name of the attribute to be placed in the response.
     * @param next Page to be transferred.
     * @param res The http response.
     */
    public static void ajaxRedirect(String attribute, String next, HttpServletRequest req, HttpServletResponse res) {
        res.setHeader(attribute, res.encodeRedirectURL(req.getContextPath() + next));
    }
}
