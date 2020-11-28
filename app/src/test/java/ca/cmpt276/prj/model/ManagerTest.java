package ca.cmpt276.prj.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {
    @Test
    void childrenList() {
        Manager manager =  Manager.getInstance();
        manager.wipeChildren();
        manager.wipeRecord();

        Child child1 = new Child("Gwyndolin", CoinSide.HEAD, null);
        Child child2 = new Child("Gwyn", CoinSide.TAIL, null);
        Child child3 = new Child("Gwynevere",CoinSide.HEAD, null);
        manager.appendChild(child1);
        manager.appendChild(child2);
        manager.appendChild(child3);

        assertEquals("Gwyndolin", manager.getChild(0).getName());
        assertEquals("Gwyn", manager.getChild(1).getName());
        assertEquals("Gwynevere", manager.getChild(2).getName());
    }

    @Test
    void flip() {
        Manager manager = Manager.getInstance();
        manager.wipeChildren();
        manager.wipeRecord();

        Child child1 = new Child("Gwyndolin", CoinSide.HEAD, null);
        Child child2 = new Child("Gwyn", CoinSide.TAIL, null);
        Child child3 = new Child("Gwynevere", CoinSide.HEAD, null);
        manager.appendChild(child1);
        manager.appendChild(child2);
        manager.appendChild(child3);

        for (int i = 0; i< manager.getChildrenList().size(); i++) {
            manager.flip();
        }
        assertNotNull(manager.getWinner());
    }
}