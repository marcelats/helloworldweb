package com.asda.filters.login;

import com.asda.beans.AccountBean;
import com.asda.model.accountsCommands.UserSessionManager;
import com.asda.utils.FlowControl;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Felipe Osorio Thomé
 */
public class RoleFilter implements Filter {

    private String role, denyPage;
    private UserSessionManager sessionMgr;

    public RoleFilter() {
        sessionMgr = UserSessionManager.getInstance();
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        role = config.getInitParameter("role");
        if (role == null) {
            throw new ServletException("The role init parameter"
                    + " must be specified.");
        }

        denyPage = config.getInitParameter("denyPage");
        if (denyPage == null) {
            throw new ServletException("The denyPage init parameter"
                    + " must be specified.");
        }
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;
            
            HttpSession session = req.getSession(true);
            AccountBean user = sessionMgr.getAccountUser(session);
            
            if ((user != null) && (user.getPermission() != null)) {
                if(user.getPermission().equals(role)) {
                    chain.doFilter(request, response);
                }
            } else {
                //FlowControl.forward(denyPage, req, res);
                chain.doFilter(request, response);
            }
        } else {
            throw new ServletException("Filter only applicable"
                    + " to HTTP and HTTPS requests.");
        }
    }

    @Override
    public void destroy() {
    }
}
