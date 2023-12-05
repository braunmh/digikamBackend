package org.braun.digikam.backend.util;

import org.braun.digikam.backend.BadRequestException;

/**
 *
 * @author mbraun
 */
public final class UncompleteDateTime {

    private Integer year;
    private Integer month;
    private Integer day;
    private Integer hour;
    private Integer minute;
    private Integer second;
    
    private int[] VALID_MONTHS_NORMAL = new int[] {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private int[] VALID_MONTHS_LEAP   = new int[] {0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public UncompleteDateTime(String in) throws BadRequestException {
        if (in == null || in.isEmpty()) {
            return;
        }
        switch(in.length()) {
            case 8:
                in = in + "------";
                break;
            case 12:
                in = in + "--";
                break;
            case 14:
                break;
            default:
                throw new BadRequestException("Invalid date. Length must be 14.");
        }
        year = toInt(in.substring(0, 4));
        month = toInt(in.substring(4, 6));
        day = toInt(in.substring(6, 8));
        hour = toInt(in.substring(8, 10));
        minute = toInt(in.substring(10, 12));
        second = toInt(in.substring(12, 14));
        if (minute == null && second != null) {
            throw new BadRequestException("Invalid Date-Pattern. Second must be empty.");
        }
        if (hour == null && minute != null) {
            throw new BadRequestException("Invalid Date-Pattern. Minute must be empty.");
        }
        if (day == null && hour != null) {
            throw new BadRequestException("Invalid Date-Pattern. Hour must be empty.");
        }
        if (month == null && day != null) {
            throw new BadRequestException("Invalid Date-Pattern. Day must be empty.");
        }
        if (getMonth() != null) {
            checkRange("Month", getMonth(), 1, 12);
            
            if (getDay() != null) {
                if (isLeapYear(getYear())) {
                    checkRange("Day", getDay(), 1, VALID_MONTHS_NORMAL[getMonth()]);
                } else {
                    checkRange("Day", getDay(), 1, VALID_MONTHS_LEAP[getMonth()]);
                }
            }
        }
        if (getHour() != null) {
            checkRange("Hour", getHour(), 0, 23);
        }
        if (getMinute() != null) {
            checkRange("Minute", getMinute(), 0, 59);
        }
        if (getSecond()!= null) {
            checkRange("Minute", getSecond(), 0, 59);
        }
    }

    public Integer getYear() {
        return year;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getDay() {
        return day;
    }

    public Integer getHour() {
        return hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public Integer getSecond() {
        return second;
    }

    private Integer toInt(String value) throws BadRequestException {
        if ("--".equals(value) || "----".equals(value)) {
            return null;
        }
        try {
            return Integer.parseUnsignedInt(value);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid date " + value);
        }
    }

    private void checkRange(String type, Integer value, int begin, int end) throws BadRequestException {
        if (value < begin || value > end) {
            throw new BadRequestException("Invalid value for " + type);
        }
    }

    /**
     * Ein Jahr ist ein Schaltjahr, wenn die Jahreszahl durch 4 teilbar ist. wenn die Jahreszahl durch 4, aber nicht durch 100
     * teilbar ist. wenn die Jahreszahl durch 4, durch 100 und durch 400 teilbar ist.
     */
    private boolean isLeapYear(int year) {
        if (year % 4 == 0) {
            if (year % 100 == 0 && year % 400 == 0) {
                return true;
            } else {
                if (year % 100 == 0 && year % 400 != 0) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static void main(String... args) {
        String tst = "20131230153054";
        try {
            UncompleteDateTime u = new UncompleteDateTime(tst);
            System.out.println(u.getYear());
        } catch (BadRequestException e) {
            e.printStackTrace(System.out);
        }
    }
}
