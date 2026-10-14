# localpack-bypass

<p>
  <a href="LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Keeled:** [English](README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · **Eesti** · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Autorid:** McChopik, Telemine3140

---

**localpack-bypass** on tasuta, avatud lähtekoodiga kliendipoolne [Fabric](https://fabricmc.net)-mod [Minecraftile](https://www.minecraft.net) 26.2, mis muudab serveri ressursipakid valututeks. Selle asemel et laadida iga liitumise ajal sama pakki uuesti alla, võrdleb see serveri saadetud paki SHA-1-teisendit juba kaustas `resourcepacks` olevate ZIP-failidega. Sobiva paki leidmisel aktiveeritakse see vaikselt, nii et liitute ilma küsimuste ja ootamiseta. Kui pakk on uus või muutunud, pakub üks ekraan võimalust laadida kõik vajalikud pakid korraga alla; seejärel mod kontrollib need üle, aktiveerib need, salvestab valiku ja laadib ressursid uuesti ainult üks kord. Allalaaditud pakid jäävad pärast serverist lahkumist aktiivseks, nagu tavalised ressursipakid, samas kui keeldumine katkestab teie ühenduse serveriga nagu vanilla-versioonis. Ekraanid ja teated järgivad mängu keelt; mod sisaldab tõlkeid 38 keelde (vt loetelu ülal).

## Kuidas see töötab

- **Pakk on juba alla laaditud:** see aktiveeritakse vaikselt. Ei mingit küsimust, ei mingit allalaadimist.
- **Uus või muutunud pakk:** üks ekraan, kus on **Laadi alla kõik ressursipakid**. Pakid salvestatakse asukohta `resourcepacks/localpack-<sha1>.zip`, kontrollitakse SHA-1-teisendiga, aktiveeritakse ja salvestatakse teie seadetesse.
- **Keeldu ja lahku:** teie ühendus serveriga katkestatakse.
- **Vanemad pakid:** modi varem alla laaditud pakid eemaldatakse valikust, kui saabub uuem versioon. Failid jäävad kettale, nii et saate kohe tagasi lülituda.

## Uuendused

Kord päevas küsib mod GitHubilt selle hoidla [uusimat versiooni](https://github.com/evgenmakarickij-wq/localpack-bypass/releases). Kui see on uuem kui teie kasutatav versioon, pakub avamenüü seda installida. Nõustumine teeb kolm asja:

1. Uus JAR-fail laaditakse alla ja selle **SHA-256-teisendit kontrollitakse** selle versiooniga avaldatud `update.json`-manifesti vastu.
2. Vana JAR-fail kustutatakse ja uus teisaldatakse kausta `mods/`.
3. Taaskäivitate mängu.

Windowsis ei saa laaditud JAR-faili kustutada, seega väike loodud skript ootab mängu sulgemist ja teeb 2. sammu teie eest; mod teavitab teid, kui see juhtub. Androidis, Linuxis ja macOS-is ei ole töötav JAR-fail lukustatud ja see lihtsalt asendatakse. Kõik, mida uuendaja puudutab, asub kaustades `mods/` ja `config/localpack-bypass/`.

Päring on tavaline anonüümne taotlus GitHubi API-le (ilma tokenita, ilma kontota); modil pole telemeetriat ega oma serverit. Vahelejäetud versioon jäetakse meelde ja `-Dlocalpack.bypass.updateCheck=false` lülitab uuendused täielikult välja.

## Nõuded

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 või uuem
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux või Androidi käivitaja nagu [PojavLauncher](https://pojavlauncherteam.github.io/): iga fail, mida mod puudutab, määratakse käivitaja mängukausta järgi, nii et ühtegi kausta pole vaja käsitsi seadistada
- Valikuline: [Mod Menu](https://modrinth.com/mod/modmenu), mis näitab modi nime ja kirjeldust teie keeles

## Ehita see ise

Mod ehitatakse iga `main`-haru pushi korral [GitHub Actionsi töövoo](.github/workflows/build.yml) abil ja JAR-fail asub jooksu artefaktide hulgas. Silt nagu `v0.0.3` avaldab lisaks [versiooni](https://github.com/evgenmakarickij-wq/localpack-bypass/releases), milles on JAR-fail ja `update.json`-manifest, mida mängusisene uuendaja loeb.

## Kust kood pärineb

- **Päritolu:** projekt kasvas välja selle hoidla algseast prototüübist, mis võrdles serveripaki SHA-1-teisendit kohalike failidega.
- **Autorid:** McChopik, Telemine3140. Tänane kood kirjutati ümber ja seda laiendati projekti autorite soovil [Claude Code](https://claude.com/claude-code)'iga, mis on [Anthropicu](https://www.anthropic.com) tehisintellekti programmeerimisvahend. Lisandusid ühe klikiga allalaadimine, koondamine, automaatne aktiveerimine, „keeldu ja lahku“, uuendaja, tõlked, teede käsitlemine Windowsi, macOS-i, Linuxi ja Androidi jaoks ning dokumentatsioon. Koodi teistest modidest ei kopeeritud.
- **Põhineb:** Minecrafti enda koodil Mojangi ametlike nimedega, [Fabric Loader ja Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) ja [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Olek:** eksperimentaalne. Seda ei ole põhjalikult testitud, seega kasutage omal vastutusel.
- **Ei ole seotud** Mojangi, Microsofti, Fabrici ega Anthropicuga.

## Litsents

Allpool olev litsentsitekst on siin esitatud oma algses inglise keeles; juriidiliselt on siduv ainult ingliskeelne sõnastus ja MIT-litsentsi tõlge ei asendaks seda.

This is an **open-source** project released under the **[MIT License](LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Kaastööd, veateated ja pull requestid on oodatud: [ava probleem](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
