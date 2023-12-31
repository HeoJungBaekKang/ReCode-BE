package com.abo2.recode.dto.attendance;

import com.abo2.recode.domain.attendance.Attendance;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AttendanceResDto {

    private Attendance attendance;

    public AttendanceResDto(Attendance attendance) {
        this.attendance = attendance;
    }

    @Getter
    @Setter
    public static class markAttendanceResDto {
        private Long studyId;
        private Long userId;
        private String status;

        @JsonFormat(pattern = "yyyy년 MM월 dd일 HH:mm")
        private LocalDateTime attendanceDate;

        public markAttendanceResDto(Attendance attendance){
            this.studyId = attendance.getStudyRoom().getId();
            this.userId = attendance.getUser().getId();
            this.status = attendance.getStatus() == 1 ? "출석" : "지각";
            this.attendanceDate = attendance.getAttendanceDate();
        }
    }
}