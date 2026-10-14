# localpack-bypass

<p>
  <a href="LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Talen:** [English](README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · **Nederlands** · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Auteurs:** McChopik, Telemine3140

---

**localpack-bypass** is een gratis, opensource [Fabric](https://fabricmc.net)-mod voor de client voor [Minecraft](https://www.minecraft.net) 26.2 die bronpakketten van servers moeiteloos maakt. In plaats van bij elke deelname hetzelfde pakket opnieuw te downloaden, vergelijkt het de SHA-1 van het pakket dat de server stuurt met de ZIP-bestanden die al in de map `resourcepacks` staan. Als een overeenkomend pakket wordt gevonden, wordt het stil geactiveerd, zodat je zonder vragen en zonder wachten deelneemt. Is een pakket nieuw of veranderd, dan biedt één scherm aan om alle benodigde pakketten in één keer te downloaden; daarna controleert de mod ze, activeert ze, slaat de keuze op en herlaadt de bronnen maar één keer. Gedownloade pakketten blijven actief nadat je de server verlaat, net als gewone bronpakketten, terwijl weigeren je net als in het oorspronkelijke spel van de server loskoppelt. Schermen en meldingen volgen de taal van het spel; de mod levert vertalingen voor 38 talen (zie de lijst met talen hierboven).

## Hoe het werkt

- **Pakket al gedownload:** het wordt stil geactiveerd. Geen vraag, geen download.
- **Nieuw of gewijzigd pakket:** één scherm met **Alle bronpakketten downloaden**. Pakketten worden opgeslagen als `resourcepacks/localpack-<sha1>.zip`, met SHA-1 gecontroleerd, geactiveerd en in je instellingen opgeslagen.
- **Weigeren en verlaten:** je wordt van de server losgekoppeld.
- **Oudere pakketten:** eerder door de mod gedownloade pakketten worden uitgeschakeld wanneer een nieuwere versie binnenkomt. De bestanden blijven op de schijf staan, dus terugschakelen is direct mogelijk.

## Updates

Eén keer per dag vraagt de mod aan GitHub naar de [nieuwste versie](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) van deze repository. Is die nieuwer dan de versie die je gebruikt, dan biedt het titelscherm aan om die te installeren. Als je ja zegt, gebeuren er drie dingen:

1. Het nieuwe JAR-bestand wordt gedownload en **de SHA-256 ervan wordt gecontroleerd**, tegen het `update.json`-manifest dat met deze versie wordt gepubliceerd.
2. Het oude JAR-bestand wordt verwijderd en het nieuwe wordt naar `mods/` verplaatst.
3. Je start het spel opnieuw op.

Onder Windows kan een JAR-bestand dat op dat moment geladen is, niet worden verwijderd, dus een kort gegenereerd script wacht tot het spel is afgesloten en doet stap 2 voor je; de mod laat het je weten wanneer dat gebeurt. Onder Android, Linux en macOS is het draaiende JAR-bestand niet vergrendeld en wordt het gewoon vervangen. Alles wat de updater aanraakt, bevindt zich in `mods/` en `config/localpack-bypass/`.

De vraag is een gewoon anoniem verzoek aan de GitHub-API (geen token, geen account); de mod heeft geen telemetrie en geen eigen server. Een overgeslagen versie wordt onthouden, en `-Dlocalpack.bypass.updateCheck=false` schakelt updates volledig uit.

## Vereisten

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 of nieuwer
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux of een Android-launcher zoals [PojavLauncher](https://pojavlauncherteam.github.io/): elk bestand dat de mod aanraakt, wordt bepaald uit de spelmap van de launcher, dus er hoeft geen map handmatig te worden ingesteld
- Optioneel: [Mod Menu](https://modrinth.com/mod/modmenu), dat de modnaam en -beschrijving in je taal toont

## Zelf bouwen

De mod wordt bij elke push naar `main` gebouwd door de [GitHub Actions-workflow](.github/workflows/build.yml); het JAR-bestand staat bij de artefacten van de uitvoering. Een tag zoals `v0.0.3` publiceert bovendien een [versie](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) met het JAR-bestand en het `update.json`-manifest dat de updater in het spel leest.

## Waar de code vandaan komt

- **Oorsprong:** het project is ontstaan uit het oorspronkelijke prototype van deze repository, dat de SHA-1 van het serverpakket met lokale bestanden vergeleek.
- **Auteurs:** McChopik, Telemine3140. De huidige code is herschreven en uitgebreid met [Claude Code](https://claude.com/claude-code), een AI-programmeerhulpmiddel van [Anthropic](https://www.anthropic.com), op verzoek van de projectauteurs. Toegevoegd zijn het downloaden met één klik, het bundelen, het automatisch activeren, „weigeren en verlaten“, de updater, de vertalingen, de padbehandeling voor Windows, macOS, Linux en Android, en de documentatie. Er is geen code uit andere mods gekopieerd.
- **Gebouwd op:** de eigen code van Minecraft met de officiële namen van Mojang, [Fabric Loader en Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) en [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Status:** experimenteel. Het is niet uitgebreid getest, dus gebruik het op eigen risico.
- **Niet verbonden** met Mojang, Microsoft, Fabric of Anthropic.

## Licentie

De licentietekst hieronder is hier weergegeven in het oorspronkelijke Engels; alleen deze Engelse formulering is juridisch bindend, een vertaling van de MIT-licentie zou die niet vervangen.

This is an **open-source** project released under the **[MIT License](LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Bijdragen, foutmeldingen en pull requests zijn welkom: [een issue openen](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
