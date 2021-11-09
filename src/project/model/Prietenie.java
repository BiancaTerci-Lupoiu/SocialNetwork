package project.model;

import java.time.LocalDateTime;

public class Prietenie extends Entity<Tuple<Integer,Integer>> {
    public Prietenie(Integer firstId, Integer secondId)
    {
        Integer first = Integer.min(firstId,secondId);
        Integer second = Integer.max(firstId,secondId);
        setId(new Tuple<>(first,second));
    }
}