package com.hx.hx.tool;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author HiWin11
 */
public  class TimestampExample {

   private  static  LocalDate today = LocalDate.now();

    // 凌晨1点时间戳
    public static long getStartTimeStamp(){
        LocalDateTime startTime = today.atTime(1,  0);
        long timestamp1am = startTime
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        return timestamp1am;
    }

    // 凌晨23点时间戳
    public static long getEndTimeStamp(){
        LocalDateTime startTime = today.atTime(23,  0);
        long timestamp2am = startTime
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        return timestamp2am;
    }


}
