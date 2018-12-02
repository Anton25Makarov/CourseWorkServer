package by.bsuir.course.entities;

import java.io.Serializable;
import java.util.Objects;

public class Diving extends Sport implements Serializable {
    private double maxMark = 10;
    private int countOfReferees = 3;

    public Diving(String name) {
        super(name);
    }

    @Override
    public void addResult(Referee referee, Mark mark) {
        marks.put(referee, mark);
    }


    @Override
    public int getCountOfReferees() {
        return countOfReferees;
    }

    @Override
    public double getMaxMark() {
        return maxMark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Diving diving = (Diving) o;
        return Double.compare(diving.maxMark, maxMark) == 0 &&
                countOfReferees == diving.countOfReferees;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), maxMark, countOfReferees);
    }
}
