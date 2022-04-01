package Talk_with.semogong;

import Talk_with.semogong.domain.Member;
import Talk_with.semogong.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class initDB {

    private final MemberService memberService;

    @PostConstruct
    public void init(){
        dbInit();
    }

    private void dbInit() {
        Member member1 = Member.createMember("박승일","1234","박승일","해돌해돌","백엔드 엔지니어",null,"17학번 썩은 물",null);
        Member member2 = Member.createMember("이재훈","1234","이재훈",null,"백엔드 엔지니어",null,"진짜 돼지입니다. 진짜 PIG",null);
        Member member3 = Member.createMember("박정빈","1234","박정빈",null,"데이터 엔지니어",null,"study with me",null);
        Member member4 = Member.createMember("이채원","1234","이채원",null,"ML 엔지니어",null,"지능기전의 마지막 희망",null);
        memberService.save(member1);
        memberService.save(member2);
        memberService.save(member3);
        memberService.save(member4);
    }


}
