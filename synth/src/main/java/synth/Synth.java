package synth;

import audiothread.*;

/**
 * Luokka kerää samplet oskillaattoreilta ja tekee niistä puskureita AudioThreadille
 */
public class Synth {

    private Oscillator[] oscillators;
    private ADSR adsr;
    private boolean shouldStopGenerating;

    /**
     * Metodi tarjoaa AudioThreadille puskureita, jotka sisältävät syntetisaattorin tuottaman äänen.
     * Puskurit sisältävät 512 samplea, joihin on summattu kaikkien oskillaattorien yhdet samplet.
     * Samplet skaalataan 16 bittisiksi, eli välille -32768 - 32767.
     */
    public short[] getNextBuffer() {
        short[] buffer = new short[AudioThread.BUFFER_SIZE];

        for (int i = 0; i < AudioThread.BUFFER_SIZE; i++) {
            double amplitude = 0;
            double envelope = adsr.getEnvelopeNext();

            // Kun envolope saa negatiivisen arvon, eli release on mennyt loppuun, lopetetaan generointi
            if (envelope < 0 || shouldStopGenerating) {
                adsr.resetEnvelopes();
                shouldStopGenerating = false;
                return null;
            }

            for (Oscillator osc : oscillators) {
                amplitude += osc.nextSample();
            }
            buffer[i] = (short) (envelope * (Short.MAX_VALUE * amplitude / oscillators.length));
        }
        return buffer;
    }

    /**
     *
     * @param adsr Sovlluksen käytössä oleva ADSR
     * @param oscillators Käytössä olevat oskillaattorit
     */
    public Synth(ADSR adsr, Oscillator[] oscillators) {
        this.adsr = adsr;
        this.oscillators = oscillators;

    }

    /**
     * Luokka sisältää tiedon käytössä olevasta näytteenottotaajuudesta.
     */
    public static class AudioInfo {
        public static final int SAMPLE_RATE = 44100;
    }

    /**
     *
     * @param value true: halutaan lopettaa äänen tuottaminen, false: halutaan jatkaa äänen tuottamista
     */
    public void setShouldStopGenerating(boolean value) {
        this.shouldStopGenerating = value;
    }
}
