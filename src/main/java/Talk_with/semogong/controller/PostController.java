package Talk_with.semogong.controller;

import Talk_with.semogong.domain.Image;
import Talk_with.semogong.domain.Member;
import Talk_with.semogong.domain.Post;
import Talk_with.semogong.domain.StudyState;
import Talk_with.semogong.domain.auth.MyUserDetail;
import Talk_with.semogong.domain.form.PostEditForm;
import Talk_with.semogong.domain.form.PostForm;
import Talk_with.semogong.service.MemberService;
import Talk_with.semogong.service.PostService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
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
        Long memberId = getLoginMemberId(authentication).getId();
        postForm.setHtml(markdownToHTML(postForm.getContent()));
        postService.post(memberId,postForm);
        return "redirect:/";
    }

    @GetMapping("/posts/{id}/edit")
    public String to_edit(@PathVariable("id") Long id, Model model, Authentication authentication) {
        log.info("posting");
        Member member = getLoginMemberId(authentication);
        Post post = postService.findOne(id);
        if (member.getId() != post.getMember().getId()) {
            return "redirect:/"; // 오류 처리해줘야 됨.
        }
        PostEditForm postEditForm = new PostEditForm(post, member);
        model.addAttribute("postForm", postEditForm);
        return "post/editPostForm";
    }

    @PostMapping("/posts/{id}/edit")
    public String edit(@PathVariable("id") Long id, @Valid @ModelAttribute("postForm") PostEditForm postEditForm, BindingResult result) {
        if (result.hasErrors()) { log.info("found Null, required re-post"); return "redirect:/posts/"+id.toString()+"/edit"; }
        postEditForm.setHtml(markdownToHTML(postEditForm.getContent()));
        postService.edit(postEditForm);
        return "redirect:/";
    }

    @PostMapping( "/posts/edit/{id}/img")
    public void postImgEdit(@PathVariable("id") Long id, @RequestParam("file")  MultipartFile[] files) throws IOException {
        postService.editPostImg(id, createImage(files));
    }

    private Member getLoginMemberId(Authentication authentication) {
        MyUserDetail userDetail =  (MyUserDetail) authentication.getPrincipal();  //userDetail 객체를 가져옴 (로그인 되어 있는 놈)
        String loginId = userDetail.getEmail();
        return memberService.findByLoginId(loginId); // "박승일"로 로그인 했다고 가정, 해당 로그인된 회원의 ID를 가져옴
    }

    private String markdownToHTML(String markdown) {
        Parser parser = Parser.builder().build();

        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        return renderer.render(document);
    }

    private PostForm createPostForm(Post post) {
        PostForm postForm = new PostForm();
        postForm.setTime(post.getCreateTime());
        postForm.setTitle(post.getTitle());
        postForm.setIntroduce(post.getIntroduce());
        postForm.setContent(post.getContent());
        postForm.setHtml(post.getHtml());
        return postForm;
    }

    private Image createImage(MultipartFile[] files) {
//        String rootPath = "/images/";

        Image image = null;
        for (MultipartFile file : files) {
            String pathName = RandomStringUtils.randomAlphanumeric(16) + file.getOriginalFilename();
            File newFileName = new File(pathName);

            try {
                file.transferTo(newFileName);
            } catch (Exception e) {
                e.printStackTrace();
            }

            image = new Image( file.getOriginalFilename(), pathName );
        }
        return image;
    }

}
