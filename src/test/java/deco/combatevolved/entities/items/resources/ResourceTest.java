package deco.combatevolved.entities.items.resources;

import org.junit.Test;

public class ResourceTest {

    Resource resource;

    @Test
    public void testResource() {
        resource = new Resource("resource", 1);
        resource = new Resource("resource", 1, "id", "texture");
    }

}