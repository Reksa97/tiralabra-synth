package synth;

import audiothread.*;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Supplier;

public class Synth {

    private Keyboard keyboard = new Keyboard();
    private Oscillator[] oscillators = new Oscillator[3];
    private ADSR adsr;
    private char lastPressed;
    private double envelope;
    private boolean shouldStopGenerating;

    private final JFrame frame = new JFrame ("Synth");

    /**
     * supplier tarjoaa AudioThreadille buffereita, jotka sisältävät syntetisaattorin tuottaman äänen
     */
    public Supplier<short[]> supplier = () -> {

        // Muuten luodaan puskurin kokoinen (512) short-array
        short[] buffer = new short[AudioThread.BUFFER_SIZE];

        // Täytetään array, jossa on 220Hz sini-aallon arvot skaalattuna välille 0-32767.
        // Näytteenottotaajuus on 44100Hz, joten yhteen sekuntiin tulee 220 aaltoa.
        for (int i = 0; i < AudioThread.BUFFER_SIZE; i++) {
            double amplitude = 0;
            envelope = adsr.getEnvelopeNext();
            if (envelope > 1) {
                System.out.println(envelope);
            }

            // Kun envolope saa negatiivisen arvon, lopetetaan generointi
            if (envelope < 0 || shouldStopGenerating) {
                adsr.resetEnvelopes();
                shouldStopGenerating = false;
                return null;
            }

            // Summataan kaikkien oskillaattorien aallot tietyssä ajankohdassa. Saadaan summa-aalto
            for (Oscillator osc : oscillators) {
                amplitude += osc.nextSample();
            }
            // Skaalataan arvot 16 bittiseksi
            buffer[i] = (short) (envelope * (Short.MAX_VALUE * amplitude / oscillators.length));
            if (buffer[i] > Short.MAX_VALUE) {
                System.out.println("vittu");
            }
        }
        return buffer;
    };


    // Luodaan audiothread.AudioThread olio, joka ottaa argumenttina Supplier<short[]> olion
    private final AudioThread audioThread = new AudioThread(supplier);

    public AudioThread getAudioThread() {
        return this.audioThread;
    }

    public Oscillator[] getOscillators() {
        return this.oscillators;
    }



    // keyAdapter määrittää mitä toimintoja näppäimillä on
    KeyAdapter keyAdapter = new KeyAdapter() {


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
                    shouldStopGenerating = false;
                }   else {
                    shouldStopGenerating = true;
                }

                adsr.resetEnvelopes();
                double frequency = keyboard.frequencyOf(pressed);


                if (frequency != -1) {
                    for (Oscillator osc : oscillators) {
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

    /**
     * Määritellään sovelluksen ulkoasu, oskillaattorit ja liu'ut
     */
    public Synth() {
        // Määritellään haluttu määrä oskillaattoreita käyttöön
        for (int i = 0; i < oscillators.length; i++) {
            // Oskillaattorille annetaan parametrina JFrame, jotta fokus saadaan pidettyä siinä.
            Oscillator osc = new Oscillator(frame);

            // Sijoitellaan horisontaalisesti
            osc.setLocation(5+(i*205),5);
            frame.add(osc);

            // Tallennetaan se myös ohjelman käytettäväksi
            oscillators[i] = osc;
        }

        this.adsr = new ADSR();
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

        // Decaylle slideri
        JSlider releaseSlider = new JSlider(JSlider.VERTICAL, 0, 100, 0);
        releaseSlider.setBounds(80,200,100,100);
        releaseSlider.setMajorTickSpacing(20);
        releaseSlider.setPaintTicks(true);
        releaseSlider.setPaintLabels(true);
        releaseSlider.addChangeListener(e -> {
            adsr.setRelease(releaseSlider.getValue());
            frame.requestFocus();
        });
        frame.add(releaseSlider);


        frame.setFocusable(true);
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

    public static class AudioInfo {
        // Käytössä on yleinen näytteenottotaajuus 44100 Hz
        public static final int SAMPLE_RATE = 44100;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void close() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
}
