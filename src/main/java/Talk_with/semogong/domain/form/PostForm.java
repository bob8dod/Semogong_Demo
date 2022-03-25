package Talk_with.semogong.domain.form;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class PostForm {

    @NotEmpty(message = "제목은 필수 입력 값입니다.")
    private String title;
    private String introduce;
    private String text;
    private LocalDateTime time;

}
