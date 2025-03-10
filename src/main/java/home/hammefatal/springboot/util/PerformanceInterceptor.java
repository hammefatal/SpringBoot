package home.hammefatal.springboot.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class PerformanceInterceptor implements HandlerInterceptor {
//    private long start; // iv - 인스턴스 변수. 싱글톤(하나의 객체) 이라서 여러 쓰레드가 하나의 객체를 공유한다.

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long start = System.currentTimeMillis();
        request.setAttribute("startTime", start);

        // Object handler: 핸들러 정보를 가지고 있음 - 요청하고 연결된 컨트롤러의 메소드
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            System.out.println("[Interceptor] handlerMethod.getMethod() = " + handlerMethod.getMethod());   // URL과 연결된 메소드
            System.out.println("[Interceptor] handlerMethod.getBean() = " + handlerMethod.getBean());       // 메소드가 포함된 컨트롤러
        }

        // return true; - 요청을 진행하고 싶을 때 (다음 인터셉터나 컨트롤러를 호출)
        // return false; - 요청을 중단하고 싶을 때 (호출 안함)
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        long start = (long) request.getAttribute("startTime");
        long end = System.currentTimeMillis();
        System.out.println("[Interceptor][" + request.getRequestURI() + "] Spended Time = " + (end - start));

        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("[Interceptor][" + request.getRequestURI() + "] Completed");

        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
