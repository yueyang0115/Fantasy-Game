package edu.duke.ece.fantasy.database;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.Item.Item;
import edu.duke.ece.fantasy.ObjectMapperFactory;

import javax.persistence.Embeddable;
import java.io.IOException;
import java.util.Objects;

@Embeddable
public class DBItem {
    private String item_class;
    private String item_properties;


    public DBItem() {
    }

    public DBItem(String item_class, String item_properties) {
        this.item_class = item_class;
        this.item_properties = item_properties;
    }

    public String getItem_class() {
        return item_class;
    }

    public void setItem_class(String item_class) {
        this.item_class = item_class;
    }

    public String getItem_properties() {
        return item_properties;
    }

    public void setItem_properties(String item_properties) {
        this.item_properties = item_properties;
    }

    public Item toGameItem() {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        try {
            if (!item_properties.equals("")) {
                return (Item) objectMapper.readValue(item_properties, Class.forName(item_class));
            } else {
                return (Item) Class.forName(item_class).getDeclaredConstructor().newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DBItem)) return false;
        DBItem dbItem = (DBItem) o;
        return Objects.equals(item_class, dbItem.item_class);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item_class, item_properties);
    }
}
