package by.bsuir.course.entities;

import java.io.Serializable;
import java.util.Objects;

public class Sportsman extends Human implements Serializable, MarkCalculator {
    private Sport performance;
    private double totalMark;

    public Sport getPerformance() {
        return performance;
    }

    public void setPerformance(Sport performance) {
        this.performance = performance;
    }

    public double getTotalMark() {
        return totalMark;
    }

    public void setTotalMark(double totalMark) {
        this.totalMark = totalMark;
    }

    @Override
    public void calculateMark() {
        throw new UnsupportedOperationException("I don't realise 'calculateMark()'");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Sportsman sportsman = (Sportsman) o;
        return Double.compare(sportsman.totalMark, totalMark) == 0 &&
                Objects.equals(performance, sportsman.performance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), performance, totalMark);
    }
}
