package ca.cmpt276.prj.model;

import org.junit.jupiter.api.Test;

import ca.cmpt276.prj.model.coinManager.CoinSide;
import ca.cmpt276.prj.model.coinManager.CoinManager;

import static org.junit.jupiter.api.Assertions.*;

class CoinManagerTest {
    @Test
    void childrenList() {
        CoinManager coinManager =  CoinManager.getInstance();
        coinManager.wipeChildrens();
        coinManager.wipeRecord();

        Child child1 = new Child("Gwyndolin", CoinSide.HEAD);
        Child child2 = new Child("Gwyn", CoinSide.TAIL);
        Child child3 = new Child("Gwynevere",CoinSide.HEAD);
        coinManager.addChild(child1);
        coinManager.addChild(child2);
        coinManager.addChild(child3);

        assertEquals("Gwyndolin", coinManager.getChild(0).getName());
        assertEquals("Gwyn", coinManager.getChild(1).getName());
        assertEquals("Gwynevere", coinManager.getChild(2).getName());
    }

    @Test
    void flip() {
        CoinManager coinManager = CoinManager.getInstance();
        coinManager.wipeChildrens();
        coinManager.wipeRecord();

        Child child1 = new Child("Gwyndolin", CoinSide.HEAD);
        Child child2 = new Child("Gwyn", CoinSide.TAIL);
        Child child3 = new Child("Gwynevere", CoinSide.HEAD);
        coinManager.addChild(child1);
        coinManager.addChild(child2);
        coinManager.addChild(child3);

        for (int i = 0; i< coinManager.getChildrenList().size(); i++) {
            coinManager.flip();
        }
        assertNotNull(coinManager.getWinner());
    }
}