import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;

import junit.framework.TestCase;

/**
 * Creates a new instance of the KaboomConsoleTest
 */
public class KaboomConsoleTest extends TestCase
{

    /**
     * Creates new instance of KaboomConsoleTest
     */
    public KaboomConsoleTest()
    {
        
    }
    
    /**
     * Sets up structure before tests are run
     */
    public void setUp() throws Exception
    {
        super.setUp();
        
        // Delete any hall of fame file before each test.
        try
        {
            File file = new File("kaboom/halloffame.ser");
            file.delete();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Test the Kaboom Console functionality
     */
    public void test1()
    {
        try
        {
            KaboomConsole app = new KaboomConsole();
            app.setIOsources(new FileReader(new File("testData/testInput1.txt")),
                new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("testData/actualOutput1.txt"), "utf-8")));
            
            app.run();
        }
        catch(Exception e)
        {
            System.out.print("");
        }
    }
    
    /**
     * Test the Kaboom Console functionality
     */
    public void test2()
    {
        try
        {
            KaboomConsole app = new KaboomConsole();
            app.setIOsources(new FileReader(new File("testData/testInput2.txt")),
                new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("testData/actualOutput2.txt"), "utf-8")));
            
            app.run();
        }
        catch(Exception e)
        {
            System.out.print("");
        }
    }

}
