package home.hammefatal.springboot.config;

import home.hammefatal.springboot.controller.DiController.Car;
import home.hammefatal.springboot.controller.DiController.Engine;
import home.hammefatal.springboot.controller.DiController.Door;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfig {

    @Bean
    Car car() {
        return new Car();
    }

    @Bean
    @Scope("prototype") // Prototype - 매번 새로운 객체를 반환한다.
    //@Scope("singleton") // Singleton - 항상 같은 객체를 반환한다. (기본값)
    Engine engine() {
        return new Engine();
    }

    @Bean
    Door door() {
        return new Door();
    }

}
