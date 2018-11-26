package synth;

// Oskillaattori tuottaa erilaisia aaltoja.
public class Oscillator {

    // Oletusaaltomuotona Sini-aalto
    private Wavetable wavetable = Wavetable.Sine;


    private int wavetableIndex;
    private int wavetableStep;

    public Oscillator() {
        wavetableIndex = 0;
        wavetableStep = 1;
    }

    /**
     *
     * @param frequency haluttu taajuus hertseinä
     */
    public void setFrequency(double frequency) {
        // Asetetaan askelväli, jolla wavetablea luetaan
        this.wavetableStep = (int) (Wavetable.SIZE * frequency / Synth.AudioInfo.SAMPLE_RATE);
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
     * @param wavetable aaltomuoto jota halutaan tuottaa
     */
    public void setWaveform(Wavetable wavetable) {
        this.wavetable = wavetable;
        this.wavetableIndex = 0;
    }

}


