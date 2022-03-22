package Talk_with.semogong.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private long id;

    private String name;
    private String nickname;
    private String desiredJob;

    @Enumerated(EnumType.STRING)
    private StudyState studyState;

    @ElementCollection
    private List<String> links = new ArrayList<>();
    private String image;
    private String introduce;

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();


    //==생성 메서드==//
    public static Member createMember(String name, String nickname, String desiredJob, String image, String introduce,StudyState state, String... links){
         Member member = new Member();
         member.name = name;
         member.nickname = nickname;
         member.desiredJob = desiredJob;
         member.image = image;
         member.introduce = introduce;
         if(links != null) member.links.addAll(Arrays.asList(links));
         member.studyState = state;
         return member;
    }


}
