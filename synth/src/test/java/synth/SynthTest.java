package synth;

import static org.junit.Assert.*;

import org.junit.*;


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
        new Synth.AudioInfo();
        assertEquals(44100, Synth.AudioInfo.SAMPLE_RATE);
    }




    @Test
    public void synthSupplierSuppliesArrays() {
        short[] shorts = new short[3];
        assertEquals(shorts.getClass(), synth.supplier.get().getClass());
    }

    @Test
    public void whenReleaseIsOverSupplierReturnsNull() {
        adsr.setRelease(1);
        adsr.keyLifted();
        assertEquals(null, synth.supplier.get());
    }

    @Test
    public void whenShouldStopGeneratingIsSetToTrueSupplierReturnsNull() {
        synth.setShouldStopGenerating(true);
        assertEquals(null, synth.supplier.get());
    }

}
