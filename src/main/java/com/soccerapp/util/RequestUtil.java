package com.soccerapp.util;

import jakarta.servlet.http.HttpServletRequest;

public class RequestUtil {

    private RequestUtil(){

    }
    public static String getEmail(HttpServletRequest request) {
        return (String) request.getAttribute("email");
    }
}
