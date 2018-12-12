package audiothread;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import synth.Synth;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

/**
 * AudioThread hoitaa kommunikoinnin äänikortin kanssa.
 * Luokka kerää puskurit ja lähettää ne äänikortille
 */
public class AudioThread extends Thread {

    // Määritellään puskurin koko ja niiden määrä jonossa
    public static final int BUFFER_SIZE = 512;
    public static final int BUFFER_COUNT = 8;

    // Synth luokan metodi getNextBuffer tuottaa samplepuskureita
    private Synth synth;

    // 8 puskuria ja indeksi joka käy niitä läpi
    private final int[] buffers = new int[BUFFER_COUNT];
    private int bufferIndex;

    // OpenAL etsii laitteen ja luo sille kontekstin
    private final long device = alcOpenDevice(alcGetString(0,ALC_DEFAULT_DEVICE_SPECIFIER));
    private final long context = alcCreateContext(device, new int[1]);
    private final int source;

    // Tuotetaanko ääntä vai ei
    private boolean running;

    // Kun suoritus lopetetaan, closed saa arvon true,
    private boolean closed;

    /**
     *
     * @param synth tuottaa metodilla getNewBuffer samplepuskureita,
     *              jotka lähetetään äänikortille
     */
    public AudioThread(Synth synth) {

        // Määritellään käytössä oleva laitteen konteksti
        alcMakeContextCurrent(context);

        // Määritellään mitä laitteella on mahdollista tehdä
        AL.createCapabilities(ALC.createCapabilities(device));

        this.synth = synth;

        // Generoidaan lähde, jonka kautta toistetaan puskurissa olevat samplet
        source = alGenSources();

        // Alustetaan puskurit tyhjillä arrayillä
        for (int i = 0; i < BUFFER_COUNT; i++) {
            bufferSamples(new short [0]);
        }

        alSourcePlay(source);
        catchInternalException();

        // Käynnistetään säie
        start();
    }

    /**
     *
     * @return onko äänisäie lähettämässä ääntä äänikortille
     */
    public boolean isRunning() {
        return running;
    }


    /**
     * Metodi kerää puskureita Synth-luokalta ja lähettää ne äänikortille
     */
    @Override
    public synchronized void run() {

        while (!closed) {
            while (!running) {
                try {
                    wait();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            int processedBuffers = alGetSourcei(source, AL_BUFFERS_PROCESSED);

            for (int i = 0; i < processedBuffers; i++) {
                short[] samples = synth.getNextBuffer();

                if (samples == null) {
                    running = false;
                    break;
                }
                alDeleteBuffers(alSourceUnqueueBuffers(source));

                buffers[bufferIndex] = alGenBuffers();
                bufferSamples(samples);

            }
            if (alGetSourcei(source, AL_SOURCE_STATE) != AL_PLAYING) {
                alSourcePlay(source);
            }
            catchInternalException();
        }
        // OpenAL ei käytä automaattista roskienkeräystä, joten kerätään roskat kun ohjelma suljetaan
        alDeleteSources(source);
        alDeleteBuffers(buffers);
        alcDestroyContext(context);
        alcCloseDevice(device);
    }

    /**
     * Kutsutaan kun halutaan poistua run-metodin while silmukasta, ja lähettää äänikortille puskureita
     * niin pitkään, kunnes bufferSupplier palauttaa null
     */
    public synchronized void triggerPlayback() {
        running = true;
        notify();
    }

    /**
     * Suljetaan ohjelma ja poistutaan run() silmukasta
     */
    public void close() {
        closed = true;
        triggerPlayback();
    }

    /**
     *
     * @param samples äänikortille lähetettävät samplet
     */
    private void bufferSamples(short[] samples) {
        // Valitaan nyt käytössä oleva puskuri ja kasvatetaan indeksiä yhdellä
        int buffer = buffers[bufferIndex];
        bufferIndex++;

        // Puskuroidaan samplet 16 bittisenä ja monona Syna-luokassa määritellyllä näytteenottotaajuudella
        // buffer:n osoittamaan puskuriin
        alBufferData(buffer, AL_FORMAT_MONO16, samples, Synth.AudioInfo.SAMPLE_RATE);

        // Lähetetään puskuri lähteeseen jonoon odottamaan toistamista
        alSourceQueueBuffers(source, buffer);

        // Jos indeksi ylittää puskurien määrän, asetetaan sen arvoksi 0
        if (bufferIndex == BUFFER_COUNT) {
            bufferIndex = 0;
        }
    }

    /**
     *  Jos OpenAL tuottaa virheen
     */
    private void catchInternalException() {
        int error = alcGetError(device);

        // Jos virhe löytyy, kutsutaan luokkaa, joka muuntaa Exceptionin Javalle
        if (error != ALC_NO_ERROR) {
            throw new OpenALException(error);
        }
    }
}