package com.example.backend.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryListDto {
    private Integer seq;
    private String name;
    private LocalDateTime insertDttm;
    private LocalDateTime updateDttm;
    private Boolean useYn;

    public String getTimesAgo() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime insertDttm = this.getInsertDttm();

        Duration duration = Duration.between(insertDttm, now);

        long seconds = duration.toSeconds();

        if (seconds < 60) {                 // 1분 이내
            return "방금 전";
        } else if (seconds < 3600) {        // 1시간 이내
            long minutes = seconds / 60;
            return minutes + " 분 전";
        } else if (seconds < 60 * 60 * 24) {       // 1일 이내
            long hours = seconds / 3600;
            return hours + " 시간 전";
        } else if (seconds < 3600 * 24 * 7) {       // 1주일
            long days = seconds / 3600 / 24;
            return days + " 일 전";
        } else if (seconds < 3600 * 24 * 7 * 4) {       // 4주 이내
            long weeks = seconds / 3600 / 24 * 7;
            return weeks + " 주 전";
        } else {
            long days = duration.toDays();
            long years = days / 365;
            return years + " 년 전";
        }
    }

    public String getUpdateTimesAgo() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime updateDttm = this.getUpdateDttm();

        Duration duration = Duration.between(updateDttm, now);

        long seconds = duration.toSeconds();

        if (seconds < 60) {                 // 1분 이내
            return "방금 전";
        } else if (seconds < 3600) {        // 1시간 이내
            long minutes = seconds / 60;
            return minutes + " 분 전";
        } else if (seconds < 60 * 60 * 24) {       // 1일 이내
            long hours = seconds / 3600;
            return hours + " 시간 전";
        } else if (seconds < 3600 * 24 * 7) {       // 1주일
            long days = seconds / 3600 / 24;
            return days + " 일 전";
        } else if (seconds < 3600 * 24 * 7 * 4) {       // 4주 이내
            long weeks = seconds / 3600 / 24 * 7;
            return weeks + " 주 전";
        } else {
            long days = duration.toDays();
            long years = days / 365;
            return years + " 년 전";
        }
    }
}
