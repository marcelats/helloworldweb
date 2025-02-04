package com.asda;

/**
 * The idea is avoid using the HttpServletResponse object in a non
 * object-oriented way. Let the servlet treat the real response while we use
 * a fake one.
 * 
 * @author Felipe Osorio Thomé
 */
public class CommandResponse {

    private boolean redirect;
    private boolean forward;
    private boolean ajax;
    private String page;

    public CommandResponse() {
        this.redirect = false;
        this.forward = false;
        this.ajax = false;
        this.page = "";
    }

    public CommandResponse(String page, String method) {
        this.page = page;
        if (method.equals("redirect")) {
            this.redirect = true;
        } else {
            this.forward = true;
        }
    }

    public boolean isRedirect() {
        return redirect;
    }

    public void setRedirect(boolean redirect) {
        this.redirect = redirect;
    }

    public boolean isForward() {
        return forward;
    }

    public void setForward(boolean forward) {
        this.forward = forward;
    }
    
    public boolean isAjax() {
        return ajax;
    }

    public void setAjax(boolean ajax) {
        this.ajax = ajax;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
