package by.bsuir.course.entities;

import java.io.Serializable;

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
}
