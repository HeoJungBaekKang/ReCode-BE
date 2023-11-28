package com.abo2.recode.dto.admin;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

public class AdminResDto {

    @Getter
    @Setter
    public static class StudyDeleteResponseDto{

        @NotEmpty
        private Long study_id;

        public StudyDeleteResponseDto(Long study_id) {
            this.study_id = study_id;
        }
    }//class StudyDeleteResponseDto

    @Getter
    @Setter
    public static class MemberRoleResDto{

   /*     {
            "code": 1,
                "msg": "사용자 권한이 성공적으로 변경되었습니다.",
                "data": {
            "userId": 42,
                    "newRole": "group_leader",
                    "updatedAt": "2023-11-06T12:00:00Z"
        }
        }*/

        @NotEmpty
        private Long userId;

        @NotEmpty
        private String newRole;

        @NotEmpty
        private LocalDateTime updatedAt;

        @Builder
        public MemberRoleResDto(Long userId, String newRole, LocalDateTime updatedAt) {
            this.userId = userId;
            this.newRole = newRole;
            this.updatedAt = updatedAt;
        }
    }//MemberRoleResDto

}//AdminResDto
