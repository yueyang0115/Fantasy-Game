package edu.duke.ece.fantasy;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

public enum SingleReflection {
    INSTANCE;

    Reflections reflections = new Reflections("edu.duke.ece.fantasy",
            new MethodAnnotationsScanner(),
            new TypeAnnotationsScanner(),
            new SubTypesScanner(false));

    public Reflections getReflections() {
        return reflections;
    }
}
