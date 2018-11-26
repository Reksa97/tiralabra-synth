import static org.junit.Assert.*;

import org.junit.*;
import synth.*;


public class SynthTest {
    Synth synth;
    Oscillator oscillator;
    Keyboard keyboard;
    ADSR adsr;


    @Before
    public void setUp() {
        this.keyboard = new Keyboard();
        this.oscillator = new Oscillator();
        this.adsr = new ADSR();


        this.synth = new Synth(adsr, keyboard, new Oscillator[]{oscillator, new Oscillator()});
    }


    @Test
    public void audioInfo() {
        System.out.println(Synth.AudioInfo.SAMPLE_RATE);
        assertEquals(44100, Synth.AudioInfo.SAMPLE_RATE);
    }

    @Test
    public void setFrequencyDoesSomething() {
        oscillator.setFrequency(10);
    }

    @Test
    public void setWaveformDoesSomething() {
        oscillator.setWaveform(Wavetable.Saw);
    }

    @Test
    public void nextSampleDoesSomething() {
        double sample = oscillator.nextSample();
        System.out.println("nextSampleDoesSomething: " + sample);
    }

    @Test
    public void nextSampleOutputsAllWaveforms() {
        oscillator.setWaveform(Wavetable.Sine);
        double sample = oscillator.nextSample();
        assertEquals(0, sample, 0.0001);


        oscillator.setWaveform(Wavetable.Saw);
        sample = oscillator.nextSample();
        assertEquals(0, sample, 0.0001);


        oscillator.setWaveform(Wavetable.Square);
        sample = oscillator.nextSample();
        assertEquals(0, sample, 0.0001);

        oscillator.setWaveform(Wavetable.Triangle);
        sample = oscillator.nextSample();
        assertEquals(0, sample, 0.0001);
    }

    @Test
    public void keysHaveFrequencies() {
        char[] keys = {'a', 'w', 's', 'd', 'r', 'f', 't', 'g', 'h', 'u', 'j','i', 'k', 'o', 'l', 'ö', 'å', 'ä'};
        for (int i = 0; i < keys.length; i++) {
            char key = keys[i];
            assertTrue(keyboard.frequencyOf(key) > 0);
        }
    }

    @Test
    public void keyNotOnKeyboardHasNoFrequency() {
        assertTrue(keyboard.frequencyOf('z') == -1);
    }

    @Test
    public void octaveUpDoublesFrequency() {
        double freq = keyboard.frequencyOf('a');
        keyboard.octaveUp();
        assertEquals(2*freq, keyboard.frequencyOf('a'), 0.01);

    }

    @Test
    public void octaveUpHasLimit() {
        keyboard.octaveUp();
        keyboard.octaveUp();
        keyboard.octaveUp();
        keyboard.octaveUp();
        keyboard.octaveUp();
        double freq = keyboard.frequencyOf('a');
        keyboard.octaveUp();
        assertEquals(freq, keyboard.frequencyOf('a'),0.01);
    }

    @Test
    public void octaveDownHalvesFrequency() {
        double freq = keyboard.frequencyOf('a');
        keyboard.octaveDown();
        assertEquals(freq/2,keyboard.frequencyOf('a'), 0.01);
    }

    @Test
    public void octaveDownHasLimit() {
        keyboard.octaveDown();
        keyboard.octaveDown();
        keyboard.octaveDown();
        keyboard.octaveDown();
        keyboard.octaveDown();
        double freq = keyboard.frequencyOf('a');
        keyboard.octaveDown();
        assertEquals(freq, keyboard.frequencyOf('a'),0.01);

    }

    @Test
    public void synthSupplierSuppliesArrays() {
        short[] shorts = new short[3];
        assertEquals(shorts.getClass(), synth.supplier.get().getClass());
    }

    @Test
    public void randomFrequencyReturnsSomethingPositive() {
        assertTrue(keyboard.randomFrequency() >= 0);
    }


}
