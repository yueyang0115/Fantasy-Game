package edu.duke.ece.fantasy;

import org.reflections.Reflections;

public enum SingleReflection {
    INSTANCE;
    Reflections reflections = new Reflections("edu.duke.ece.fantasy");

    public Reflections getReflections(){
        return reflections;
    }
}
