package synth;

import static org.junit.Assert.*;
import org.junit.*;

public class SynthTest {
    private Synth synth;
    private ADSR adsr;

    @Before
    public void setUp() {
        this.adsr = new ADSR();
        
        this.synth = new Synth(adsr, new Oscillator[]{new Oscillator(), new Oscillator()});
    }

    @Test
    public void audioInfo() {
        new Synth.AudioInfo();
        assertEquals(44100, Synth.AudioInfo.SAMPLE_RATE);
    }

    @Test
    public void synthNextBufferReturnsArrays() {
        short[] shorts = new short[3];
        assertEquals(shorts.getClass(), synth.getNextBuffer().getClass());
    }

    @Test
    public void whenReleaseIsOverNextBufferIsNull() {
        adsr.setRelease(1);
        adsr.keyLifted();
        assertEquals(null, synth.getNextBuffer());
    }

    @Test
    public void whenShouldStopGeneratingIsSetToTrueNextBufferISNull() {
        synth.setShouldStopGenerating(true);
        assertEquals(null, synth.getNextBuffer());
    }
}