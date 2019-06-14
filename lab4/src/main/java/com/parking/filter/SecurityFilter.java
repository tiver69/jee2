package com.parking.filter;

import com.parking.entity.Role;
import com.parking.entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SecurityFilter implements Filter {

    private String USER_PATTERN = "/user/";
    private String ADMIN_PATTERN = "/admin/";
    private List<String> GUEST_PATTERN = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) {
        GUEST_PATTERN.add("/registration.xhtml");
        GUEST_PATTERN.add("/signIn.xhtml");
        GUEST_PATTERN.add("/");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        User user = (User) httpRequest.getSession().getAttribute("User");
        Role requiredRole = getRequiredRole(httpRequest);

        System.out.println((user!=null ? user.getRoles() : "") + " - " +requiredRole);

        if (requiredRole == null){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (requiredRole == Role.GUEST && user == null) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if(requiredRole == Role.ADMIN  && user != null &&
                user.getRoles().contains(requiredRole)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if(requiredRole == Role.USER  && user != null &&
                (user.getRoles().contains(requiredRole) ||user.getRoles().contains(Role.ADMIN))) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Override
    public void destroy() {

    }

    private Role getRequiredRole(HttpServletRequest request) {
        String action = request.getRequestURI()
                .substring(request.getContextPath().length());
        System.out.println(action);
        return
                action.startsWith(USER_PATTERN) ? Role.USER :
                        action.startsWith(ADMIN_PATTERN) ? Role.ADMIN :
                                GUEST_PATTERN.contains(action)? Role.GUEST :
                                        null;
    }
}