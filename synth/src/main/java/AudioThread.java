// OpenAL importit
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

// Tämän voisi toteuttaa itse myöhemmin
import java.util.function.Supplier;

// Tässä luokassa
public class AudioThread extends Thread {

    // Määritellään puskurin koko ja niiden määrä jonossa
    static final int BUFFER_SIZE = 512;
    static final int BUFFER_COUNT = 8;

    // bufferSupplier nimensä mukaisesti tuottaa puskureita luokalle käytettäväksi
    private final Supplier<short[]> bufferSupplier;

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

    // Konstruktori ottaa argumentiksi puskurin tuottajan
    AudioThread(Supplier<short[]> bufferSupplier) {

        // Määritellään käytössä oleva laitteen konteksti
        alcMakeContextCurrent(context);

        // Määritellään mitä laitteella on mahdollista tehdä
        AL.createCapabilities(ALC.createCapabilities(device));

        // Asetetaan argumentiksi saatu puskurin tuottaja luokan puskurin tuottajaksi
        this.bufferSupplier = bufferSupplier;

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

    boolean isRunning() {
        return running;
    }

    // Metodi on synkronoitu koska käytetään wait() ja notify() -metodeja
    @Override
    public synchronized void run() {

        // Ollaan silmukassa kun ohjelmaa ei ole suljettu
        while (!closed) {

            // Odotetaan kunnes laitetaan käyntiin
            while (!running) {
                try {
                    wait();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Selvitetään kuinka monta puskuria on prosessoitu
            int processedBuffers = alGetSourcei(source, AL_BUFFERS_PROCESSED);

            // Jokainen prosessoitu puskuri korvataan uudella
            for (int i = 0; i < processedBuffers; i++) {
                // Getataan puskurien tuottajalta uusi puskuri
                short[] samples = bufferSupplier.get();

                // Jos tuottaja ei halua tuottaa puskuria, kytketään äänen toistaminen pois päältä
                if (samples == null) {
                    running = false;
                    break;
                }

                // Poistetaan viimeksi prosessoitu puskuri
                alDeleteBuffers(alSourceUnqueueBuffers(source));

                // Generoidaan uusi puskuri
                buffers[bufferIndex] = alGenBuffers();
                // Pusketaan samplet uuteen puskuriin
                bufferSamples(samples);

            }

            // Jos lähde ei toista ääntä, laitetaan se toistamaan
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

    // Tätä kutsutaan kun halutaan poistua wait() -silmukasta
    synchronized void triggerPlayback() {
        running = true;
        notify();
    }

    // Suljetaan ohjelma
    void close() {
        closed = true;
        // Poistutaan silmukasta
        triggerPlayback();
    }



    // Metodi puskuroi argumenttina annetut samplet lähteeseen jonoon.
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

    // Jos OpenAL tuottaa virheen
    private void catchInternalException() {
        int error = alcGetError(device);

        // Jos virhe löytyy, kutsutaan luokkaa, joka muuntaa Exceptionin Javalle
        if (error != ALC_NO_ERROR) {
            throw new OpenALException(error);
        }
    }


}
