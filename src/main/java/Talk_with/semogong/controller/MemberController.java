package Talk_with.semogong.controller;

import Talk_with.semogong.domain.Image;
import Talk_with.semogong.domain.Member;
import Talk_with.semogong.domain.auth.MyUserDetail;
import Talk_with.semogong.domain.form.MemberForm;
import Talk_with.semogong.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpSession;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.validation.Valid;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {


    private final MemberService memberService;

    // 회원가입 폼
    @GetMapping("/members/signup")
    public String signUpForm(Model model) {
        log.info("signup");
        model.addAttribute("memberForm", new MemberForm());
        model.addAttribute("first", true);
        return "member/createMemberForm";
    }

    // 회원가입 진행
    @PostMapping("/members/signup")
    public String signUp(@Valid MemberForm memberForm, BindingResult result) {
        if (result.hasErrors()) { log.info("found Null, re-joining"); return "member/createMemberForm"; }
        Member member = Member.createMember(memberForm.getLoginId(), memberForm.getPassword(), memberForm.getName(),memberForm.getNickname(),memberForm.getDesiredJob(), null,memberForm.getIntroduce(),null ,null);
        member.setRole("USER");
        memberService.save(member);
        return "redirect:/";
    }

    @GetMapping("/members/edit/{id}")
    public String memberEdit(@PathVariable("id") Long id, Model model) {
        Member member = memberService.findOne(id);
        MemberForm memberForm = createMemberForm(member);
        model.addAttribute("memberForm",memberForm);
        model.addAttribute("first",false);
        return "member/createMemberForm";
    }



    @PostMapping( "/members/edit/{id}/img")
    public void memberImgEdit(@PathVariable("id") Long id, @RequestParam("file")  MultipartFile[] files) throws IOException {
        memberService.editMemberImg(id, createImage(files));
    }


    @PostMapping("/members/edit/{id}")
    public String memberEdit(@PathVariable("id") Long id, MemberForm memberForm){
        List<String> links = memberForm.getLinks(); while (links.remove("")){ }
        memberForm.setLinks(links);
        memberService.editMember(id, memberForm);
        return "redirect:/";
    }


    @GetMapping("/members/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/"; //주소 요청으로 변경
    }

    private Long getLoginMemberId(Authentication authentication) {
        MyUserDetail userDetail =  (MyUserDetail) authentication.getPrincipal();  //userDetail 객체를 가져옴 (로그인 되어 있는 놈)
        String loginId = userDetail.getEmail();
        return memberService.findByLoginId(loginId).getId(); // "박승일"로 로그인 했다고 가정, 해당 로그인된 회원의 ID를 가져옴
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
        memberForm.setLinks(member.getLinks());
        memberForm.setImage(member.getImage());
        return memberForm;
    }

    private Image createImage(MultipartFile[] files) {

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

    @Data
    static class LinkList{
        private List<LinkString> links;

        public LinkList() {}
        public LinkList(List<LinkString> links) {
            this.links = links;
        }
    }

    @Data
    static class LinkString {
        private String name;

        public LinkString() { }

        public LinkString(String name) {
            this.name = name;
        }
    }


}
