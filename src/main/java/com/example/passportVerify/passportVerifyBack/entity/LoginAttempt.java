package com.example.passportVerify.passportVerifyBack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class LoginAttempt {
    @Id
    @GeneratedValue
    private Long id;
    private int failedAttempt;
    private Date time;
    @OneToOne
    private User userId;
}
