package synth;

public class ADSR {
    // attackNext kertoo seuraavan samplen kertoimen kun attackDone = false
    private double envelopeNext;
    private boolean attackDone;
    // kertoo kuinka paljon kerroin kasvaa jokaisessa samplessa
    private double attackInc;

    private boolean decayStarted;
    private double decayDec;


    private int sampleRate;

    public ADSR() {
        this.sampleRate = Synth.AudioInfo.SAMPLE_RATE;
        this.envelopeNext = 0;
        this.attackInc = 1;
        this.attackDone = false;
        this.decayDec = 1;
        this.decayStarted = false;
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
        if (decayStarted) {
            env = this.envelopeNext;
            envelopeNext -= decayDec;
        }

        if (env > 1) {
            env = 1;
            envelopeNext = 1;
        }
        /*
        System.out.print(String.format("\033[%dA",3)); // 3 riviä ylös
        System.out.print("\033[2K"); // poistetaan sisältö
        System.out.println(env  + " atk: " + attackDone +" dec: "+ decayStarted + "\n \n"); */
        return env;
    }

    /**
     * kun koskettimesta päästetään irti, alkaa decay, jolloin attackin täytyy loppua
     */
    public void keyLifted() {
        this.decayStarted = true;
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

    public void setDecay(int amount) {
        if (amount <= 0 || amount > 100) {
            decayDec = 1;
        }   else {
            // kun amount=100, on decayn kesto 2 sekuntia. 50 -> 1 sekuntti ja 25 -> 0,5 sekuntia
            decayDec = (1d/(sampleRate * (amount/50d)));
        }
    }

    /**
     * alusta envelopet uudelle äänelle
     */
    public void resetEnvelopes() {
        this.envelopeNext = 0;
        this.attackDone = false;
        this.decayStarted = false;
    }

}
