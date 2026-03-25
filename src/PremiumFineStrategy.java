package strategy;

public class PremiumFineStrategy implements FineStrategy {

    private static final double FINE_PER_DAY = 1.0;
    private static final double MAX_FINE = 30.0;

    @Override
    public double calculateFine(long daysOverdue) {
        if (daysOverdue <= 0) {
            return 0.0;
        }
        double fine = daysOverdue * FINE_PER_DAY;
        return Math.min(fine, MAX_FINE);
    }

    @Override
    public String getStrategyName() {
        return "Premium Member Fine (Rs. 1.00 per day, max Rs. 30.00)";
    }
}
