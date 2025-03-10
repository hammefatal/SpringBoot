package home.hammefatal.springboot.util;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class PerformanceFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 1. 전처리 작업
        long start = System.currentTimeMillis();

        // 2. 서블릿 또는 다음 필터 호출
        filterChain.doFilter(servletRequest, servletResponse);

        // 3. 후처리 작업
        long end = System.currentTimeMillis();
        System.out.println("[Filter][" + ((HttpServletRequest) servletRequest).getRequestURI() + "] Spended Time = " + (end - start));
    }

}
