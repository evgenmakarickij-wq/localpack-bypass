# localpack-bypass

<p>
  <a href="../LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Sprog:** [English](../README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · **Dansk** · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Forfattere:** McChopik, Telemine3140

---

**localpack-bypass** er et gratis, open source, klientsidemod til [Fabric](https://fabricmc.net) til [Minecraft](https://www.minecraft.net) 26.2, som gør serverressourcepakker smertefrie. I stedet for at downloade den samme pakke igen, hver gang du forbinder til serveren, sammenligner det SHA-1-summen af den pakke, serveren sender, med de ZIP-filer, der allerede ligger i mappen `resourcepacks`. Findes der en matchende pakke, aktiveres den lydløst, så du forbinder uden forespørgsler og uden ventetid. Er en pakke ny eller ændret, tilbyder én skærm at downloade alle nødvendige pakker på én gang; derefter verificerer moddet dem, aktiverer dem, gemmer valget og genindlæser kun ressourcerne én gang. Downloadede pakker forbliver aktive, efter du forlader serveren, ligesom almindelige ressourcepakker, mens et afslag afbryder dig fra serveren som i vanilla. Skærme og beskeder følger spilsproget; moddet leverer oversættelser til 38 sprog (se listen ovenfor).

## Sådan fungerer det

- **Pakken er allerede downloadet:** den aktiveres lydløst. Ingen forespørgsel, ingen download.
- **Ny eller ændret pakke:** en skærm med **Download alle ressourcepakker**. Pakkerne gemmes under `resourcepacks/localpack-<sha1>.zip`, verificeres med SHA-1, aktiveres og gemmes i dine indstillinger.
- **Afvis og forlad:** du afbrydes fra serveren.
- **Ældre pakker:** pakker, som moddet tidligere har downloadet, fravælges, når en nyere version ankommer. Filerne bliver på disken, så du kan skifte tilbage med det samme.

## Opdateringer

En gang om dagen spørger moddet GitHub om den [nyeste version](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) af dette repository. Er den nyere end den version, du bruger, tilbyder hovedmenuen at installere den. Et ja gør tre ting:

1. Den nye JAR-fil downloades, og dens **SHA-256 verificeres** mod det `update.json`-manifest, der udgives sammen med den version.
2. Den gamle JAR-fil slettes, og den nye flyttes til `mods/`.
3. Du genstarter spillet.

Under Windows kan en JAR-fil, der er i brug, ikke slettes, så et lille genereret script venter på, at spillet lukker, og udfører trin 2 for dig; moddet fortæller dig, når det sker. Under Android, Linux og macOS er den kørende JAR-fil ikke låst og bliver blot erstattet. Alt, hvad opdateringen rører ved, ligger under `mods/` og `config/localpack-bypass/`.

Forespørgslen er en almindelig anonym anmodning til GitHub-API'en (ingen token, ingen konto), og moddet har ingen telemetri og ingen egen server. En sprunget version huskes, og `-Dlocalpack.bypass.updateCheck=false` slår opdateringer helt fra.

## Krav

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 eller nyere
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux eller en Android-launcher som [PojavLauncher](https://pojavlauncherteam.github.io/): hver fil, som moddet rører ved, findes ud fra launcherens spilmappe, så ingen mappe skal opsættes manuelt
- Valgfrit: [Mod Menu](https://modrinth.com/mod/modmenu), som viser moddets navn og beskrivelse på dit sprog

## Byg det selv

Moddet bygges ved hvert push til `main` af [GitHub Actions-workflowen](../.github/workflows/build.yml), og JAR-filen findes under kørslens artefakter. Et tag som `v0.0.3` udgiver desuden en [version](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) med JAR-filen og det `update.json`-manifest, som opdateringen i spillet læser.

## Hvor koden kommer fra

- **Oprindelse:** projektet opstod ud fra dette repositorys oprindelige prototype, som sammenlignede serverpakkens SHA-1 med lokale filer.
- **Forfattere:** McChopik, Telemine3140. Den nuværende kode blev omskrevet og udvidet efter projektforfatternes ønske med [Claude Code](https://claude.com/claude-code), et AI-programmeringsværktøj fra [Anthropic](https://www.anthropic.com). Der er tilføjet download med ét klik, samling, automatisk aktivering, »afvis og forlad«, opdateringen, oversættelserne, stihåndtering til Windows, macOS, Linux og Android samt dokumentationen. Kode fra andre mods er ikke blevet kopieret.
- **Bygget på:** Minecrafts egen kode med Mojangs officielle navne, [Fabric Loader og Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) og [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Status:** eksperimentel. Det er ikke blevet testet grundigt, så brug det på egen risiko.
- **Ikke forbundet** med Mojang, Microsoft, Fabric eller Anthropic.

## Licens

Licensteksten nedenfor gengives her på sit oprindelige engelsk; kun den engelske ordlyd er juridisk bindende, og en oversættelse af MIT-licensen ville ikke erstatte den.

This is an **open-source** project released under the **[MIT License](../LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Bidrag, fejlrapporter og pull requests er velkomne: [opret et issue](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
