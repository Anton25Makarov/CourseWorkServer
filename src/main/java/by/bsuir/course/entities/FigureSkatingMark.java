package by.bsuir.course.entities;

import java.io.Serializable;
import java.util.Objects;

public class FigureSkatingMark extends Mark implements Serializable {
    private double technicalMark;
    private double presentationMark;

    public FigureSkatingMark(double technicalMark, double presentationMark) {
        this.technicalMark = technicalMark;
        this.presentationMark = presentationMark;
    }

    public double getTechnicalMark() {
        return technicalMark;
    }

    public void setTechnicalMark(double technicalMark) {
        this.technicalMark = technicalMark;
    }

    public double getPresentationMark() {
        return presentationMark;
    }

    public void setPresentationMark(double presentationMark) {
        this.presentationMark = presentationMark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FigureSkatingMark that = (FigureSkatingMark) o;
        return Double.compare(that.technicalMark, technicalMark) == 0 &&
                Double.compare(that.presentationMark, presentationMark) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(technicalMark, presentationMark);
    }
}
