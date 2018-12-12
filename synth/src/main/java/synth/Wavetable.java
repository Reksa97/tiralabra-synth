package synth;

/**
 * Sisältää sini-, neliö, saha-, ja kolmioaaltojen wavetablet.
 * Jokaisessa taulussa on yksi aalto jaettu 8192 alkioon.
 */
public enum Wavetable {
    Sine, Square, Saw, Triangle;

    public static final int SIZE = 8192;

    private final double[] samples = new double[SIZE];

    /**
     * Täytetään kaikki wavetablet.
     */
    static {
        long start = System.nanoTime();

        final double freq = 1d / (SIZE / (double) Synth.AudioInfo.SAMPLE_RATE);

        final double angularFreq = 2*Math.PI * freq; // Taajus muutettu hertseistä kulmataajuudeksi

        for (int i = 0; i < SIZE; i++) {

            // Määritellään apumuuttuja (t jaettuna a:lla). Siis ajankohta jaettuna jakson (äänenkorkeuden) jakson pituudella.
            // Tarvitaan saha- ja kolmioaalloissa
            double t = i / (double) Synth.AudioInfo.SAMPLE_RATE;
            double ta = t/ (1d / freq);

            Sine.samples[i] = Math.sin(angularFreq * t);

            Square.samples[i] = Math.signum(Sine.samples[i]);

            Saw.samples[i] = 2d * (ta - Math.floor(0.5 + ta));

            Triangle.samples[i] = 2d * Math.abs(Saw.samples[i]);
        }
        long end = System.nanoTime();
        long timeUsed = end-start;

        System.out.println("Wavetablejen laskemiseen meni aikaa: " + (timeUsed/1000000d) + " millisekuntia");
    }

    /**
     *
     * @return halutun aaltomuodon yhden aallon samplet
     */
    public double[] getSamples() {
        return samples;
    }

}
