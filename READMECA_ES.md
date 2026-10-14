# localpack-bypass

<p>
  <a href="LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Idiomes:** [English](README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · **Català** · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Autors:** McChopik, Telemine3140

---

**localpack-bypass** és un mod [Fabric](https://fabricmc.net) gratuït, de codi obert i per al client per a [Minecraft](https://www.minecraft.net) 26.2 que fa que els paquets de recursos dels servidors siguin una cosa senzilla. En lloc de tornar a descarregar el mateix paquet cada vegada que et connectes, compara el SHA-1 del paquet que envia el servidor amb els fitxers ZIP que ja hi ha a la carpeta `resourcepacks`. Si es troba un paquet coincident, s'activa silenciosament, de manera que et connectes sense preguntes ni esperes. Si un paquet és nou o ha canviat, una sola pantalla ofereix descarregar tots els paquets necessaris alhora; després el mod els verifica, els activa, desa la selecció i recarrega els recursos només una vegada. Els paquets descarregats romanen actius després de sortir del servidor, com els paquets de recursos normals, mentre que rebutjar-ho et desconnecta del servidor com en el joc original. Les pantalles i els missatges segueixen l'idioma del joc; el mod inclou traduccions per a 38 idiomes (consulta la llista d'idiomes de dalt).

## Com funciona

- **Paquet ja descarregat:** s'activa silenciosament. Sense preguntes, sense descàrregues.
- **Paquet nou o modificat:** una pantalla amb **Descarrega tots els paquets de recursos**. Els paquets es desen a `resourcepacks/localpack-<sha1>.zip`, es verifiquen per SHA-1, s'activen i es desen a les teves opcions.
- **Rebutjar i sortir:** et desconnecta del servidor.
- **Paquets més antics:** els paquets descarregats prèviament pel mod es desmarquen quan arriba una versió més nova. Els fitxers romanen al disc, així que pots tornar enrere immediatament.

## Actualitzacions

Un cop al dia, el mod pregunta a GitHub per la [versió més recent](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) d'aquest repositori. Si és més nova que la versió que utilitzes, la pantalla de títol ofereix instal·lar-la. Si acceptes, passen tres coses:

1. Es descarrega el fitxer JAR nou i **es verifica el seu SHA-256** contra el manifest `update.json` publicat amb aquesta versió.
2. Es suprimeix el fitxer JAR vell i el nou es mou a `mods/`.
3. Reinicies el joc.

A Windows no es pot suprimir un fitxer JAR que està carregat en aquell moment, així que un petit script generat espera que es tanqui el joc i fa el pas 2 per tu; el mod t'ho indica quan passa. A Android, Linux i macOS el fitxer JAR en execució no està blocat i simplement se substitueix. Tot el que toca l'actualitzador és a `mods/` i `config/localpack-bypass/`.

La consulta és una petició anònima normal a l'API de GitHub (sense testimoni ni compte); el mod no té telemetria ni servidor propi. Una versió omesa es recorda, i `-Dlocalpack.bypass.updateCheck=false` desactiva completament les actualitzacions.

## Requisits

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 o superior
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux o un llançador d'Android com ara [PojavLauncher](https://pojavlauncherteam.github.io/): cada fitxer que toca el mod es determina a partir del directori de joc del llançador, així que no cal configurar cap carpeta a mà
- Opcional: [Mod Menu](https://modrinth.com/mod/modmenu), que mostra el nom i la descripció del mod en el teu idioma

## Compilar-ho tu mateix

El mod es compila a cada push a `main` mitjançant el [flux de treball de GitHub Actions](.github/workflows/build.yml), i el fitxer JAR es troba entre els artefactes de l'execució. Una etiqueta com `v0.0.3` publica a més una [versió](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) amb el fitxer JAR i el manifest `update.json` que l'actualitzador llegeix dins del joc.

## D'on ve el codi

- **Origen:** el projecte va néixer del prototip original d'aquest repositori, que comparava el SHA-1 del paquet del servidor amb fitxers locals.
- **Autors:** McChopik, Telemine3140. El codi actual es va reescriure i ampliar amb [Claude Code](https://claude.com/claude-code), una eina de programació amb IA d'[Anthropic](https://www.anthropic.com), a petició dels autors del projecte. S'hi han afegit la descàrrega amb un clic, l'agrupació, l'activació automàtica, „rebutjar i sortir“, l'actualitzador, les traduccions, la gestió de rutes per a Windows, macOS, Linux i Android, i la documentació. No s'ha copiat codi d'altres mods.
- **Construït sobre:** el codi propi de Minecraft amb els noms oficials de Mojang, [Fabric Loader i Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) i [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Estat:** experimental. No s'ha provat a fons, així que utilitza'l sota la teva responsabilitat.
- **No està afiliat** a Mojang, Microsoft, Fabric ni Anthropic.

## Llicència

El text de la llicència que hi ha a continuació es reprodueix aquí en el seu anglès original; l'única redacció legalment vinculant és aquesta en anglès, i una traducció de la llicència MIT no la substituiria.

This is an **open-source** project released under the **[MIT License](LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Les contribucions, els informes d'errors i les pull request són benvinguts: [obre una issue](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
