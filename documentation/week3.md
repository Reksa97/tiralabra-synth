## Viikkoraportti 3

Tällä viikolla tutustuin wavetableihin ja attackin toimintaan, sekä toteutin niitä ohjelmaan. 
Lisäksi refaktoroin sovellusta pakkauksiin. Ohjelma on edistynyt mukavasti ja uskon että saan ADSR-envelopet tehtyä melko
nopeasti toimiviksi.

Opin että vaikka wavetablet eivät parannakkaan O-merkinnän aikavaatimuksia, on se silti paljon tehokkaampaa, kuin että jokaisen 
samplen ja aaltomuodon kohdalla laskettaisiin arvot uudestaan. Nyt siis lasketaan jokaisesta aaltotyypistä vain yksi aalto,
joka tallennetaan 8192 -kokoiseen double-arrayhin, josta sitten haetaan indeksin ja askelpituuden mukaan halutut arvot (aallot) 
halutulle taajuudelle.

Attack kertoo siis kuinka kauan kestää, että saavutetaan äänenvoimakkuuden huippu. Kun syntikka kerää oskillaattorien aallot 
yhteen, kerrotaan summa-aalto ADSR-luokalta haetulta envelopen arvolla (0-1). Envelopen arvo muuttuu jokaisella samplella, kun 
attack ei ole vielä käyty loppuun.
