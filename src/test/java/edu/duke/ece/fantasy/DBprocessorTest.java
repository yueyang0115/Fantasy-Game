//package edu.duke.ece.fantasy;
//
//import org.junit.jupiter.api.Test;
//
//class DBprocessorTest {
//
//    @Test
//    void connectDB() {
//        int id = 8;
//        int re1 = 0;
//        int re2 = 0;
//        int re3 = 0;
//
//        DBprocessor processor = new DBprocessor();
//        processor.connectDB();
//
//       processor.addUser("Junqi","SJQ1234",id);
//       re1 = processor.checkUser("Junqi","SJQ1234");
//       System.out.printf("wid is %d\n", re1);
//        re2 = processor.checkUser("Junqi","123");
//        System.out.printf("wid is %d\n", re2);
//        re3 = processor.checkUser("Drew","1234");
//        System.out.printf("wid is %d\n", re3);
//
//
//        int wid = 8;
//        double x = 17.000000000000;
//        double y = 99.012345678901;
//        String stat = "rest";
//        boolean exist = true;
////        int twid1 = 8;
////        double x1 = 17.000000000000;
////        double y1 = 99.012345678901;
//
//        int twid2 = 8;
//        double x2 = 18.0000000000;
//        double y2 = 79.123456789012;
//
//        processor.addTerritory(wid, x, y, stat);
//        exist=processor.checkTerritory(wid,x,y);
//        System.out.printf("status exist? %b\n", exist);
//        exist=processor.checkTerritory(twid2,x2,y2);
//        System.out.printf("status exist? %b\n", exist);
//    }
//}