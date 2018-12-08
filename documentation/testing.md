## Testausdokumentti

### Jatkuva integraatiotestaus

#### Travis CI

[![Build Status](https://travis-ci.com/Reksa97/tiralabra-synth.svg?branch=master)](https://travis-ci.com/Reksa97/tiralabra-synth)

#### Codecov

[![codecov](https://codecov.io/gh/Reksa97/tiralabra-synth/branch/master/graph/badge.svg)](https://codecov.io/gh/Reksa97/tiralabra-synth)

Yksikkötestausta tehty ja raportti näkyy Codecovissa. Pakkauksia audiothread ja gui ei yksikkötestata, koska Travisilla OpenAL-kirjaston käyttö aiheutti virheitä.

Myös paikallisesti voidaan ajaa jUnit testejä ja luoda raportteja Jacocolla. Raportin saa luotua kansiossa `tiralabra-synth/synth` komennolla `./gradlew test jacocoTestReport`. Raportti ilmestyy suorituksen jälkeen sijaintiin `tiralabra-synth/synth/build/jacoco/test/index.html`. 

#### Codacy

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/ab3dcf3ccf2244c1a1be8baa74b1e4e9)](https://www.codacy.com/app/Reksa97/tiralabra-synth?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Reksa97/tiralabra-synth&amp;utm_campaign=Badge_Grade)

Codacy tarkkailee koodin laatua, antaa korjausehdotuksia ja koodin laatua kuvaavan arvosanan.

### Empiirinen testaus

Testasin oskillaattorien määrän ja prosessointitehon vaativuuden yhteyttä mutta huomasin, että osuus prosessorin käytöstä pysyi koko ajan samana. Kun oskillaattorien määrä kasvatettiin yli 8000, pitkien äänten toistaminen alkoi pätkiä ja poksua.

Oskillaattoreiden määrän ja sovelluksen käyttämän muistin määrän yhteys on kuitenkin selvä.

oskillaattorit | muisti (MiB)
-----|-----
1 | 59.0
2 | 59.5
4 | 60.0
8 | 60.5
16 | 61.0
32 | 62.0
64 | 66.0
128 | 67.5
256 | 77.0
512 | 96.0
1024 | 148.5
2028 | 182.0
4096 | 243.0
8192 | 480.0

Testasin myös wavetablejen täyttämiseen menevän ajan suhdetta niiden kokoon. Sovelluksessa käytetään normaalisti kokoa 8192. Näyttäisi siltä, että taulun koon n suhteen sen täyttämisen aikavaativuus on O(n).

koko | kesto (ms)
---- | ----
2048 | 7.2 
4096 | 14.3
8192 | 31.5
16384 | 73.0
32768 | 103.4
