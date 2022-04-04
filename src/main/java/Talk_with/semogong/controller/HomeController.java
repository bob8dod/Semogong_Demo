package Talk_with.semogong.controller;

import Talk_with.semogong.domain.Comment;
import Talk_with.semogong.domain.Member;
import Talk_with.semogong.domain.auth.MyUserDetail;
import Talk_with.semogong.domain.form.CommentForm;
import Talk_with.semogong.domain.form.MemberForm;
import Talk_with.semogong.service.MemberService;
import Talk_with.semogong.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
            Member member = getLoginMember(authentication);
            MemberForm memberForm = createMemberForm(member);

            model.addAttribute("check",true);
            model.addAttribute("member", memberForm);
        }
        else{
            model.addAttribute("check",false);
        }
        model.addAttribute("page",-1);
        model.addAttribute("posts",postService.findByPage(0));
        model.addAttribute("commentForm", new CommentForm());
        return "home";
    }

    @RequestMapping("/{page}")
    public String home_page(@PathVariable("page") Integer page, Model model, Authentication authentication){
        log.info("opened home");
        if (authentication != null) {
            Member member = getLoginMember(authentication);
            MemberForm memberForm = createMemberForm(member);

            model.addAttribute("check",true);
            model.addAttribute("member", memberForm);
        }
        else{
            model.addAttribute("check",false);
        }
        model.addAttribute("posts",postService.findByPage((page-1)*12));
        model.addAttribute("page",page);
        return "home";
    }

    @GetMapping("/home_temp")
    public String home_temp(){
        return "home_temp";
    }

    private Member getLoginMember(Authentication authentication) {
        MyUserDetail userDetail =  (MyUserDetail) authentication.getPrincipal();  //userDetail 객체를 가져옴 (로그인 되어 있는 놈)
        String loginId = userDetail.getEmail();
        return memberService.findByLoginId(loginId); // "박승일"로 로그인 했다고 가정, 해당 로그인된 회원의 ID를 가져옴
    }

    private MemberForm createMemberForm(Member member) {
        MemberForm memberForm = new MemberForm();
        memberForm.setId(member.getId());
        memberForm.setLoginId(member.getLoginId());
        memberForm.setName(member.getName());
        memberForm.setNickname(member.getNickname());
        memberForm.setDesiredJob(member.getDesiredJob());
        memberForm.setIntroduce(member.getIntroduce());
        memberForm.setState(member.getState());
        return memberForm;
    }
}
