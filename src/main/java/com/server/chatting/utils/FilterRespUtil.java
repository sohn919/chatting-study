package com.server.chatting.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.chatting.error.exception.Exception401;
import com.server.chatting.error.exception.Exception403;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FilterRespUtil {
    public static void unAuthorized(HttpServletResponse resp, Exception401 e) throws IOException {
        resp.setStatus(e.status().value());
        resp.setContentType("application/json; charset=utf-8");
        ObjectMapper om = new ObjectMapper();
        String responseBody = om.writeValueAsString(e.body());
        resp.getWriter().println(responseBody);
    }

    public static void forbidden(HttpServletResponse resp, Exception403 e) throws IOException {
        resp.setStatus(e.status().value());
        resp.setContentType("application/json; charset=utf-8");
        ObjectMapper om = new ObjectMapper();
        String responseBody = om.writeValueAsString(e.body());
        resp.getWriter().println(responseBody);
    }
}
