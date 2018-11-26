## Toteutusdokumentti

Sovellus sisältää kolme pakkausta

### main

Pakkaus sisältää luokan Main, jossa kutsutaan Synth-luokan konstruktoria. Luokassa annetaan myös ohjeita käyttäjälle.

### audiothread
Pakkaus sisältää OpenAL-kirjaston kanssa toimimisen. AudioThread -luokka vastaanottaa Synth-luokalta sampleja ja lähettää 
ne edelleen äänikortille. OpenALException vastaa mahdollisten virheilmoitusten muuttamisesta Javalle.

### synth
Pakkaus sisältää itse syntetisaattorin toiminnallisuudet. Synth-luokka luo GUI:n ja kerää yhteen eri luokat ja luo niistä 
sampleista koostuvia puskureita, jotka lähetetään AudioThreadiin.

Keyboard-luokassa tehdään näppäimistä koskettimet ja annetaan niille eri taajuus tai toiminto. Luokalle syötetään painettu 
näppäin, joka muunnetaan taajuudeksi.

Oscillator-luokka muuttaa taajuuden wavetablejen avulla sampleiksi.

Wavetable-luokka sisältää aaltomuotojen wavetablet.

ADSR-luokka vastaa envelopeista. Synth-luokka hakee jokaiselle samplelle kertoimen tältä luokalta. Kerroin vaihtuu sliderien 
valintojen mukaan.
