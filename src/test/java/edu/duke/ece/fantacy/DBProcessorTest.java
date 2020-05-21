package edu.duke.ece.fantacy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DBProcessorTest {

    @Test
    void connectDB() {
        DBProcessor processor = new DBProcessor();
        processor.connectDB();
    }
}