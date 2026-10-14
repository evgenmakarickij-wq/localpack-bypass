# localpack-bypass

<p>
  <a href="LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Languages:** **English** · [Українська](READMEUA.md) · [Русский](READMERU.md)

**Authors:** McChopik, Telemine3140

---

**localpack-bypass** is a free, open-source, client-side [Fabric](https://fabricmc.net) mod for [Minecraft](https://www.minecraft.net) 26.2 that makes server resource packs painless. Instead of downloading the same pack every time you join, it compares the SHA-1 of the pack the server sends with the ZIP files already in your `resourcepacks` folder. If a matching pack is found, it is enabled silently, so you join without prompts or waiting. If a pack is new or has changed, a single screen offers to download all the needed packs at once; the mod then verifies them, enables them, saves the selection and reloads resources only once. Downloaded packs stay active after you leave the server, like normal resource packs, while declining disconnects you from the server, like in vanilla. The screen and messages follow the game language: English, Deutsch, Polski, Čeština, Slovenčina, Беларуская, Русский and Українська.

## How it works

- **Pack already downloaded:** it is enabled silently. No prompt, no download.
- **New or changed pack:** one screen with **Download all resource packs**. Packs are saved to `resourcepacks/localpack-<sha1>.zip`, verified by SHA-1, enabled and saved to your options.
- **Decline and leave:** you are disconnected from the server.
- **Older packs:** packs previously downloaded by the mod are deselected when a newer version arrives. The files stay on disk, so going back is instant.

## Updates

Once a day the mod asks GitHub for the [latest release](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) of this repository. If it is newer than the version you are running, the title screen offers to install it. Accepting does three things:

1. The new jar is downloaded and its **SHA-256 is checked** against the `update.json` manifest published with that release.
2. The old jar is deleted and the new one is moved into `mods/`.
3. You restart the game.

On Windows a jar that is currently loaded cannot be deleted, so a short generated script waits for the game to exit and performs step 2 for you; the mod says so when it happens. On Android, Linux and macOS the running jar is not locked and is simply replaced. Everything the updater touches is under `mods/` and `config/localpack-bypass/`.

The check is a plain anonymous GitHub API request (no token, no account), the mod has no telemetry and no server of its own. Skipping a version is remembered, and `-Dlocalpack.bypass.updateCheck=false` disables updates entirely.

## Requirements

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 or newer
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux or an Android launcher such as [PojavLauncher](https://pojavlauncherteam.github.io/): every file the mod touches is resolved from the launcher's game directory, so no folder has to be set up by hand
- Optional: [Mod Menu](https://modrinth.com/mod/modmenu), which shows the mod name and description in your language

## Build

The mod is built by the [GitHub Actions workflow](.github/workflows/build.yml) on every push to `main`, with the jar available under the run's Artifacts. Push a tag such as `v0.0.2` to also publish a [release](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) with the jar and the `update.json` manifest the in-game updater reads.

## Where the code comes from

- **Origin:** the project started from this repository's original prototype, which matched the SHA-1 of the server's pack against local files.
- **Authors:** McChopik, Telemine3140. The current code was rewritten and extended with [Claude Code](https://claude.com/claude-code), an AI coding tool by [Anthropic](https://www.anthropic.com), at the request of the project's authors. It added the one-click download, batching, automatic enabling, "decline and leave", the updater, the translations, path handling that covers Windows, macOS, Linux and Android, and the documentation. No code was copied from other mods.
- **Built on:** Minecraft's own code with Mojang's official names, [Fabric Loader and Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) and [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Status:** experimental. It has not been tested extensively, so use it at your own risk.
- **Not affiliated** with Mojang, Microsoft, Fabric or Anthropic.

## License

This is an **open-source** project released under the **[MIT License](LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Contributions, issues and pull requests are welcome: [open an issue](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
