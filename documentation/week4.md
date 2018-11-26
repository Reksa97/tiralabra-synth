## Viikkoraportti 4

Tällä viikolla toteutin ADSR - luokkaan decay, sustain ja release -ominaisuudet. 

ADSR-luokalta haetaan siis jokaiselle samplelle kerroin, joka kasvaa tai laskee edellisestä kertoimesta sliderien määrämän määrän.
Vain yksi envelopeista on käytössä kullakin hetkellä. Kun painetaan kosketinta ensimmäisen kerran, tulee ensiksi voimaan attack.
Kerroin kasvaa halutun määrän joka kerralla, kunnes saavutetaan äänenvoimakkuuden huippu, eli kerroin 1. Tällöin annetaan vuoro 
decaylle, jolloin kerroin laskee halutun märään kunnes saavutetaan sustain-sliderilla valittu taso, joka pidetään kunnes päästetään 
irti koskettimesta. Nyt voimaan tulee release, joka laskee kerrointa halutun määrän, kunnes kerroin laskee nollaan, ja äänen 
tuottaminen lopetetaan.

Koitin myös ottaa käyttöön travis-ci mutta jostain syystä testit eivät mene läpi palvelimella, vaikka ne menevätkin lokaalisti 
läpi. Tähän mennyt jo melko paljon aikaa, joten varmaan täytyy hoitaa yksikkötestien näyttäminen muuten. Mietin mitä empiirisiä testejä aiheesta voisi tehdä, mutta ideoita ei ole tullut paljoa. Ainakin voisin testata prosessoinnin vaativuuden kasvua suhteessa oskillaattorien määrään. Muita vaihtoehtoja voisi olla wavetablejen arvojen tarkkuuden vaikutus ja näytteenottotaajuuden vaikutus. Äänen luominen on reaaliaikaista, joten testaaminen on ehkä monimutkaisempaa, enkä vielä tiedä miten sen aion tehdä.

Tällä viikolla aikaa käytetty noin 11 tuntia.
