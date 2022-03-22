package Talk_with.semogong.controller;

import Talk_with.semogong.domain.Member;
import Talk_with.semogong.domain.form.MemberForm;
import Talk_with.semogong.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import javax.validation.constraints.Null;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {


    private final MemberService memberService;

    @GetMapping("/members/join")
    public String to_join(Model model){
        log.info("joining");
        model.addAttribute("memberForm", new MemberForm());
        return "member/createMemberForm";
    }

    @PostMapping("/members/join")
    public String join(@Valid MemberForm memberForm, BindingResult result) {
        if (result.hasErrors()) { log.info("found Null, re-joining"); return "member/createMemberForm"; }
        Member member = Member.createMember(memberForm.getName(),memberForm.getNickname(),memberForm.getDesiredJob(), null,memberForm.getIntroduce(),null ,null);
        memberService.save(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String memberList(Model model) {
        model.addAttribute("members", memberService.findAll());
        log.info("Searching Members");
        return "member/memberList";
    }
}
