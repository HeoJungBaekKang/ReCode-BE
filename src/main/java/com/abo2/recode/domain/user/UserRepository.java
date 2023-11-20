package com.abo2.recode.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long userId);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    void deleteById(Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE StudyRoom sr SET sr.master = null WHERE sr.master.id = :userId")
    void dissociateStudyRooms(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE Post p SET p.user = null WHERE p.user.id = :userId")
    void dissociatePosts(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE PostReply pp SET pp.user.id = null WHERE pp.user.id = :userId")
    void dissociatePostReply(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE Qna q SET q.user_id = null WHERE q.user_id = :userId")
    void dissociateQnas(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE Study_Member sm SET sm.user.id = null WHERE sm.user.id = :userId")
    void dissociateStudyMember(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE Quiz q SET q.user.id = null WHERE q.user.id = :userId")
    void dissociateQuiz(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Attendance a WHERE a.user.id = :userId")
    void deleteUsersAttendance(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.id = :userId")
    void deleteWithoutRelatedInfo(@Param("userId") Long userId);


}
