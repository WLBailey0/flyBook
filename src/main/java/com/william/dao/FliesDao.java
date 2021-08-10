package com.william.dao;

import com.william.model.Flies;

import java.util.List;

public interface FliesDao {

    List<Flies> getAllFlies();

    List<Flies> getFliesBySeason(String season);

    Flies getFly(int flyId);
    //todo put all three under a "modify fly" in CLI
    Flies createFly(Flies fly);

    void updateFly(Flies fly);

    void deleteFly(int flyId);

    Flies getRecipe(int parseInt);
}
