import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


// Oskillaattori tuottaa erilaisia aaltoja. Tässä luokka toimii myös paneelina JFramessa.
public class Oscillator extends JPanel implements ActionListener {

    // Oletusaaltomuotona Sini-aalto
    private Waveform currentWaveform = Waveform.Sine;


    private int wavePosition;
    private double frequency;
    private JFrame frame;

    // Konstruktorille annetaan parametrina JFrame-olio, jotta fokus voidaan pitää framessa, eikä oskillaattorissa.
    public Oscillator(JFrame frame) {
        this.frame = frame;
        Waveform[] waveformsArray = {Waveform.Sine, Waveform.Saw, Waveform.Square, Waveform.Triangle, Waveform.Nothing};

        // Valintaboksi
        JComboBox<Waveform> waveforms = new JComboBox<>(waveformsArray);
        waveforms.setSelectedIndex(0);
        waveforms.setBounds(10,10,100,25);

        // Kuuntelija
        waveforms.addActionListener(this);

        // Lisätään paneeliin
        add(waveforms);

        // Paneelin tyylittely
        setSize(200,80);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setLayout(null);
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public double nextSample() {
        double sampleRate = (double) Synth.AudioInfo.SAMPLE_RATE;

        // Taajus muutettu hertseistä kulmataajuudeksi
        double angularFreq = 2*Math.PI * frequency;

        // Määritellään apumuuttuja (t jaettuna a:lla). Siis ajankohta jaettuna jakson (äänenkorkeuden) jakson pituudella.
        // Tarvitaan saha- ja kolmioaalloissa
        double ta = (wavePosition++ / sampleRate) / ( (double)1 / frequency);


        // Aaltojen matemaattiset kaavat katsottu Wikipediasta ja muutettu Javalle
        switch (currentWaveform) {
            case Sine:
                return Math.sin( angularFreq * wavePosition++ / sampleRate);

            case Square:
                return Math.signum(Math.sin( angularFreq* wavePosition++ / sampleRate));

            case Saw:
                return (double)2*(ta - Math.floor(0.5 + ta));

            case Triangle:
                return (double)2* Math.abs( (double)2*(ta - Math.floor(0.5 + ta)) );

            case Nothing:
                return 0;

            default:
                throw new RuntimeException("Unknown waveform");
        }
    }

    // Kun aaltomuotoa vaihdetaan valintaboksista, vaihdetaan myös currentWaveform-muuttujaa.
    // Lopuksi siirretään fokus framelle.
    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        currentWaveform = (Waveform) cb.getSelectedItem();
        frame.requestFocus();
    }

    public void setWaveform(Waveform waveform) {
        this.currentWaveform = waveform;
    }

    // Aaltotyypit
    public enum Waveform  {
        Sine, Saw, Square, Triangle, Nothing, Unknown;
    }
}


