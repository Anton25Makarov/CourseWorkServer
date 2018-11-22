package by.bsuir.course.entities;

import java.io.Serializable;

public class Diving extends Sport implements Serializable {
    private static final double MAX_MARK = 10;
    private static final int COUNT_OF_JUDGES = 5;

    public Diving(String name) {
        super(name);
    }

    @Override
    public void addResult(Referee referee, Mark mark) {
        marks.put(referee, mark);
    }


}
