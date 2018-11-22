package by.bsuir.course.entities;

import java.io.Serializable;

public class SkiJumpingMark extends Mark implements Serializable {
    private double mark;

    public SkiJumpingMark(double mark) {
        this.mark = mark;
    }

    public double getMark() {
        return mark;
    }

    public void setMark(double mark) {
        this.mark = mark;
    }
}
