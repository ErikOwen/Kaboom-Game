

/**
 * The test class HighScoreComparatorTest.
 *
 * @author  Erik Owen
 * @version 1
 */
public class HighScoreComparatorTest extends junit.framework.TestCase
{
    /**
     * Default constructor for test class HighScoreComparatorTest
     */
    public HighScoreComparatorTest()
    {
    }

    /**
     * Tests the HighScore functionality
     */
    public void test1()
    {
        HighScoreComparator comp = new HighScoreComparator();
        
        assertEquals(-1, comp.compare(new HighScore(1, "Joe"),
            new HighScore(3, "Steve")));
        assertEquals(1, comp.compare(new HighScore(66, "Joe"), new HighScore(33,
            "Steve")));
        assertEquals(1, comp.compare(new HighScore(5, "George"), new HighScore(2,
            "Mavin")));
        assertEquals(1, comp.compare(new HighScore(5, "April"), new HighScore(5,
            "Alvin")));
        assertEquals(-1, comp.compare(new HighScore(9, "Sue"), new HighScore(9,
            "Tom")));
        assertEquals(0, comp.compare(new HighScore(9, "Bab"), new HighScore(9,
            "Bab")));
    }
}

