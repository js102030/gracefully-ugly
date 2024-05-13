package com.gracefullyugly.domain.verification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Verification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "verification_id")
    private Long id;

    private Long userId;

    private String verificationCode;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;

    @Builder
    public Verification(Long userId, String verificationCode, Date expiryDate) {
        this.userId = userId;
        this.verificationCode = verificationCode;
        this.expiryDate = expiryDate;
    }

}
