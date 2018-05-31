package com.seewo.datamock.test3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenHandlerInterceptorAdapter extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(TokenHandlerInterceptorAdapter.class);

    public TokenHandlerInterceptorAdapter() {
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    public void valiOriginRequest(HttpServletRequest request, HttpServletResponse response) {

    }

    private void setupResponse(HttpServletRequest request, HttpServletResponse response, String originUrl) {
    }
}
