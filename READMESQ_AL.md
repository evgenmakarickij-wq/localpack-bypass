# localpack-bypass

<p>
  <a href="LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Gjuhët:** [English](README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · **Shqip** · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Autorët:** McChopik, Telemine3140

---

**localpack-bypass** është një mod falas, me kod të hapur, nga ana e klientit për [Fabric](https://fabricmc.net) dhe [Minecraft](https://www.minecraft.net) 26.2, që i bën paketat e burimeve të serverit pa dhimbje. Në vend që të shkarkojë sërish të njëjtën paketë sa herë që bashkohesh, ai krahason SHA-1 e paketës që dërgon serveri me skedarët ZIP që ndodhen tashmë në dosjen `resourcepacks`. Nëse gjen një paketë që përputhet, aktivizohet në heshtje, kështu që bashkohesh pa kërkesa dhe pa pritje. Nëse paketa është e re ose ka ndryshuar, një ekran i vetëm ofron të shkarkosh të gjitha paketat e nevojshme menjëherë; më pas modi i verifikon, i aktivizon, ruan zgjedhjen dhe i ringarkon burimet vetëm një herë. Paketat e shkarkuara mbeten aktive pas daljes nga serveri, si paketat e zakonshme të burimeve, ndërsa refuzimi, si në lojën e zakonshme, të shkëput nga serveri. Ekranet dhe mesazhet ndjekin gjuhën e lojës; modi sjell përkthime për 38 gjuhë (shih listën më sipër).

## Si funksionon

- **Paketa është shkarkuar tashmë:** aktivizohet në heshtje. Pa kërkesa, pa shkarkim.
- **Paketë e re ose e ndryshuar:** një ekran me **Shkarko të gjitha paketat e burimeve**. Paketat ruhen në `resourcepacks/localpack-<sha1>.zip`, verifikohen me SHA-1, aktivizohen dhe ruhen në cilësimet e tua.
- **Refuzo dhe dil:** do të shkëputesh nga serveri.
- **Paketat e vjetra:** paketat e shkarkuara më parë nga modi çaktivizohen kur mbërrin një version më i ri. Skedarët mbeten në disk, kështu që kthimi është i menjëhershëm.

## Përditësimet

Një herë në ditë modi pyet GitHub-in për [versionin më të ri](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) të këtij depoje. Nëse është më i ri se versioni që përdor, ekrani kryesor ofron ta instalosh. Pëlqimi bën tri gjëra:

1. Skedari i ri JAR shkarkohet dhe **verifikohet SHA-256 i tij**, pikërisht kundrejt manifestit `update.json`, që publikohet me këtë version.
2. Skedari i vjetër JAR fshihet, dhe ai i riu zhvendoset në `mods/`.
3. Rindiz lojën.

Në Windows një skedar JAR që është i ngarkuar pikërisht tani nuk mund të fshihet, prandaj një skript i shkurtër i krijuar pret mbylljen e lojës dhe kryen hapin 2 për ty; modi ta thotë kur kjo ndodh. Në Android, Linux dhe macOS skedari JAR që po ekzekutohet nuk është i bllokuar dhe thjesht zëvendësohet. Gjithçka që prek përditësuesi ndodhet nën `mods/` dhe `config/localpack-bypass/`.

Kërkesa është një kërkesë e zakonshme anonime drejt API-t të GitHub (pa token, pa llogari), modi nuk ka telemetri dhe pa server të vetin. Versioni i anashkaluar mbahet mend, dhe `-Dlocalpack.bypass.updateCheck=false` i çaktivizon plotësisht përditësimet.

## Kërkesat

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 ose më i ri
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux ose një launcher Android si [PojavLauncher](https://pojavlauncherteam.github.io/): çdo skedar që prek modi përcaktohet nga drejtoria e lojës e launcherit, kështu që nuk duhet të përgatitet asnjë dosje me dorë
- Opsionale: [Mod Menu](https://modrinth.com/mod/modmenu), që shfaq emrin dhe përshkrimin e modit në gjuhën tënde

## Ndërtoje vetë

Modi ndërtohet në çdo push drejt `main` nga [rrjedha e punës GitHub Actions](.github/workflows/build.yml), skedari JAR ndodhet ndër artefaktet e ekzekutimit. Një tag si `v0.0.3` publikon gjithashtu një [version](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) me skedarin JAR dhe manifestin `update.json`, që përditësuesi lexon në lojë.

## Nga vjen kodi

- **Origjina:** projekti lindi nga prototipi fillestar i kësaj depoje, që krahasonte SHA-1 e paketës së serverit me skedarët lokalë.
- **Autorët:** McChopik, Telemine3140. Kodi i sotëm u rishkrua dhe u zgjerua me [Claude Code](https://claude.com/claude-code), një mjet programimi me inteligjencë artificiale nga [Anthropic](https://www.anthropic.com), me kërkesë të autorëve të projektit. U shtuan shkarkimi me një klikim, grumbullimi, aktivizimi automatik, „refuzo dhe dil“, përditësuesi, përkthimet, trajtimi i shtigjeve për Windows, macOS, Linux dhe Android, si dhe dokumentacioni. Kod nga moda të tjerë nuk u kopjua.
- **Ndërtuar mbi:** kodin e vetë Minecraft me emrat zyrtarë nga Mojang, [Fabric Loader dhe Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) dhe [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Statusi:** eksperimental. Nuk është testuar gjerësisht, prandaj përdore me përgjegjësinë tënde.
- **Nuk është i lidhur** me Mojang, Microsoft, Fabric ose Anthropic.

## Licenca

Teksti i licencës më poshtë është riprodhuar këtu në formën e tij origjinale angleze; ligjërisht detyruese është vetëm ky formulim anglez, një përkthim i licencës MIT nuk do ta zëvendësonte atë.

This is an **open-source** project released under the **[MIT License](LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Kontributet, raportet e gabimeve dhe pull request-et janë të mirëpritura: [hap një issue](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
