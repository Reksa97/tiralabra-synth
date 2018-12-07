package synth;
// Määritellään keyboard, jossa on 18 kosketinta. Lisäksi oktaavia voidaan vaihtaa, joten voidaan soittaa
// laajemmalta skaalalta.

import utils.KeyCodeToKeyboardIndex;

import java.util.Random;

public class Keyboard {

    // Määritellään 78 soitettavaa taajuutta
    private double[] frequencies = new double[78];

    // Tasavireessä sävel nousee puolisävelaskelta kun se kerrotaan kahden kahdennellatoista juurella
    public static final double TWELTH_ROOT_OF_TWO = 1.0594630943593;

    // Aluksi ollaan alhaalta laskettuna oktaavissa 3
    private int currentOctave = 3;

    // Häröilyyn
    private Random rndm = new Random();

    // Luokan avulla muutetaan koodi indeksiksi
    private KeyCodeToKeyboardIndex codeToIndex;


    /**
     * lasketaan koskettimien taajuudet
     */
    public Keyboard() {
        codeToIndex = new KeyCodeToKeyboardIndex();

        // Matalimman äänen taajuus on 27,5 Hz
        frequencies[0] = 27.5;
        double previousA = 27.5;
        double previousFreq = 27.5;

        // Lasketaan loppujen äänten taajuudet
        for (int i = 1; i < frequencies.length; i++) {

            // Jotta vältytään pyöristysvirheiltä, palataan aina A-sävelellä tarkkaan hertsimäärään
            if (i % 12 == 0) {
                previousFreq = previousA = frequencies[i] = previousA*2;

            } else {
                previousFreq = frequencies[i] = previousFreq * TWELTH_ROOT_OF_TWO;
            }
        }
    }

    public double randomFrequency() {

        return 40+rndm.nextDouble()*800;
    }

    // Metodi etsii painetulle näppäimelle taajuuden

    /**
     *
     * @param keyPressed painetun koskettimen kirjain
     * @return taajuus, joka vastaa painettua kosketinta, ottaen huomioon käytössä oleva oktaavi
     */
    public double frequencyOf(int keyPressed) {
        System.out.print(String.format("\033[%dA",5)); // 5 riviä ylös
        System.out.print("\033[2K"); // poistetaan sisältö


        // Etsitään indeksi näppäimelle
        int indexOfChar = codeToIndex.getKeyboardIndex(keyPressed);

        if (indexOfChar > 17 || indexOfChar < 0) {
            if (indexOfChar == 18) {
                this.octaveDown();
                return -1;
            } else if (indexOfChar == 19) {
                this.octaveUp();
                return -1;
            } else if (indexOfChar == 20) {
                double rand = this.randomFrequency();
                System.out.println("\n\nSatunnainen taajuus: " + Math.round(rand) + " Hz  \n\n");
                return rand;
            }   else {
                System.out.println("\n\n\n\n");
                return -1;
            }
        }

        // Tulostetaan tietoa käyttäjälle painetusta näppäimestä
        System.out.println("\nKoskettimen numero: " + (currentOctave*12+indexOfChar));
        System.out.println("Taajuus: "+ Math.round(frequencies[currentOctave*12 + indexOfChar]) + " Hz  " + "          \n\n");

        // Katsotaan toistettava taajuus listalta ottamalla huomioon oktaavi (Oktaavissa on 12 sävelaskelta).
        return frequencies[currentOctave*12 + indexOfChar];
    }

    /**
     * oktaavi ylös
     */
    public void octaveUp() {
        if (currentOctave < 5) {
            currentOctave++;
        }

        System.out.println("\n\n\nOktaavi:" + currentOctave + " \n");
    }

    /**
     * oktaavi alas
     */
    public void octaveDown() {
        if (currentOctave > 0) {
            currentOctave--;
        }
        System.out.println("\n\n\nOktaavi:" + currentOctave + "\n");
    }

    public int getCurrentOctave() {
        return currentOctave;
    }
}
