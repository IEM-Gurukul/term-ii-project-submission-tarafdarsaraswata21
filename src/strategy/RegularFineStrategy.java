package strategy;

public class RegularFineStrategy implements FineStrategy {

    private static final double FINE_PER_DAY = 2.0;

    @Override
    public double calculateFine(long daysOverdue) {
        if (daysOverdue <= 0) {
            return 0.0;
        }
        return daysOverdue * FINE_PER_DAY;
    }

    @Override
    public String getStrategyName() {
        return "Regular Member Fine (Rs. 2.00 per day)";
    }
}
