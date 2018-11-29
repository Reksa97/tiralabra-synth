# Syntetisaattori 
Tarkoituksena on toteuttaa digitaalinen syntetisaattori, jolla voidaan tuottaa sini-, neliö-, saha- tai kolmioaaltoa, yhdistellä niitä ja muuttaa niitä ADSR-vaipoilla(envelopeilla).

Kun painetaan kosketinta syntetisaattorissa:
  * Attack määrittelee ajan, missä ääni saavuttaa kovimman voimakkuutensa.
  * Decay määrittelee ajan, missä ääni laskee sustain-tasolle.
  * Sustain määrittelee äänenvoimakkuuden, missä ääni pysyy, kun kosketinta pidetään pohjassa.
  * Release määrittelee ajan, missä ääni laskee nollaan koskettimen vapauduttua.

Ohjelma saa syötteenä näppäimistöltä äänen korkeuden ja pituuden, sekä valitut ADSR-arvot. Syntetisaattori kerää käytössä olevilta oskillaattoreilta yhdet samplet, summaa ne yhteen ja kertoo saadun amplitudin seuraavalla envelopen antamalla kertoimella. Samplet lähtetään puskureissa äänikortille.

## Käytettävät algoritmit ja tietorakenteet

### Digitaalinen ääni
Työssä käytetään yleistä 44100 Hz näytteenottotaajutta, eli näytteitä on 44100 sekunnissa. Näytteet tallennetaan aaltofunktiosta 16-bittiseen short-arrayhin,
eli amplitudi skaalataan välille -32768-32767. LWJGL OpenAL-kirjaston avulla näytteet puskuroidaan äänikortille.

### Oskillaattori
Oskillaattorilla voidaan tuottaa joko sini-, neliö-, saha- tai kolmioaaltoa wavetablen avulla. Oskillaattoreita voidaan käyttää samanaikasesti useampia, jolloin äänikortille lähetetään niiden summa-aalto. Oskillaattori hakee käytössä olevaa aaltomuotoa vastaavasta wavetablesta sampleja askelpituuden mukaan. Askelpituus lasketaan oskillaattorille syötetystä äänentaajuudesta, joka kerrotaan wavetablen koolla ja jaetaan näytteenottotaajuudella.

### Wavetable
Käytetyille aaltomuodoille (sini, neliö, saha ja kolmio), luodaan jokaiselle 8192-kokoinen double-array joihin tallennetaan yksi kokonainen aalto. Sini-aallon laskemisessa käytetään tavallisten matemaattisten operaatioiden lisäksi sini-funktiota, neliö-aallossa signum-funktiota, saha-aallossa floor-funktiota ja kolmio-aallossa abs-funktiota. Yhden arvon laskeminen on siis aikavaatimukseltaan O(1). Taulujen täyttäminen on arrayn koon n suhteen O(n) operaatio. Ilman wavetableja jokainen näyte täytyisi laskea uudelleen ja vaikka aikavaatimus ei kasvaisi, olisi se silti paljon epätehokkaampi tapa.

### ADSR-envelopet

Synth-luokka hakee ADSR-luokalta jokaiselle samplelle kertoimen väliltä 0-1. Vain yksi envelopeista on käynnissä kullakin hetkellä.

#### Attack
Attackin arvo voidaan valita liulla, jolla on arvot 0-100. Arvoa muuttamalla vaikutetaan siihen, kuinka paljon kerroin kasvaa jokaisella samplella. Oletusarvo 0 tarkoittaa, että korkein äänenvoimakkuus saavutetaan heti kun ääni alkaa. Arvolla 100 korkein äänenvoimakkuus saavutetaan 2 sekuntia äänen alun jälkeen, arvolla 50 sekunnin jälkeen, ja arvolla 25 puolen sekunnin jälkeen jne. Jos kosketin on vielä pohjassa kun huippu on saavutettu, siirrytään decayhin.

#### Decay
Decay-liuku voi saada arvoja välillä 0-200. Arvot vaikuttavat siihen, kuinka paljon kerroin laskee jokaisella samplella. Oletusarvolla 100 kestäisi kertoimesta 1 laskeminen nollaan 2 sekuntia. Arvolla 200 kestäisi 4 sekuntia, samaan tapaan kuin attackilla. Kun kerroin on laskenut sustainin tasolle on decay käsitelty.

#### Sustain
Sustain voi saada arvoja välillä 0-100. Kun attack ja decay on käsitelty ja kosketin on vielä pohjassa, pysyy kerroin samana kunnes päästetään irti. Arvolla 100 sustainin aikana kerroin on 1, arvolla 50 kerroin on 0.5, arvolla 25 kerroin on 0.25 jne.

#### Release
Release voi saada arvo välillä 0-100. Kun päästetään irti koskettimesta, release alkaa oli käynnissä mikä vain vaihe. Oletusarvolla 0 äänen tuotto loppuu heti, arvolla 50 kestää sekunti ja arvolla 100 kestää 2 sekuntia.

### Koskettimet
Kosketin määrittää soivan sävelen, eli käytössä olevan taajuuden. Sävelen A taajuutena käytetään 440Hz ja seuraavan oktaavin
saa kertomalla tai jakamalla taajuuden kahdella. Oktaavi jaetaan 12 säveleen (puolisävelaskeleeseen) ja seuraavan saa edeltävästä kertomalla sen kahden kahdennellatoista juurella (noin 1.0594630943593). Kun koskettimien määrä on n, on niiden
taajuuksien laskemisen aikavaativuus O(n).

Kosketinta painettaessa etsitään sen indeksi. Listalta etsimisen aikavaativuus olisi O(n), mutta toteutetaan se nyt Javan
switch -toiminnolla, joka on luultavasti nopeampi. Tähän voisi keksiä ja toteuttaa itse jonkin systeemin.

### Käytössä olevat koskettimet ja niitä vastaavat sävelet

a = A

w = A#/Bb

s = B

d = C

r = C#/Db

f = D

t = D#/Eb

g = E

h = F

u = F#/Gb

j = G

i = G#/Ab

k = A

o = A#/Bb

l = B

ö = C

å = C#/Db

ä = D

#### Lisäksi

m = oktaavi ylöspäin

n = oktaavi alaspäin

0 = satunnainen sävel

##### Lähteet
[Wikipedia - ADSR](https://fi.wikipedia.org/wiki/ADSR)

[Wikipedia - Synthesizer](https://en.wikipedia.org/wiki/Synthesizer#Components)

[Wikipedia - Aaltotyypit](https://en.wikipedia.org/wiki/Non-sinusoidal_waveform)

[OpenAL](https://www.openal.org/documentation/OpenAL_Programmers_Guide.pdf)
