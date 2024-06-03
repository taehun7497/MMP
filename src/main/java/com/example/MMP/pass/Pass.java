package com.example.MMP.pass;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Pass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passId;

    private String passName;

    private String passTitle;

    private Long passCount;

    private LocalDateTime passStart;

    private LocalDateTime passFinish;

    private int passPrice;

    private LocalDateTime createDate;
}
