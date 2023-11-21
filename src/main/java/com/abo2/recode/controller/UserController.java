package com.abo2.recode.controller;

import com.abo2.recode.config.auth.LoginUser;
import com.abo2.recode.domain.studyroom.StudyRoom;
import com.abo2.recode.domain.user.User;
import com.abo2.recode.dto.ResponseDto;
import com.abo2.recode.dto.study.StudyResDto;
import com.abo2.recode.dto.user.UserReqDto;
import com.abo2.recode.dto.user.UserRespDto;
import com.abo2.recode.service.StudyService;
import com.abo2.recode.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {

    private final UserService userService;
    private final StudyService studyService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid UserReqDto.JoinReqDto joinReqDto, BindingResult bindingResult) {
        UserRespDto.JoinRespDto joinRespDto = userService.회원가입(joinReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 성공", joinRespDto), HttpStatus.CREATED);
    }

    @PostMapping("/admin_join")
    public ResponseEntity<?> admin_join(@RequestBody @Valid UserReqDto.JoinAdminReqDto joinAdminReqDto, BindingResult bindingResult) {
        UserRespDto.JoinRespDto joinRespDto = userService.admin_join(joinAdminReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "관리자 회원가입 성공", joinRespDto), HttpStatus.CREATED);
    }

    @PostMapping("/find-username")
    public ResponseEntity<?> findUsername(@RequestBody @Valid UserReqDto.FindUsernameReqDto findUsernameReqDto, BindingResult bindingResult){
        UserRespDto.FindUsernameRespDto findUsernameRespDto = userService.findUsername(findUsernameReqDto);
        return  new ResponseEntity<>(new ResponseDto<>(1, "아이디 찾기 성공", findUsernameRespDto), HttpStatus.OK);
    }

    @GetMapping("/user-name/{username}/exists")
    public ResponseEntity<?> checkIdDuplicate(@PathVariable @Valid String username){

        // 1. username 중복 값 확인
        if (userService.checkUsernameDuplicate(username) == true) {
            return new ResponseEntity<>(new ResponseDto<>(-1, "이미 사용 중인 아이디 입니다.", null), HttpStatus.CONFLICT);
        }else {
            return new ResponseEntity<>(new ResponseDto<>(1,"사용 가능한 아이디 입니다.", null), HttpStatus.OK);
        }
    }

    @GetMapping("/user-email/{email}/exists")
    public ResponseEntity<?> checkEmailDuplicate(@PathVariable @Valid String email){

        // 1. email 중복 값 확인
        if (userService.checkEmailDuplicate(email) == true) {
            return new ResponseEntity<>(new ResponseDto<>(-1, "이미 사용 중인 이메일 입니다.", null), HttpStatus.CONFLICT);
        }else {
            return new ResponseEntity<>(new ResponseDto<>(1,"사용 가능한 이메일 입니다.", null), HttpStatus.OK);
        }
    }

    @PutMapping("/v1/users/{id}")
    public ResponseEntity<?> modifyUserInfo(@AuthenticationPrincipal LoginUser loginUser, @RequestBody @Valid UserReqDto.UpdateUserReqDto updateUserReqDto, BindingResult bindingResult){
        UserRespDto.UpdateUserRespDto updateUserRespDto = userService.updateUser(loginUser.getUser().getId(), updateUserReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1,"회원정보 수정 성공", updateUserRespDto), HttpStatus.OK);
    }

    @PostMapping(value = "/v1/mypage/{id}/essay")
    public ResponseEntity<?> writeEssay(@AuthenticationPrincipal LoginUser loginUser, @RequestBody @Valid UserReqDto.WriteEssayReqDto writeEssayReqDto, BindingResult bindingResult){
        UserRespDto.EssayRespDto essayRespDto = userService.writeEssay(loginUser.getUser().getId(), writeEssayReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "소개글이 성공적으로 업데이트 되었습니다.", essayRespDto), HttpStatus.OK);
    }

    @GetMapping(value = "/v1/mypage/{id}/getessay")
    public ResponseEntity<?> getEssay(@AuthenticationPrincipal LoginUser loginUser){
        UserRespDto.EssayRespDto essayRespDto = userService.getEssay(loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "소개글 조회에 성공하였습니다.", essayRespDto), HttpStatus.OK);
    }

    @PostMapping(value = "/v1/users/{id}/withdraw")
    public ResponseEntity<?> withdrawUser(@AuthenticationPrincipal LoginUser loginUser){
        userService.withdrawUser(loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "탈퇴에 성공하였습니다.", null), HttpStatus.OK);
    }

    @GetMapping(value = "/v1/users/{id}/getuser")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal LoginUser loginUser){
        UserRespDto.getUserInfoDto getUserInfoDto = userService.getUserInfo(loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "개인 정보 조회에 성공하였습니다", getUserInfoDto), HttpStatus.OK);
    }

    @GetMapping(value = "/v1/users/{id}/study-applications")
    public ResponseEntity<?> myStudy(@AuthenticationPrincipal LoginUser loginUser){
        List<StudyResDto.MyStudyRespDto> myStudyRespDto = userService.myStudy(loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "사용자의 스터디 가입 신청 목록을 성공적으로 조회했습니다.", myStudyRespDto), HttpStatus.OK);
    }

    // 스터디 룸 가입 여부 확인
    @GetMapping("/v1/users/{userId}/studyrooms/{studyRoomId}/isInStudyRoom")
    public ResponseEntity<Boolean> isInStudyRoom(@PathVariable Long userId, @PathVariable Long studyRoomId) {
        User user = userService.findUserById(userId);
        StudyRoom studyRoom = studyService.findStudyRoomById(studyRoomId);

        boolean isInStudyRoom = studyService.isUserInStudyRoom(user, studyRoom);
        return ResponseEntity.ok(isInStudyRoom);
    }

    @PostMapping(value = {"/v1/change-password", "/change-password"})
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal LoginUser loginUser, @RequestBody @Valid UserReqDto.ChangePasswordReqDto changePasswordReqDto) {
        UserRespDto.changePasswordRespDto changePasswordRespDto = userService.changePassword(loginUser.getUser().getId(), changePasswordReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "비밀번호 변경 성공",changePasswordRespDto), HttpStatus.OK);
    }

}
