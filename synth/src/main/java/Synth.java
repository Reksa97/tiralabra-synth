import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Synth {

    private boolean shouldGenerate;
    private int wavePosition;

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
            buffers[i] = (short) (Short.MAX_VALUE * Math.sin( (2*Math.PI * 220 ) * wavePosition++ / AudioInfo.SAMPLE_RATE) );

        }
        return buffers;
    });



    Synth() {
        frame.addKeyListener(new KeyAdapter() {

            // Mitä tahansa näppäintä painettaessa tarkistetaan onko äänisäie käynnissä,
            // ja tarvittaessa käynnistetään puskirien luominen ja äänen toistaminen.
            @Override
            public void keyPressed(KeyEvent e) {
                if (!audioThread.isRunning()) {

                    shouldGenerate = true;
                    audioThread.triggerPlayback();
                }
            }

            // Kun päästetään näppäimistä irti, lopetetaan äänen toistaminen.
            @Override
            public void keyReleased(KeyEvent e) {
                shouldGenerate = false;
            }
        });


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
        frame.setSize(600,350);

        // Ikkunaa ei voida muuttaa eri kokoiseksi
        frame.setResizable(false);

        // Ei vielä layoutia, eikä sijainnin suhdetta mihinkään.
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
