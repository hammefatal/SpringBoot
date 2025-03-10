package home.hammefatal.springboot.controller;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.regex.Pattern;

@RestController
public class AOPController {

    /*
    AOP(Aspect Oriented Programming) -> 관점 지향 프로그래밍
     - 부가 기능 (advice) 을 동적으로 추가해주는 기술이다.
     - 메소드의 시작 또는 끝에 자동으로 코드 (advice) 를 삽입해준다.

     Before advice -> 메소드의 시작 부분에 실행될 코드 ( @Before )
     Around advice -> 메소드의 시작과 끝 부분에 실행될 코드 ( @Around )
     After advice -> 메소드의 끝 부분에 실행될 코드 ( @After )
                        @AfterReturning -> 메소드의 끝 부분에 실행될 코드 (예외가 발생하지 않은 경우)
                        @AfterThrowing -> 메소드의 끝 부분에 실행될 코드 (예외가 발생한 경우)

    @Order -> advice 의 실행 순서를 지정한다. (낮은 숫자일수록 먼저 실행된다.)

     target -> advice 가 적용될 대상
     advice -> target 에 동적으로 추가 될 부가 기능 (코드)
     join point -> advice 가 추가(join) 될 대상 (메소드)
     pointcut -> join point 들을 정의한 패턴. e.g) execution(* home.hammefatal.springboot.controller.*.*(..))
     proxy -> target 에 advice 가 동적으로 추가되어 생성된 객체
     weaving -> target 에 advice 를 추가해서 proxy 를 생성하는 과정
     */

    @GetMapping("/aopTest")
    public String aopTest() throws Exception{
/*
        Class myClass = Class.forName("home.hammefatal.springboot.controller.AOPController$MyClass");
        Object o = myClass.newInstance();

        MyAdvice myAdvice = new MyAdvice();

        for (Method m : myClass.getDeclaredMethods()) {
            myAdvice.invoke(m, o, null);
        }
*/
        return "aopTest";
    }


    class MyAdvice {
        Pattern p = Pattern.compile("aaa|bbb");

        boolean matches(Method method) {
            return p.matcher(method.getName()).matches();
        }

        void invoke(Method m, Object obj, Object... args) {
            // 메소드의 시작 부분에 실행될 코드
            if (matches(m)) {
                System.out.println("[before] {");
            }

            try {
                m.invoke(obj, args);
            } catch (Exception e) {
                // 메소드의 예외 발생 시 실행될 코드
                System.out.println("[EXCEPTION!!!!] }");
            } finally {
                // 메소드의 끝 부분에 실행될 코드
                if (matches(m)) {
                    System.out.println("} [after]");
                }
            }
        }
    }

    class MyClass {
        void aaa() {
            System.out.println("aaa()");
        }
        void bbb() {
            System.out.println("bbb()");
        }
        void ccc() {
            System.out.println("ccc()");
        }
    }


    @Aspect
    @Component
    // @EnableAspectJAutoProxy, @ComponentScan, @Configuration
    public class LoggingAdvice {
        // advice 가 추가될 메소드를 지정하기 위한 패턴을 지정한다.
        // execution( 접근제어자 반환타입 패키지명.클래스명.메소드명(매개변수 목록))
        // -> execution(* home.hammefatal.springboot.controller.*.*(..)) -> home.hammefatal.springboot.controller 패키지의 모든 클래스의 모든 메소드
        @Around("execution(* home.hammefatal.springboot.controller.*.*(..))")    // pointcut - 부가기능이 적용될 메소드의 패턴을 지정한다.
        public Object methodCallLog(ProceedingJoinPoint joinPoint) throws Throwable {
            long startTime = System.currentTimeMillis();
            System.out.println("[Start] 메소드 호출 전 : " + joinPoint.getSignature().getName() + Arrays.toString(joinPoint.getArgs()));

            Object result = joinPoint.proceed(); // Target 메소드 호출

            System.out.println("result=" + result);
            System.out.println("[End] 메소드 호출 후 : " + joinPoint.getSignature().getName() + " : " + (System.currentTimeMillis() - startTime) + "ms");

            return result; // 메소드 호출결과 반환
        }
    }

}
