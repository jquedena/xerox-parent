package com.everis.web.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.WebRequest;

public class AjaxUtils {

    private static final String X_REQUESTED_WITH = "X-Requested-With";
    private static final String XML_HTTP_REQUEST = "XMLHttpRequest";

    public static boolean isAjaxRequest(HttpServletRequest webRequest) {
        String requestedWith = webRequest.getHeader(X_REQUESTED_WITH);
        return requestedWith != null ? XML_HTTP_REQUEST.equals(requestedWith) : false;
    }

    public static boolean isAjaxRequest(WebRequest webRequest) {
        String requestedWith = webRequest.getHeader(X_REQUESTED_WITH);
        return requestedWith != null ? XML_HTTP_REQUEST.equals(requestedWith) : false;
    }

    private AjaxUtils() {
    }
}
