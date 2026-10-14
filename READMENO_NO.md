# localpack-bypass

<p>
  <a href="LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Språk:** [English](README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · **Norsk** · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Forfattere:** McChopik, Telemine3140

---

**localpack-bypass** er en gratis, åpen kildekode-mod på klientsiden for [Fabric](https://fabricmc.net) til [Minecraft](https://www.minecraft.net) 26.2 som gjør serverens ressurspakker smertefrie. I stedet for å laste ned den samme pakken på nytt hver gang du kobler til, sammenligner den SHA-1-summen til pakken serveren sender med ZIP-filene som allerede ligger i mappen `resourcepacks`. Hvis en passende pakke blir funnet, aktiveres den stille, slik at du kobler til uten spørsmål og uten å vente. Er en pakke ny eller endret, tilbyr én skjerm å laste ned alle nødvendige pakker på én gang; deretter verifiserer modden dem, aktiverer dem, lagrer valget og laster inn ressursene på nytt bare én gang. Nedlastede pakker forblir aktive etter at du forlater serveren, som vanlige ressurspakker, mens et avslag kobler deg fra serveren som i vanilla. Skjermer og meldinger følger spillspråket; modden leverer oversettelser for 38 språk (se listen ovenfor).

## Slik fungerer det

- **Pakken er allerede lastet ned:** den aktiveres stille. Ingen forespørsel, ingen nedlasting.
- **Ny eller endret pakke:** en skjerm med **Last ned alle ressurspakker**. Pakkene lagres under `resourcepacks/localpack-<sha1>.zip`, verifiseres med SHA-1, aktiveres og lagres i innstillingene dine.
- **Avslå og forlat:** du kobles fra serveren.
- **Eldre pakker:** pakker modden tidligere har lastet ned, velges bort når en nyere versjon kommer. Filene blir liggende på disken, så du kan bytte tilbake med en gang.

## Oppdateringer

Én gang om dagen spør modden GitHub om den [nyeste versjonen](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) av dette arkivet. Hvis den er nyere enn versjonen du bruker, tilbyr hovedmenyen å installere den. Et ja gjør tre ting:

1. Den nye JAR-filen lastes ned, og **SHA-256-en verifiseres** mot `update.json`-manifestet som publiseres med den versjonen.
2. Den gamle JAR-filen slettes, og den nye flyttes til `mods/`.
3. Du starter spillet på nytt.

I Windows kan en JAR-fil som er lastet, ikke slettes, så et lite generert skript venter på at spillet avsluttes og utfører trinn 2 for deg; modden sier fra når det skjer. I Android, Linux og macOS er den kjørende JAR-filen ikke låst og blir bare erstattet. Alt oppdatereren berører, ligger under `mods/` og `config/localpack-bypass/`.

Forespørselen er en vanlig anonym forespørsel til GitHub-API-et (ingen token, ingen konto), og modden har ingen telemetri og ingen egen server. En hoppet-over versjon huskes, og `-Dlocalpack.bypass.updateCheck=false` slår av oppdateringer helt.

## Krav

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 eller nyere
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux eller en Android-launcher som [PojavLauncher](https://pojavlauncherteam.github.io/): hver fil modden berører, bestemmes ut fra launcherens spillmappe, så ingen mappe må settes opp manuelt
- Valgfritt: [Mod Menu](https://modrinth.com/mod/modmenu), som viser modens navn og beskrivelse på språket ditt

## Bygg det selv

Modden bygges ved hver push til `main` av [GitHub Actions-arbeidsflyten](.github/workflows/build.yml), og JAR-filen ligger blant kjøringens artefakter. En tagg som `v0.0.3` publiserer i tillegg en [versjon](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) med JAR-filen og det `update.json`-manifestet som oppdatereren i spillet leser.

## Hvor koden kommer fra

- **Opphav:** prosjektet vokste ut av dette arkivets opprinnelige prototyp, som sammenlignet serverpakkens SHA-1 med lokale filer.
- **Forfattere:** McChopik, Telemine3140. Dagens kode ble skrevet om og utvidet på prosjektforfatternes ønske med [Claude Code](https://claude.com/claude-code), et AI-programmeringsverktøy fra [Anthropic](https://www.anthropic.com). Lagt til er nedlasting med ett klikk, samling, automatisk aktivering, «avslå og forlat», oppdatereren, oversettelsene, stibehandling for Windows, macOS, Linux og Android samt dokumentasjonen. Kode fra andre modder er ikke kopiert.
- **Bygget på:** Minecrafts egen kode med Mojangs offisielle navn, [Fabric Loader og Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) og [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Status:** eksperimentell. Den er ikke testet grundig, så bruk den på egen risiko.
- **Ikke tilknyttet** Mojang, Microsoft, Fabric eller Anthropic.

## Lisens

Lisensteksten nedenfor gjengis her på sitt opprinnelige engelsk; bare den engelske ordlyden er juridisk bindende, og en oversettelse av MIT-lisensen ville ikke erstatte den.

This is an **open-source** project released under the **[MIT License](LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Bidrag, feilrapporter og pull requests er velkomne: [åpne en sak](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
