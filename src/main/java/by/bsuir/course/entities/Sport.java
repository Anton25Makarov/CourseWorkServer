package by.bsuir.course.entities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class Sport implements Serializable {
    protected String name;
    protected Map<Referee, Mark> marks;

    public Sport(String name) {
        marks = new HashMap<>();
        this.name = name;
    }

    public abstract void addResult(Referee referee, Mark mark);

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
}
