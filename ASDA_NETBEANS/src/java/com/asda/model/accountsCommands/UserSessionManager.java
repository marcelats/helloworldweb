package com.asda.model.accountsCommands;

import com.asda.beans.AccountBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Felipe Osorio Thomé
 */
public class UserSessionManager {

    // Singleton
    static private UserSessionManager INSTANCE = new UserSessionManager();
    static final public String SESSION_USER_ATTRIBUTE = "user";
    static final public String REQUEST_LOGINTARGET_ATTRIBUTE = "loginTarget";

    private UserSessionManager() {
    }

    public static UserSessionManager getInstance() {
        return INSTANCE;
    }

    public void setSessionUser(HttpSession session, AccountBean user) {
        session.setAttribute(SESSION_USER_ATTRIBUTE, user);
        if ("admin".equals(user.getPermission())) {
            // Eight hours
            session.setMaxInactiveInterval(60 * 60 * 8);
        } else {
            // Fifty minutes
            session.setMaxInactiveInterval(60 * 50);
        }
    }

    public AccountBean getAccountUser(HttpSession session) {
        return (AccountBean) session.getAttribute(SESSION_USER_ATTRIBUTE);
    }

    public void removeSessionUser(HttpSession session) {
        session.removeAttribute(SESSION_USER_ATTRIBUTE);
    }

    public void setLoginTarget(HttpServletRequest req, String loginTarget) {
        req.setAttribute(REQUEST_LOGINTARGET_ATTRIBUTE, loginTarget);
    }

    public String getLoginTarget(HttpServletRequest req, boolean clear) {
        String target = (String) req.getAttribute(REQUEST_LOGINTARGET_ATTRIBUTE);
        if (clear) {
            req.removeAttribute(REQUEST_LOGINTARGET_ATTRIBUTE);
        }
        return target;
    }
}