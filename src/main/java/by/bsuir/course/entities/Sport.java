package by.bsuir.course.entities;

import java.util.Map;

public abstract class Sport {
    private String name;
    private Map<Referee, Mark> marks;

    public Sport(String name) {
        this.name = name;
    }

    public abstract void addMark(Mark mark);

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
