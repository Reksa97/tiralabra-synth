package synth;

public class ADSR {
    // attackNext kertoo seuraavan samplen kertoimen kun attackDone = false
    private double attackNext;
    private boolean attackDone;
    // kertoo kuinka paljon kerroin kasvaa jokaisessa samplessa
    private double attackInc;

    private int sampleRate;

    public ADSR() {
        this.sampleRate = Synth.AudioInfo.SAMPLE_RATE;
        this.attackNext = 0;
        this.attackInc = 1;
        this.attackDone = false;
    }

    public double getEnvelopeNext() {
        // jos envelopet
        double env = 1;

        // jos attack ei ole vielä käsitelty
        if (!attackDone) {
            env = this.attackNext;
            attackNext += attackInc;
            if (env >= 1) {
                this.attackDone = true;
            }
            return env;
        }

        return env;
    }

    public void setAttack(int amount) {

        if (amount < 0) {
            return;
        }

        if (amount == 0) {
            attackNext = 1;
            attackInc = 1;
            attackDone = true;
        }   else {
            // kun amount=100, on attackin kesto 2 sekuntia. 50 -> 1 sekuntti ja 25 -> 0,5 sekuntia
            attackInc = (1d/(sampleRate * (amount/50d)));
        }
    }

    public void resetEnvelopes() {
        this.attackNext = 0;
        this.attackDone = false;
    }
}
