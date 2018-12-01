package by.bsuir.course.entities;

import java.io.Serializable;
import java.util.Objects;

public class DivingMark extends by.bsuir.course.entities.Mark implements Serializable {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DivingMark that = (DivingMark) o;
        return Double.compare(that.Mark, Mark) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Mark);
    }
}
