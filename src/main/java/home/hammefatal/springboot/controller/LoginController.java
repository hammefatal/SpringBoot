package home.hammefatal.springboot.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;

@Controller
@RequestMapping("/users")
public class LoginController {

    //@RequestMapping("/login")
    //@RequestMapping(value="/login", method= {RequestMethod.GET, RequestMethod.POST}) // GET, POST 둘 다 받음
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    //@RequestMapping(value="/login", method= RequestMethod.POST)
    @PostMapping("/login")
    public String login(HttpServletRequest request, String username, String password, RedirectAttributes model) throws Exception {
        System.out.println("login");
        if (loginCheck(username, password)) {
            model.addAttribute("username", username);
            model.addAttribute("password", password);
            return "userInfo";
        } else {
            //String msg = URLEncoder.encode("Wrong username or password.", "UTF-8");
            String msg = "Wrong username or password.";

            //return "redirect:/users/login?msg="+msg;   // GET
            model.addAttribute("msg", msg); // 쿼리스트링 파라미터로 붙여준다.(RedirectAttributes)
            model.addFlashAttribute("message", "일회용 메세지"); // 세션에 담아서 전달하고, 바로 삭제된다.
            request.setAttribute("msg2", "request scope message"); // request scope

            return "redirect:/users/login";
            //return "forward:/users/login";
        }
    }

    private boolean loginCheck(String username, String password) {
        return "username".equals(username) && "password".equals(password);
    }

}
