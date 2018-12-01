package by.bsuir.course.entities;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkiJumpingMark that = (SkiJumpingMark) o;
        return Double.compare(that.mark, mark) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mark);
    }
}
