# localpack-bypass

<p>
  <a href="LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Languages:** **English** · [Українська](docs/READMEUA.md) · [Русский](docs/READMERU.md) · [Беларуская](docs/READMEBE_BY.md) · [Čeština](docs/READMECS_CZ.md) · [Deutsch](docs/READMEDE_DE.md) · [Polski](docs/READMEPL_PL.md) · [Slovenčina](docs/READMESK_SK.md) · [العربية](docs/READMEAR_SA.md) · [Български](docs/READMEBG_BG.md) · [Català](docs/READMECA_ES.md) · [Dansk](docs/READMEDA_DK.md) · [Ελληνικά](docs/READMEEL_GR.md) · [Español](docs/READMEES_ES.md) · [Eesti](docs/READMEET_EE.md) · [Suomi](docs/READMEFI_FI.md) · [Français](docs/READMEFR_FR.md) · [Hrvatski](docs/READMEHR_HR.md) · [Magyar](docs/READMEHU_HU.md) · [Bahasa Indonesia](docs/READMEID_ID.md) · [Íslenska](docs/READMEIS_IS.md) · [Italiano](docs/READMEIT_IT.md) · [日本語](docs/READMEJA_JP.md) · [한국어](docs/READMEKO_KR.md) · [Lietuvių](docs/READMELT_LT.md) · [Latviešu](docs/READMELV_LV.md) · [Nederlands](docs/READMENL_NL.md) · [Norsk](docs/READMENO_NO.md) · [Português (Brasil)](docs/READMEPT_BR.md) · [Română](docs/READMERO_RO.md) · [Slovenščina](docs/READMESL_SI.md) · [Shqip](docs/READMESQ_AL.md) · [Српски](docs/READMESR_SP.md) · [Svenska](docs/READMESV_SE.md) · [Türkçe](docs/READMETR_TR.md) · [Tiếng Việt](docs/READMEVI_VN.md) · [简体中文](docs/READMEZH_CN.md) · [繁體中文](docs/READMEZH_TW.md)

**Authors:** McChopik, Telemine3140

---

**localpack-bypass** is a free, open-source, client-side [Fabric](https://fabricmc.net) mod for [Minecraft](https://www.minecraft.net) 26.2 that caches server resource packs in the normal `resourcepacks` folder. Minecraft keeps full control of consent, required packs, downloading, SHA-1 verification, pack ordering and resource reloads. The mod only seeds Minecraft's verified download cache from matching local ZIP files and archives successful downloads for later use.

## How it works

- **Pack already downloaded:** the local file is copied into Minecraft's server-pack cache and Minecraft verifies its SHA-1 before use. No HTTP download is needed.
- **New or changed pack:** Minecraft downloads it with its normal proxy, headers, 250 MiB limit and checksum validation. The verified result is archived as `resourcepacks/localpack-<sha1>.zip`.
- **Consent and required packs:** the normal per-server Prompt, Enabled and Disabled setting is authoritative. Optional and required packs behave exactly as in vanilla.
- **Pack lifetime:** server packs are removed on `pop` packets and disconnect. UUID replacement, ordering and reload failures are handled by Minecraft.
- **Manual selection stays manual:** archived ZIP files appear in the normal resource-pack screen, but the mod never edits `options.txt` and never turns a manually disabled archive back on.

## Settings (Mod Menu)

With [Mod Menu](https://modrinth.com/mod/modmenu) installed the mod appears in the mod list with a settings screen of its own. No hotkeys were added; everything is there.

- **Folder for downloaded packs** — where an extra archive copy is kept. Leave it empty to use the game's own `resourcepacks` folder. Relative paths are resolved from the game directory.
- **Keep a record** — records which server offered a pack and what happened. URLs, checksums, sizes and diagnostics are stored only when **Also record the details** is enabled.
- **Downloaded packs** — the packs the mod has downloaded, grouped by the server they came from, showing whether each is on, off, or gone from the folder. Press one to switch it on or off; a pack whose file has disappeared can be forgotten. **Forget everything** clears what the mod remembers and never deletes a pack file.
- **What servers sent** — the record itself, newest first, with the details of every entry.

## Requirements

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 or newer
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux or an Android launcher such as [PojavLauncher](https://pojavlauncherteam.github.io/): every file the mod touches is resolved from the launcher's game directory, so no folder has to be set up by hand
- Optional: [Mod Menu](https://modrinth.com/mod/modmenu), which shows the mod name and description in your language and gives the mod its [settings screen](#settings-mod-menu)

## Build

The mod is built by the [GitHub Actions workflow](.github/workflows/build.yml) on every push to `main`, with the jar available under the run's Artifacts. A `v*` tag may publish a release; ordinary commits never do.

## Where the code comes from

- **Origin:** the project started from this repository's original prototype, which matched the SHA-1 of the server's pack against local files.
- **Authors:** McChopik, Telemine3140. The current implementation keeps Minecraft's server-pack protocol intact and adds a persistent local cache around its verified downloader. No code was copied from other mods.
- **Built on:** Minecraft's own code with Mojang's official names, [Fabric Loader and Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) and [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Status:** experimental. It has not been tested extensively, so use it at your own risk.
- **Not affiliated** with Mojang, Microsoft, Fabric or Anthropic.

## License

This is an **open-source** project released under the **[MIT License](LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Contributions, issues and pull requests are welcome: [open an issue](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
