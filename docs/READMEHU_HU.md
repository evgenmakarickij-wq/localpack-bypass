# localpack-bypass

<p>
  <a href="../LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Nyelvek:** [English](../README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · **Magyar** · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Szerzők:** McChopik, Telemine3140

---

**localpack-bypass** egy ingyenes, nyílt forráskódú, kliensoldali [Fabric](https://fabricmc.net) mod a [Minecraft](https://www.minecraft.net) 26.2 verziójához, amely fájdalommentessé teszi a szerver erőforráscsomagjait. Ahelyett, hogy minden csatlakozáskor újra letöltené ugyanazt a csomagot, összehasonlítja a szerver által küldött csomag SHA-1-jét a `resourcepacks` mappában már meglévő ZIP-fájlokkal. Ha egyező csomagot talál, csendben aktiválja, így kérdések és várakozás nélkül csatlakozol. Ha egy csomag új vagy megváltozott, egyetlen képernyő kínálja fel az összes szükséges csomag egyidejű letöltését; utána a mod ellenőrzi, aktiválja, elmenti a kiválasztást, és csak egyszer tölti újra az erőforrásokat. A letöltött csomagok a szerver elhagyása után is aktívak maradnak, mint a rendes erőforráscsomagok, míg az elutasítás a vanilla játékhoz hasonlóan lecsatlakoztat a szerverről. A képernyők és üzenetek a játék nyelvét követik; a mod 38 nyelvhez tartalmaz fordítást (lásd a fenti listát).

## Hogyan működik

- **A csomag már le van töltve:** csendben aktiválódik. Nincs kérdés, nincs letöltés.
- **Új vagy módosított csomag:** egy képernyő az **Az összes erőforráscsomag letöltése** lehetőséggel. A csomagok a `resourcepacks/localpack-<sha1>.zip` helyre kerülnek, SHA-1-gyel ellenőrződnek, aktiválódnak, és elmentődnek a beállításaidba.
- **Elutasítás és kilépés:** lecsatlakozol a szerverről.
- **Régebbi csomagok:** a mod által korábban letöltött csomagok kijelölése megszűnik, amikor újabb verzió érkezik. A fájlok a lemezen maradnak, így a visszaváltás azonnal lehetséges.
- **Semmi sem hibázik csendben:** egy letöltés, amely nem működött, egy ellenőrzőösszeg, amely nem egyezett, vagy egy fájl, amelyet a játék nem volt hajlandó aktiválni, a képernyőn jelenik meg, vagy értesítésként, ha nincs elérhető képernyő.

## Beállítások (Mod Menu)

Ha a [Mod Menu](https://modrinth.com/mod/modmenu) telepítve van, a mod megjelenik a modlistán a saját beállításképernyőjével. Billentyűparancsokat nem adtunk hozzá; minden ott van.

- **Letöltött csomagok mappája** — hol tárolódnak a csomagok. Hagyd üresen a játék saját `resourcepacks` mappájához. Bármely más mappa működik, beleértve egy másik meghajtót vagy egy Android megosztott mappát; a csomagok ilyenkor a `resourcepacks` mappába is átmásolódnak, mielőtt aktiválnák őket, mert ez az egyetlen mappa, amelyet maga a játék olvas. A gombok bármelyik mappát megnyitják.
- **Jegyezd meg az elfogadott szervereket** — egy szervert, amelyről már vettél csomagokat, nem kérdez meg újra. A válasz a játék saját, szerverenkénti *Server Resource Packs* beállításába is beíródik, a Prompt, Enabled és Disabled mellett.
- **Tartsd meg a más szerverekről származó csomagokat** — hagyd bekapcsolva, hogy megtartsd a már meglévő csomagjaidat. Kapcsold ki, hogy minden szervert csak az adott szerver csomagjaival kezdj.
- **Várj a kérdezés előtt** — mennyi ideig gyűjtse egy köteg többi részét, mielőtt a képernyő megjelenik, ezredmásodpercben vagy másodpercben. A szerverek egyszerre küldik az összes csomagjukat, így a várakozás több kérdést egyetlen kérdéssé alakít.
- **Add fel ennyi után** és **Próbálkozások** — mennyi ideig tarthat egy letöltés, és egy sikertelen hányszor próbálódik újra. A nem egyező ellenőrzőösszeg kudarcnak számít, mert a félig kész átvitel így néz ki.
- **Vezess nyilvántartást** — minden erőforráscsomag, amelyet egy szerver küld, feljegyzésre kerül: melyik szerver, melyik cím, az ellenőrzőösszeg, a méret, mi történt vele és mennyi ideig tartott. Ez egy fájl a saját számítógépeden; semmi sem kerül elküldésre sehova.
- **Letöltött csomagok** — a mod által letöltött csomagok, a származási szerverük szerint csoportosítva, jelezve, hogy az egyes csomagok be vannak-e kapcsolva, ki vannak-e kapcsolva, vagy eltűntek-e a mappából. Nyomj meg egyet a be- vagy kikapcsolásához; egy csomag, amelynek a fájlja eltűnt, elfelejthető. **Minden elfelejtése** törli, amire a mod emlékszik, és soha nem törli a csomagfájlt.
- **Mit küldtek a szerverek** — maga a nyilvántartás, a legújabbak elöl, minden bejegyzés részleteivel.

## Frissítések

A mod naponta egyszer megkérdezi a GitHubot erről a tárolóról szóló [legújabb verzióról](https://github.com/evgenmakarickij-wq/localpack-bypass/releases). Ha újabb, mint az általad használt verzió, a nyitóképernyő felkínálja a telepítését. Az elfogadás három dolgot tesz:

1. Letöltődik az új JAR-fájl, és a **SHA-256 ellenőrzése megtörténik** az adott verzióval kiadott `update.json` jegyzékkel szemben.
2. A régi JAR-fájl törlődik, az új pedig a `mods/` mappába kerül.
3. Újraindítod a játékot.

Windows alatt egy éppen betöltött JAR-fájl nem törölhető, ezért egy rövid, generált szkript megvárja a játék bezárását, és elvégzi helyetted a 2. lépést; erről a mod értesít, amikor ez megtörténik. Androidon, Linuxon és macOS-en a futó JAR-fájl nincs zárolva, és egyszerűen lecserélődik. Minden, amihez a frissítő hozzányúl, a `mods/` és a `config/localpack-bypass/` alatt található.

A lekérdezés egy szokásos névtelen kérés a GitHub API felé (nincs token, nincs fiók), a modnak nincs telemetriája és saját szervere. A kihagyott verzió megjegyződik, és a `-Dlocalpack.bypass.updateCheck=false` teljesen kikapcsolja a frissítéseket.

**Figyelmeztetés a verziószámozásra:** a `0.0.1.2026-alpha-build-beta-release` `[0, 0, 1, 2026]`-ként olvasandó, ami a korábbi `0.0.3-beta` (`[0, 0, 3]`) *alá* sorolódik. A `0.0.3-beta` vagy magasabb számmal jelölt verziók nem kapják meg ezt automatikusan, és kézzel kell lecserélni őket.

## Követelmények

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 vagy újabb
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux vagy egy Android-indító, például a [PojavLauncher](https://pojavlauncherteam.github.io/): minden fájl, amihez a mod hozzányúl, az indító játékkönyvtárából határozódik meg, így nem kell kézzel mappát beállítani
- Nem kötelező: [Mod Menu](https://modrinth.com/mod/modmenu), amely a mod nevét és leírását a te nyelveden jeleníti meg, és beállításképernyőt ad a modnak

## Építés saját kezűleg

A modot minden `main` ágra történő push alkalmával a [GitHub Actions munkafolyamat](../.github/workflows/build.yml) építi meg, a JAR-fájl pedig a futás artefaktumai között található. Egy `v0.0.3`-hoz hasonló címke emellett kiad egy [verziót](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) a JAR-fájllal és azzal az `update.json` jegyzékkel, amelyet a játékban a frissítő olvas.

## Honnan származik a kód

- **Eredet:** a projekt e tároló eredeti prototípusából született, amely a szervercsomag SHA-1-jét hasonlította össze a helyi fájlokkal.
- **Szerzők:** McChopik, Telemine3140. A mai kódot a projektszerzők kérésére az [Anthropic](https://www.anthropic.com) mesterségesintelligencia-programozóeszközével, a [Claude Code](https://claude.com/claude-code)-dal írták újra és bővítették. Hozzákerült az egykattintásos letöltés, a csomagolás, az automatikus aktiválás, az „elutasítás és kilépés”, a frissítő, a fordítások, a Windows, macOS, Linux és Android útvonalkezelése, valamint a dokumentáció. Más modokból nem másoltak kódot.
- **Amire épül:** a Minecraft saját kódja Mojang hivatalos neveivel, a [Fabric Loader és a Fabric API](https://fabricmc.net), a [Fabric Loom](https://github.com/FabricMC/fabric-loom) és a [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Állapot:** kísérleti. Nem lett széles körben tesztelve, ezért saját felelősségre használd.
- **Nincs kapcsolatban** a Mojanggal, a Microsofttal, a Fabrickel vagy az Anthropickal.

## Licenc

Az alábbi licencszöveg eredeti angol nyelvén áll itt; jogilag kizárólag ez az angol szöveg kötelező érvényű, és az MIT licenc fordítása nem helyettesítené azt.

This is an **open-source** project released under the **[MIT License](../LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

A hozzájárulások, hibajelentések és pull requestek szívesen látottak: [nyiss egy issue-t](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
