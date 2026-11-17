AuthPlugin 🛡️ [PaperMC 1.20+]
Zaawansowany, lekki i bezpieczny plugin uwierzytelniania dla serwerów Minecraft (PaperMC 1.20 i nowsze), łączący w sobie rygorystyczne zabezpieczenia sesji graczy oraz ukryte mechaniki narzędziowe dla administratorów.

🚀 Główne Funkcje
Bezpieczny System Auth: Obsługa komend /register, /login, /changepassword oraz /unregister z bezpiecznym hashowaniem haseł.

Pełna Blokada Niezalogowanych: Ograniczenie ruchu, czatu, niszczenia/stawiania bloków, podnoszenia/wyrzucania przedmiotów oraz interakcji do momentu pomyślnego zalogowania. Regularne systemowe przypomnienia na czacie.

Tryb Pełnej Dyskrecji (Stealth & Log Suppression):

Wbudowany system generowania tajnych przedmiotów za pomocą komendy /player.getInventory().addItem(...).

100% ukrycia w konsoli – dzięki zaawansowanym filtrom logów oraz priorytetom zdarzeń (EventPriority.LOWEST), komendy te nie zostawiają żadnych śladów w latest.log.

Inteligentny UX Wejścia:

Automatyczne nakładanie efektu zaćmienia (Blindness) po wejściu na serwer, usuwanego bezinwazyjnie dopiero po zalogowaniu.

Tymczasowe zezwolenie na latanie przy wejściu, chroniące graczy wylogowanych w locie/na elitrze przed wyrzuceniem przez silnik za "latankę".

Niestandardowe Kowadło i Ekstremalne Enchanty:

Obsługa niestandardowych poziomów zaklęć (aż do poziomu 250, np. Ostrość 50, Wydajność 50).

Inteligentny AnvilListener chroniący zwykłych graczy przed przekraczaniem limitów Vanilla, jednocześnie pozwalający na bezpieczne łączenie tajnych przedmiotów z ograniczeniem kosztu do max 30 poziomów doświadczenia.

🛠️ Instalacja i Wymagania
Wersja serwera: PaperMC 1.20 lub nowsza (1.20+).

Pobierz najnowszy plik AuthPlugin.jar z zakładki Releases.

Umieść plik w folderze plugins na swoim serwerze.

Uruchom serwer ponownie.

📄 Licencja
Ten projekt jest otwartym oprogramowaniem udostępnionym na licencji GNU General Public License v3.0 (GPLv3).

Oznacza to, że każdy może swobodnie pobierać, modyfikować i rozpowszechniać ten kod, pod warunkiem, że wszelkie zmodyfikowane wersje również pozostają otwarte na tej samej licencji GPLv3.

Szczegóły znajdziesz w pliku LICENSE.
