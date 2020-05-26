package edu.duke.ece.fantacy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantacy.json.MessagesS2C;
import edu.duke.ece.fantacy.json.PositionResultMessage;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TerritoryHandlerTest {
    TerritoryHandler th = new TerritoryHandler();
    Logger logger = LoggerFactory.getLogger(TerritoryHandler.class);
    ObjectMapper objectMapper = new ObjectMapper();
    double latitude = 36.0985416;
    double longitude = -78.80034;
    int wid = 0;
    int[] coor = th.MillierConvertion(latitude,longitude);
    TerrainHandler terrainHandler = new TerrainHandler();

    @Test
    void getTerritories() {
        try {
            terrainHandler.initialTerrain();
            th.addTerritories(wid, latitude, longitude);
            List<Territory> res = th.getTerritories(wid, latitude, longitude);
            res = th.getTerritories(wid, latitude, longitude);
            assertEquals(9, res.size());


            MessagesS2C msg = new MessagesS2C();
            PositionResultMessage positionResultMessage = new PositionResultMessage();
            positionResultMessage.setTerritoryArray(res);
            msg.setPositionResultMessage(positionResultMessage);
            try{
                logger.info(objectMapper.writeValueAsString(msg));
            } catch (JsonProcessingException e){
                logger.debug(e.getMessage());
            }
        } catch (Exception e) {
            HibernateUtil.shutdown();
        } finally {
            HibernateUtil.shutdown();
        }



    }

    @Test
    void conversion(){
        int[] res = th.MillierConvertion(latitude,longitude);
        logger.info(res[0]+","+res[1]);
    }

    @Test
    void updateTerritory() {
        th.addTerritory(wid,coor[0],coor[1],"unexplored");
        th.updateTerritory(wid,coor[0],coor[1],"explored");
        assertEquals("explored",th.getTerritory(wid,coor[0],coor[1]).getStatus());
    }
}