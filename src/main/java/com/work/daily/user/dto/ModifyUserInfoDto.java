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
public class ModifyUserInfoDto {

    @NotBlank
    private String id;

    @NotBlank(message = "이름을 입력해주세요.")
    @Pattern(regexp = "^[가-힣]{2,6}$", message = "이름 형식에 맞게 입력해주세요.")
    private String name;

    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$", message = "이메일 형식에 맞게 입력해주세요.")
    private String email;

    @Builder
    public ModifyUserInfoDto(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public User toEntity = User.builder()
                                .id(id)
                                .name(name)
                                .email(email)
                                .build();
}
