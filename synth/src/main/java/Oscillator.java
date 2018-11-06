import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Oscillator extends JPanel implements ActionListener {

    private Waveform currentWaveform = Waveform.Sine;

    public Oscillator() {
        Waveform[] waveformsArray = {Waveform.Sine, Waveform.Saw, Waveform.Square, Waveform.Triangle};

        JComboBox<Waveform> waveforms = new JComboBox<>(waveformsArray);
        waveforms.setSelectedIndex(0);
        waveforms.setBounds(10,10,100,25);

        waveforms.addActionListener(this);

        add(waveforms);
        setSize(200,80);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setLayout(null);
    }

    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        currentWaveform = (Waveform) cb.getSelectedItem();
        System.out.println(currentWaveform);
    }

    public enum Waveform  {
        Sine, Saw, Square, Triangle;
    }
}


