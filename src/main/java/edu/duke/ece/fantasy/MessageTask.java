package edu.duke.ece.fantasy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MessageTask extends DistributeTask {
    private Method method;
    private Object[] para;
    private Object controller;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public MessageTask(Method method, Object[] para, Object controller, int distributeKey) {
        super(distributeKey);
        this.method = method;
        this.para = para;
        this.controller = controller;
    }

    @Override
    public void action() {
        try {
            method.invoke(controller, para);
        } catch (Exception e) {
            logger.error("message task execution failed,", e);
        }
    }
}
