## Syntetisaattori 
Tarkoituksena on toteuttaa digitaalinen syntetisaattori, jolla voidaan tuottaa erilaisia raakaääniä, esimerkiksi sini-aallosta, ja muuttaa niitä ADSR-vaipoilla(envelopeilla),
sekä mahdollisesti VCF-suodattimilla(filttereillä).

Kun painetaan kosketinta syntetisaattorissa:
* Attack määrittelee ajan, missä ääni saavuttaa kovimman voimakkuutensa.
* Decay määrittelee ajan, missä ääni laskee sustain-tasolle.
* Sustain määrittelee äänenvoimakkuuden, missä ääni pysyy, kun kosketinta pidetään pohjassa.
* Release määrittelee ajan, missä ääni laskee nollaan koskettimen vapauduttua.

Ohjelma saa syötteenä näppäimistöltä äänen korkeuden ja pituuden, sekä valitut ADSR-arvot, joiden avulla ääni muokataan halutuksi.

### Käytettävät algoritmit ja tietorakenteet
Aihe ei ole minulla vielä täysin hallussa ja en tiedä mitä kaikkea työhön tulee. Tulen päivittämään tätä osiota työn edetessä.

#### Digitaalinen ääni
Työssä käytetään yleistä 44100 Hz näytteenottotaajutta, eli näytteitä on 44100 sekunnissa. Näytteet tallennetaan aaltofunktiosta short-arrayhin,
eli amplitudi skaalataan välille 0-32767. LWJGL OpenAL-kirjaston avulla näytteet puskuroidaan äänikortille 16-bittisenä.


##### Lähteet
[Wikipedia - ADSR](https://fi.wikipedia.org/wiki/ADSR)

[OpenAL](https://www.openal.org/documentation/OpenAL_Programmers_Guide.pdf)
