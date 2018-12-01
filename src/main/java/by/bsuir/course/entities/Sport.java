package by.bsuir.course.entities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class Sport implements Serializable {
    protected String name;
    protected Map<Referee, Mark> marks;

    public Sport(String name) {
        marks = new HashMap<>();
        this.name = name;
    }

    public abstract void addResult(Referee referee, Mark mark);

    public abstract int getCountOfReferees();

    public abstract double getMaxMark();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Referee, Mark> getMarks() {
        return marks;
    }

    public void setMarks(Map<Referee, Mark> marks) {
        this.marks = marks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sport sport = (Sport) o;
        return Objects.equals(name, sport.name) &&
                Objects.equals(marks, sport.marks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, marks);
    }
}
