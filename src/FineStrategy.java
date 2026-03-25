package strategy;

public interface FineStrategy {

    double calculateFine(long daysOverdue);

    String getStrategyName();
}
