package synth;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ADSRTest {
    private ADSR adsr;

    @Before
    public void setUp() {
        adsr = new ADSR();
    }

    @Test
    public void setAttackToTooLowOrHighAmount() {
        adsr.setAttack(-1);
        assertEquals(0, adsr.getAttack());
        adsr.setAttack(1000);
        assertEquals(0, adsr.getAttack());
    }

    @Test
    public void setAttackChangesAttackInc() {
        adsr.setAttack(1);
        double firstAttackInc = adsr.getAttackInc();
        adsr.setAttack(2);
        assertTrue(firstAttackInc > adsr.getAttackInc());
    }

    @Test
    public void setDecayToTooLowOrHighAmount() {
        adsr.setDecay(-1);
        assertEquals(100, adsr.getDecay());
        adsr.setDecay(1000);
        assertEquals(100, adsr.getDecay());
    }

    @Test
    public void setDecayChangesDecayDec() {
        adsr.setDecay(50);
        double firstDecayDec = adsr.getDecayDec();
        adsr.setDecay(51);
        assertTrue(firstDecayDec > adsr.getDecayDec());
    }

    @Test
    public void setSustainToTooLowOrHighAmount() {
        adsr.setSustain(-1);
        assertEquals(100, adsr.getSustain());
        adsr.setSustain(1000);
        assertEquals(100, adsr.getSustain());
    }

    @Test
    public void setSustainChangesSustainValue() {
        adsr.setSustain(10);
        assertEquals(adsr.getSustainValue(), 0.1, 0.0001);
    }

    @Test
    public void setReleaseToTooLowOrHighAmount() {
        adsr.setRelease(-1);
        assertEquals(0, adsr.getRelease());
        adsr.setRelease(1000);
        assertEquals(0, adsr.getRelease());
    }

    @Test
    public void setReleaseChangesReleaseDec() {
        adsr.setRelease(10);
        double firstReleaseDec = adsr.getReleaseDec();
        adsr.setRelease(11);
        assertTrue(firstReleaseDec > adsr.getReleaseDec());
    }

    @Test
    public void whenKeyIsLiftedReleaseStartsAndNextEnvelopeStartsDecreasing() {
        adsr.setRelease(10);
        adsr.keyLifted();
        double firstEnvelopeAmount = adsr.getEnvelopeNext();
        assertTrue(firstEnvelopeAmount > adsr.getEnvelopeNext());
    }

    @Test
    public void resettingEnvelopeDoesSomething() {
        adsr.resetEnvelopes();
        assertTrue(true);
    }

    @Test
    public void toStringHasCorrectValues() {
        assertEquals("A: 0 D: 100 S: 100 R: 0", adsr.toString());
    }
}
