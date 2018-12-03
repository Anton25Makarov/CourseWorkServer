package by.bsuir.course.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Sportsman extends Human implements Serializable, MarkCalculator {
    private Sport performance;

    public Sport getPerformance() {
        return performance;
    }

    public void setPerformance(Sport performance) {
        this.performance = performance;
    }

    @Override
    public double calculateMark() {
        double total = 0;
        if (performance instanceof FigureSkating) {
            total = calculateFigureSkatingMark();
        } else if (performance instanceof Diving) {
            total = calculateDivingMark();
        } else if (performance instanceof SkiJumping) {
            total = calculateSkiJumpingMark();
        }
        return total;
    }

    private double calculateFigureSkatingMark() {
        List<Mark> marks = new ArrayList<>(performance.marks.values());
        double total = 0;
        for (Mark mark : marks) {
            double technicalMark = ((FigureSkatingMark) mark).getTechnicalMark();
            double presentationMark = ((FigureSkatingMark) mark).getPresentationMark();
            total += (technicalMark + presentationMark) / 2;
        }
        return total / marks.size();
    }

    private double calculateDivingMark() {
        List<Mark> marks = new ArrayList<>(performance.marks.values());
        double total = 0;
        if (marks.size() == 3) {
            marks = marks.stream()
                    .sorted(Comparator.comparingDouble(mark -> ((DivingMark) mark).getMark()))
                    .collect(Collectors.toList());
            return ((DivingMark) marks.get(1)).getMark() * 0.6;
        }
        return 0;
    }

    private double calculateSkiJumpingMark() {
        List<Mark> marks = new ArrayList<>(performance.marks.values());
        double total = 0;
        marks = marks.stream()
                .sorted(Comparator.comparingDouble(mark -> ((DivingMark) mark).getMark()))
                .collect(Collectors.toList());
        if (marks.size() > 2) {
            marks.remove(0);
            marks.remove(marks.size() - 1);
            for (Mark mark : marks) {
                total += ((SkiJumpingMark) mark).getMark();
            }
            return ((DivingMark) marks.get(1)).getMark() * 0.6;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Sportsman sportsman = (Sportsman) o;
        return Objects.equals(performance, sportsman.performance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), performance);
    }
}
