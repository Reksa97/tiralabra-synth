package synth;

public class ADSR {
    // attackNext kertoo seuraavan samplen kertoimen kun attackDone = false
    private double envelopeNext;
    private boolean attackDone;
    // kertoo kuinka paljon kerroin kasvaa jokaisessa samplessa
    private double attackInc;

    private double decayDec;
    private boolean decayStarted;
    private boolean decayDone;

    private double sustainValue;
    private boolean sustainStarted;
    private boolean sustainDone;

    private boolean releaseStarted;
    private double releaseDec;


    // toStringiä varten
    private int attackAmount;
    private int decayAmount;
    private int sustainAmount;
    private int releaseAmount;

    private int sampleRate;

    /**
     * ADSR vastaa envelopejen toiminnasta.
     */
    public ADSR() {
        this.sampleRate = Synth.AudioInfo.SAMPLE_RATE;
        this.envelopeNext = 0;
        this.attackInc = 1;
        this.attackDone = false;

        this.decayDec = 1;
        this.decayStarted = false;
        this.decayDone = false;
        this.decayAmount = 100;

        this.sustainValue = 1;
        this.sustainStarted = false;
        this.sustainDone = false;
        this.sustainAmount = 100;

        this.releaseDec = 1;
        this.releaseStarted = false;
        this.releaseAmount = 0;
    }

    /**
     * Metodi katsoo, mikä envelope on käytössä ja palauttaa seuraavan arvon sen mukaisesti.
     * @return Seuraava envelopen arvo väliltä 0-1.
     */
    public double getEnvelopeNext() {
        double env = 1;

        if (!attackDone) {
            env = envelopeNext;
            this.envelopeNext += attackInc;
            if (env >= 1) {
                attackDone = true;
                decayStarted = true;
                envelopeNext = 1;
            }
        }
        if (decayStarted && !decayDone) {
            env = envelopeNext;
            envelopeNext -= decayDec;
            if (env < sustainValue) {
                envelopeNext = sustainValue;
                decayDone = true;
                sustainStarted = true;
            }
        }
        if (sustainStarted && !sustainDone) {
            env = envelopeNext;
            envelopeNext = sustainValue;
        }
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
        System.out.println(env  + " atk: " + attackDone + "dec: "+ decayStarted + "sus: "+sustainStarted + " rel: "+ releaseStarted + "\n \n"); */
        return env;
    }

    /**
     * Kun koskettimesta päästetään irti, alkaa decay, jolloin attackin täytyy loppua.
     */
    public void keyLifted() {
        this.releaseStarted = true;
        this.attackDone = true;
        this.decayDone = true;
        this.sustainDone = true;
    }

    /**
     *
     * @param amount Aseta attackille arvo väliltä 0-100
     */
    public void setAttack(int amount) {
        attackAmount = amount;
        if (amount <= 0 || amount > 100) {
            attackAmount = 0;
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
     * @param amount Aseta decaylle arvo väliltä 0-200
     */
    public void setDecay(int amount) {
        decayAmount = amount;
        if (amount <= 0 || amount > 200) {
            decayAmount = 100;
            envelopeNext = 1;
            decayDec = 1;
        }   else {
            // kun amount=100, kestäisi decaylla 2 sekuntia päästä 1-tasolta 0-tasolle. 50 -> 1 sekuntti ja 25 -> 0,5 sekuntia
            decayDec =  (1d/(sampleRate * (amount/50d)));
        }
    }

    /**
     *
     * @param amount Aseta sustainille arvo väliltä 0-100
     */
    public void setSustain(int amount) {
        sustainAmount = amount;
        if (amount <= 0 || amount >  100) {
            sustainAmount = 100;
            sustainValue = 1;
        }   else {
            sustainValue = (double) amount / 100d;
        }
    }


    /**
     *
     * @param amount Aseta releaselle arvo väliltä 0-100
     */
    public void setRelease(int amount) {
        releaseAmount = amount;
        if (amount <= 0 || amount > 100) {
            releaseAmount = 0;
            releaseDec = 0.2;
        }   else {
            // kun amount=100, on releasen kesto 2 sekuntia. 50 -> 1 sekuntti ja 25 -> 0,5 sekuntia
            releaseDec = (1d/(sampleRate * (amount/50d)));
        }
    }

    public int getAttack() {
        return attackAmount;
    }

    public double getAttackInc() {
        return attackInc;
    }

    public int getDecay() {
        return decayAmount;
    }

    public double getDecayDec() {
        return decayDec;
    }

    public int getSustain() {
        return sustainAmount;
    }

    public double getSustainValue() {
        return sustainValue;
    }

    public int getRelease() {
        return releaseAmount;
    }

    public double getReleaseDec() {
        return releaseDec;
    }

    /**
     * Resetoi envelopet uutta ääntä varten.
     */
    public void resetEnvelopes() {
        this.envelopeNext = 0;
        this.attackDone = false;
        this.decayStarted = false;
        this.decayDone = false;
        this.sustainStarted = false;
        this.sustainDone = false;
        this.releaseStarted = false;
    }

    /**
     *
     * @return Envelopejen arvot tulostusta varten.
     */
    @Override
    public String toString() {
        return "A: " + attackAmount + " D: " + decayAmount + " S: " + sustainAmount + " R: " + releaseAmount;
    }

}
