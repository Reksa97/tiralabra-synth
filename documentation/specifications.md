## Syntetisaattori 
Tarkoituksena on toteuttaa digitaalinen syntetisaattori, jolla voidaan tuottaa sini-, neliö-, saha- tai kolmioaaltoa, yhdistellä niitä ja muuttaa niitä ADSR-vaipoilla(envelopeilla),
sekä mahdollisesti VCF-suodattimilla(filttereillä).

Kun painetaan kosketinta syntetisaattorissa:
* Attack määrittelee ajan, missä ääni saavuttaa kovimman voimakkuutensa.
* Decay määrittelee ajan, missä ääni laskee sustain-tasolle.
* Sustain määrittelee äänenvoimakkuuden, missä ääni pysyy, kun kosketinta pidetään pohjassa.
* Release määrittelee ajan, missä ääni laskee nollaan koskettimen vapauduttua.

Ohjelma saa syötteenä näppäimistöltä äänen korkeuden ja pituuden, sekä valitut ADSR-arvot, joiden avulla ääni muokataan halutuksi.

### Käytettävät algoritmit ja tietorakenteet

#### Digitaalinen ääni
Työssä käytetään yleistä 44100 Hz näytteenottotaajutta, eli näytteitä on 44100 sekunnissa. Näytteet tallennetaan aaltofunktiosta 16-bittiseen short-arrayhin,
eli amplitudi skaalataan välille -32768-32767. LWJGL OpenAL-kirjaston avulla näytteet puskuroidaan äänikortille.

#### Oskillaattori
Oskillaattorilla voidaan tuottaa joko sini-, neliö-, saha- tai kolmioaaltoa. Niitä voidaan käyttää samanaikasesti useampia,
jolloin äänikortille lähetetään niiden summa-aalto. Yhden näytteen laskemisessa käytetään tavallisia matemaattisia operaatioita, sekä sin- ja signum-funktioita, eli se vie vakioajan ja sen aikavaativuus on O(1). Wavetableja käyttämällä
laskentatehoa tarvittaisiin vähemmän, mutta O-merkintä ei muuttuisi. Kun oskillaattoreiden määrä on
n, on niiden yhteen laskemisen aikavaativuus O(n).

#### Koskettimet
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

##### Lähteet
[Wikipedia - ADSR](https://fi.wikipedia.org/wiki/ADSR)

[OpenAL](https://www.openal.org/documentation/OpenAL_Programmers_Guide.pdf)
