package utils;

public class KeyCodeToKeyboardIndex {
    private int[] codeToIndex = new int[88];

    public KeyCodeToKeyboardIndex() {
        for (int i=0; i<codeToIndex.length; i++) {
            codeToIndex[i] = -1;
        }

        // koskettimet ja niitä vastaavat koodit
        char[] keys = {'a', 'w', 's', 'd', 'r', 'f', 't', 'g', 'h', 'u', 'j','i', 'k', 'o', 'l', 'ö', 'å', 'ä', 'n', 'm', '0'};
        int[] keyCodes = new int[keys.length];
        for (int i=0; i<keys.length; i++) {
            int code = java.awt.event.KeyEvent.getExtendedKeyCodeForChar(keys[i]);

            // ö, å ja ä koodit ovat paljon suurempia ja muut ovat kaikki alle 100, joten muunnetaan niiden koodit pienemmiksi
            if (code > 100) {
                code -= 16777370;
            }
            keyCodes[i] = code;
        }

        for (int i=0; i<keyCodes.length; i++) {
            codeToIndex[keyCodes[i]] = i;
        }
    }

    public int getKeyboardIndex(int keyCode) {
        if (keyCode > 87) {
            keyCode -= 16777370;
        }

        if (keyCode < 0) {
            return -1;
        }

        return codeToIndex[keyCode];
    }
}
