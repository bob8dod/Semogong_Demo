package Talk_with.semogong.controller;

import Talk_with.semogong.domain.Member;
import Talk_with.semogong.domain.Post;
import Talk_with.semogong.domain.StudyState;
import Talk_with.semogong.domain.form.PostForm;
import Talk_with.semogong.service.MemberService;
import Talk_with.semogong.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        log.info("joining");
        PostForm postForm = new PostForm();
        postForm.setTime(LocalDateTime.now());
        model.addAttribute("postForm", postForm);
        model.addAttribute("members", memberService.findAll());
        return "post/createPostForm";
    }

    @PostMapping("/posts/new")
    public String create(@RequestParam("memberId") Long memberId, @Valid PostForm postForm, BindingResult result) {
        if (result.hasErrors()) { log.info("found Null, required re-post"); return "post/createPostForm"; }
        postService.post(memberId,postForm);
        return "redirect:/posts";
    }

    @GetMapping("/posts")
    public String memberList(Model model) {
        model.addAttribute("posts", postService.findAll());
        log.info("Searching Posts");
        return "post/posts";
    }
}
