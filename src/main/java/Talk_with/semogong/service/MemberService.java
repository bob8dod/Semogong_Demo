package Talk_with.semogong.service;

import Talk_with.semogong.domain.Member;
import Talk_with.semogong.domain.Post;
import Talk_with.semogong.domain.StudyState;
import Talk_with.semogong.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Long save(Member member) {
        memberRepository.save(member);
        return member.getId();
    }

    public Member findOne(Long id){
        return memberRepository.findOne(id);
    }

    public List<Member> findAll(){
        return memberRepository.findAll();
    }

    public Member findByName(String name){
        return memberRepository.findByName(name);
    }


    public StudyState checkState(Long memberId) {
        Member curr_member = memberRepository.findOne(memberId);
        return curr_member.getState();
    }

    public void changeState(Long memberId, StudyState state) {
        Member curr_member = memberRepository.findOne(memberId);
        curr_member.changeState(state);
    }
}
