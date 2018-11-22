package by.bsuir.course.entities;

import java.io.Serializable;

public class FigureSkating extends Sport implements Serializable {
    private static final double MAX_MARK = 6;
    private static final int COUNT_OF_JUDGES = 4;

    public FigureSkating(String name) {
        super(name);

    }

    @Override
    public void addResult(Referee referee, Mark mark) {
        marks.put(referee, mark);
    }
}
