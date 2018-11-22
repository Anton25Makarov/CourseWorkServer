package by.bsuir.course.entities;

import java.io.Serializable;

public class SkiJumping extends Sport implements Serializable {
    private static final double MAX_MARK = 6;
    private static final int COUNT_OF_JUDGES = 4;

    public SkiJumping(String name) {
        super(name);
    }

    @Override
    public void addResult(Referee referee, Mark mark) {
        marks.put(referee, mark);
    }
}
