# localpack-bypass

<p>
  <a href="../LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Języki:** [English](../README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · **Polski** · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Autorzy:** McChopik, Telemine3140

---

**localpack-bypass** to darmowy, otwartoźródłowy mod [Fabric](https://fabricmc.net) działający po stronie klienta dla [Minecraft](https://www.minecraft.net) 26.2, który sprawia, że paczki zasobów serwera przestają być problemem. Zamiast za każdym razem pobierać ten sam pakiet od nowa, porównuje SHA-1 paczki wysyłanej przez serwer z plikami ZIP znajdującymi się już w twoim folderze `resourcepacks`. Jeśli pasująca paczka zostanie znaleziona, jest po cichu włączana, dzięki czemu dołączasz bez pytań i czekania. Jeśli paczka jest nowa lub się zmieniła, jeden ekran proponuje pobranie wszystkich potrzebnych paczek naraz; mod następnie weryfikuje je, włącza, zapisuje wybór i przeładowuje zasoby tylko raz. Pobrane paczki pozostają aktywne po opuszczeniu serwera, jak zwykłe paczki zasobów, natomiast odmowa rozłącza cię z serwerem, tak jak w podstawowej wersji gry. Ekrany i komunikaty są zgodne z językiem gry; mod zawiera tłumaczenia na 38 języków (zobacz listę powyżej).

## Jak to działa

- **Paczka już pobrana:** jest po cichu włączana. Bez pytań, bez pobierania.
- **Nowa lub zmieniona paczka:** jeden ekran z **Pobierz wszystkie paczki zasobów**. Paczki są zapisywane w `resourcepacks/localpack-<sha1>.zip`, weryfikowane przez SHA-1, włączane i zapisywane w twoich opcjach.
- **Odmowa i wyjście:** zostajesz rozłączony z serwerem.
- **Starsze paczki:** paczki pobrane wcześniej przez mod są odznaczane, gdy pojawi się nowsza wersja. Pliki pozostają na dysku, więc powrót jest natychmiastowy.
- **Nic nie zawodzi po cichu:** nieudane pobranie, niezgodna suma kontrolna lub plik, którego gra nie chciała włączyć, są zgłaszane na ekranie lub jako powiadomienie, jeśli żaden ekran nie jest dostępny.

## Ustawienia (Mod Menu)

Po zainstalowaniu [Mod Menu](https://modrinth.com/mod/modmenu) mod pojawia się na liście modów z własnym ekranem ustawień. Nie dodano żadnych skrótów klawiszowych; wszystko jest na miejscu.

- **Folder na pobrane paczki** — gdzie przechowywane są paczki. Zostaw go pustym, aby użyć własnego folderu gry `resourcepacks`. Każdy inny folder zadziała, w tym inny dysk lub udostępniony folder Androida; paczki są wtedy dodatkowo kopiowane do `resourcepacks` przed włączeniem, ponieważ to jedyny folder, który odczytuje sama gra. Przyciski otwierają każdy z folderów.
- **Pamiętaj serwery, którym powiedziałeś tak** — serwer, z którego paczki już pobrano, nie jest pytany ponownie. Odpowiedź jest też zapisywana w własnym ustawieniu gry dla danego serwera *Server Resource Packs*, obok Prompt, Enabled i Disabled.
- **Zachowaj paczki z innych serwerów** — zostaw to włączone, aby zachować paczki, które już masz. Wyłącz, aby rozpoczynać każdy serwer tylko z paczkami danego serwera.
- **Odczekaj przed pytaniem** — jak długo zbierać resztę partii, zanim pojawi się ekran, w milisekundach lub sekundach. Serwery wysyłają wszystkie swoje paczki naraz, więc oczekiwanie zamienia kilka pytań w jedno.
- **Poddaj się po** i **Próby** — jak długo może trwać jedno pobieranie i ile razy nieudane jest ponawiane. Niezgodna suma kontrolna liczy się jako niepowodzenie, bo tak wygląda niedokończony transfer.
- **Prowadź zapis** — każda paczka zasobów wysłana przez serwer jest zapisywana: który serwer, który adres, suma kontrolna, rozmiar, co się z nią stało i ile to zajęło. To plik na twoim własnym komputerze; nic nie jest nigdzie wysyłane.
- **Pobrane paczki** — paczki pobrane przez mod, pogrupowane według serwera, z którego pochodzą, pokazujące, czy każda jest włączona, wyłączona czy zniknęła z folderu. Naciśnij jedną, aby ją włączyć lub wyłączyć; paczkę, której plik zniknął, można zapomnieć. **Zapomnij wszystko** czyści to, co mod pamięta, i nigdy nie usuwa pliku paczki.
- **Co wysłały serwery** — sam zapis, od najnowszego, ze szczegółami każdego wpisu.

## Aktualizacje

Raz dziennie mod pyta GitHub o [najnowszą wersję](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) tego repozytorium. Jeśli jest nowsza niż wersja, której używasz, ekran tytułowy proponuje jej instalację. Zaakceptowanie robi trzy rzeczy:

1. Nowy plik JAR jest pobierany, a jego **SHA-256 jest sprawdzany** względem manifestu `update.json` publikowanego z tą wersją.
2. Stary plik JAR jest usuwany, a nowy przenoszony do `mods/`.
3. Ponownie uruchamiasz grę.

W systemie Windows nie można usunąć pliku JAR, który jest aktualnie załadowany, dlatego krótki wygenerowany skrypt czeka na zamknięcie gry i wykonuje za ciebie krok 2; mod informuje, gdy tak się dzieje. W systemach Android, Linux i macOS działający plik JAR nie jest zablokowany i jest po prostu zastępowany. Wszystko, czego dotyka aktualizator, znajduje się w `mods/` i `config/localpack-bypass/`.

Sprawdzenie to zwykłe anonimowe zapytanie do API GitHuba (bez tokenu, bez konta), mod nie ma telemetrii ani własnego serwera. Pominięta wersja jest zapamiętywana, a `-Dlocalpack.bypass.updateCheck=false` całkowicie wyłącza aktualizacje.

**Zastrzeżenie dotyczące numeracji wersji:** `0.0.1.2026-alpha-build-beta-release` jest odczytywane jako `[0, 0, 1, 2026]`, co sortuje się *poniżej* wcześniejszej `0.0.3-beta` (`[0, 0, 3]`). Kompilacje o numerze `0.0.3-beta` lub wyższym nie otrzymają tej automatycznie i trzeba je zastąpić ręcznie.

## Wymagania

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 lub nowszy
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux lub launcher na Androida taki jak [PojavLauncher](https://pojavlauncherteam.github.io/): każdy plik, którego dotyka mod, jest ustalany na podstawie katalogu gry launchera, więc żadnego folderu nie trzeba ustawiać ręcznie
- Opcjonalnie: [Mod Menu](https://modrinth.com/mod/modmenu), które pokazuje nazwę i opis moda w twoim języku oraz daje modowi ekran ustawień

## Zbuduj samodzielnie

Mod jest budowany przy każdym pushu do gałęzi `main` przez [workflow GitHub Actions](../.github/workflows/build.yml), a plik JAR jest dostępny w artefaktach danego uruchomienia. Tag taki jak `v0.0.3` publikuje dodatkowo [wydanie](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) z plikiem JAR i manifestem `update.json`, który odczytuje aktualizator w grze.

## Skąd pochodzi kod

- **Pochodzenie:** projekt powstał z oryginalnego prototypu tego repozytorium, który porównywał SHA-1 paczki serwera z plikami lokalnymi.
- **Autorzy:** McChopik, Telemine3140. Obecny kod został przepisany i rozszerzony za pomocą [Claude Code](https://claude.com/claude-code), narzędzia do programowania z AI od [Anthropic](https://www.anthropic.com), na prośbę autorów projektu. Dodano pobieranie jednym kliknięciem, grupowanie, automatyczne włączanie, «Odmów i wyjdź», aktualizator, tłumaczenia, obsługę ścieżek dla Windows, macOS, Linux i Androida oraz dokumentację. Nie skopiowano kodu z innych modów.
- **Zbudowany na:** własnym kodzie Minecrafta z oficjalnymi nazwami Mojanga, [Fabric Loader i Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) i [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Status:** eksperymentalny. Nie został dokładnie przetestowany, więc używaj go na własne ryzyko.
- **Niepowiązany** z Mojang, Microsoft, Fabric ani Anthropic.

## Licencja

Tekst licencji poniżej jest przytoczony w oryginalnym języku angielskim; prawnie wiążące jest wyłącznie to angielskie brzmienie, a tłumaczenie licencji MIT nie zastąpiłoby go.

This is an **open-source** project released under the **[MIT License](../LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Wkład, zgłoszenia błędów i pull requesty są mile widziane: [otwórz zgłoszenie](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
