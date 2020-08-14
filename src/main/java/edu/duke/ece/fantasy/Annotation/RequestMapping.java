package edu.duke.ece.fantasy.Annotation;

import java.lang.annotation.*;

/**
 * A method whose type is meta-annotated with this
 * is used to be a logic handler
 * @author kingston
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {

}