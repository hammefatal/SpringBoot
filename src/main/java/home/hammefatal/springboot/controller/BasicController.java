package home.hammefatal.springboot.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class BasicController {

    @RequestMapping("/springboot")
    public void basic(HttpServletRequest req,
                      HttpServletResponse resp) throws IOException {
        String input1 = req.getParameter("input1");
        int input2 = Integer.parseInt(req.getParameter("input2"));

        PrintWriter writer = resp.getWriter();
        writer.println("Input#1 = " + input1 + " & Input#2 = " + input2);
    }

    @RequestMapping("/springboot2")
    public String basic2(String input1, int input2,
                       Model model) throws IOException {
        model.addAttribute("input1", input1);
        model.addAttribute("input2", input2);

        return "basic";
    }

    @RequestMapping("/springboot3")
    public ModelAndView basic3(String input1, int input2) throws IOException {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("basic");
        mv.addObject("input1", input1);
        mv.addObject("input2", input2);

        return mv;
    }

}
