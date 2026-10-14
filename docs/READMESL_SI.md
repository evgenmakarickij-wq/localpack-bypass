# localpack-bypass

<p>
  <a href="../LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Jeziki:** [English](../README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · **Slovenščina** · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Avtorji:** McChopik, Telemine3140

---

**localpack-bypass** je brezplačen odprtokoden odjemalski [Fabric](https://fabricmc.net) mod za [Minecraft](https://www.minecraft.net) 26.2, ki pakete virov strežnika naredi neboleče. Namesto da bi ob vsaki pridružitvi znova prenesel isti paket, primerja SHA-1 paketa, ki ga pošlje strežnik, z datotekami ZIP, ki so že v mapi `resourcepacks`. Če najde ujemajoči se paket, ga tiho omogoči, tako da se pridružiš brez vprašanj in brez čakanja. Če je paket nov ali spremenjen, en zaslon ponudi prenos vseh potrebnih paketov naenkrat; mod jih nato preveri, omogoči, shrani izbor in znova naloži vire samo enkrat. Preneseni paketi ostanejo omogočeni po zapustitvi strežnika, kot običajni paketi virov, medtem ko te zavrnitev odklopi od strežnika kot v vanilla igri. Zasloni in sporočila sledijo jeziku igre; mod prinaša prevode za 38 jezikov (glej seznam zgoraj).

## Kako deluje

- **Paket je že prenesen:** tiho se omogoči. Brez vprašanj, brez prenosa.
- **Nov ali spremenjen paket:** zaslon s **Prenesi vse pakete virov**. Paketi se shranijo v `resourcepacks/localpack-<sha1>.zip`, preverijo s SHA-1, omogočijo in shranijo v tvoje nastavitve.
- **Zavrni in izstopi:** odklopjen si od strežnika.
- **Starejši paketi:** paketi, ki jih je mod prej prenesel, se odznačijo, ko prispe novejša različica. Datoteke ostanejo na disku, zato je vrnitev takojšnja.

## Posodobitve

Enkrat dnevno mod vpraša GitHub za [najnovejšo različico](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) tega repozitorija. Če je novejša od različice, ki jo uporabljaš, naslovni zaslon ponudi njeno namestitev. Potrditev stori tri stvari:

1. Nova datoteka JAR se prenese in njen **SHA-256 se preveri** glede na manifest `update.json`, objavljen ob tej različici.
2. Stara datoteka JAR se izbriše, nova pa premakne v `mods/`.
3. Igro znova zaženeš.

V sistemu Windows datoteke JAR, ki je pravkar naložena, ni mogoče izbrisati, zato kratka ustvarjena skripta počaka na zaprtje igre in korak 2 opravi namesto tebe; mod ti to sporoči, ko se zgodi. V sistemih Android, Linux in macOS tekoča datoteka JAR ni zaklenjena in se preprosto zamenja. Vse, česar se posodabljalnik dotakne, je v `mods/` in `config/localpack-bypass/`.

Poizvedba je običajna anonimna zahteva API-ju GitHub (brez žetona, brez računa), mod nima telemetrije in lastnega strežnika. Preskočeno različico si mod zapomni, `-Dlocalpack.bypass.updateCheck=false` pa posodobitve popolnoma izklopi.

## Zahteve

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 ali novejši
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux ali zaganjalnik za Android, kot je [PojavLauncher](https://pojavlauncherteam.github.io/): vsaka datoteka, ki se je mod dotakne, se določi iz igrine mape zaganjalnika, zato ni treba ročno nastavljati mape
- Neobvezno: [Mod Menu](https://modrinth.com/mod/modmenu), ki prikaže ime in opis moda v tvojem jeziku

## Gradnja sam

Mod se ob vsakem potisku na `main` zgradi z [delovnim tokom GitHub Actions](../.github/workflows/build.yml), datoteka JAR pa je med artefakti tega zagona. Oznaka, kot je `v0.0.3`, dodatno objavi [različico](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) z datoteko JAR in manifestom `update.json`, ki ga posodabljalnik v igri bere.

## Od kod izvira koda

- **Izvor:** projekt je nastal iz prvotnega prototipa tega repozitorija, ki je primerjal SHA-1 paketa strežnika z lokalnimi datotekami.
- **Avtorji:** McChopik, Telemine3140. Današnjo kodo so na željo avtorjev projekta znova napisali in razširili z [Claude Code](https://claude.com/claude-code), orodjem za programiranje z umetno inteligenco podjetja [Anthropic](https://www.anthropic.com). Dodani so bili prenos z enim klikom, združevanje, samodejno omogočanje, »zavrni in izstopi«, posodabljalnik, prevodi, obravnava poti za Windows, macOS, Linux in Android ter dokumentacija. Koda iz drugih modov ni bila kopirana.
- **Zgrajeno na:** Minecraftovi lastni kodi z Mojangovimi uradnimi imeni, [Fabric Loader in Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) in [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Stanje:** eksperimentalno. Ni bilo obsežno preizkušeno, zato ga uporabljaj na lastno odgovornost.
- **Ni povezano** z Mojang, Microsoft, Fabric ali Anthropic.

## Licenca

Besedilo licence v nadaljevanju je tu navedeno v izvirnem angleškem jeziku; pravno zavezujoče je le to angleško besedilo, prevod licence MIT pa ga ne bi nadomestil.

This is an **open-source** project released under the **[MIT License](../LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Prispevki, prijave napak in pull requesti so dobrodošli: [odpri issue](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
