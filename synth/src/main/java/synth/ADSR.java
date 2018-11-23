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

    public double getEnvelopeNext() {

        double env = 1;

        if (decayStarted) {
            env = this.envelopeNext;
            envelopeNext -= decayDec;
            //return env;
        }

        // jos attack ei ole vielä käsitelty
        if (!attackDone) {
            env = this.envelopeNext;
            envelopeNext += attackInc;
            if (env >= 1) {
                this.attackDone = true;
            }
        }

        if (env > 1) {
            env = 1;
        }
        return env;
    }

    public void keyLifted() {
        this.decayStarted = true;
        this.attackDone = true;
    }

    public void setAttack(int amount) {

        if (amount < 0) {
            return;
        }

        if (amount == 0) {
            envelopeNext = 1;
            attackInc = 1;
            attackDone = true;
            decayStarted = true;
            decayDec = 1;
        }   else {
            // kun amount=100, on attackin kesto 2 sekuntia. 50 -> 1 sekuntti ja 25 -> 0,5 sekuntia
            attackInc =  5*(1d/(sampleRate * (amount/50d)));
            decayDec = (1d/(sampleRate * (amount/50d)));

        }
    }

    public void resetEnvelopes() {
        this.envelopeNext = 0;
        this.attackDone = false;
        this.decayStarted = false;
    }

}
