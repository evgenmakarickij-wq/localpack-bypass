# localpack-bypass

<p>
  <a href="../LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Kalbos:** [English](../README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · **Lietuvių** · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Autoriai:** McChopik, Telemine3140

---

**localpack-bypass** – tai nemokamas, atvirojo kodo, kliento pusės [Fabric](https://fabricmc.net) modas, skirtas [Minecraft](https://www.minecraft.net) 26.2, kuris palengvina darbą su serverio resursų paketais. Užuot kaskart prisijungus vėl atsisiuntus tą patį paketą, jis palygina serverio siunčiamo paketo SHA-1 su aplanke `resourcepacks` jau esančiais ZIP failais. Jei randamas atitinkantis paketas, jis tyliai įjungiamas, tad prisijungiate be klausimų ir laukimo. Jei paketas naujas ar pasikeitęs, viename ekrane siūloma iš karto atsisiųsti visus reikiamus paketus; tada modas juos patikrina, įjungia, išsaugo pasirinkimą ir resursus perkrauna tik vieną kartą. Atsisiųsti paketai lieka įjungti išėjus iš serverio, kaip įprasti resursų paketai, o atsisakymas atjungia jus nuo serverio taip pat, kaip įprastame žaidime. Ekranai ir pranešimai seka žaidimo kalbą; modas pateikia vertimus į 38 kalbas (žr. aukščiau pateiktą kalbų sąrašą).

## Kaip tai veikia

- **Paketas jau atsisiųstas:** jis įjungiamas tyliai. Jokių klausimų, jokio atsisiuntimo.
- **Naujas ar pasikeitęs paketas:** ekranas su mygtuku **Atsisiųsti visus resursų paketus**. Paketai saugomi kaip `resourcepacks/localpack-<sha1>.zip`, patikrinami pagal SHA-1, įjungiami ir išsaugomi jūsų nustatymuose.
- **Atsisakymas ir išėjimas:** būsite atjungti nuo serverio.
- **Senesni paketai:** anksčiau šio modo atsisiųsti paketai atžymimi, kai pasirodo naujesnė versija. Failai lieka diske, todėl grįžti galima akimirksniu.

## Atnaujinimai

Kartą per dieną modas užklausia GitHub apie šios saugyklos [naujausią versiją](https://github.com/evgenmakarickij-wq/localpack-bypass/releases). Jei ji naujesnė už jūsų naudojamą versiją, tituliniame ekrane siūloma ją įdiegti. Sutikus atsitinka trys dalykai:

1. Atsisiunčiamas naujasis JAR failas ir **patikrinama jo SHA-256**, lyginant su `update.json` manifestu, publikuojamu su šia versija.
2. Senasis JAR failas ištrinamas, o naujasis perkeliamas į `mods/`.
3. Iš naujo paleidžiate žaidimą.

Windows sistemoje šiuo metu įkelta JAR failo ištrinti negalima, todėl trumpas sugeneruotas scenarijus palaukia, kol žaidimas bus uždarytas, ir atlieka 2 žingsnį už jus; modas jums apie tai praneša. Android, Linux ir macOS sistemose veikiantis JAR failas nėra užrakintas ir tiesiog pakeičiamas. Viskas, ką paliečia atnaujintuvas, yra `mods/` ir `config/localpack-bypass/`.

Užklausa yra įprasta anoniminė GitHub API užklausa (jokio žetono, jokios paskyros); modas neturi telemetrijos ir savo serverio. Praleista versija įsimenama, o `-Dlocalpack.bypass.updateCheck=false` visiškai išjungia atnaujinimus.

## Reikalavimai

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 arba naujesnis
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux arba Android paleidėjas, pavyzdžiui, [PojavLauncher](https://pojavlauncherteam.github.io/): kiekvienas failas, kurį liečia modas, nustatomas pagal paleidėjo žaidimo katalogą, todėl jokio aplanko nereikia nustatyti rankiniu būdu
- Neprivaloma: [Mod Menu](https://modrinth.com/mod/modmenu), kuris rodo modo pavadinimą ir aprašymą jūsų kalba

## Kaip susikompiliuoti

Modas statomas prie kiekvieno push į `main` per [GitHub Actions darbo eigą](../.github/workflows/build.yml); JAR failas yra tarp vykdymo artefaktų. Žymė, pavyzdžiui, `v0.0.3`, papildomai paskelbia [versiją](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) su JAR failu ir `update.json` manifestu, kurį žaidime skaito atnaujintuvas.

## Iš kur atsiranda kodas

- **Kilmė:** projektas atsirado iš pradinio šios saugyklos prototipo, kuris lygino serverio paketo SHA-1 su vietiniais failais.
- **Autoriai:** McChopik, Telemine3140. Dabartinis kodas buvo perrašytas ir išplėstas naudojant [Claude Code](https://claude.com/claude-code), [Anthropic](https://www.anthropic.com) DI programavimo įrankį, projekto autorių pageidavimu. Pridėtas atsisiuntimas vienu paspaudimu, paketų grupavimas, automatinis įjungimas, „atsisakyti ir išeiti“, atnaujintuvas, vertimai, kelio apdorojimas Windows, macOS, Linux ir Android sistemose bei dokumentacija. Kodas iš kitų modų nebuvo kopijuojamas.
- **Pastatyta ant:** Minecraft savo paties kodo su oficialiais Mojang pavadinimais, [Fabric Loader ir Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) ir [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Būsena:** eksperimentinė. Jis nebuvo išsamiai išbandytas, todėl naudokite savo atsakomybe.
- **Nesusijęs** su Mojang, Microsoft, Fabric ar Anthropic.

## Licencija

Žemiau pateiktas licencijos tekstas čia reprodukuojamas originalia anglų kalba; teisiškai privaloma yra tik ši angliška formuluotė, o MIT licencijos vertimas jos nepakeistų.

This is an **open-source** project released under the **[MIT License](../LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Indėliai, klaidų pranešimai ir pull request'ai laukiami: [atidarykite issue](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
