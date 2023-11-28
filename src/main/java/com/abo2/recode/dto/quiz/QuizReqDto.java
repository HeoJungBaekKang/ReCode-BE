package com.abo2.recode.dto.quiz;

import com.abo2.recode.domain.quiz.Quiz;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class QuizReqDto {

    /*
        QUIZ_ID  	DIFFICULTY  	QUIZ_LINK  	TITLE  	STUDY_ROOM_ID  	USER_ID
    */
    @Getter
    @Setter
    public static class QuizWriteReqDto {
        private Long quizId;
        private String title;
        private Integer difficulty;
        private String quiz_link;
        private Long studyRoomId;
        private Long userId;
        private LocalDateTime created_At;
    }
}
