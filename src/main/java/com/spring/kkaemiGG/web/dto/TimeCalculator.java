package com.spring.kkaemiGG.web.dto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

public class TimeCalculator {

    public static String untilNow(Temporal temporal) {
        Temporal now = LocalDateTime.now();
        return temporal.until(now, ChronoUnit.YEARS) != 0 ? temporal.until(now, ChronoUnit.YEARS) + "년 전"
                : temporal.until(now, ChronoUnit.MONTHS) != 0 ? temporal.until(now, ChronoUnit.MONTHS) + "개월 전"
                : temporal.until(now, ChronoUnit.WEEKS) != 0 ? temporal.until(now, ChronoUnit.WEEKS) + "주일 전"
                : temporal.until(now, ChronoUnit.DAYS) != 0 ? temporal.until(now, ChronoUnit.DAYS) + "일 전"
                : temporal.until(now, ChronoUnit.HOURS) != 0 ? temporal.until(now, ChronoUnit.HOURS) + "시간 전"
                : temporal.until(now, ChronoUnit.MINUTES) != 0 ? temporal.until(now, ChronoUnit.MINUTES) + "분 전"
                : temporal.until(now, ChronoUnit.SECONDS) != 0 ? temporal.until(now, ChronoUnit.SECONDS) + "초 전"
                : "1초 전";
    }

}
