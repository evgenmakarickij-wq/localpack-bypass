# localpack-bypass

<p>
  <a href="../LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Jazyky:** [English](../README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · **Slovenčina** · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Autori:** McChopik, Telemine3140

---

**localpack-bypass** je zadarmo dostupný, open-source, klientský mod [Fabric](https://fabricmc.net) pre [Minecraft](https://www.minecraft.net) 26.2, ktorý robí serverové balíčky zdrojov bezbolestnými. Namiesto opätovného sťahovania toho istého balíčka pri každom pripojení porovná SHA-1 balíčka, ktorý server posiela, so súbormi ZIP, ktoré už ležia v priečinku `resourcepacks`. Ak nájde zodpovedajúci balíček, ticho ho zapne, takže sa pripojíš bez otázok a bez čakania. Ak je balíček nový alebo sa zmenil, jedna obrazovka ponúkne stiahnuť všetky potrebné balíčky naraz; mod ich potom overí, zapne, uloží voľbu a zdroje znovu načíta len raz. Stiahnuté balíčky zostanú po opustení servera aktívne, ako bežné balíčky zdrojov, zatiaľ čo odmietnutie ťa rovnako ako vo vanilkovej hre odpojí od servera. Obrazovky a hlásenia sa riadia jazykom hry; mod prináša preklady pre 38 jazykov (pozri zoznam vyššie).

## Ako to funguje

- **Balíček už stiahnutý:** ticho sa zapne. Žiadna otázka, žiadne sťahovanie.
- **Nový alebo zmenený balíček:** obrazovka s **Stiahnuť všetky balíčky zdrojov**. Balíčky sa ukladajú do `resourcepacks/localpack-<sha1>.zip`, overia sa pomocou SHA-1, zapnú sa a uložia do tvojich nastavení.
- **Odmietnuť a odísť:** budeš odpojený od servera.
- **Staršie balíčky:** skôr modom stiahnuté balíčky sa odznačia, keď dorazí novšia verzia. Súbory zostávajú na disku, takže návrat je okamžitý.

## Aktualizácie

Raz denne sa mod opýta GitHubu na [najnovšiu verziu](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) tohto repozitára. Ak je novšia než verzia, ktorú používaš, úvodná obrazovka ponúkne jej inštaláciu. Súhlas urobí tri veci:

1. Stiahne sa nový súbor JAR a **overí sa jeho SHA-256**, a to proti manifestu `update.json`, ktorý sa s touto verziou zverejňuje.
2. Starý súbor JAR sa vymaže a nový sa presunie do `mods/`.
3. Reštartuješ hru.

Vo Windows sa nedá vymazať súbor JAR, ktorý je práve načítaný, preto krátky vygenerovaný skript počká na ukončenie hry a vykoná za teba krok 2; mod ti to oznámi, keď sa to stane. Na Androide, Linuxe a macOS nie je bežiaci súbor JAR uzamknutý a jednoducho sa nahradí. Všetko, čoho sa aktualizátor dotkne, leží v `mods/` a `config/localpack-bypass/`.

Dopyt je obyčajná anonymná požiadavka na API GitHubu (žiadny token, žiadny účet), mod nemá žiadnu telemetriu ani vlastný server. Preskočená verzia sa zapamätá a `-Dlocalpack.bypass.updateCheck=false` aktualizácie úplne vypne.

## Požiadavky

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 alebo novší
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux alebo androidový launcher ako [PojavLauncher](https://pojavlauncherteam.github.io/): každý súbor, ktorého sa mod dotkne, sa zistí z adresára hry launcheru, takže netreba ručne nastavovať žiadny priečinok
- Voliteľné: [Mod Menu](https://modrinth.com/mod/modmenu), ktoré zobrazuje názov a popis modu v tvojom jazyku

## Zostavenie vlastnými silami

Mod sa zostavuje pri každom pushi do `main` pomocou [workflow GitHub Actions](../.github/workflows/build.yml), súbor JAR leží medzi artefaktami behu. Značka ako `v0.0.3` navyše zverejní [verziu](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) so súborom JAR a manifestom `update.json`, ktorý aktualizátor v hre číta.

## Odkiaľ pochádza kód

- **Pôvod:** projekt vznikol z pôvodného prototypu tohto repozitára, ktorý porovnával SHA-1 serverového balíčka s miestnymi súbormi.
- **Autori:** McChopik, Telemine3140. Súčasný kód bol znovu napísaný a rozšírený pomocou [Claude Code](https://claude.com/claude-code), nástroja umelej inteligencie na programovanie od [Anthropic](https://www.anthropic.com), na želanie autorov projektu. Pribudlo sťahovanie jedným kliknutím, dávkovanie, automatické zapínanie, „odmietnuť a odísť“, aktualizátor, preklady, práca s cestami pre Windows, macOS, Linux a Android a dokumentácia. Kód z iných modov nebol skopírovaný.
- **Postavené na:** vlastnom kóde Minecraftu s oficiálnymi názvami od Mojang, [Fabric Loader a Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) a [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Stav:** experimentálny. Nie je dôkladne otestovaný, používaj ho teda na vlastné riziko.
- **Nie je spojený** s Mojang, Microsoft, Fabric ani Anthropic.

## Licencia

Licenčný text nižšie je tu uvedený v pôvodnom anglickom znení; právne záväzné je iba toto anglické znenie, preklad licencie MIT by ho nenahradil.

This is an **open-source** project released under the **[MIT License](../LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Príspevky, hlásenia chýb a pull requesty sú vítané: [otvor issue](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
