package synth;

/**
 * Oskillaattori lukee yhden aaltotyypin wavetablea tietyllä taajudella, ja lähettää sampleja pyydettäessä.
 */
public class Oscillator {

    private Wavetable wavetable = Wavetable.Sine;

    private int wavetableIndex;
    private int wavetableStep;

    public Oscillator() {
        wavetableIndex = 0;
        wavetableStep = 1;
    }

    /**
     * Metodi muuttaa taajuuden askelväliksi, jolla wavetablea luetaan.
     * @param frequency Haluttu taajuus hertseinä
     */
    public void setFrequency(double frequency) {
        this.wavetableStep = (int) (Wavetable.SIZE * frequency / Synth.AudioInfo.SAMPLE_RATE);
    }

    /**
     *
     * @return Askelväli, jolla wavetablea luetaan tällä hetkellä.
     */
    public int getWavetableStep() {
        return this.wavetableStep;
    }

    /**
     *
     * @return wavetableIndexiä vastaava sample
     */
    public double nextSample() {
        //
        double sample = wavetable.getSamples()[wavetableIndex];

        // Kasvatetaan indeksiä asekeleen verran. Jos ylitetään wavetablen koko, käytetään moduloa.
        wavetableIndex = (wavetableIndex + wavetableStep) % Wavetable.SIZE;

        return sample;
    }

    /**
     *
     * @param wavetable Aaltomuoto jota halutaan tuottaa
     */
    public void setWaveform(Wavetable wavetable) {
        this.wavetable = wavetable;
        this.wavetableIndex = 0;
    }

    /**
     *
     * @return Käytössä oleva wavetable tai aaltotyyppi.
     */
    public Wavetable getWavetable() {
        return this.wavetable;
    }

}


