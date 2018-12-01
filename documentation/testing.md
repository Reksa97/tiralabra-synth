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
Tarkoituksena on testata ainakin prosessoinnin vaativuutta kun oskillaattorien määrää kasvatetaan.
