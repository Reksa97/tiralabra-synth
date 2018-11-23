package synth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


// Oskillaattori tuottaa erilaisia aaltoja. Tässä luokka toimii myös paneelina JFramessa.
public class Oscillator extends JPanel implements ActionListener {

    // Oletusaaltomuotona Sini-aalto
    private Wavetable wavetable = Wavetable.Sine;


    private int wavetableIndex;
    private int wavetableStep;

    private JFrame frame;

    /**
     *
     * @param frame annetaan parametrina JFrame-olio, jotta fokus voidaan pitää framessa, eikä oskillaattorissa.
     */
    public Oscillator(JFrame frame) {
        this.frame = frame;
        Wavetable[] wavetableArray = Wavetable.values();

        // Valintaboksi
        JComboBox<Wavetable> wavetables = new JComboBox<>(wavetableArray);
        wavetables.setSelectedIndex(0);
        wavetables.setBounds(10,10,100,25);

        // Kuuntelija
        wavetables.addActionListener(this);

        // Lisätään paneeliin
        add(wavetables);

        // Paneelin tyylittely
        setSize(200,80);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setLayout(null);
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
     * @param e valintaboksin valinnan vaihto rekisteröidään
     */
    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        this.wavetable = (Wavetable) cb.getSelectedItem();
        frame.requestFocus();
    }

    /**
     *
     * @param wavetable aaltomuoto jota halutaan tuottaa
     */
    public void setWaveform(Wavetable wavetable) {
        this.wavetable = wavetable;
    }

}


