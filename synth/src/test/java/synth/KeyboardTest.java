package synth;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class KeyboardTest {
    private Keyboard keyboard;

    @Before
    public void setUp() {
        keyboard = new Keyboard();
    }

    @Test
    public void keysHaveFrequencies() {
        char[] keys = {'a', 'w', 's', 'd', 'r', 'f', 't', 'g', 'h', 'u', 'j','i', 'k', 'o', 'l', 'ö', 'å', 'ä', '0'};
        for (int i = 0; i < keys.length; i++) {
            char key = keys[i];
            assertTrue(keyboard.frequencyOf(java.awt.event.KeyEvent.getExtendedKeyCodeForChar(key)) > 0);
        }
    }

    @Test
    public void keyNotOnKeyboardHasNoFrequency() {
        assertTrue(keyboard.frequencyOf(java.awt.event.KeyEvent.getExtendedKeyCodeForChar('z')) == -1);
    }

    @Test
    public void randomFrequencyReturnsSomethingPositive() {
        assertTrue(keyboard.randomFrequency() >= 0);
    }

    @Test
    public void octaveUpDoublesFrequency() {
        double freq = keyboard.frequencyOf(java.awt.event.KeyEvent.getExtendedKeyCodeForChar('a'));
        keyboard.octaveUp();
        assertEquals(2*freq, keyboard.frequencyOf(java.awt.event.KeyEvent.getExtendedKeyCodeForChar('a')), 0.01);

    }

    @Test
    public void octaveUpHasLimit() {
        keyboard.octaveUp();
        keyboard.octaveUp();
        keyboard.octaveUp();
        keyboard.octaveUp();
        keyboard.octaveUp();
        double freq = keyboard.frequencyOf(java.awt.event.KeyEvent.getExtendedKeyCodeForChar('a'));
        keyboard.octaveUp();
        assertEquals(freq, keyboard.frequencyOf(java.awt.event.KeyEvent.getExtendedKeyCodeForChar('a')),0.01);
    }

    @Test
    public void octaveDownHalvesFrequency() {
        double freq = keyboard.frequencyOf(java.awt.event.KeyEvent.getExtendedKeyCodeForChar('a'));
        keyboard.octaveDown();
        assertEquals(freq/2,keyboard.frequencyOf(java.awt.event.KeyEvent.getExtendedKeyCodeForChar('a')), 0.01);
    }

    @Test
    public void octaveDownHasLimit() {
        keyboard.octaveDown();
        keyboard.octaveDown();
        keyboard.octaveDown();
        keyboard.octaveDown();
        keyboard.octaveDown();
        double freq = keyboard.frequencyOf(java.awt.event.KeyEvent.getExtendedKeyCodeForChar('a'));
        keyboard.octaveDown();
        assertEquals(freq, keyboard.frequencyOf(java.awt.event.KeyEvent.getExtendedKeyCodeForChar('a')),0.01);

    }

    @Test
    public void octaveUpAndDownWorkWithFrequencyOf() {
        int octave = keyboard.getCurrentOctave();
        keyboard.frequencyOf(java.awt.event.KeyEvent.getExtendedKeyCodeForChar('n'));
        assertEquals(octave-1, keyboard.getCurrentOctave());
        keyboard.frequencyOf(java.awt.event.KeyEvent.getExtendedKeyCodeForChar('m'));
        assertEquals(octave, keyboard.getCurrentOctave());
    }
}
