## Toteutusdokumentti

Sovellus sisältää neljä pakkausta

### main

Pakkaus sisältää luokan Main, jossa kysytään oskillaattorien määrä ja kutsutaan Frame-luokan konstruktoria. Luokassa annetaan myös ohjeita käyttäjälle.

### audiothread
Pakkaus sisältää OpenAL-kirjaston kanssa toimimisen. AudioThread -luokka vastaanottaa Synth-luokalta sampleja ja lähettää 
ne edelleen äänikortille. OpenALException vastaa mahdollisten virheilmoitusten muuttamisesta Javalle.

### synth
Pakkaus sisältää itse syntetisaattorin toiminnallisuudet. Synth-luokka kerää oskillaattoreilta sampleja, yhdistää ne ja kokoaa ne puskureihin, jotka AudioThread-luokka kerää.

Keyboard-luokassa tehdään näppäimistä koskettimet ja annetaan niille eri taajuus tai toiminto. Luokalle syötetään painettu 
näppäin, joka muunnetaan taajuudeksi.

Oscillator-luokka muuttaa taajuuden wavetablejen avulla sampleiksi.

Wavetable-luokka sisältää aaltomuotojen wavetablet.

ADSR-luokka vastaa envelopeista. Synth-luokka hakee jokaiselle samplelle kertoimen tältä luokalta. Kerroin vaihtuu sliderien 
valintojen mukaan.

Synth-pakkaukselle on myös testipakkaus, joka sisältää yksikkötestit kaikille pakkauksen luokille.

### gui
Frame luokka vastaa ohjelmaikkunasta ja toiminnallisuuksien kokoamisesta. Konstruktorille syötetään haluttujen oskillaattorien määrä, jonka perusteella luodaan oskillaattorit ja niille paneelit. ADSR-liu'ut luodaan ja asetellaan niille labelit ja kuuntelijat. Luokka määrittelee myös KeyAdapterin joka käsittelee näppäinten painamiset.

### utils
Pakkauksessa on tällä hetkellä vain yksi luokka KeyCodeToKeyboardIndex, jonka avulla Keyboard muuttaa sille annetun näppäimen 
koodin koskettimen indeksiksi. Koskettimien indeksit menevät äänenkorkeuden mukaisesti. Merkkien `ö`, `å` ja `ä` koodit ovat paljon suuremmat kuin muiden, joten ne muutetaan pienemmeksi ja saadaan aikaan 88-kokoinen int-array, josta saadaan koodeilla (0-88) niitä vastaava koskettimen indeksi. Esimerkiksi näppäintä `h` vastaava koskettimen indeksi löytyy int-arrayn indeksistä `72`, joka on näppäimen KeyEvent-koodi. 

## Sävelen tuottaminen
Painetaan jotain koskettimeksi määriteltyä näppäintä, esim `a`. Frame-luokassa oleva KeyAdapter rekisteröi tapahtuman ja hakee Keyboard-luokalta merkkiä `a` vastaavan taajuuden. Saatu taajuus asetetaan kaikille käytössä oleville oskillaattoreille ja pyydetään AudioThreadia tuottamaan ääntä. AudioThread gettaa Synth-luokalta seuraavan puskurin ja lähettää sen äänikortille.
