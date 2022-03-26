package Talk_with.semogong.controller;

import Talk_with.semogong.domain.auth.MyUserDetail;
import Talk_with.semogong.domain.form.PostForm;
import Talk_with.semogong.service.MemberService;
import Talk_with.semogong.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final MemberService memberService;

    @GetMapping("/posts/new")
    public String to_create(Model model){
        log.info("posting");

        PostForm postForm = new PostForm();
        postForm.setTime(LocalDateTime.now());
        model.addAttribute("postForm", postForm);
        return "post/createPostForm";
    }

    @PostMapping("/posts/new")
    public String create(@Valid PostForm postForm, BindingResult result, Authentication authentication) {
        if (result.hasErrors()) { log.info("found Null, required re-post"); return "post/createPostForm"; }
        Long memberId = getLoginMemberId(authentication);
        postService.post(memberId,postForm);
        return "redirect:/";
    }

    @GetMapping("/posts")
    public String memberList(Model model) {
        model.addAttribute("posts", postService.findAll());
        log.info("Searching Posts");
        return "post/posts";
    }

    private Long getLoginMemberId(Authentication authentication) {
        MyUserDetail userDetail =  (MyUserDetail) authentication.getPrincipal();  //userDetail 객체를 가져옴 (로그인 되어 있는 놈)
        String loginId = userDetail.getEmail();
        return memberService.findByLoginId(loginId).getId(); // "박승일"로 로그인 했다고 가정, 해당 로그인된 회원의 ID를 가져옴
    }
}
