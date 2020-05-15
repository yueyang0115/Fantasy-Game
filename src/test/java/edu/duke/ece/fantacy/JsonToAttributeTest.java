package edu.duke.ece.fantacy;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class JsonToAttributeTest {
    @Test
    void Test() {
        String attributeStr = "{'location':{'x':'100','y':'100'}}";
        JsonToAttribute jsonToattribute = new JsonToAttribute(attributeStr);
        Attribute attribute = jsonToattribute.getAttribute();
        assertEquals(attribute.getLocation().getX(),100);

        AttributeToJson attributeTojson = new AttributeToJson(attribute);
        System.out.println(attributeTojson.getAttributeObj().toString());
    }
}
