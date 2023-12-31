package com.abo2.recode.controller;

import com.abo2.recode.config.auth.LoginUser;
import com.abo2.recode.domain.qna.QnaReply;
import com.abo2.recode.domain.qna.QnaReplyRepository;
import com.abo2.recode.domain.user.UserEnum;
import com.abo2.recode.dto.ResponseDto;
import com.abo2.recode.dto.qna.QnaReplyDTO;
import com.abo2.recode.service.QnaReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class QnaReplyController {

    private final QnaReplyService qnaReplyService;
    private final QnaReplyRepository qnaReplyRepository;

    //Qna 댓글 생성
    @PostMapping("/qna-reply/{qnaId}")
    public ResponseEntity<?> postQnaReply(@AuthenticationPrincipal LoginUser loginUser, @PathVariable(value = "qnaId") Long qnaId, @RequestBody QnaReplyDTO qnaReplyDTO) {
        qnaReplyDTO.setUserId(loginUser.getUser().getId());

        qnaReplyService.postQnaReply(qnaId, qnaReplyDTO);

        System.out.println(qnaReplyDTO.getUserId());
        return new ResponseEntity<>(new ResponseDto<>(1, "Qna 댓글 작성 성공", qnaReplyDTO), HttpStatus.OK);
    }


    //Qna 댓글 삭제 (관리자 권한)
    @Secured(value = "ROLE_ADMIN")
    @DeleteMapping("/qna-reply/{qnaId}/{qnaReplyId}")
    public ResponseEntity<?> qnaReplyDelete(@AuthenticationPrincipal LoginUser loginUser, @PathVariable Long qnaId, @PathVariable Long qnaReplyId) {

        QnaReply reply = qnaReplyRepository.findById(qnaReplyId).orElseThrow();

        if (loginUser.getUser().getId().equals(reply.getUser().getId()) || loginUser.getUser().getRole() == UserEnum.ADMIN) {


            qnaReplyService.qnaReplyDelete(qnaReplyId);

            return new ResponseEntity<>(new ResponseDto<>(1, "Qna 댓글 삭제 성공", null), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseDto<>(-1, "권한 없음", null), HttpStatus.BAD_REQUEST);
    }
}