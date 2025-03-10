package home.hammefatal.springboot.controller;

import home.hammefatal.springboot.config.AppConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Properties;

@RestController
public class DiController {

    @GetMapping("/test")
    public String test() throws Exception {
        Car car = (Car) getObject("car");
        return car.toString();
    }

    // Java Reflection API
    // -> 동적으로 클래스 객체를 생성하고, 메소드 호출 등을 가능하게 한다. (Runtime)
    @GetMapping("/test2")
    public String test2() throws Exception {

        Class carClass = Car.class; // Car 클래스의 Class 객체를 얻어온다.
        // Class 클래스의 객체. 클래스 당 1개만 존재한다.
        // 해당 클래스의 정보 조회, 객체 생성 등의 기능을 제공한다.
        // 클래스 파일(*.class)을 읽어서 메모리에 로드하고, 클래스의 정보를 담고 있는 객체를 생성한다.
        /*
        클래스 객체를 얻는 방법
        1. 클래스명.class
            Class classObj = Car.class;
        2. 객체.getClass()
            Car car = new Car();
            Class classObj = car.getClass();
        3. Class.forName("클래스명")
            Class classObj = Class.forName("home.hammefatal.springboot.domain.Car");
         */
        if (carClass.isInstance(Car.class)) {
            System.out.println("Car 클래스의 객체입니다.");
        }
        Car car = (Car) carClass.newInstance(); // Car 클래스의 객체를 생성한다. ( Car car = new Car() 와 동일한 효과 )

        Method[] methods = carClass.getMethods();   // Car 클래스의 메소드 목록을 얻어온다.
        Method[] declaredMethods = carClass.getDeclaredMethods(); // Car 클래스의 메소드 목록을 얻어온다. (선언된 메소드만)
        Field[] fields = carClass.getFields();      // Car 클래스의 필드 목록을 얻어온다.(iv, cv)
        Field[] declaredFields = carClass.getDeclaredFields(); // Car 클래스의 필드 목록을 얻어온다. (선언된 필드만)

        Method method = carClass.getMethod("setEngine", Engine.class); // Car 클래스의 setEngine 메소드를 얻어온다.
        method.invoke(car, new Engine()); // car 객체의 setEngine 메소드를 호출한다. ( car.setEngine(new Engine()) 와 동일한 효과 )

        Annotation[] annotations = carClass.getAnnotations(); // Car 클래스의 어노테이션 목록을 얻어온다.
        Annotation[] declaredAnnotations = carClass.getDeclaredAnnotations(); // Car 클래스의 어노테이션 목록을 얻어온다. (선언된 어노테이션만)

        return car.toString();
    }

    static Object getObject(String key) throws Exception {
        Properties prop = new Properties();
        prop.load(new FileReader("config.txt"));

        String clazzName = prop.getProperty(key);
        Class clazz = Class.forName(clazzName);

        return clazz.newInstance();
    }


    public static class Car {
        private Engine engine;
        private Door door;

        @Override
        public String toString() {
            return "Car{" +
                    "engine=" + engine +
                    ", door=" + door +
                    '}';
        }
    }

    public static class Engine { }

    public static class Door { }

    @GetMapping("/test3")
    public String test3() {
        // ApplicationContext 생성 - AC 의 설정파일은 AppConfig.class
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Car car = context.getBean("car", Car.class); // byName - 객체(Bean)를 가져온다. (Singleton)
        System.out.println(car.toString());

        Engine engine = context.getBean(Engine.class);  // byType - 객체(Bean)를 가져온다. (Prototype)
        System.out.println(engine.toString());

        Door door = context.getBean(Door.class);
        System.out.println(door.toString());

        System.out.println("context.getBeanDefinitionCount() = " + context.getBeanDefinitionCount());
        System.out.println("context.getBeanDefinitionNames() = " + Arrays.toString(context.getBeanDefinitionNames()));
        System.out.println("context.containsBeanDefinition(\"engine\") = " + context.containsBeanDefinition("engine"));
        System.out.println("context.isSingleton(\"car\") = " + context.isSingleton("car"));
        System.out.println("context.isPrototype(\"engine\") = " + context.isPrototype("engine"));

        return "test3";
    }

    @GetMapping("/ditest")
    public String ditest() {

        // @Configuration - AppConfig 클래스에 @Configuration 어노테이션을 붙여서 AppConfig 클래스를 설정파일로 사용한다.
        // @ComponentScan - @Component 어노테이션이 붙은 클래스를 찾아서 Bean으로 등록한다.

        // @Value - properties 파일의 값을 가져온다.
        //          -> @Value("${key}")
        //          -> @Value("#{systemProperties['key']}")
        // @PropertySource - properties 파일을 읽어온다.

        // @Autowired - Spring Container 에서 Bean을 Type 으로 검색해서 참조 변수에 자동 주입해준다.(DI) - byType
        //              검색된 Bean 이 N개이면, 그 중에 참조변수와 이름이 일치하는 것을 주입한다.
        //              @Autowired(required = false) - Bean 이 없으면, null 을 주입한다.
        // @Qualifier - @Autowired 와 함께 사용하며, 같은 Type의 Bean이 여러 개일 때, 특정 Bean을 주입받을 때 사용한다.
        //              @Qualifier("beanName") - beanName 이라는 이름을 가진 Bean을 주입받는다.
        //              @Autowired @Qualifier("beanName") - Bean 의 이름을 지정해서 주입한다.
        // @Resource - @Autowired 와 유사하게 동작한다. Bean을 이름으로 검색해서 주입한다. - byName
        //             @Resource(name = "beanName") - beanName 이라는 이름을 가진 Bean을 주입받는다.

        return "ditest";
    }

}
