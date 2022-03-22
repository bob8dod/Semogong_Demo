package Talk_with.semogong.service;

import Talk_with.semogong.domain.Post;
import Talk_with.semogong.domain.form.PostForm;
import Talk_with.semogong.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final MemberService memberService;
    private final PostRepository postRepository;

    public void save(Post post){
        postRepository.save(post);
    }

    public Long post(Long memberId, PostForm postForm) {
        Post post = Post.createPost(memberService.findOne(memberId), postForm.getTitle(), postForm.getIntroduce(), postForm.getText(), LocalDateTime.now());
        postRepository.save(post);
        return post.getId();
    }

    public Post findOne(Long id) {
        return postRepository.findOne(id);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }
}
