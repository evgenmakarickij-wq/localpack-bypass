# localpack-bypass

<p>
  <a href="LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Limbi:** [English](README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · **Română** · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Autori:** McChopik, Telemine3140

---

**localpack-bypass** este un mod [Fabric](https://fabricmc.net) gratuit, cu sursă deschisă, pentru client, destinat [Minecraft](https://www.minecraft.net) 26.2, care simplifică pachetele de resurse ale serverelor. În loc să descarci din nou același pachet de fiecare dată când te conectezi, compară SHA-1 al pachetului trimis de server cu fișierele ZIP care se află deja în folderul `resourcepacks`. Dacă se găsește un pachet potrivit, acesta este activat în tăcere, astfel încât te conectezi fără întrebări și fără să aștepți. Dacă un pachet este nou sau s-a schimbat, un singur ecran îți oferă să descarci toate pachetele necesare deodată; apoi modul le verifică, le activează, salvează selecția și reîncarcă resursele o singură dată. Pachetele descărcate rămân active după ce părăsești serverul, ca pachetele de resurse obișnuite, în timp ce refuzul te deconectează de la server ca în jocul original. Ecranele și mesajele urmează limba jocului; modul include traduceri pentru 38 de limbi (vezi lista de limbi de mai sus).

## Cum funcționează

- **Pachet deja descărcat:** este activat în tăcere. Fără întrebări, fără descărcări.
- **Pachet nou sau modificat:** un ecran cu **Descarcă toate pachetele de resurse**. Pachetele sunt salvate la `resourcepacks/localpack-<sha1>.zip`, verificate prin SHA-1, activate și salvate în opțiunile tale.
- **Refuz și ieșire:** ești deconectat de la server.
- **Pachete mai vechi:** pachetele descărcate anterior de mod sunt debifate când sosește o versiune mai nouă. Fișierele rămân pe disc, așa că poți reveni imediat.

## Actualizări

O dată pe zi, modul întreabă GitHub despre [cea mai nouă versiune](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) a acestui depozit. Dacă este mai nouă decât versiunea pe care o folosești, ecranul de titlu îți oferă să o instalezi. Dacă accepți, se întâmplă trei lucruri:

1. Noul fișier JAR este descărcat și **SHA-256 al său este verificat** față de manifestul `update.json` publicat cu această versiune.
2. Vechiul fișier JAR este șters, iar cel nou este mutat în `mods/`.
3. Repornești jocul.

Pe Windows, un fișier JAR care tocmai este încărcat nu poate fi șters, așa că un mic script generat așteaptă închiderea jocului și face pasul 2 pentru tine; modul te anunță când se întâmplă asta. Pe Android, Linux și macOS fișierul JAR aflat în execuție nu este blocat și este pur și simplu înlocuit. Tot ceea ce atinge actualizatorul se află în `mods/` și `config/localpack-bypass/`.

Interogarea este o cerere anonimă obișnuită către API-ul GitHub (fără token, fără cont); modul nu are telemetrie și nici server propriu. O versiune omisă este reținută, iar `-Dlocalpack.bypass.updateCheck=false` dezactivează complet actualizările.

## Cerințe

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 sau mai nou
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux sau un launcher de Android precum [PojavLauncher](https://pojavlauncherteam.github.io/): fiecare fișier pe care îl atinge modul este determinat din directorul de joc al launcherului, așa că nu trebuie configurat niciun folder manual
- Opțional: [Mod Menu](https://modrinth.com/mod/modmenu), care afișează numele și descrierea modului în limba ta

## Compilarea proprie

Modul este compilat la fiecare push către `main` de către [fluxul de lucru GitHub Actions](.github/workflows/build.yml), iar fișierul JAR se află printre artefactele rulării. Un tag precum `v0.0.3` publică în plus o [versiune](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) cu fișierul JAR și manifestul `update.json` pe care actualizatorul îl citește în joc.

## De unde provine codul

- **Origine:** proiectul a luat naștere din prototipul original al acestui depozit, care compara SHA-1 al pachetului serverului cu fișiere locale.
- **Autori:** McChopik, Telemine3140. Codul actual a fost rescris și extins cu [Claude Code](https://claude.com/claude-code), un instrument de programare cu IA de la [Anthropic](https://www.anthropic.com), la cererea autorilor proiectului. Au fost adăugate descărcarea cu un singur clic, gruparea, activarea automată, „refuză și ieși“, actualizatorul, traducerile, gestionarea căilor pentru Windows, macOS, Linux și Android, precum și documentația. Nu s-a copiat cod din alte moduri.
- **Construit pe:** codul propriu al Minecraft cu denumirile oficiale de la Mojang, [Fabric Loader și Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) și [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Stare:** experimental. Nu a fost testat pe larg, așa că folosește-l pe propriul risc.
- **Neafiliat** cu Mojang, Microsoft, Fabric sau Anthropic.

## Licență

Textul licenței de mai jos este reprodus aici în engleza sa originală; singura formulare obligatorie din punct de vedere juridic este aceasta în engleză, iar o traducere a licenței MIT nu l-ar înlocui.

This is an **open-source** project released under the **[MIT License](LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Contribuțiile, raportările de erori și pull request-urile sunt binevenite: [deschide un issue](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
