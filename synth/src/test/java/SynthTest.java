import static org.junit.Assert.*;
import java.util.*;

import org.junit.*;

import javax.swing.*;

public class SynthTest {
    Synth synthStub;
    AudioThread audioThread;
    Oscillator[] oscillators;
    JFrame frame;

    @Before
    public void setUp() {
        synthStub = new Synth();

        oscillators = synthStub.getOscillators();

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
        oscillators[0].setWaveform(Oscillator.Waveform.Saw);
    }

    @Test
    public void nextSampleDoesSomething() {
        double sample = oscillators[0].nextSample();
        System.out.println("nextSampleDoesSomething: " + sample);
    }

    @Test
    public void nextSampleOutputsAllWaveforms() {
        oscillators[0].setWaveform(Oscillator.Waveform.Sine);
        double sample = oscillators[0].nextSample();
        System.out.println("nextSampleOutputsAllWaveforms (Sine): " + sample);

        oscillators[0].setWaveform(Oscillator.Waveform.Saw);
        sample = oscillators[0].nextSample();
        System.out.println("nextSampleOutputsAllWaveforms (Saw): " + sample);

        oscillators[0].setWaveform(Oscillator.Waveform.Square);
        sample = oscillators[0].nextSample();
        System.out.println("nextSampleOutputsAllWaveforms (Square): " + sample);

        oscillators[0].setWaveform(Oscillator.Waveform.Triangle);
        sample = oscillators[0].nextSample();
        System.out.println("nextSampleOutputsAllWaveforms (Triangle): " + sample);

        oscillators[0].setWaveform(Oscillator.Waveform.Nothing);
        sample = oscillators[0].nextSample();
        System.out.println("nextSampleOutputsAllWaveforms (Nothing): " + sample);

        assertEquals(0, sample, 0.0001);

    }

    @Test
    public void nextSampleOnUnknownWaveform() {

        oscillators[0].setWaveform(Oscillator.Waveform.Unknown);

        try {
            oscillators[0].nextSample();
        } catch (Exception e) {

        }

    }


    @After
    public void close() {
        synthStub.close();
    }



}
