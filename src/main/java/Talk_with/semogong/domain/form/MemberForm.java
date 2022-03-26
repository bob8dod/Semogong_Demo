package Talk_with.semogong.domain.form;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "ID는 필수로 입력하셔야 됩니다.")
    private String loginId;
    @NotEmpty(message = "비밀번호는 필수로 입력하셔야 됩니다.")
    private String password;

    @NotEmpty(message = "이름은 필수로 입력하셔야 됩니다.")
    private String name;
    private String nickname;
    private String desiredJob;

    private List<String> links = new ArrayList<>();
    private String image;

    private String introduce;

}
