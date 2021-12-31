package com.work.daily.user.dto;

import com.work.daily.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@ToString
public class ModifyPasswordDto {

    @NotBlank
    private String id;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{10,32}", message = "비밀번호 형식에 맞게 입력해주세요.")
    private String password;

    @Builder
    public ModifyPasswordDto(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public User toEntity = User.builder()
                            .id(id)
                            .password(password)
                            .build();
}
