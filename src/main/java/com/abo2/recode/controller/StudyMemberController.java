package com.abo2.recode.controller;

import com.abo2.recode.config.auth.LoginUser;
import com.abo2.recode.domain.user.User;
import com.abo2.recode.domain.user.UserRepository;
import com.abo2.recode.dto.ResponseDto;
import com.abo2.recode.dto.study.StudyReqDto;
import com.abo2.recode.dto.study.StudyResDto;
import com.abo2.recode.handler.ex.CustomApiException;
import com.abo2.recode.service.EmailService;
import com.abo2.recode.service.StudyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api")
public class StudyMemberController {

    private static final Logger logger = LoggerFactory.getLogger(StudyroomController.class);

    private StudyService studyService;

    private EmailService emailService;

    private UserRepository userRepository;

    @Autowired
    public StudyMemberController(StudyService studyService, EmailService emailService, UserRepository userRepository) {
        this.studyService = studyService;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    //스터디 조장의 스터디 조원 승인
    @PostMapping(value = "/v1/study-member/{studyId}/{userId}")
    public ResponseEntity<?> membershipUpdate(
            @RequestBody StudyReqDto.StudyMembershipReqDto studyMembershipReqDto,
            @AuthenticationPrincipal LoginUser loginUser,
            @PathVariable(name = "studyId") Long studyId,
            @PathVariable(name = "userId") Long userId
    ) throws MessagingException {

        StudyResDto.StudyMembershipResDto studyMembershipResDto;

        // 승인 대상 유저가 실제로 존재하는 지 체크
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            return ResponseEntity.badRequest().body(new ResponseDto<>(-1, "유저를 찾을 수 없습니다.", null));
        }

        User user = userOpt.get();

        //승인 API 보내는 사람이 실제 조장이 맞는지 체크
        if (
                loginUser.getUser().getId() != studyService.findcreatedByBystudyId(studyId)
        ) {
            throw new CustomApiException("조장만이 가입 신청을 처리 할 수 있습니다.");
        }

        if (studyMembershipReqDto.getStatus().equals("Approved")) {
            // 승인 된 경우
            //StudyMember 테이블 업데이트(상태값 0 -> 1)
            studyService.membershipUpdate(studyMembershipReqDto.getStatus(),
                    studyId, userId);

            studyMembershipResDto = StudyResDto.StudyMembershipResDto.builder()
                    .id(loginUser.getUser().getId())
                    .studyId(studyId)
                    .userId(userId)
                    .status("Approved")
                    .build();

            emailService.sendApprovedEmail(studyId, userId, user);

            return new ResponseEntity<>(new ResponseDto<>(1, "가입 신청이 승인되었습니다.",
                    studyMembershipResDto), HttpStatus.OK);
        }
        // 거절 된 경우
        //StudyMember 테이블 업데이트(상태값 0 -> 2)
        studyService.membershipUpdate(studyMembershipReqDto.getStatus(),
                studyId, userId);

        studyMembershipResDto = StudyResDto.StudyMembershipResDto.builder()
                .id(loginUser.getUser().getId())
                .studyId(studyId)
                .userId(userId)
                .status("Rejected")
                .build();

        emailService.sendRejectedEmail(studyId, userId, user);

        return new ResponseEntity<>(new ResponseDto<>(1, "가입 신청이 거부되었습니다.",
                studyMembershipResDto), HttpStatus.OK);

    }
}