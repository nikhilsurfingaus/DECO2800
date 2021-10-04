package deco.combatevolved.entities.dynamicentities;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SkillNodeTest {

    @Test
    public void testAddChild() {
        SkillNode testChild = new SkillNode("test1", null, 0, null, 0);
        SkillNode testParent = new SkillNode("test2", null, 0, null, 0);
        testParent.addChild(testChild);
        assertTrue(testChild.getParents().get(0) == testParent);
        assertTrue(testParent.getChildren().get(0) == testChild);
    }

    @Test
    public void testIsRootNode() {
        SkillNode testChild = new SkillNode("test1", null, 0, null, 0);
        SkillNode testParent = new SkillNode("test2", null, 0, null, 0);
        testParent.addChild(testChild);
        assertTrue(testParent.isRootNode());
    }
}
