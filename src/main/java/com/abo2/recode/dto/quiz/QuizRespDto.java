package com.abo2.recode.dto.quiz;

import com.abo2.recode.domain.quiz.Quiz;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class QuizRespDto {

    @Getter
    @Setter
    public static class QuizWriteRespDto {
        private Long id;
        private String title;
        private Integer difficulty;
        private String quiz_link;
        private String nickname;
        private LocalDate createdAt;
        private LocalDate updatedAt;


        public QuizWriteRespDto(Quiz quiz) {
            this.id = quiz.getId();
            this.title = quiz.getTitle();
            this.difficulty = quiz.getDifficulty();
            this.quiz_link = quiz.getQuiz_link();
            this.nickname = quiz.getUser().getNickname();
            this.createdAt = quiz.getCreatedAt();
            this.updatedAt = quiz.getUpdatedAt();
        }
    }

    @Getter
    @Setter
    public static class QuizListRespDto {
        private Long id;
        private String title;
        private Integer difficulty;
        private String quiz_link;
        private String nickname;
        private Long studyId;

        @JsonFormat(pattern = "yyyy년 MM월 dd일")
        private LocalDate updatedAt;

        public QuizListRespDto(Quiz quiz) {
            this.id = quiz.getId();
            this.title = quiz.getTitle();
            this.difficulty = quiz.getDifficulty();
            this.quiz_link = quiz.getQuiz_link();
            if (quiz.getUser() != null) {
                this.nickname = quiz.getUser().getNickname();
            } else {
                this.nickname = "탈퇴한 회원입니다.";
            }
            this.studyId = quiz.getStudyRoom().getId();
            this.updatedAt = quiz.getUpdatedAt();
        }
    }

    @Getter
    @Setter
    public static class QuizDetailRespDto {
        private Long id;
        private String title;
        private String quiz_link;
        private String nickname;
        private Integer difficulty;

        public QuizDetailRespDto(Quiz quiz) {
            this.id = quiz.getId();
            this.title = quiz.getTitle();
            this.difficulty = quiz.getDifficulty();
            this.quiz_link = quiz.getQuiz_link();
            this.nickname = quiz.getUser().getNickname();
        }
    }
}