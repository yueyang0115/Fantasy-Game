package edu.duke.ece.fantasy.net;

import edu.duke.ece.fantasy.SingleReflection;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum MessageUtil {
    INSTANCE;

    private Map<Integer, Class<?>> id2Clazz = new HashMap<>();

    MessageUtil() {
        initMessagePool();
    }

    /**
     * scan all message class and register into pool
     */
    public void initMessagePool() {
        Reflections r = SingleReflection.INSTANCE.getReflections();
        Set<Class<?>> messages = r.getTypesAnnotatedWith(MessageMeta.class);
        for (Class<?> messageType : messages) {
            MessageMeta meta = messageType.getAnnotation(MessageMeta.class); // meta will not be null
            int key = buildKey(meta.module(), meta.cmd());
            if (id2Clazz.containsKey(key)) {
                throw new RuntimeException("message meta [" + meta.module() + "]," + "[" + meta.cmd() + "] duplicate！！");
            }
            id2Clazz.put(key, messageType);
        }
    }

    public Class<?> getMessage(short module, byte cmd) {
        return id2Clazz.get(buildKey(module, cmd));
    }

    public Class<?> getMessage(int id) {
        short module = (short) (id / 1000);
        byte cmd = (byte) (id % 1000);
        return id2Clazz.get(buildKey(module, cmd));
    }

    private int buildKey(short module, byte cmd) {
        return module * 1000 + +cmd;
    }

}
