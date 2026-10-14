# localpack-bypass

<p>
  <a href="../LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Jazyky:** [English](../README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · **Čeština** · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Autoři:** McChopik, Telemine3140

---

**localpack-bypass** je zdarma dostupný, open-source, klientský mod [Fabric](https://fabricmc.net) pro [Minecraft](https://www.minecraft.net) 26.2, který dělá serverové balíčky zdrojů bezbolestnými. Místo opětovného stahování stejného balíčku při každém připojení porovná SHA-1 balíčku, který server posílá, se soubory ZIP, které už leží ve složce `resourcepacks`. Pokud najde odpovídající balíček, tiše ho zapne, takže se připojíš bez dotazů a bez čekání. Je-li balíček nový nebo se změnil, jedna obrazovka nabídne stáhnout všechny potřebné balíčky najednou; mod je poté ověří, zapne, uloží volbu a znovu načte zdroje jen jednou. Stažené balíčky zůstanou po opuštění serveru aktivní, jako běžné balíčky zdrojů, zatímco odmítnutí tě stejně jako ve vanilkové hře odpojí od serveru. Obrazovky a zprávy se řídí jazykem hry; mod přináší překlady pro 38 jazyků (viz seznam výše).

## Jak to funguje

- **Balíček už stažen:** tiše se zapne. Žádný dotaz, žádné stahování.
- **Nový nebo změněný balíček:** obrazovka s **Stáhnout všechny balíčky zdrojů**. Balíčky se ukládají do `resourcepacks/localpack-<sha1>.zip`, ověří se pomocí SHA-1, zapnou se a uloží do tvých nastavení.
- **Odmítnout a odejít:** budeš odpojen od serveru.
- **Starší balíčky:** dříve modem stažené balíčky se odznačí, když dorazí novější verze. Soubory zůstávají na disku, takže návrat je okamžitý.

## Aktualizace

Jednou denně se mod dotáže GitHubu na [nejnovější verzi](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) tohoto repozitáře. Je-li novější než verze, kterou používáš, úvodní obrazovka nabídne její instalaci. Souhlas udělá tři věci:

1. Stáhne se nový soubor JAR a **ověří se jeho SHA-256**, a to proti manifestu `update.json`, který se s touto verzí zveřejňuje.
2. Starý soubor JAR se smaže a nový se přesune do `mods/`.
3. Restartuješ hru.

Ve Windows nelze smazat soubor JAR, který je právě načten, proto krátký vygenerovaný skript počká na ukončení hry a provede za tebe krok 2; mod ti to oznámí, až se to stane. Na Androidu, Linuxu a macOS není běžící soubor JAR uzamčen a jednoduše se nahradí. Vše, čeho se aktualizátor dotkne, leží v `mods/` a `config/localpack-bypass/`.

Dotaz je obyčejný anonymní požadavek na API GitHubu (žádný token, žádný účet), mod nemá žádnou telemetrii ani vlastní server. Přeskočená verze se zapamatuje a `-Dlocalpack.bypass.updateCheck=false` aktualizace úplně vypne.

## Požadavky

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 nebo novější
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux nebo androidový launcher jako [PojavLauncher](https://pojavlauncherteam.github.io/): každý soubor, kterého se mod dotkne, se zjistí z adresáře hry launcheru, takže není třeba ručně nastavovat žádnou složku
- Volitelné: [Mod Menu](https://modrinth.com/mod/modmenu), které zobrazuje název a popis modu v tvém jazyce

## Sestavení vlastními silami

Mod se sestavuje při každém pushi do `main` pomocí [workflow GitHub Actions](../.github/workflows/build.yml), soubor JAR leží mezi artefakty běhu. Značka jako `v0.0.3` navíc zveřejní [verzi](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) se souborem JAR a manifestem `update.json`, který aktualizátor ve hře čte.

## Odkud pochází kód

- **Původ:** projekt vznikl z původního prototypu tohoto repozitáře, který porovnával SHA-1 serverového balíčku s místními soubory.
- **Autoři:** McChopik, Telemine3140. Současný kód byl znovu napsán a rozšířen pomocí [Claude Code](https://claude.com/claude-code), nástroje umělé inteligence pro programování od [Anthropic](https://www.anthropic.com), na přání autorů projektu. Přibylo stahování jedním kliknutím, dávkování, automatické zapínání, „odmítnout a odejít“, aktualizátor, překlady, práce s cestami pro Windows, macOS, Linux a Android a dokumentace. Kód z jiných modů nebyl zkopírován.
- **Postaveno na:** vlastním kódu Minecraftu s oficiálními názvy od Mojang, [Fabric Loader a Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) a [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Stav:** experimentální. Není důkladně otestován, používej ho tedy na vlastní riziko.
- **Není spojen** s Mojang, Microsoft, Fabric ani Anthropic.

## Licence

Licenční text níže je zde uveden v původním anglickém znění; právně závazné je pouze toto anglické znění, překlad licence MIT by je nenahradil.

This is an **open-source** project released under the **[MIT License](../LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Příspěvky, hlášení chyb a pull requesty jsou vítány: [otevři issue](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
