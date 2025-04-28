package kiosk.constants;

import java.util.Arrays;

public enum DiscountRule {
    NATIONAL_MERIT("국가유공자", 10),
    MILITARY("군인", 5),
    STUDENT("학생", 3),
    GENERAL("일반", 0);

    private final String type;
    private final int percentage;

    DiscountRule(String type, int percentage) {
        this.type = type;
        this.percentage = percentage;
    }

    public String getType() {
        return type;
    }

    public int getPercentage() {
        return percentage;
    }

    public int discountApply(int totalPrice){
        return totalPrice - totalPrice*percentage/100;
    }

//    public String getFormattedString() {
//        return String.format("%s\t: %d%%", type, percentage);
//    }

    public static DiscountRule selectNumOf(int num){
        return Arrays.stream(DiscountRule.values())
                .filter(rule -> rule.ordinal() == num)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
