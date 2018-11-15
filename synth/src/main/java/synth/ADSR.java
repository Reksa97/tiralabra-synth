package synth;

public class ADSR {
    private double attackNext;
    private double attackIncrement;
    private int sampleRate;

    public ADSR() {
        this.sampleRate = Synth.AudioInfo.SAMPLE_RATE;
        this.attackNext = 0;
        this.attackIncrement = 1;
    }

    public double getAttackNext() {
        double att = this.attackNext;

        if (att >= 1) {
            attackNext = 1;
        }   else {
            attackNext += attackIncrement;
        }
        return att;
    }

    public void setAttack(int amount) {
        if (amount == 0) {
            attackNext = 1;
            attackIncrement = 1;
        }   else {
            attackIncrement = (1d/(sampleRate * (amount/50d)));
        }
    }

    public void resetEnvelopes() {
        this.attackNext = 0;
    }
}
