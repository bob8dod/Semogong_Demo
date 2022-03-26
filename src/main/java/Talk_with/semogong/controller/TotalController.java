package Talk_with.semogong.controller;


import Talk_with.semogong.domain.StudyState;
import Talk_with.semogong.domain.auth.MyUserDetail;
import Talk_with.semogong.service.MemberService;
import Talk_with.semogong.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
@Slf4j
@RequiredArgsConstructor
public class TotalController {

    private final PostService postService;
    private final MemberService memberService;


    // 공부중 클릭
    @GetMapping("/studying") // 나중엔 로그인 된 회원의 id를 가져와 줘야됨. -> 회원 조회 및 회원 상태 변경을 위함
    public String studying(Authentication authentication){


        MyUserDetail userDetail =  (MyUserDetail) authentication.getPrincipal();  //userDetail 객체를 가져옴 (로그인 되어 있는 놈)
        String loginId = userDetail.getEmail();
        Long memberId = memberService.findByLoginId(loginId).getId(); // "박승일"로 로그인 했다고 가정, 해당 로그인된 회원의 ID를 가져옴
        StudyState state = memberService.checkState(memberId); // 현재 로그인된 회원의 상태를 조회

        // Case #1 (사용자의 현상태가 공부완료 -> 즉, 공부 시작)
        if (state == StudyState.END) {
            memberService.changeState(memberId, StudyState.STUDYING);
            return "redirect:/posts/new";
        }

        // Case #2 (사용자의 현상태가 휴식 중 -> 즉, 공부 재시작)
        if (state == StudyState.BREAKING) {
            memberService.changeState(memberId, StudyState.STUDYING);
            postService.addTime(memberId, LocalDateTime.now());
            return "redirect:/";
        }

        // Case #3 (사용자의 현상태가 공부 중 -> 오류 던지기)
        return "redirect:/";
    }

    // 휴식중 클릭
    @GetMapping("/breaking") // 나중엔 로그인 된 회원의 id를 가져와 줘야됨. -> 회원 조회 및 회원 상태 변경을 위함
    public String breaking(Authentication authentication){

        MyUserDetail userDetail =  (MyUserDetail) authentication.getPrincipal();  //userDetail 객체를 가져옴 (로그인 되어 있는 놈)
        String loginId = userDetail.getEmail();
        Long memberId = memberService.findByLoginId(loginId).getId(); // "박승일"로 로그인 했다고 가정, 해당 로그인된 회원의 ID를 가져옴
        StudyState state = memberService.checkState(memberId); // 현재 로그인된 회원의 상태를 조회

        // Case #1 (사용자의 현상태가 공부 중 -> 즉, 휴식 시작)
        if (state == StudyState.STUDYING) {
            memberService.changeState(memberId, StudyState.BREAKING);
            postService.addTime(memberId, LocalDateTime.now());
            return "redirect:/";
        }

        // Case #2 (사용자의 현상태가 휴식 중, 공부 완료 -> 오류 던지기)
        return "redirect:/";
    }

    // 완료 클릭
    @GetMapping("/end") // 나중엔 로그인 된 회원의 id를 가져와 줘야됨. -> 회원 조회 및 회원 상태 변경을 위함
    public String end(Authentication authentication){

        MyUserDetail userDetail =  (MyUserDetail) authentication.getPrincipal();  //userDetail 객체를 가져옴 (로그인 되어 있는 놈)
        String loginId = userDetail.getEmail();
        Long memberId = memberService.findByLoginId(loginId).getId(); // "박승일"로 로그인 했다고 가정, 해당 로그인된 회원의 ID를 가져옴
        StudyState state = memberService.checkState(memberId); // 현재 로그인된 회원의 상태를 조회

        // Case #1 (사용자의 현상태가 공부 중 -> 즉, 공부 완료)
        if (state == StudyState.STUDYING) {
            memberService.changeState(memberId, StudyState.END);
            postService.addTime(memberId, LocalDateTime.now());
            return "redirect:/";
        }

        // Case #2 (사용자의 현상태가 휴식 중 -> 그대로 상태 변경 후 종료)
        if (state == StudyState.BREAKING) {
            memberService.changeState(memberId, StudyState.END);
            return "redirect:/";
        }

        return "redirect:/";
    }
}
