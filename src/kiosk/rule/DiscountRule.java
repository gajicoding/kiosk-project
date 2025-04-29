package kiosk.rule;

import java.util.Arrays;

public enum DiscountRule {
    NATIONAL_MERIT("Veteran", 10, "국가유공자"),
    MILITARY("Soldier", 5, "군인"),
    STUDENT("Student", 3, "학생"),
    GENERAL("General", 0, "일반");

    private final String type;
    private final int percentage;
    private final String typeKr;

    DiscountRule(String type, int percentage, String typeKr) {
        this.type = type;
        this.percentage = percentage;
        this.typeKr = typeKr;
    }

    public String getType() {
        return type;
    }

    public int getPercentage() {
        return percentage;
    }

    public String getTypeKr() {
        return typeKr;
    }

    public int discountApply(int totalPrice){
        return totalPrice - totalPrice*percentage/100;
    }

    public static DiscountRule selectNumOf(int num){
        return Arrays.stream(DiscountRule.values())
                .filter(rule -> rule.ordinal() == num)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
