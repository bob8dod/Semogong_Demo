package Talk_with.semogong.controller;

import Talk_with.semogong.service.MemberService;
import Talk_with.semogong.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final PostService postService;
    private final MemberService memberService;

    @RequestMapping("/home")
    public String home(Model model){
        log.info("opened home");
        model.addAttribute("posts",postService.findAll());
        model.addAttribute("member", memberService.findByName("박승일"));
        return "home";
    }

    @RequestMapping("/")
    public String temp_home(Model model){
        log.info("opened home");
        return "temp_home";
    }
}
