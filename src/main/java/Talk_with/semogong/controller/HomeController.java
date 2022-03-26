package Talk_with.semogong.controller;

import Talk_with.semogong.domain.auth.MyUserDetail;
import Talk_with.semogong.service.MemberService;
import Talk_with.semogong.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final PostService postService;
    private final MemberService memberService;

    @RequestMapping("/")
    public String home(Model model, Authentication authentication){
        log.info("opened home");
        if (authentication != null) {
            MyUserDetail userDetail =  (MyUserDetail) authentication.getPrincipal();  //userDetail 객체를 가져옴
            model.addAttribute("check",true);
            model.addAttribute("member", userDetail);
        }
        else{
            model.addAttribute("check",false);
        }
        model.addAttribute("posts",postService.findAll());
        return "home";
    }
}
