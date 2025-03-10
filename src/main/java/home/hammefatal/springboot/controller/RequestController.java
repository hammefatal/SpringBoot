package home.hammefatal.springboot.controller;

import home.hammefatal.springboot.dto.ReqParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class RequestController {

    // WebDataBinder -> Validator -> ModelAttribute -> RequestParam
    // @ModelAttribute -> 적용 대상을 Model 의 속성으로 자동 추가해준다.
    //                   반환 타입 또는 컨트롤러 메소드의 매개변수에 적용 가능하다.
    @RequestMapping("/main1")
    public String main1(@ModelAttribute ReqParameter reqParam, BindingResult result, Model model) throws IOException {
        System.out.println(reqParam.getStrInput());
        System.out.println(reqParam.getIntInput());

        model.addAttribute("doubleStr", getDoubleStr(reqParam.getIntInput()));
        return "req";
    }

    private String getDoubleStr(Integer num) {
        System.out.println("num = " + num);

        if (num != null) {
            return num + " * 2 = " + (num * 2);
        } else {
            return "NULL";
        }
    }

    // @RequestParam -> 요청 파라미터를 메소드의 매개변수로 전달받을 수 있다.
    @RequestMapping("/main2")
    public String main2(@RequestParam(name="input", required=true, defaultValue = "1") String input) throws IOException {
        return "req";
    }

}
