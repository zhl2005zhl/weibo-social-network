package com.weibo.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * XSS过滤器，用于过滤请求参数中的XSS攻击代码
 */
@Component
public class XssFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(httpRequest);
        chain.doFilter(xssRequest, response);
    }

    /**
     * 自定义HttpServletRequestWrapper，用于过滤XSS攻击代码
     */
    private static class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

        private static final Pattern[] XSS_PATTERNS = {
            // 匹配<script>标签
            Pattern.compile("<script[^>]*>([\\S\\s]*?)</script>", Pattern.CASE_INSENSITIVE),
            // 匹配<img>标签的onerror属性
            Pattern.compile("<img[^>]*onerror[^>]*>", Pattern.CASE_INSENSITIVE),
            // 匹配on事件处理器
            Pattern.compile("on[a-zA-Z]+\s*=\s*[\"']?[^\"'>]+", Pattern.CASE_INSENSITIVE),
            // 匹配javascript:伪协议
            Pattern.compile("javascript:[^\\s]+", Pattern.CASE_INSENSITIVE)
        };

        public XssHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getParameter(String name) {
            String value = super.getParameter(name);
            return value != null ? stripXss(value) : null;
        }

        @Override
        public String[] getParameterValues(String name) {
            String[] values = super.getParameterValues(name);
            if (values != null) {
                for (int i = 0; i < values.length; i++) {
                    values[i] = stripXss(values[i]);
                }
            }
            return values;
        }

        @Override
        public String getHeader(String name) {
            String value = super.getHeader(name);
            return value != null ? stripXss(value) : null;
        }

        /**
         * 过滤XSS攻击代码
         */
        private String stripXss(String value) {
            if (value == null) {
                return null;
            }

            String result = value;
            for (Pattern pattern : XSS_PATTERNS) {
                result = pattern.matcher(result).replaceAll("");
            }
            return result;
        }
    }
}