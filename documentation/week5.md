## Viikkoraportti 5

Viime viikolla käytin paljon aikaa Travis CI:n käyttöönottoon mutta OpenAL kirjasto aiheutti ongelmia sen kanssa. Päädyin refaktoroimaan sovellusta paketteihin gui, audiothread ja synth. Paketti synth on nyt täysin riippumaton gui:sta ja audiothreadista, joten sille saadaan testit tehtyä kivuttomasti. Refaktoroin myös testejä ja kasvatin kattavuutta. Nyt build menee läpi Travis CI:stä ja sain myös Codecovin käyttöön. Päätin jättää gui- ja audiothread- paketit testaamatta.




