# localpack-bypass

<p>
  <a href="../LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Valodas:** [English](../README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · **Latviešu** · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Autori:** McChopik, Telemine3140

---

**localpack-bypass** ir bezmaksas, atvērtā koda, klienta puses [Fabric](https://fabricmc.net) mods [Minecraft](https://www.minecraft.net) 26.2 versijai, kas atvieglo darbu ar servera resursu pakām. Tā vietā, lai katru reizi pievienojoties atkal lejupielādētu vienu un to pašu paku, tas salīdzina servera sūtītās pakas SHA-1 ar ZIP failiem, kas jau atrodas mapē `resourcepacks`. Ja tiek atrasta atbilstoša paka, tā tiek klusi iespējota, tāpēc tu pievienojies bez jautājumiem un gaidīšanas. Ja paka ir jauna vai mainījusies, viens ekrāns piedāvā lejupielādēt visas nepieciešamās pakas uzreiz; pēc tam mods tās pārbauda, iespējo, saglabā izvēli un pārlādē resursus tikai vienu reizi. Lejupielādētās pakas paliek iespējotas pēc servera atstāšanas, tāpat kā parastas resursu pakas, savukārt atteikšanās atvieno tevi no servera tāpat kā parastajā spēlē. Ekrāni un ziņojumi seko spēles valodai; mods nodrošina tulkojumus 38 valodās (skatiet valodu sarakstu augstāk).

## Kā tas darbojas

- **Paka jau lejupielādēta:** tā tiek klusi iespējota. Nekādu jautājumu, nekādu lejupielāžu.
- **Jauna vai mainīta paka:** ekrāns ar pogu **Lejupielādēt visas resursu pakas**. Pakas tiek saglabātas kā `resourcepacks/localpack-<sha1>.zip`, pārbaudītas pēc SHA-1, iespējotas un saglabātas tavos iestatījumos.
- **Atteikšanās un aiziešana:** tu tiksi atvienots no servera.
- **Vecākas pakas:** iepriekš ar modu lejupielādētās pakas tiek atzīmētas kā neaktīvas, kad pienāk jaunāka versija. Faili paliek diskā, tāpēc atgriezties var uzreiz.
- **Nekas neizdodas klusējot:** lejupielāde, kas neizdevās, kontrolsumma, kas nesakrita, vai fails, ko spēle nevarēja ieslēgt, tiek ziņots ekrānā vai kā paziņojums, ja ekrāns nav pieejams.

## Iestatījumi (Mod Menu)

Ja ir instalēts [Mod Menu](https://modrinth.com/mod/modmenu), mods parādās modu sarakstā ar savu iestatījumu ekrānu. Nekādas karstās taustiņas netika pievienotas; viss ir tur.

- **Mape lejupielādētajām pakām** — kur tiek glabātas pakas. Atstāj to tukšu, lai izmantotu spēles pašas mapi `resourcepacks`. Der arī jebkura cita mape, tostarp cits disks vai Android koplietotā mape; tad pakas pirms ieslēgšanas tiek arī nokopētas uz `resourcepacks`, jo tā ir vienīgā mape, ko pati spēle nolasa. Pogas atver vienu vai otru mapi.
- **Atcerēties serverus, kuriem piekriti** — serverim, no kura jau esi ņēmis pakas, vairs nejautā. Atbilde tiek ierakstīta arī spēles pašā katra servera iestatījumā *Server Resource Packs*, blakus Prompt, Enabled un Disabled.
- **Paturēt pakas no citiem serveriem** — atstāj to ieslēgtu, lai paturētu pakas, kas tev jau ir. Izslēdz to, lai katru serveri sāktu tikai ar šī servera pakām.
- **Gaidīt pirms jautāšanas** — cik ilgi vākt pārējo partiju pirms ekrāna parādīšanās, milisekundēs vai sekundēs. Serveri sūta visas savas pakas uzreiz, tāpēc gaidīšana vairākus jautājumus pārvērš vienā.
- **Atteikties pēc** un **Mēģinājumi** — cik ilgi viena lejupielāde var ilgt un cik reizes neizdevusies tiek mēģināta atkārtoti. Kontrolsumma, kas nesakrīt, tiek uzskatīta par neveiksmi, jo tā izskatās puspabeigta pārsūtīšana.
- **Saglabāt ierakstu** — tiek pierakstīta katra resursu paka, ko serveris nosūta: kurš serveris, kura adrese, kontrolsumma, izmērs, kas ar to notika un cik ilgi tas aizņēma. Tas ir fails tavā paša datorā; nekas netiek nekur nosūtīts.
- **Lejupielādētās pakas** — pakas, ko mods ir lejupielādējis, sagrupētas pēc servera, no kura tās nākušas, parādot, vai katra ir ieslēgta, izslēgta vai pazudusi no mapes. Nospied uz kādas, lai to ieslēgtu vai izslēgtu; paku, kuras fails ir pazudis, var aizmirst. **Aizmirst visu** notīra to, ko mods atceras, un nekad neizdzēš pakas failu.
- **Ko serveri nosūtīja** — pats ieraksts, jaunākais vispirms, ar katras vienības detaļām.

## Atjauninājumi

Reizi dienā mods vaicā GitHub par šī repozitorija [jaunāko versiju](https://github.com/evgenmakarickij-wq/localpack-bypass/releases). Ja tā ir jaunāka par versiju, ko tu izmanto, sākuma ekrāns piedāvā to instalēt. Piekrītot notiek trīs lietas:

1. Tiek lejupielādēts jaunais JAR fails un **tiek pārbaudīts tā SHA-256** pret `update.json` manifestu, kas tiek publicēts kopā ar šo versiju.
2. Vecais JAR fails tiek izdzēsts, un jaunais tiek pārvietots uz `mods/`.
3. Tu restartē spēli.

Operētājsistēmā Windows šobrīd ielādētu JAR failu nevar izdzēst, tāpēc īss ģenerēts skripts gaida, līdz spēle tiek aizvērta, un veic 2. soli tavā vietā; mods tevi par to informē. Operētājsistēmās Android, Linux un macOS darbojošais JAR fails nav bloķēts un tiek vienkārši aizstāts. Viss, ko atjauninātājs aizskar, atrodas `mods/` un `config/localpack-bypass/`.

Vaicājums ir parasts anonīms GitHub API pieprasījums (bez marķiera, bez konta); modam nav telemetrijas un nav sava servera. Izlaistā versija tiek iegaumēta, un `-Dlocalpack.bypass.updateCheck=false` pilnībā izslēdz atjauninājumus.

**Versiju numurēšanas piezīme:** `0.0.1.2026-alpha-build-beta-release` tiek nolasīts kā `[0, 0, 1, 2026]`, kas sarindojas *zem* iepriekšējās `0.0.3-beta` (`[0, 0, 3]`). Būvējumi ar numuru `0.0.3-beta` vai augstāku šo nesaņems automātiski un būs jāaizstāj ar roku.

## Priekšnosacījumi

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 vai jaunāks
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux vai Android palaidējs, piemēram, [PojavLauncher](https://pojavlauncherteam.github.io/): katrs fails, ko mods aizskar, tiek noteikts no palaidēja spēles direktorijas, tāpēc neviena mape nav jāiestata manuāli
- Neobligāti: [Mod Menu](https://modrinth.com/mod/modmenu), kas rāda moda nosaukumu un aprakstu tavā valodā un piešķir modam iestatījumu ekrānu

## Kā uzbūvēt pašam

Mods tiek būvēts pie katra push uz `main` ar [GitHub Actions darbplūsmu](../.github/workflows/build.yml); JAR fails atrodas darbplūsmas artefaktos. Tags, piemēram, `v0.0.3`, papildus publicē [versiju](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) ar JAR failu un `update.json` manifestu, ko spēlē nolasa atjauninātājs.

## No kurienes nāk kods

- **Izcelsme:** projekts radās no šī repozitorija sākotnējā prototipa, kas salīdzināja servera pakas SHA-1 ar vietējiem failiem.
- **Autori:** McChopik, Telemine3140. Pašreizējais kods tika pārrakstīts un paplašināts ar [Claude Code](https://claude.com/claude-code), [Anthropic](https://www.anthropic.com) mākslīgā intelekta programmēšanas rīku, pēc projekta autoru lūguma. Pievienota lejupielāde ar vienu klikšķi, paku apvienošana, automātiska iespējošana, „atteikties un aiziet“, atjauninātājs, tulkojumi, ceļu apstrāde Windows, macOS, Linux un Android vidēs, kā arī dokumentācija. Kods no citiem modiem netika kopēts.
- **Balstīts uz:** Minecraft paša koda ar oficiālajiem Mojang nosaukumiem, [Fabric Loader un Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) un [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Statuss:** eksperimentāls. Tas nav rūpīgi pārbaudīts, tāpēc lieto to uz savu risku.
- **Nav saistīts** ar Mojang, Microsoft, Fabric vai Anthropic.

## Licence

Zemāk redzamais licences teksts šeit ir iekļauts tā oriģinālajā angļu valodā; juridiski saistošs ir tikai šis angļu formulējums, MIT licences tulkojums to neaizstātu.

This is an **open-source** project released under the **[MIT License](../LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Ieguldījumi, kļūdu ziņojumi un pull request ir gaidīti: [atver issue](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
