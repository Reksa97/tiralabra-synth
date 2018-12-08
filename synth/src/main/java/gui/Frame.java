package gui;

import audiothread.AudioThread;
import synth.ADSR;
import synth.Keyboard;
import synth.Oscillator;
import synth.Synth;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Frame {

    private JFrame frame;
    private Synth synth;
    private ADSR adsr;
    private Keyboard keyboard;
    private OscillatorPanel[] oscillatorPanels;
    private int lastPressed;

    // Luodaan audiothread.AudioThread olio, joka ottaa argumenttina Supplier<short[]> olion
    private AudioThread audioThread;

    // keyAdapter määrittää mitä toimintoja näppäimillä on
    private KeyAdapter keyAdapter = new KeyAdapter() {

        @Override
        public void keyPressed(KeyEvent e) {
            int pressedKeyCode = e.getExtendedKeyCode();

            // jos viimeksi painettu näppäin on eri kuin nyt painettu, on tulossa uusi taajuus
            boolean newFreqComing = lastPressed != pressedKeyCode;
            // näppäimellä 0 tulee satunnainen taajuus, joka on myös uusi
            if (e.getKeyChar() == '0') {
                newFreqComing = true;
            }

            if (!audioThread.isRunning() || newFreqComing) {
                // jos säie ei ole käynnissä, ei sitä pitäisi sammuttaa
                // jos taas säie on käynnissä ja on tulossa uusi taajuus, pitäisi säie resetoida
                if (!audioThread.isRunning()) {
                    synth.setShouldStopGenerating(false);
                }   else {
                    synth.setShouldStopGenerating(true);
                }
                adsr.resetEnvelopes();
                double frequency = keyboard.frequencyOf(pressedKeyCode);

                if (frequency != -1) {
                    for (OscillatorPanel osc : oscillatorPanels) {
                        osc.setFrequency(frequency);
                    }
                    audioThread.triggerPlayback();
                }
            }
            lastPressed = pressedKeyCode;
        }

        // Kun päästetään näppäimistä irti, lopetetaan äänen toistaminen.
        @Override
        public void keyReleased(KeyEvent e) {
            adsr.keyLifted();
        }
    };

    public Frame(int howManyOscillators) {
        frame = new JFrame ("Synth");

        Oscillator[] oscillators = new Oscillator[howManyOscillators];
        this.oscillatorPanels = new OscillatorPanel[howManyOscillators];

        for (int i = 0; i < howManyOscillators; i++) {
            // Oskillaattorille annetaan parametrina JFrame, jotta fokus saadaan pidettyä siinä.
            Oscillator osc = new Oscillator();
            oscillators[i] = osc;
            OscillatorPanel oscPanel = new OscillatorPanel(frame, osc);

            // Sijoitellaan oskillaattorit
            if (i >= 4) {
                // alarivi
                oscPanel.setLocation(370+ (i-4)*205, 120);
            }   else {
                // ylärivi
                oscPanel.setLocation(370+(i*205),5);
            }
            frame.add(oscPanel);

            // Tallennetaan se myös ohjelman käytettäväksi
            oscillatorPanels[i] = oscPanel;
        }

        adsr = new ADSR();
        keyboard = new Keyboard();
        synth = new Synth(adsr, oscillators);
        audioThread = new AudioThread(synth);

        // Attackille slideri
        JSlider attackSlider = new JSlider(JSlider.VERTICAL, 0, 100, 0);
        attackSlider.setMajorTickSpacing(20);
        attackSlider.addChangeListener(e -> {
            adsr.setAttack(attackSlider.getValue());
            frame.requestFocus();
        });
        // Decaylle slider
        JSlider decaySlider = new JSlider(JSlider.VERTICAL, 0, 200, 100);
        decaySlider.setMajorTickSpacing(40);
        decaySlider.addChangeListener(e -> {
            adsr.setDecay(decaySlider.getValue());
            frame.requestFocus();
        });
        adsr.setDecay(100);

        // Sustainille slider
        JSlider sustainSlider = new JSlider(JSlider.VERTICAL, 0, 100, 100);
        sustainSlider.setMajorTickSpacing(20);
        sustainSlider.addChangeListener(e -> {
            adsr.setSustain(sustainSlider.getValue());
            frame.requestFocus();
        });

        // Releaselle slideri
        JSlider releaseSlider = new JSlider(JSlider.VERTICAL, 0, 100, 0);
        releaseSlider.setMajorTickSpacing(20);
        releaseSlider.addChangeListener(e -> {
            adsr.setRelease(releaseSlider.getValue());
            frame.requestFocus();
        });

        String[] sliderNames = {"Attack", "Decay", "Sustain", "Release"};
        JSlider[] envelopeSliders = {attackSlider, decaySlider, sustainSlider, releaseSlider};
        for (int i = 0; i < envelopeSliders.length; i++) {
            JSlider slider = envelopeSliders[i];
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);

            JLabel label = new JLabel(sliderNames[i]);
            label.setLocation(12 + i*80, 5);
            label.setSize(label.getPreferredSize());
            frame.add(label);

            slider.setBounds(5 + i*80,20,100,180);
            frame.add(slider);
        }

        this.frame.setFocusable(true);
        frame.addKeyListener(keyAdapter);

        frame.addWindowListener(new WindowAdapter() {
            // Kun ikkuna suljetaan, täytyy myös sulkea äänisäie.
            @Override
            public void windowClosing(WindowEvent e) {
                audioThread.close();
            }
        });

        // Ikkunan sulkeminen hävittää sen
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // Ikkunan koko
        frame.setSize(1250,250);

        // Ikkunaa ei voida muuttaa eri kokoiseksi
        frame.setResizable(false);

        // Ei määritellä (ainakaan vielä) layoutia, eikä sijainnin suhdetta mihinkään.
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        // Asetetaan näkyväksi.
        frame.setVisible(true);

    }

}
