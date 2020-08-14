package edu.duke.ece.fantasy.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class MessageTask extends DistributeTask {
    private final Method method;
    private final Object[] para;
    private final Object controller;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public MessageTask(Method method, Object controller, Object[] para, int distributeKey) {
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
