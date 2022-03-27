package Talk_with.semogong.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    private LocalDateTime createTime;

    //==연관관계 메서드==//
    private void setMember(Member member){
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
        post.createTime = time;
        return post;
    }

    public void addTime(LocalDateTime time) {
        this.times.add(time);
    }
}
