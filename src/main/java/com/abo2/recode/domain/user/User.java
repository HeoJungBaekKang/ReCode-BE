package com.abo2.recode.domain.user;

import com.abo2.recode.domain.quiz.Quiz;
import com.abo2.recode.domain.studyroom.Attendance;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name ="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(length = 100)
    private String essay;

    @Column(nullable = false, length = 20)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserEnum role;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public User(Long id, String username, String nickname, String password, String essay, String email, UserEnum role, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.essay = essay;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }



    public void updateUser(String nickname, String email){
        this.email = email;
        this.nickname = nickname;
    }

    public void writeEssay(String essay){
        this.essay = essay;
    }

}
