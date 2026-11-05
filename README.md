# localpack-bypass

<p>
  <a href="LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Documentation:** **English** · [Українська](docs/READMEUA.md) · [Русский](docs/READMERU.md). The in-game interface still ships all 38 translations.

**Authors:** McChopik, Telemine3140

---

**localpack-bypass** is a free, open-source, client-side [Fabric](https://fabricmc.net) mod for [Minecraft](https://www.minecraft.net) 26.2 that caches server resource packs in the normal `resourcepacks` folder. Minecraft keeps full control of consent, required packs, downloading, SHA-1 verification, pack ordering and resource reloads. The mod only seeds Minecraft's verified download cache from matching local ZIP files and archives successful downloads for later use.

## How it works

- **Pack already downloaded:** the local file is copied into Minecraft's server-pack cache and Minecraft verifies its SHA-1 before use. No HTTP download is needed.
- **New or changed pack:** Minecraft downloads it with its normal proxy, headers, 250 MiB limit and checksum validation. The verified result is archived as `resourcepacks/localpack-<sha1>.zip`.
- **Consent and required packs:** the normal per-server Prompt, Enabled and Disabled setting is authoritative. Optional and required packs behave exactly as in vanilla.
- **Pack lifetime:** after Minecraft successfully applies a pack once, its archived copy stays enabled after disconnect. Session UUID replacement, ordering and reload failures remain vanilla-owned.
- **Manual selection stays manual:** the first successful application is persisted, but if the player later disables that archive, the mod records the choice and never turns it back on automatically.

## Settings (Mod Menu)

With [Mod Menu](https://modrinth.com/mod/modmenu) installed the mod appears in the mod list with a settings screen of its own. No hotkeys were added; everything is there.

- **Folder for downloaded packs** — where an extra archive copy is kept. Leave it empty to use the game's own `resourcepacks` folder. Relative paths are resolved from the game directory.
- **Parallel downloads and total speed** — several packs are transferred together while every worker shares one aggregate bandwidth limit. Lower the limit if downloading affects ping.
- **Keep a record** — records which server offered a pack and what happened. URLs, checksums, sizes and diagnostics are stored only when **Also record the details** is enabled.
- **Downloaded packs** — packs grouped by server, with manual on/off controls. **Delete all downloaded** deselects and removes every mod-owned archive and matching Minecraft cache file without restarting the game.
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
- **Built on:** Minecraft's own code with Mojang's official names, [Fabric Loader and Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom), [SpongePowered Mixin](https://github.com/SpongePowered/Mixin), and the embedded [ModuLib](https://github.com/mcchopik/modulib) utilities.
- **Status:** experimental. It has not been tested extensively, so use it at your own risk.
- **Not affiliated** with Mojang, Microsoft, Fabric or Anthropic.

## License

This is an **open-source** project released under the **[MIT License](LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Contributions, issues and pull requests are welcome: [open an issue](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
