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
    private OscillatorPanel[] oscillatorPanels = new OscillatorPanel[3];
    private char lastPressed;

    // Luodaan audiothread.AudioThread olio, joka ottaa argumenttina Supplier<short[]> olion
    private AudioThread audioThread;

    // keyAdapter määrittää mitä toimintoja näppäimillä on
    private KeyAdapter keyAdapter = new KeyAdapter() {

        @Override
        public void keyPressed(KeyEvent e) {

            char pressed = e.getKeyChar();

            // jos viimeksi painettu näppäin on eri kuin nyt painettu, on tulossa uusi taajuus
            boolean newFreqComing = lastPressed != pressed;
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
                double frequency = keyboard.frequencyOf(pressed);

                if (frequency != -1) {
                    for (OscillatorPanel osc : oscillatorPanels) {
                        osc.setFrequency(frequency);
                    }
                    audioThread.triggerPlayback();
                }
            }
            lastPressed = e.getKeyChar();
        }

        // Kun päästetään näppäimistä irti, lopetetaan äänen toistaminen.
        @Override
        public void keyReleased(KeyEvent e) {
            adsr.keyLifted();
        }
    };

    public Frame() {
        frame = new JFrame ("Synth");

        Oscillator[] oscillators = new Oscillator[oscillatorPanels.length];
        for (int i = 0; i < oscillatorPanels.length; i++) {
            // Oskillaattorille annetaan parametrina JFrame, jotta fokus saadaan pidettyä siinä.
            Oscillator osc = new Oscillator();
            oscillators[i] = osc;
            OscillatorPanel oscPanel = new OscillatorPanel(frame, osc);

            // Sijoitellaan horisontaalisesti
            oscPanel.setLocation(5+(i*205),5);
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
        attackSlider.setBounds(0,200,100,100);
        attackSlider.setMajorTickSpacing(20);
        attackSlider.setPaintTicks(true);
        attackSlider.setPaintLabels(true);
        attackSlider.addChangeListener(e -> {
            adsr.setAttack(attackSlider.getValue());
            frame.requestFocus();
        });
        frame.add(attackSlider);

        // Decaylle slider
        JSlider decaySlider = new JSlider(JSlider.VERTICAL, 0, 200, 100);
        decaySlider.setBounds(80,200,100,100);
        decaySlider.setMajorTickSpacing(40);
        decaySlider.setPaintTicks(true);
        decaySlider.setPaintLabels(true);
        decaySlider.addChangeListener(e -> {
            adsr.setDecay(decaySlider.getValue());
            frame.requestFocus();
        });
        adsr.setDecay(100);
        frame.add(decaySlider);

        // Sustainille slider
        JSlider sustainSlider = new JSlider(JSlider.VERTICAL, 0, 100, 100);
        sustainSlider.setBounds(160,200,100,100);
        sustainSlider.setMajorTickSpacing(20);
        sustainSlider.setPaintTicks(true);
        sustainSlider.setPaintLabels(true);
        sustainSlider.addChangeListener(e -> {
            adsr.setSustain(sustainSlider.getValue());
            frame.requestFocus();
        });
        frame.add(sustainSlider);

        // Releaselle slideri
        JSlider releaseSlider = new JSlider(JSlider.VERTICAL, 0, 100, 0);
        releaseSlider.setBounds(240,200,100,100);
        releaseSlider.setMajorTickSpacing(20);
        releaseSlider.setPaintTicks(true);
        releaseSlider.setPaintLabels(true);
        releaseSlider.addChangeListener(e -> {
            adsr.setRelease(releaseSlider.getValue());
            frame.requestFocus();
        });
        frame.add(releaseSlider);

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
        frame.setSize(625,350);

        // Ikkunaa ei voida muuttaa eri kokoiseksi
        frame.setResizable(false);

        // Ei määritellä (ainakaan vielä) layoutia, eikä sijainnin suhdetta mihinkään.
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        // Asetetaan näkyväksi.
        frame.setVisible(true);

    }




}
