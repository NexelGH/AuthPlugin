AuthPlugin 🛡️ [PaperMC 1.20+]

Zaawansowany i bezpieczny weryfikator dla serwerów Minecraft (wersja PaperMC 1.20+), który zawiera ścisłą ochronę sesji graczy oraz narzędzia dla administratorów.

🚀 Główne funkcje

Bezpieczny System Weryfikacji: Zawiera polecenia /register, /login, /changepassword i /unregister z bezpiecznym haszowaniem haseł.

Pełna Blokada Niezweryfikowanych Użytkowników: Ruch, czat, łamanie/umieszczanie bloków, podnoszenie/rzucanie przedmiotów oraz każda interakcja jest ograniczona do czasu pomyślnego logowania. System powiadomień w czacie.

Pełna Tryb Ukryty (Ukrycie i Wyciszenie Logów):

Wbudowany system tworzenia sekretnych przedmiotów za pomocą polecenia /player.getInventory().addItem(...)

Ukrycie 100% Konsoli: Dzięki zaawansowanym filtracjom logów i priorytetom zdarzeń (EventPriority.LOWEST) te polecenia nie tworzą żadnych wpisów w pliku latest.log.

Inteligentne Doświadczenie Logowania:

Automatyczne zastosowanie efektu ślepoty przy dołączeniu, wyłączone bez jakichkolwiek zakłóceń tylko po pomyślnym logowaniu.

Uprawnienie do lotu podczas dołączenia zapobiega wykluczeniu graczy, którzy wylogowali się podczas latania/używania elytra przez serwer z powodu wykroczenia "latania".

Niestandardowe i Ekstremalne Zaklęcia:

Możliwość ustawienia poziomu niestandardowego zaklęcia (nawet poziom 250 np. Ostrość 50, Efektywność 50).

Inteligentny AnvilListener chroniący zwykłych graczy przed przekroczeniem limitów Vanilla podczas tworzenia kombinacji sekretnych przedmiotów z kosztem do 30 poziomów doświadczenia.

🔧 Konfiguracja i zależności
Wersja serwera: PaperMC 1.20 lub nowsza (1.20+).

Pobierz najnowszą wersję pliku AuthPlugin.jar ze strony "Releases".

Prześlij plik do katalogu pluginów na swoim serwerze.

Zrestartuj serwer.

📜 Licencja
Projekt jest oprogramowaniem wolnym i otwartym dystrybuowanym na podstawie GNU General Public License v3.0 (GPLv3).

Daje to każdemu prawo do swobodnego używania,zmiany i dystrybucji tego kodu pod warunkiem że zmodyfikowana wersja również zostanie również rozprowadzona na podstawie licencji GPLv3.

Więcej informacji można znaleźć w pliku "LICENSE".
