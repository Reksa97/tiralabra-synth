package synth;

public enum Wavetable {
    // Aaltotyypit
    Sine, Square, Saw, Triangle;

    // Aaltotaulun koko
    public static final int SIZE = 8192;

    private final double[] samples = new double[SIZE];

    static {
        final double freq = 1d / (SIZE / (double) Synth.AudioInfo.SAMPLE_RATE);
        // Taajus muutettu hertseistä kulmataajuudeksi
        final double angularFreq = 2*Math.PI * freq;


        // Generoidaan jokaiselle aaltomuodolle aaltotauluihin arvot
        // Yhteen tauluun tulee siis yksi kokonainen aalto
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
    }

    public double[] getSamples() {
        return samples;
    }

}
