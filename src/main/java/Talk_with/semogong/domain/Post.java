package Talk_with.semogong.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.time.format.FormatStyle.LONG;

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
    private String content;
    @Column(columnDefinition="TEXT")
    private String html;
    @ElementCollection
    private List<String> times = new ArrayList<>();

    private LocalDateTime createTime;
    private String formatCreateTime;

    //==연관관계 메서드==//
    private void setMember(Member member){
        this.member = member;
        member.getPosts().add(this);
    }

    //==생성 메서드==//
    public static Post createPost(Member member, String title, String introduce, String content, String html ,LocalDateTime time){
        Post post = new Post();
        post.setMember(member);
        post.title = title;
        post.introduce = introduce;
        post.content = content;
        post.html = html;
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        post.times.add(time.format(timeFormatter));
        post.createTime = time;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(LONG).withLocale(Locale.ENGLISH);
        post.formatCreateTime = time.format(dateFormatter);
        return post;
    }

    public void addTime(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String timeString = time.format(formatter);
        this.times.add(timeString);
    }
}
