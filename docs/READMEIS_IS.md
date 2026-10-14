# localpack-bypass

<p>
  <a href="../LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Tungumál:** [English](../README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · **Íslenska** · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Höfundar:** McChopik, Telemine3140

---

**localpack-bypass** er ókeypis, opinn upprunaþókn (open source) biðlaraforrit (mod) fyrir [Fabric](https://fabricmc.net) á [Minecraft](https://www.minecraft.net) 26.2 sem gerir efnispakka netþjóna sársaukalausa. Í stað þess að sækja sama pakkann aftur í hvert sinn sem þú tengist, ber það saman SHA-1 pakkans sem netþjónninn sendir við ZIP-skrárnar sem þegar eru í möppunni `resourcepacks`. Finnist samsvarandi pakki er hann virkjaður hljóðlaust, svo þú tengist án fyrirspurna og án biðtíma. Ef pakki er nýr eða breyttur býður einn skjár upp á að sækja alla nauðsynlega pakka í einu; síðan staðfestir modið þá, virkjar þá, vistar valið og endurhleður efninu aðeins einu sinni. Niðurhlaðnir pakkar haldast virkir eftir að þú yfirgefur netþjóninn, eins og venjulegir efnispakkar, en ef þú hafnar er tengingunni við netþjóninn slitið eins og í vanilla. Skjáir og skilaboð fylgja tungumáli leiksins; modið fylgir með þýðingum fyrir 38 tungumál (sjá listann hér að ofan).

## Hvernig það virkar

- **Pakki þegar niðurhlaðinn:** hann er virkjaður hljóðlaust. Engin fyrirspurn, ekkert niðurhal.
- **Nýr eða breyttur pakki:** einn skjár með **Sækja alla efnispakka**. Pakkarnir eru vistaðir undir `resourcepacks/localpack-<sha1>.zip`, staðfestir með SHA-1, virkjaðir og vistaðir í stillingunum þínum.
- **Hafna og yfirgefa:** tengingunni við netþjóninn er slitið.
- **Eldri pakkar:** pakkar sem modið hefur áður sótt eru afvaldir þegar nýrri útgáfa berst. Skrárnar haldast á disknum, svo þú getur skipt til baka samstundis.
- **Ekkert mistekst hljóðlaust:** niðurhal sem gekk ekki, gátsumma sem passaði ekki, eða skrá sem leikurinn vildi ekki virkja er tilkynnt á skjánum, eða sem tilkynning ef enginn skjár er tiltækur.

## Stillingar (Mod Menu)

Með [Mod Menu](https://modrinth.com/mod/modmenu) uppsettu birtist modið í modlistanum með eigin stillingaskjá. Engir flýtilyklar voru bættir við; allt er þar.

- **Mappa fyrir niðurhlaðna pakka** — hvar pakkarnir eru geymdir. Láttu hana tóma fyrir eigin möppu `resourcepacks` leiksins. Önnur mappa virkar, þar á meðal annar diskur eða sameiginleg Android-mappa; pakkarnir eru þá einnig afritaðir inn í `resourcepacks` áður en þeir eru virkjaðir, því það er eina mappan sem leikurinn sjálfur les. Hnappar opna aðra hvora möppuna.
- **Muna netþjóna sem þú samþykktir** — netþjónn sem þú hefur þegar sótt pakka frá er ekki spurður aftur. Svarið er líka skrifað í eigin *Efnispakkar netþjóns*-stillingu leiksins fyrir hvern netþjón, við hlið Spyrja, Virkt og Óvirkt.
- **Halda pökkum frá öðrum netþjónum** — hafðu þetta á til að halda pökkunum sem þú ert nú þegar með. Slökktu á því til að hefja hvern netþjón með aðeins pökkum þess netþjóns.
- **Bíða áður en spurt er** — hversu lengi á að safna restinni af lotunni áður en skjárinn birtist, í millisekúndum eða sekúndum. Netþjónar senda alla pakkana sína í einu, svo bið breytir nokkrum spurningum í eina.
- **Gefa upp eftir** og **Tilraunir** — hversu lengi eitt niðurhal má taka, og hversu oft misheppnað er endurreynt. Gátsumma sem passar ekki telst mistök, því þannig lítur hálfkláraður flutningur út.
- **Halda skrá** — sérhver efnispakki sem netþjónn sendir er skráður: hvaða netþjónn, hvaða netfang, gátsumman, stærðin, hvað varð um hann og hversu lengi það tók. Það er skrá á þinni eigin tölvu; ekkert er sent neitt.
- **Niðurhlaðnir pakkar** — pakkarnir sem modið hefur hlaðið niður, flokkaðir eftir netþjónnum sem þeir komu frá, sem sýnir hvort hver er á, af eða horfinn úr möppunni. Ýttu á einn til að kveikja eða slökkva á honum; pakki sem skráin hans er horfin má gleymast. **Gleyma öllu** hreinsar það sem modið man og eyðir aldrei pakkaskrá.
- **Það sem netþjónar sendu** — skráin sjálf, nýjast fyrst, með upplýsingum um hverja færslu.

## Uppfærslur

Einu sinni á dag spyr modið GitHub um [nýjustu útgáfuna](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) af þessari geymslu. Ef hún er nýrri en útgáfan sem þú notar býður valmyndin upp á að setja hana upp. Já gerir þrjá hluti:

1. Nýja JAR-skráin er sótt og **SHA-256 hennar staðfest** gegn `update.json`-skýrslunni sem birt er með þeirri útgáfu.
2. Gömlu JAR-skránni er eytt og nýja flutt í `mods/`.
3. Þú endurræsir leikinn.

Í Windows er ekki hægt að eyða JAR-skrá sem er hlaðin, þannig að lítið búið skriftu bíður eftir að leiknum lýkur og framkvæmir skref 2 fyrir þig; modið segir þér þegar það gerist. Í Android, Linux og macOS er keyrandi JAR-skráin ekki læst og er einfaldlega skipt út. Allt sem uppfærsluhugbúnaðurinn snertir er undir `mods/` og `config/localpack-bypass/`.

Fyrirspurnin er venjuleg nafnlaus beiðni til GitHub-API-sins (ekkert tákn, enginn reikningur); modið er án fjarstýringargagna og án eigin netþjóns. Útgáfa sem er sleppt er munað, og `-Dlocalpack.bypass.updateCheck=false` slekkur alveg á uppfærslum.

**Fyrirvari um útgáfunúmer:** `0.0.1.2026-alpha-build-beta-release` er túlkað sem `[0, 0, 1, 2026]`, sem raðast *neðar en* eldri `0.0.3-beta` (`[0, 0, 3]`). Útgáfur númeraðar `0.0.3-beta` eða hærri fá þessa ekki boðna sjálfkrafa og þarf að skipta þeim út handvirkt.

## Kröfur

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 eða nýrri
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux eða Android-ræsir eins og [PojavLauncher](https://pojavlauncherteam.github.io/): hver skrá sem modið snertir er ákvörðuð út frá leikmöppu ræsisins, svo enga möppu þarf að setja upp handvirkt
- Valfrjálst: [Mod Menu](https://modrinth.com/mod/modmenu), sem sýnir nafn og lýsingu modsins á þínu tungumáli og gefur modinu stillingaskjá

## Byggja það sjálf/ur

Modið er byggt við hvern push í `main` af [GitHub Actions-vinnuflæðinu](../.github/workflows/build.yml), og JAR-skráin er meðal afurða keyrslunnar. Merki eins og `v0.0.3` birtir að auki [útgáfu](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) með JAR-skránni og `update.json`-skýrslunni sem uppfærsluhugbúnaðurinn í leiknum les.

## Hvaðan kóðinn kemur

- **Uppruni:** verkefnið varð til úr upprunalegu frumgerð þessarar geymslu, sem bar SHA-1 netþjónspakkans saman við staðbundnar skrár.
- **Höfundar:** McChopik, Telemine3140. Kóðinn í dag var endurskrifaður og aukinn að ósk höfunda verkefnisins með [Claude Code](https://claude.com/claude-code), gervigreindarforritunartóli frá [Anthropic](https://www.anthropic.com). Við bættust niðurhal með einum smelli, samantekt, sjálfvirk virkjun, „hafna og yfirgefa“, uppfærsluhugbúnaðurinn, þýðingarnar, slóðameðferð fyrir Windows, macOS, Linux og Android og skjölunin. Kóði frá öðrum moddum var ekki afritaður.
- **Byggt á:** eigin kóða Minecrafts með opinberum nöfnum Mojang, [Fabric Loader og Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) og [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Staða:** tilraunakennd. Ekki hefur verið prófað mikið, svo notaðu það á eigin ábyrgð.
- **Ekki tengt** Mojang, Microsoft, Fabric eða Anthropic.

## Leyfi

Leyfistextinn hér að neðan er birtur í sínu upprunalega enska máli; aðeins enski textinn er lagalega bindandi, og þýðing á MIT-leyfinu myndi ekki koma í hans stað.

This is an **open-source** project released under the **[MIT License](../LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Framlög, villutilkynningar og pull requests eru vel þegin: [opna málefni](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
