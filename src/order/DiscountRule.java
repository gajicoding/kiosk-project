package order;

public enum DiscountRule {
    NATIONAL_MERIT("국가유공자", 0.1),
    MILITARY("군인", 0.05),
    STUDENT("학생", 0.03),
    GENERAL("일반", 0);

    private final String type;
    private final double percentage;

    DiscountRule(String type, double percentage) {
        this.type = type;
        this.percentage = percentage;
    }


    public int discountApply(int totalPrice){
        return totalPrice - (int)(totalPrice*percentage);
    }

    public String getFormattedString() {
        return String.format("%s\t: %d%%", type, (int)(percentage*100));
    }
}
