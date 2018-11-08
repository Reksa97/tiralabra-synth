## Viikkoraportti 2

Tällä viikolla tutustuin lisää syntetisaattorien toimintaan ja sain toteutettua oskillaattorille ja koskettimille luokat. 
Lisäksi aloittelin testien tekemista Jacocolla.

Oskillaattori tuottaa jotain raakaääntä, jonka syntetisaattori voi sitten muuttaa eri tavoin ja lähettää äänikortille.
Käytössä on nyt tavallisimmat aaltotyypit, eli sini-, saha-, neliö- ja kolmio-aallot. Oskilllaattoreiden tuottamaa aaltotyyppiä
voidaan vaihdella ja yhdistellä vapaasti. Tuloksena on erilaisia yhdistelmäaaltoja. Saman aikaan toimivien oskillaattoreiden määrän
kasvu nostaa ohjelman laskennan vaativuutta.

Kosketin-luokka määrittää taajuudet eri koskettimille. Taajuudet ovat tasavireestä, missä sävelen A taajuus on 440Hz.
Oktaavia ylemmän A:n taajuus on kaksinkertainen, eli 880Hz. Oktaavissa on 12 puolisävelaskelta ja sävelestä seuraavaksi ylempi 
on edellinen kerrottuna kahden kahdennellatoista neliöjuurella. Siis esim. A:ta puolisävelaskeleen yläpuolella olevan A#:n taajuus
on noin 466Hz. Valitsin koskettimiksi hieman pianoa muistuttavat näppäinvälit A-Ä ja W-Å. Lisäksi oktaavin saa ylöspäin painamalla
M, ja alas painamalla N.

Testien tekeminen oli hieman haastaava, mutta sain tehtyä niitä melko kattavasti. Niihin täytyy paneutua vielä lisää.
