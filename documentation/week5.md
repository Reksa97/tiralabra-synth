## Viikkoraportti 5

Viime viikolla käytin paljon aikaa Travis CI:n käyttöönottoon mutta OpenAL kirjasto aiheutti ongelmia sen kanssa. Päädyin refaktoroimaan sovellusta paketteihin gui, audiothread ja synth. Paketti synth on nyt täysin riippumaton gui:sta ja audiothreadista, joten sille saadaan testit tehtyä kivuttomasti. Refaktoroin myös testejä ja kasvatin kattavuutta. Nyt build menee läpi Travis CI:stä ja sain myös Codecovin käyttöön. Päätin jättää gui- ja audiothread- paketit testaamatta.

Poistin Supplierin importin, ja korvasin toiminnallisuuden Synth-luokan metodilla getNextBuffer.

Aikaisemmin kun haettiin painettua näppäintä vastaavaa säveltä, käytettiin Javan switchiä. Toteutin toiminnon nyt omalla pakkaukseen `utils` luokkaan `KeyCodeToKeyboardIndex`. Käytössä on array, jonka indeksejä vastaa keyCode ja sisältö on keyCodea vastaava koskettimien indeksi. Luokan avulla saatiin aikavaativuus pienennettyä O(1). Nyt käytössä ei ole enää valmiita tietorakenteita.

Seuraavaksi aion keskittyä empiiriseen testaukseen, dokumentaatioon, ja jos aika riittää voisin ehkä tehdä vielä jonkin toiminnon.

Aikaa meni tällä viikolla noin 12 tuntia.
