package home.hammefatal.springboot.controller;

import org.springframework.context.annotation.*;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@RestController
public class ConditionalController {

    @GetMapping("/conditional")
    public String conditional() {
        return "conditional";
    }

    @Component
    @Conditional(ConditionClazz.class)  // 조건에 따라 Bean을 등록할지 결정한다.
        // 조건에 따라 Bean 을 등록하는 방법은 두 가지가 있다.
        // 1. @Bean, @Component + @Conditional 어노테이션을 사용하는 방법
        // 2. @Configuration + @Import + ImportSelector 를 사용하는 방법
    class Engine {
        public String toString() {
            return "Engine{}";
        }
    }

    static class ConditionClazz implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            // Bean의 등록 여부를 결정할 조건문을 작성한다.
            // 여기서는 true를 반환하면 Bean이 등록되고, false를 반환하면 Bean이 등록되지 않는다.
            // @Bean, @Component 와 같이 사용한다.
            return false;
        }
    }

    /*
    조건에 따라 다른 Configuration 을 적용할 떄 사용한다.
    ImportSelector 를 구현하고 이를 @Import 어노테이션과 함께 사용한다.
     */
    static class MyImportSelector implements ImportSelector {
        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            // Bean의 등록 여부를 결정할 조건문을 작성한다.
            // 여기서는 true를 반환하면 Bean이 등록되고, false를 반환하면 Bean이 등록되지 않는다.
            // @Configuration, @Import 와 같이 사용한다.

            AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                    importingClassMetadata.getAnnotationAttributes(EnableMyAutoConfiguration.class.getName(), false)
            );

            String value = attributes.getString("value");
            if ("test".equals(value))
                return new String[]{Config1.class.getName()};
            else
                return new String[]{Config2.class.getName()};
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Import(MyImportSelector.class)
    @interface EnableMyAutoConfiguration {
        String value() default "";
    }

    @EnableMyAutoConfiguration("test")
    class MainConfig {
        @Bean Car carObj() { return new Car();}
    }

    class Config1 {
        @Bean Car sportsCar() { return new SportsCar(); }
    }
    class Config2 {
        @Bean Car sportsCar2() { return new SportsCar2(); }
    }

    class Car {}
    class SportsCar extends Car {}
    class SportsCar2 extends Car {}

}
