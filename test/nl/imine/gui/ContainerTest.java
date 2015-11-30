package nl.imine.gui;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Sander
 */
public class ContainerTest {

    public ContainerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getInventorySize method, of class Container.
     */
    @Test
    public void testGetInventorySize() {
        System.out.println("getInventorySize");
        assertEquals(9, Container.getInventorySize(5));
        assertEquals(18, Container.getInventorySize(17));
        assertEquals(45, Container.getInventorySize(100));
    }

}
