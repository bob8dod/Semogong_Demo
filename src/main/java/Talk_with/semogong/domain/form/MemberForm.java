package Talk_with.semogong.domain.form;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "이름은 필수로 입력하셔야 됩니다.")
    private String name;
    private String nickname;
    private String desiredJob;

    private List<String> links = new ArrayList<>();
    private String image;

    @NotEmpty(message = "간단한 자기 소개를 적어주세요!")
    private String introduce;

}
