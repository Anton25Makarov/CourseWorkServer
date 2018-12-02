package by.bsuir.course.factory;

import by.bsuir.course.entities.*;

public class SportFactory {
    public Sport createSport(String sport) {
        switch (sport) {
            case "Фигурное катание":
                return new FigureSkating(sport);
            case "Дайвинг":
                return new Diving(sport);
            case "Прыжки с трамплина":
                return new SkiJumping(sport);
            default:
                throw new UnsupportedOperationException();
        }
    }
}
