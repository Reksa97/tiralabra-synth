package synth;

public class ADSR {
    // attackNext kertoo seuraavan samplen kertoimen kun attackDone = false
    private double envelopeNext;
    private boolean attackDone;
    // kertoo kuinka paljon kerroin kasvaa jokaisessa samplessa
    private double attackInc;

    private boolean releaseStarted;
    private double releaseDec;


    private int sampleRate;

    public ADSR() {
        this.sampleRate = Synth.AudioInfo.SAMPLE_RATE;
        this.envelopeNext = 0;
        this.attackInc = 1;
        this.attackDone = false;
        this.releaseDec = 1;
        this.releaseStarted = false;
    }

    /**
     *
     * @return seuraava envelopen arvo väliltä 0-1.
     */
    public double getEnvelopeNext() {
        // jos säätöjä ei käytetä on envelopen arvo tasan 1
        double env = 1;



        // jos attack ei ole vielä käsitelty
        if (!attackDone) {
            env = this.envelopeNext;
            envelopeNext += attackInc;
            if (env >= 1) {
                this.attackDone = true;
                envelopeNext = 1;
            }
        }
        // kun päästetty koskettimesta irti, alkaa decay
        if (releaseStarted) {
            env = this.envelopeNext;
            envelopeNext -= releaseDec;
        }

        if (env > 1) {
            env = 1;
            envelopeNext = 1;
        }
        /* jos halutaan nähdä envelopen määrä terminaalista
        System.out.print(String.format("\033[%dA",3)); // 3 riviä ylös
        System.out.print("\033[2K"); // poistetaan sisältö
        System.out.println(env  + " atk: " + attackDone +" dec: "+ decayStarted + "\n \n"); */
        return env;
    }

    /**
     * kun koskettimesta päästetään irti, alkaa decay, jolloin attackin täytyy loppua
     */
    public void keyLifted() {
        this.releaseStarted = true;
        this.attackDone = true;
    }

    /**
     *
     * @param amount aseta attackille arvo väliltä 0-100
     */
    public void setAttack(int amount) {

        if (amount <= 0 || amount > 100) {
            envelopeNext = 1;
            attackInc = 1;
            attackDone = true;
        }   else {
            // kun amount=100, on attackin kesto 2 sekuntia. 50 -> 1 sekuntti ja 25 -> 0,5 sekuntia
            attackInc =  (1d/(sampleRate * (amount/50d)));
        }
    }

    /**
     *
     * @param amount aseta releaselle arvo väliltä 0-100
     */
    public void setRelease(int amount) {
        if (amount <= 0 || amount > 100) {
            releaseDec = 0.2;
        }   else {
            // kun amount=100, on releasen kesto 2 sekuntia. 50 -> 1 sekuntti ja 25 -> 0,5 sekuntia
            releaseDec = (1d/(sampleRate * (amount/50d)));
        }
    }

    /**
     * alusta envelopet uudelle äänelle
     */
    public void resetEnvelopes() {
        this.envelopeNext = 0;
        this.attackDone = false;
        this.releaseStarted = false;
    }

}
