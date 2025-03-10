package home.hammefatal.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExternalConfigController {

    /*
    src/main/resources/application.properties -> 어플리케이션 속성의 기본 값을 바꿀 수 있는 설정 파일
                                                 16개의 분류, 약 1700개의 속성을 제공한다.
    @ConfigurationProperties -> 사용자 정의 속성을 추가할 수 있다.
    @Value, @PropertySource -> 속성을 주입할 수 있다.
     */

    @GetMapping("/externalConfig")
    public String getExternalConfig() {
        return "external config";
    }




}
