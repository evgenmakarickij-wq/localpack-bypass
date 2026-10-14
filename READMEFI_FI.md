# localpack-bypass

<p>
  <a href="LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Kielet:** [English](README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · **Suomi** · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Tekijät:** McChopik, Telemine3140

---

**localpack-bypass** on ilmainen, avoimen lähdekoodin asiakaspuolen [Fabric](https://fabricmc.net)-modi [Minecraftille](https://www.minecraft.net) 26.2, joka tekee palvelimen resurssipaketeista vaivattomia. Sen sijaan että sama paketti ladattaisiin uudelleen joka kerta liittyessäsi, modi vertaa palvelimen lähettämän paketin SHA-1-tiivistettä `resourcepacks`-kansiossa jo oleviin ZIP-tiedostoihin. Jos vastaava paketti löytyy, se aktivoidaan äänettömästi, joten liityt ilman kyselyitä ja ilman odottamista. Jos paketti on uusi tai muuttunut, yksi näyttö tarjoaa kaikkien tarvittavien pakettien lataamisen kerralla; sen jälkeen modi tarkistaa ne, aktivoi ne, tallentaa valinnan ja lataa resurssit uudelleen vain kerran. Ladatut paketit pysyvät aktiivisina palvelimelta poistumisen jälkeen, kuten tavalliset resurssipaketit, kun taas kieltäytyminen katkaisee yhteytesi palvelimeen kuten vanilla-versiossa. Näytöt ja viestit noudattavat pelin kieltä; modi sisältää käännökset 38 kielelle (katso luettelo yllä).

## Näin se toimii

- **Paketti on jo ladattu:** se aktivoidaan äänettömästi. Ei kyselyä, ei latausta.
- **Uusi tai muuttunut paketti:** yksi näyttö, jossa **Lataa kaikki resurssipaketit**. Paketit tallennetaan polkuun `resourcepacks/localpack-<sha1>.zip`, tarkistetaan SHA-1-tiivisteellä, aktivoidaan ja tallennetaan asetuksiisi.
- **Hylkää ja poistu:** yhteytesi palvelimeen katkaistaan.
- **Vanhemmat paketit:** modin aiemmin lataamat paketit poistetaan valinnasta, kun uudempi versio saapuu. Tiedostot jäävät levylle, joten voit vaihtaa takaisin heti.

## Päivitykset

Kerran päivässä modi kysyy GitHubilta tämän tietovaraston [uusinta versiota](https://github.com/evgenmakarickij-wq/localpack-bypass/releases). Jos se on uudempi kuin käyttämäsi versio, päävalikko tarjoaa sen asentamista. Myöntyminen tekee kolme asiaa:

1. Uusi JAR-tiedosto ladataan ja sen **SHA-256-tiiviste tarkistetaan** kyseisen version mukana julkaistua `update.json`-manifestia vasten.
2. Vanha JAR-tiedosto poistetaan ja uusi siirretään kansioon `mods/`.
3. Käynnistät pelin uudelleen.

Windowsissa ladattua JAR-tiedostoa ei voi poistaa, joten pieni luotu komentosarja odottaa pelin sulkeutumista ja tekee vaiheen 2 puolestasi; modi kertoo sinulle, kun niin tapahtuu. Androidissa, Linuxissa ja macOS:ssä käynnissä oleva JAR-tiedosto ei ole lukittu ja se vain korvataan. Kaikki, mihin päivittäjä koskee, on kansioissa `mods/` ja `config/localpack-bypass/`.

Kysely on tavallinen nimetön pyyntö GitHub-rajapintaan (ei tokenia, ei tiliä), eikä modissa ole telemetriaa eikä omaa palvelinta. Ohitettu versio muistetaan, ja `-Dlocalpack.bypass.updateCheck=false` kytkee päivitykset kokonaan pois.

## Vaatimukset

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 tai uudempi
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux tai Android-käynnistin kuten [PojavLauncher](https://pojavlauncherteam.github.io/): jokainen tiedosto, johon modi koskee, määritetään käynnistimen pelikansiosta, joten mitään kansiota ei tarvitse määrittää käsin
- Valinnainen: [Mod Menu](https://modrinth.com/mod/modmenu), joka näyttää modin nimen ja kuvauksen omalla kielelläsi

## Rakenna se itse

Modi rakennetaan jokaisella `main`-haaraan työnnettävällä pushilla [GitHub Actions -työnkulun](.github/workflows/build.yml) avulla, ja JAR-tiedosto löytyy ajon artefaktoista. Tagi kuten `v0.0.3` julkaisee lisäksi [version](https://github.com/evgenmakarickij-wq/localpack-bypass/releases), jossa on JAR-tiedosto ja `update.json`-manifesti, jonka pelin päivittäjä lukee.

## Mistä koodi tulee

- **Alkuperä:** projekti syntyi tämän tietovaraston alkuperäisestä prototyypistä, joka vertasi palvelinpaketin SHA-1-tiivistettä paikallisiin tiedostoihin.
- **Tekijät:** McChopik, Telemine3140. Nykyinen koodi kirjoitettiin uudelleen ja sitä laajennettiin projektin tekijöiden pyynnöstä [Claude Code](https://claude.com/claude-code) -työkalulla, joka on [Anthropicin](https://www.anthropic.com) tekoälyohjelmointityökalu. Lisättyä on yhden napsautuksen lataus, niputtaminen, automaattinen aktivointi, ”hylkää ja poistu”, päivittäjä, käännökset, polkujen käsittely Windowsille, macOS:lle, Linuxille ja Androidille sekä dokumentaatio. Muista modeista peräisin olevaa koodia ei kopioitu.
- **Rakennettu:** Minecraftin omalle koodille Mojangin virallisilla nimillä, [Fabric Loader ja Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) ja [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Tila:** kokeellinen. Sitä ei ole testattu laajasti, joten käytä omalla vastuullasi.
- **Ei sidoksissa** Mojangiin, Microsoftiin, Fabriciin tai Anthropiciin.

## Lisenssi

Alla oleva lisenssiteksti toistetaan tässä alkuperäisellä englannillaan; vain englanninkielinen sanamuoto on oikeudellisesti sitova, eikä MIT-lisenssin käännös korvaisi sitä.

This is an **open-source** project released under the **[MIT License](LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Osallistuminen, virheraportit ja pull requestit ovat tervetulleita: [avaa ongelma](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
