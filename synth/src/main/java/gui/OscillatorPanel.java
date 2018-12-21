package gui;

import synth.Oscillator;
import synth.Wavetable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Luokka vastaa oskillaattorin paneelista ja sen muutoksien välittämisestä oskillaattorille.
 */
public class OscillatorPanel extends JPanel implements ActionListener {
    private JFrame frame;
    private Oscillator oscillator;

    /**
     *
     * @param frame tarvitaan JFrame-olio, jotta fokus voidaan pitää framessa, eikä oskillaattorissa.
     * @param oscillator paneelia vastaava oskillaattori
     */
    public OscillatorPanel(JFrame frame, Oscillator oscillator) {
        this.frame = frame;
        this.oscillator = oscillator;
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
     * @param e valintaboksin valinnan vaihto, joka rekisteröidään
     */
    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        this.oscillator.setWaveform((Wavetable) cb.getSelectedItem());
        frame.requestFocus();
    }

    /**
     *
     * @param frequency Taajuus, joka asetetaan oskillaattorille
     */
    public void setFrequency(double frequency) {
        this.oscillator.setFrequency(frequency);
    }
}
