package Talk_with.semogong.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Post {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    private String title;
    private String introduce;
    @Column(columnDefinition="TEXT")
    private String text;
    @ElementCollection
    private List<LocalDateTime> times = new ArrayList<>();

    //==연관관계 메서드==//
    public void setMember(Member member){
        this.member = member;
        member.getPosts().add(this);
    }

    //==생성 메서드==//
    public static Post createPost(Member member, String title, String introduce, String text, LocalDateTime time){
        Post post = new Post();
        post.setMember(member);
        post.title = title;
        post.introduce = introduce;
        post.text = text;
        post.times.add(time);

        return post;
    }
}
