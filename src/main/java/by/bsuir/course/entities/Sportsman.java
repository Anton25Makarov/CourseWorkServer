package by.bsuir.course.entities;

import java.io.Serializable;

public class Sportsman extends Human implements Serializable {
    private Sport performance;

    public Sport getPerformance() {
        return performance;
    }

    public void setPerformance(Sport performance) {
        this.performance = performance;
    }

}
