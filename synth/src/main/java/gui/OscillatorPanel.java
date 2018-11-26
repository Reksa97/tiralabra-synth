package gui;

import synth.Oscillator;
import synth.Wavetable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class OscillatorPanel extends JPanel implements ActionListener {
    private JFrame frame;
    private Oscillator oscillator;

    /**
     *
     * @param frame annetaan parametrina JFrame-olio, jotta fokus voidaan pitää framessa, eikä oskillaattorissa.
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
     * @param e valintaboksin valinnan vaihto rekisteröidään
     */
    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        this.oscillator.setWaveform((Wavetable) cb.getSelectedItem());
        frame.requestFocus();
    }

    public void setFrequency(double frequency) {
        this.oscillator.setFrequency(frequency);
    }
}
