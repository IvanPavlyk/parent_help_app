package ca.cmpt276.prj.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    @Test
    void childrenList() {
        Game game =  Game.getInstance();
        game.wipeChildrens();
        game.wipeRecord();

        Child child1 = new Child("Gwyndolin", CoinSide.HEAD);
        Child child2 = new Child("Gwyn", CoinSide.TAIL);
        Child child3 = new Child("Gwynevere",CoinSide.HEAD);
        game.addChild(child1);
        game.addChild(child2);
        game.addChild(child3);

        assertEquals("Gwyndolin", game.getChild(0).getName());
        assertEquals("Gwyn", game.getChild(1).getName());
        assertEquals("Gwynevere", game.getChild(2).getName());
    }

    @Test
    void flip() {
        Game game = Game.getInstance();
        game.wipeChildrens();
        game.wipeRecord();

        Child child1 = new Child("Gwyndolin", CoinSide.HEAD);
        Child child2 = new Child("Gwyn", CoinSide.TAIL);
        Child child3 = new Child("Gwynevere", CoinSide.HEAD);
        game.addChild(child1);
        game.addChild(child2);
        game.addChild(child3);

        for (int i=0; i<game.getChildrenList().size(); i++) {
            game.flip();
        }
        assertNotNull(game.getWinner());
    }
}