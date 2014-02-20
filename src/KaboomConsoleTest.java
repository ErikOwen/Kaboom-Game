import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;

import junit.framework.TestCase;


public class KaboomConsoleTest extends TestCase {

	public KaboomConsoleTest()
	{
		
	}
	
	public void setUp() throws Exception {
		super.setUp();
		
        // Delete any hall of fame file before each test.
        try
        {
            File file = new File("kaboom/halloffame.ser");
            file.delete();
        }
        catch(Exception e){
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

}
