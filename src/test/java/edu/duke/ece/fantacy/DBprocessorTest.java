package edu.duke.ece.fantacy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DBprocessorTest {
    DBprocessor db = new DBprocessor();
    @Test
    void connectDB() {
        db.connectDB();
    }
}