# Board-Game-Collector

## Opis zadania
Naszym zadaniem jest stworzenie aplikacji do przechowywania informacji o kolekcji gier
planszowych o roboczym tytule BoardGameCollector (BGC). W aplikacji mamy mieć
możliwość dodawania nowych posiadanych gier wraz z dodatkowymi informacjami (np. data
zakupu, cena, kod) zapisywania informacji o lokalizacji gier i przeglądania naszej kolekcji.
Aplikacja ma współpracować z serwisem BoardGameGeek (boardgamegeek.com) w zakresie
wyspecyfikowanym w dalszej części opisu.
## Szczegóły techniczne
Aplikacja musi wykorzystywać bazę danych SQLite, w której będzie przechowywać
informację o grach. Zaczynamy z pustą bazą danych i będziemy ją wypełniać danymi w
trakcie pracy aplikacji.
Informacje, które chcemy przechowywać o grze to:
• Tytuł gry (np. Kawerna: Rolnicy z Jaskiń),
• Oryginalny tytuł gry (np. Caverna: Cave Farmers)
• Rok wydania (np. 2013)
• Nazwiska projektantów (np. Uwe Rosenberg) [osobna relacja Projektanci w związku
n<->n z relacją gry]
• Nazwiska artystów (np. Klemens Franz) [osobna relacja Artyści w związku n<->n z
relacją gry]
• Opis (tekst dowolnej długości)
• Data zamówienia
• Data dodania do kolekcji (dla zamówień z Kickstartera te daty mogą być dosyć
odległe)
• Koszt zakupu (dowolny tekst, może być np. €29 = 133 zł)
• SCD (Sugerowana Cena Detaliczna) w momencie zakupu (dowolny tekst)
• Kod EAN/UPC
• Identyfikator w bazie BGG (to może być Long - np. 102794)
• Kod produkcyjny (tekst dowolnej długości, np. LFCABF189, 5265851-001_07/2015)
• Aktualna pozycja w rankingu (Int, np. 31) [uwaga: dodatki do gier nie mają rankingu,
gra bez rankingu może mieć wartość =0]
• Informacja czy gra to gra podstawowa/dodatek/mieszany (czasami gra jest
jednocześnie grą podstawową i dodatkiem do gry, np. pierwsza edycja Dominion
Intryga)
• Komentarz (tekst dowolnej długości)
• Miniaturka ze zdjęciem gry [opcjonalna – może być niedostępna].
Do tego dochodzi relacja, która będzie przechowywać historyczne pozycje w rankingu gier
jako datę pozyskania pozycji i oczywiście pozycję w rankingu (sugeruję, aby ostatnia pozycja
w rankingu była przechowywana w głównej relacji) oraz relacja wiążąca grę z jedną z
lokalizacji do przechowywania (np. szafa w pokoju, regał, strych pudło nr 67, wypożyczona) i
opcjonalnym komentarzem (np. kiedy i komu wypożyczona).
## BGG API
BoardGameGeek jest największym serwisem zawierającym informacje o grach planszowych,
który jest na tyle miły, że udostępnia publiczne API i to nawet bez konieczności rejestracji.
Dokumentacja API dostępna jest tutaj:
https://boardgamegeek.com/wiki/page/BGG_XML_API2. My będziemy korzystać tylko z
ograniczonego zakresu funkcji, np. nie będziemy korzystać z kont użytkownika i
synchronizować danych o kolekcji w obie strony. Należy pamiętać, że dla dużych zapytań (np.
kolekcja użytkownika) serwis odpowiada, że dane zostaną przygotowane później i należy po
chwili zapytać się ponownie.
Ma też pewne ograniczenia dotyczące obciążenia, więc nie należy przesadzać z liczbą
zapytań (testy parsowania danych najlepiej prowadzić na lokalnych kopiach plików).
Funkcje, które będą nam potrzebne opisane są poniżej.
### Wyszukanie gry
Chcemy wyszukać informacje o grze.
https://www.boardgamegeek.com/xmlapi2/search?query=kawerna&type=boardgame
gdzie kawerna to tytuł szukanej gry. W odpowiedzi otrzymamy plik XML:
### Szczegóły gry
Mając identyfikator gry, możemy zapytać się o jej szczegóły:
https://www.boardgamegeek.com/xmlapi2/thing?id=102794&stats=1, gdzie 102794 to
identyfikator gry znaleziony wcześniej.
Odpowiedź jest za duża, żeby ją tu zamieścić, ale z ciekawszych informacji mamy:
• thumbnail – link do małego obrazka,
• image – link do dużego obrazka,
• description – opis po angielsku,
• yearpublished – rok wydania,
• link z wartościami atrybutu „boardgamedesigner” i „boardgameartist” –
nazwiska twórców,
• oraz statystyki w sekcji statistics, pokazane poniżej:
W statystykach nas najbardziej interesuje aktualna pozycja w rankingu gier,
znajdująca się w ranks/rank z typem subtype i name=boardgame, która dla
wspomnianej gry wynosi 31.
### Lista gier użytkownika
Jeżeli chcemy uzyskać listę gier w kolekcji jednego z użytkowników BGG można zadać
pytanie: https://www.boardgamegeek.com/xmlapi2/collection?username=rahdo, gdzie
rahdo to login użytkownika. Otrzymamy w odpowiedzi plik, którego początek wygląda jak
poniżej:
Oczywiście w objectid mamy identyfikatory gier, o które możemy się dalej dopytać.
Do zapytania możemy dopisać jeszcze stats=1
(https://www.boardgamegeek.com/xmlapi2/collection?username=rahdo&stats=1) i wtedy
przy każdej grze będziemy mieć jej ranking w sekcji statistics.
Takie zapytanie, przy założeniu, że nasz użytkownik ma konto na BGG i ma zaznaczone swoje
gry, może nam znacząco ułatwić pobieranie rankingów jego gier.
## Przykładowy interfejs użytkownika (Mockup)
### Główna aktywność
Po uruchomieniu widzimy listę posiadanych gier uszeregowaną alfabetycznie (tylko gry
podstawowe, bez niesamodzielnych dodatków o ile mamy możliwość wiązania dodatków z
grami podstawowymi, jeżeli nie, to wyświetlamy wszystko). Mamy możliwość posortowanie
gier wg daty wydania, pozycji w rankingu. Każdy wpis na liście powinien składać się z
miniaturki, tytułu gry z rokiem wydania, pozycją w rankingu. Mniej więcej, jak poniżej:
Użytkownik ma oczywiście możliwość dodania nowej gry i usunięcia już istniejącej z bazy.
Stuknięcie palcem w jedną z gier powoduje przejście do ekranu szczegółów gry.
### Szczegóły gry
Biorąc pod uwagę ilość informacji ten ekran musi być przewijany (scroll) lub podzielony na
zakładki. Tutaj możemy przejrzeć wszystkie informacje na temat danej gry w bazie danych
oraz zobaczyć listę dostępnych dla niej dodatków. Niektóre z informacji można oczywiście
edytować, gdyby przy dodawaniu zostały popełnione błędy.
Tutaj mamy też możliwość ustawienia, gdzie gra się znajduje – wybór jednej ze
zdefiniowanych lokalizacji i ewentualny komentarz oraz przejścia (np. po stuknięciu w
aktualną pozycję w rankingu) do historii rankingu tej gry.
### Dodawanie gry
Ten ekran powinien polegać na wpisaniu najpierw tytułu gry. Potem wyszukujemy ją w BGG i
dajemy możliwość użytkownikowi potwierdzenia/wyboru znalezionej gry. Jeżeli użytkownik
zatwierdzi to importujemy dane z BGG, w przeciwnym wypadku użytkownik może przejść do
ręcznego wpisywania danych, a id z BGG będzie równe 0. Musimy oczywiście mieć możliwość
edycji wszystkich danych.
### Ekran BGG
Można go też uruchomić z ekranu głównego. Tutaj użytkownik może po wpisaniu nazwy
użytkownika BGG importować kolekcję, a także uruchomić funkcję pobierania aktualnego
rankingu dla posiadanych przez siebie gier (w pierwszej kolejności pobieramy kolekcję gracza
ze statystykami, a potem próbujemy szukać rankingu dla gier, które nie są wpisane po
stronie BGG, ale mają w naszej bazie swój identyfikator BGG.
### Ekran historii rankingu gry
Dla uproszczenia w postaci tabelarycznej informacja w postaci data i pozycja w rankingu,
oczywiście posortowane wg dat malejąco. Można tu przejść ze szczegółów gry.
Dla chętnych można przedstawić historię w postaci wykresu. Proszę pamiętać, że dodatki do
gier nie są rangowane.
### Ekran edycji lokalizacji
Tu możemy dodawać, edytować, usuwać (tylko puste) lokalizacje naszych gier oraz zobaczyć,
jakie gry znajdują się w danej lokalizacji. Można tu przejść z widoku głównego.
