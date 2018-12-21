## Käyttöohje

### Käynnistys

#### Jar-tiedosto

Lataa [täältä](https://github.com/Reksa97/tiralabra-synth/releases/tag/1.0) jar-tiedosto, ja käynnistä se komennolla `java -jar synth-1.0-all.jar`.

#### Gradle

Ladattuasi projektin omalle koneellesi, suorita `synth` kansiossa komennot `./gradlew build` ja                                  
`java -jar build/libs/synth-1.0-all.jar` tai vaihtoehtoisesti `./gradlew run`.

### Ohjelman käyttäminen

Ohjelma kysyy aluksi komentoriviltä kuinka monta oskillaattoria halutaan käyttää. Kun luku on syötetty, syntetisaattori avautuu omaan ikkunaan. 
Klikkaa ikkunaa, jotta se saa fokuksen. Komentoriville tulostuu tietoa painetuista koskettimista. ADSR-envelopeja voidaan säätää vasemman 
puolen liu'uista ja erilaisia aaltotyppiyhdistelmiä saadaan muuttamalla aaltotyyppejä valikoista.

Käytössä on seuraavat koskettimet ja niitä vastaavat sävelet:

| Näppäin | Sävel |
|---------|-------|
| a | A |
| w | A#/Bb |
| s | B |
| d | C |
| r | C#/Db |
| f | D |
| t | D#/Eb |
| g | E |
| h | F |
| u | F#/Gb |
| j | G |
| i | G#/Ab |
| k | A |
| o | A#/Bb |
| l | B |
| ö | C |
| å | C#/Db |
| ä | D |

ja lisäksi toiminnot:

| Näppäin | Toiminto |
|---|---|
| m | oktaavi ylöspäin |
| n | oktaavi alaspäin |
| 0 | satunnainen sävel |

### Sulkeminen
Ohjelma suljetaan sulkemalla Synth-ikkuna.
