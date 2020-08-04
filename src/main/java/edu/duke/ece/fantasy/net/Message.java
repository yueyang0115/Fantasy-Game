package edu.duke.ece.fantasy.net;


import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * base class of IO message
 */
public abstract class Message {

    /**
     * messageMeta, module of message
     * @return
     */
    @JsonIgnore
    public short getModule() {
        MessageMeta annotation = getClass().getAnnotation(MessageMeta.class);
        if (annotation != null) {
            return annotation.module();
        }
        return 0;
    }

    /**
     * messageMeta, subType of module
     * @return
     */
    @JsonIgnore
    public byte getCmd() {
        MessageMeta annotation = getClass().getAnnotation(MessageMeta.class);
        if (annotation != null) {
            return annotation.cmd();
        }
        return 0;
    }

    public String key() {
        return this.getModule() + "_" + this.getCmd();
    }

}
