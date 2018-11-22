package by.bsuir.course.entities;

import java.io.Serializable;

public class DivingMark extends Mark implements Serializable {
    private double Mark;

    public DivingMark(double mark) {
        Mark = mark;
    }

    public double getMark() {
        return Mark;
    }

    public void setMark(double mark) {
        Mark = mark;
    }
}
