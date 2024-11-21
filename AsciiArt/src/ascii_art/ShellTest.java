package ascii_art;

import org.junit.jupiter.api.Test;
import org.testng.Assert;

import static org.junit.jupiter.api.Assertions.*;

class ShellTest {

    @Test
    void parseCharRange_invalidInput() {
        Assert.assertThrows(IllegalArgumentException.class, ()->Shell.parseCharRange("balbal"));
        Assert.assertThrows(IllegalArgumentException.class, ()-> Shell.parseCharRange("c-a"));
        Assert.assertThrows(IllegalArgumentException.class, ()-> Shell.parseCharRange("c-"));
        Assert.assertThrows(IllegalArgumentException.class, ()-> Shell.parseCharRange("ac"));
        Assert.assertThrows(IllegalArgumentException.class, ()-> Shell.parseCharRange("a-cb"));
    }
    @Test
    void parseCharRange_validInput(){
        assertArrayEquals(new char[]{'a', 'c'}, Shell.parseCharRange("a-c"));
        assertArrayEquals(new char[]{' ', ' '}, Shell.parseCharRange("space"));
        assertArrayEquals(new char[]{' ', '~'}, Shell.parseCharRange("all"));
        assertArrayEquals(new char[]{'a', 'a'}, Shell.parseCharRange("a"));
    }
}