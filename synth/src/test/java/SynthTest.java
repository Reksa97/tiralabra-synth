import static org.junit.Assert.*;

import org.junit.*;

import audiothread.AudioThread;
import synth.Keyboard;
import synth.Oscillator;
import synth.Synth;
import synth.Wavetable;

import javax.swing.*;
import java.awt.*;

public class SynthTest {
    Synth synthStub;
    AudioThread audioThread;
    Oscillator[] oscillators;
    Keyboard keyboard;
    Robot robot;

    JFrame frame;

    @Before
    public void setUp() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        synthStub = new Synth();

        oscillators = synthStub.getOscillators();

        keyboard = new Keyboard();

        frame = synthStub.getFrame();

        audioThread = synthStub.getAudioThread();
        audioThread.triggerPlayback();
    }

    @Test
    public void isRunningReturnsSomething() {
        boolean isRunning = audioThread.isRunning();
        System.out.println("isRunning: " + isRunning);
    }

    @Test
    public void audioInfo() {
        System.out.println(Synth.AudioInfo.SAMPLE_RATE);
        assertEquals(44100, Synth.AudioInfo.SAMPLE_RATE);
    }

    @Test
    public void setFrequencyDoesSomething() {
        oscillators[0].setFrequency(10);
    }

    @Test
    public void setWaveformDoesSomething() {
        oscillators[0].setWaveform(Wavetable.Saw);
    }

    @Test
    public void nextSampleDoesSomething() {
        double sample = oscillators[0].nextSample();
        System.out.println("nextSampleDoesSomething: " + sample);
    }

    @Test
    public void nextSampleOutputsAllWaveforms() {
        oscillators[0].setWaveform(Wavetable.Sine);
        double sample = oscillators[0].nextSample();
        System.out.println("nextSampleOutputsAllWaveforms (Sine): " + sample);

        oscillators[0].setWaveform(Wavetable.Saw);
        sample = oscillators[0].nextSample();
        System.out.println("nextSampleOutputsAllWaveforms (Saw): " + sample);

        oscillators[0].setWaveform(Wavetable.Square);
        sample = oscillators[0].nextSample();
        System.out.println("nextSampleOutputsAllWaveforms (Square): " + sample);

        oscillators[0].setWaveform(Wavetable.Triangle);
        sample = oscillators[0].nextSample();
        System.out.println("nextSampleOutputsAllWaveforms (Triangle): " + sample);

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
    public void randomFrequencyReturnsSomethingPositive() {
        assertTrue(keyboard.randomFrequency() >= 0);
    }

    @After
    public void close() {
        synthStub.close();
    }



}
