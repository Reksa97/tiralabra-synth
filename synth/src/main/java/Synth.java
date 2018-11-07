import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Synth {

    private Keyboard keyboard = new Keyboard();
    private Oscillator[] oscillators = new Oscillator[3];

    private boolean shouldGenerate;

    private final JFrame frame = new JFrame ("Synth");

    // Luodaan AudioThread olio, joka ottaa argumenttina Supplier<short[]> olion
    private final AudioThread audioThread = new AudioThread(() -> {
        // Jos ei pidä generoida buffereita, palautetaan null
        if (!shouldGenerate) {
            return null;
        }
        // Muuten luodaan puskurin kokoinen (512) short-array
        short[] buffers = new short[AudioThread.BUFFER_SIZE];

        // Täytetään array, jossa on 220Hz sini-aallon arvot skaalattuna välille 0-32767.
        // Näytteenottotaajuus on 44100Hz, joten yhteen sekuntiin tulee 220 aaltoa.
        for (int i = 0; i < AudioThread.BUFFER_SIZE; i++) {
            double amplitude = 0;

            // Summataan kaikkien oskillaattorien aallot tietyssä ajankohdassa. Saadaan summa-aalto
            for (Oscillator osc : oscillators) {
                amplitude += osc.nextSample();
            }
            // Skaalataan arvot 16 bittiseksi
            buffers[i] = (short) (Short.MAX_VALUE * amplitude / oscillators.length);
        }
        return buffers;
    });

    // keyAdapter määrittää mitä toimintoja näppäimillä on
    KeyAdapter keyAdapter = new KeyAdapter() {

        // Mitä tahansa näppäintä painettaessa tarkistetaan onko äänisäie käynnissä,
        // ja tarvittaessa käynnistetään puskirien luominen ja äänen toistaminen.
        @Override
        public void keyPressed(KeyEvent e) {

            if (!audioThread.isRunning()) {
                switch (e.getKeyChar()) {
                    case 'm':
                        keyboard.octaveUp();
                        break;

                    case 'n':
                        keyboard.octaveDown();
                        break;

                    case '0':

                        double frequency = keyboard.randomFrequency();

                        for (Oscillator osc : oscillators) {
                            osc.setFrequency(frequency);
                        }

                        shouldGenerate = true;
                        audioThread.triggerPlayback();

                        break;


                    default:
                        frequency = keyboard.frequencyOf(e.getKeyChar());

                        if (frequency != -1) {
                            for (Oscillator osc : oscillators) {
                                osc.setFrequency(frequency);
                            }

                            shouldGenerate = true;
                            audioThread.triggerPlayback();
                        }


                }
            }

        }

        // Kun päästetään näppäimistä irti, lopetetaan äänen toistaminen.
        @Override
        public void keyReleased(KeyEvent e) {
            shouldGenerate = false;

        }

    };

    Synth() {
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
}
