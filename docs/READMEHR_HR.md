# localpack-bypass

<p>
  <a href="../LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Jezici:** [English](../README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · **Hrvatski** · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Autori:** McChopik, Telemine3140

---

**localpack-bypass** je besplatan, otvorenog koda, klijentski [Fabric](https://fabricmc.net) mod za [Minecraft](https://www.minecraft.net) 26.2 koji pakete resursa poslužitelja čini bezbolnima. Umjesto ponovnog preuzimanja istog paketa pri svakom pridruživanju, uspoređuje SHA-1 paketa koji poslužitelj pošalje sa ZIP datotekama koje se već nalaze u mapi `resourcepacks`. Ako se pronađe odgovarajući paket, tiho se aktivira, pa se pridružuješ bez pitanja i bez čekanja. Ako je paket nov ili izmijenjen, jedan zaslon nudi preuzimanje svih potrebnih paketa odjednom; mod ih zatim provjerava, aktivira, sprema odabir i ponovno učitava resurse samo jednom. Preuzeti paketi ostaju aktivni nakon napuštanja poslužitelja, kao obični paketi resursa, dok te odbijanje odspaja s poslužitelja kao u vanilla igri. Zasloni i poruke prate jezik igre; mod donosi prijevode za 38 jezika (vidi popis gore).

## Kako funkcionira

- **Paket je već preuzet:** tiho se aktivira. Nema pitanja, nema preuzimanja.
- **Novi ili izmijenjeni paket:** zaslon s **Preuzmi sve pakete resursa**. Paketi se spremaju u `resourcepacks/localpack-<sha1>.zip`, provjeravaju se putem SHA-1, aktiviraju i spremaju u tvoje postavke.
- **Odbij i izađi:** odspojen si s poslužitelja.
- **Stariji paketi:** paketi koje je mod prethodno preuzeo odznačuju se kada stigne novija verzija. Datoteke ostaju na disku, pa je povratak na staro trenutan.

## Ažuriranja

Jednom dnevno mod pita GitHub za [najnoviju verziju](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) ovog repozitorija. Ako je novija od verzije koju koristiš, glavni izbornik nudi njezinu instalaciju. Prihvaćanje čini tri stvari:

1. Nova JAR datoteka se preuzima i njezin se **SHA-256 provjerava** prema manifestu `update.json` koji se objavljuje uz tu verziju.
2. Stara JAR datoteka se briše, a nova premješta u `mods/`.
3. Ponovno pokrećeš igru.

Na Windowsu se JAR datoteka koja je upravo učitana ne može izbrisati, pa kratka generirana skripta čeka zatvaranje igre i obavlja korak 2 umjesto tebe; mod ti to kaže kada se to dogodi. Na Androidu, Linuxu i macOS-u pokrenuta JAR datoteka nije zaključana i jednostavno se zamjenjuje. Sve što program za ažuriranje dira nalazi se u `mods/` i `config/localpack-bypass/`.

Upit je običan anoniman zahtjev GitHub API-ju (bez tokena, bez računa), mod nema telemetriju ni vlastiti poslužitelj. Preskočena verzija se pamti, a `-Dlocalpack.bypass.updateCheck=false` potpuno isključuje ažuriranja.

## Preduvjeti

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 ili noviji
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux ili Android pokretač poput [PojavLauncher](https://pojavlauncherteam.github.io/): svaka datoteka koju mod dira određuje se iz igrinog direktorija pokretača, pa nije potrebno ručno postavljati mapu
- Neobavezno: [Mod Menu](https://modrinth.com/mod/modmenu), koji prikazuje naziv i opis moda na tvom jeziku

## Izgradnja samostalno

Mod se gradi pri svakom pushu na `main` pomoću [GitHub Actions workflowa](../.github/workflows/build.yml), a JAR datoteka nalazi se među artefaktima tog pokretanja. Oznaka poput `v0.0.3` dodatno objavljuje [verziju](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) s JAR datotekom i manifestom `update.json` koji program za ažuriranje u igri čita.

## Odakle dolazi kod

- **Podrijetlo:** projekt je nastao iz izvornog prototipa ovog repozitorija koji je uspoređivao SHA-1 paketa poslužitelja s lokalnim datotekama.
- **Autori:** McChopik, Telemine3140. Današnji je kod po želji autora projekta iznova napisan i proširen pomoću [Claude Code](https://claude.com/claude-code), AI alata za programiranje tvrtke [Anthropic](https://www.anthropic.com). Dodani su preuzimanje jednim klikom, grupiranje, automatsko aktiviranje, „odbij i izađi“, program za ažuriranje, prijevodi, rukovanje putanjama za Windows, macOS, Linux i Android te dokumentacija. Kod iz drugih modova nije kopiran.
- **Izgrađeno na:** Minecraftovu vlastitom kodu s Mojangovim službenim nazivima, [Fabric Loaderu i Fabric API-ju](https://fabricmc.net), [Fabric Loomu](https://github.com/FabricMC/fabric-loom) i [SpongePowered Mixinu](https://github.com/SpongePowered/Mixin).
- **Status:** eksperimentalno. Nije temeljito testirano, pa ga koristi na vlastitu odgovornost.
- **Nije povezano** s Mojangom, Microsoftom, Fabricom ni Anthropicom.

## Licenca

Tekst licence u nastavku naveden je ovdje u svom izvornom engleskom obliku; pravno je obvezujuć samo taj engleski tekst, a prijevod MIT licence ne bi ga zamijenio.

This is an **open-source** project released under the **[MIT License](../LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Doprinosi, prijave grešaka i pull requestovi su dobrodošli: [otvori issue](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
