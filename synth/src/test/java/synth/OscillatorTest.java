package synth;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OscillatorTest {
    private Oscillator oscillator;

    @Before
    public void setUp() {
        oscillator = new Oscillator();
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
}
