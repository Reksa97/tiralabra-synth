package synth;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OscillatorTest {
    private Oscillator oscillator;

    @Before
    public void setUp() {
        oscillator = new Oscillator();
    }

    @Test
    public void setFrequencyChangesWavestepCorrectly() {
        oscillator.setFrequency(10);
        int step = oscillator.getWavetableStep();
        oscillator.setFrequency(1000);
        assertTrue(step < oscillator.getWavetableStep());
    }

    @Test
    public void setWaveformSetsWaveform() {
        oscillator.setWaveform(Wavetable.Saw);
        assertEquals(Wavetable.Saw, oscillator.getWavetable());
    }

    @Test
    public void nextSampleIsBetweenMinusOneAndOne() {
        double sample = oscillator.nextSample();
        assertTrue(sample <= 1 && sample >= -1);
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
