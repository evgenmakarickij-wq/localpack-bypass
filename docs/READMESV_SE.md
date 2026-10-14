# localpack-bypass

<p>
  <a href="../LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Språk:** [English](../README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · **Svenska** · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Författare:** McChopik, Telemine3140

---

**localpack-bypass** är en gratis, öppen källkods-mod på klientsidan för [Fabric](https://fabricmc.net) till [Minecraft](https://www.minecraft.net) 26.2 som gör serverns resurspaket smärtfria. I stället för att ladda ned samma paket igen varje gång du ansluter jämför den SHA-1-summan för paketet som servern skickar med de ZIP-filer som redan ligger i mappen `resourcepacks`. Om ett matchande paket hittas aktiveras det tyst, så att du ansluter utan frågor och utan att vänta. Är ett paket nytt eller ändrat erbjuder en enda skärm att ladda ned alla nödvändiga paket på en gång; därefter verifierar modden dem, aktiverar dem, sparar valet och laddar om resurserna bara en gång. Nedladdade paket förblir aktiva efter att du lämnat servern, precis som vanliga resurspaket, medan ett avböjande kopplar bort dig från servern som i vanilla. Skärmar och meddelanden följer spelspråket; modden levererar översättningar för 38 språk (se listan ovan).

## Så fungerar det

- **Paketet är redan nedladdat:** det aktiveras tyst. Ingen fråga, ingen nedladdning.
- **Nytt eller ändrat paket:** en skärm med **Ladda ned alla resurspaket**. Paketen sparas under `resourcepacks/localpack-<sha1>.zip`, verifieras med SHA-1, aktiveras och sparas i dina inställningar.
- **Avböj och lämna:** du kopplas bort från servern.
- **Äldre paket:** paket som modden tidigare laddat ned avmarkeras när en nyare version anländer. Filerna ligger kvar på disken, så du kan byta tillbaka direkt.

## Uppdateringar

En gång om dagen frågar modden GitHub efter den [senaste versionen](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) av detta arkiv. Om den är nyare än versionen du använder erbjuder huvudmenyn att installera den. Ett ja gör tre saker:

1. Den nya JAR-filen laddas ned och dess **SHA-256 verifieras** mot `update.json`-manifestet som publiceras med den versionen.
2. Den gamla JAR-filen tas bort och den nya flyttas till `mods/`.
3. Du startar om spelet.

I Windows går det inte att ta bort en JAR-fil som är laddad, så ett litet genererat skript väntar på att spelet avslutas och utför steg 2 åt dig; modden säger till när det händer. I Android, Linux och macOS är den körande JAR-filen inte låst och ersätts helt enkelt. Allt som uppdateraren rör ligger under `mods/` och `config/localpack-bypass/`.

Frågan är en vanlig anonym begäran till GitHub-API:et (ingen token, inget konto), och modden har ingen telemetri och ingen egen server. En överhoppad version kommer ihåg, och `-Dlocalpack.bypass.updateCheck=false` stänger av uppdateringar helt.

## Krav

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 eller nyare
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux eller en Android-launcher som [PojavLauncher](https://pojavlauncherteam.github.io/): varje fil som modden rör bestäms utifrån launchens spelmapp, så ingen mapp behöver ställas in manuellt
- Valfritt: [Mod Menu](https://modrinth.com/mod/modmenu), som visar moddens namn och beskrivning på ditt språk

## Bygg det själv

Modden byggs vid varje push till `main` av [GitHub Actions-arbetsflödet](../.github/workflows/build.yml), och JAR-filen finns bland körningens artefakter. En tagg som `v0.0.3` publicerar dessutom en [version](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) med JAR-filen och det `update.json`-manifest som uppdateraren i spelet läser.

## Varifrån koden kommer

- **Ursprung:** projektet växte fram ur detta arkivs ursprungliga prototyp, som jämförde serverpaketets SHA-1 med lokala filer.
- **Författare:** McChopik, Telemine3140. Dagens kod skrevs om och byggdes ut på projektförfattarnas begäran med [Claude Code](https://claude.com/claude-code), ett AI-programmeringsverktyg från [Anthropic](https://www.anthropic.com). Tillagt är nedladdning med ett klick, sammanbuntning, automatisk aktivering, ”avböj och lämna”, uppdateraren, översättningarna, sökvägshantering för Windows, macOS, Linux och Android samt dokumentationen. Kod från andra moddar har inte kopierats.
- **Byggt på:** Minecrafts egen kod med Mojangs officiella namn, [Fabric Loader och Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) och [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Status:** experimentell. Den har inte testats utförligt, så använd den på egen risk.
- **Inte kopplad** till Mojang, Microsoft, Fabric eller Anthropic.

## Licens

Licenstexten nedan återges här på sitt ursprungliga engelska; endast den engelska ordalydelsen är juridiskt bindande, och en översättning av MIT-licensen skulle inte ersätta den.

This is an **open-source** project released under the **[MIT License](../LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Bidrag, felrapporter och pull requests är välkomna: [öppna ett ärende](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
