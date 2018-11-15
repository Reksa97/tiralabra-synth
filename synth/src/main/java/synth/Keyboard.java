package synth;// Määritellään keyboard, jossa on 18 kosketinta. Lisäksi oktaavia voidaan vaihtaa, joten voidaan soittaa
// laajemmalta skaalalta.

import java.util.Random;

public class Keyboard {

    // Näppäimet vastaavat pianon koskettimia korkeusjärjestyksessä alhaalta ylös.
    char[] keys = {'a', 'w', 's', 'd', 'r', 'f', 't', 'g', 'h', 'u', 'j','i', 'k', 'o', 'l', 'ö', 'å', 'ä'};

    // Määritellään 78 soitettavaa taajuutta
    double[] frequencies = new double[78];

    // Tasavireessä sävel nousee puolisävelaskelta kun se kerrotaan kahden kahdennellatoista juurella
    public static final double TWELTH_ROOT_OF_TWO = 1.0594630943593;

    // Aluksi ollaan alhaalta laskettuna oktaavissa 3
    private int currentOctave = 3;

    // Häröilyyn
    private Random rndm = new Random();


    public Keyboard() {

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

        return 440+rndm.nextDouble()*50;
    }

    // Metodi etsii painetulle näppäimelle taajuuden
    public double frequencyOf(char keyPressed) {

        // Etsitään indeksi näppäimelle
        int indexOfChar = -1;

        switch (keyPressed) {
            case 'a':
                indexOfChar = 0;
                break;
            case 'w':
                indexOfChar = 1;
                break;
            case 's':
                indexOfChar = 2;
                break;
            case 'd':
                indexOfChar = 3;
                break;
            case 'r':
                indexOfChar = 4;
                break;
            case 'f':
                indexOfChar = 5;
                break;
            case 't':
                indexOfChar = 6;
                break;
            case 'g':
                indexOfChar = 7;
                break;
            case 'h':
                indexOfChar = 8;
                break;
            case 'u':
                indexOfChar = 9;
                break;
            case 'j':
                indexOfChar = 10;
                break;
            case 'i':
                indexOfChar = 11;
                break;
            case 'k':
                indexOfChar = 12;
                break;
            case 'o':
                indexOfChar = 13;
                break;
            case 'l':
                indexOfChar = 14;
                break;
            case 'ö':
                indexOfChar = 15;
                break;
            case 'å':
                indexOfChar = 16;
                break;
            case 'ä':
                indexOfChar = 17;
                break;
                default:
                    return -1;
        }

        // Tulostetaan tietoa käyttäjälle painetusta näppäimestä
        System.out.println("Koskettimen numero: " + (currentOctave*12+indexOfChar));
        System.out.println("Taajuus: "+ Math.round(frequencies[currentOctave*12 + indexOfChar]) + " Hz");
        System.out.println();

        // Katsotaan toistettava taajuus listalta ottamalla huomioon oktaavi (Oktaavissa on 12 sävelaskelta).
        return frequencies[currentOctave*12 + indexOfChar];
    }

    public void octaveUp() {
        if (currentOctave < 5) {
            currentOctave++;
            System.out.println("Oktaavi:" + currentOctave);
        }
    }

    public void octaveDown() {
        if (currentOctave > 0) {
            currentOctave--;
            System.out.println("Oktaavi: " + currentOctave);
        }
    }
}
