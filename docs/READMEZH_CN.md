# localpack-bypass

<p>
  <a href="../LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**语言:** [English](../README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · **简体中文** · [繁體中文](READMEZH_TW.md)

**作者:** McChopik, Telemine3140

---

**localpack-bypass** 是一个免费、开源的客户端 [Fabric](https://fabricmc.net) 模组，适用于 [Minecraft](https://www.minecraft.net) 26.2，让服务器资源包不再麻烦。它不再让你每次加入时都重新下载同一个资源包，而是把服务器发送的资源包的 SHA-1 与你 `resourcepacks` 文件夹中已有的 ZIP 文件进行比较。如果找到匹配的资源包，就会静默启用，让你无需确认、无需等待即可加入。如果资源包是新的或已更改，一个界面会让你一次性下载所有需要的资源包；随后模组会校验它们、启用它们、保存选择，并只重新加载一次资源。下载的资源包在你离开服务器后仍像普通资源包一样保持启用，而拒绝则会像原版一样让你断开与服务器的连接。界面和消息会跟随游戏语言；本模组附带 38 种语言的翻译（见上方的语言列表）。

## 工作原理

- **资源包已下载：** 会静默启用。没有提示，也不会下载。
- **新的或已更改的资源包：** 一个带有 **下载所有资源包** 的界面。资源包会保存到 `resourcepacks/localpack-<sha1>.zip`，通过 SHA-1 校验、启用，并保存到你的设置中。
- **拒绝并离开：** 你会与服务器断开连接。
- **较旧的资源包：** 当有更新的版本到来时，模组之前下载的资源包会被取消勾选。文件仍保留在磁盘上，因此随时可以立刻切回。
- **不会无声失败：** 未能完成的下载、不匹配的校验和，或游戏无法启用的文件都会在界面上报告；如果没有界面，则以通知的形式报告。

## 设置（Mod Menu）

安装 [Mod Menu](https://modrinth.com/mod/modmenu) 后，模组会出现在模组列表中，并拥有自己的设置界面。没有添加快捷键；一切都在其中。

- **已下载资源包的文件夹** — 资源包的保存位置。留空则使用游戏自带的 `resourcepacks` 文件夹。任何其他文件夹都可以，包括其他驱动器或 Android 共享文件夹；此时资源包在被启用前也会复制到 `resourcepacks` 中，因为那是游戏本身唯一读取的文件夹。按钮可打开这两个文件夹中的任意一个。
- **记住你已同意过的服务器** — 你已经从中获取过资源包的服务器不会再被询问。该回答也会写入游戏自带的按服务器区分的 *Server Resource Packs* 设置中，位于 Prompt、Enabled 和 Disabled 旁边。
- **保留来自其他服务器的资源包** — 保持开启以保留你已有的资源包。关闭后，每个服务器启动时只带该服务器自己的资源包。
- **询问前先等待** — 在界面出现前收集同一批次其余资源包的时长，以毫秒或秒计。服务器会一次性发送其所有资源包，因此等待能把多个问题合并为一个。
- **放弃前等待** 和 **尝试次数** — 一次下载最多可以花多长时间，以及失败后会重试多少次。校验和不匹配算作一次失败，因为那正是传输只完成一半的样子。
- **保留记录** — 服务器发送的每个资源包都会被记录下来：哪个服务器、哪个地址、校验和、大小、它发生了什么以及花了多长时间。它是你自己电脑上的一个文件；不会发送到任何地方。
- **已下载的资源包** — 模组已下载的资源包，按来源服务器分组，显示每个资源包是开启、关闭还是已从文件夹中消失。按一下即可开启或关闭；文件已消失的资源包可以被遗忘。**全部遗忘** 会清除模组记住的内容，且绝不会删除任何资源包文件。
- **服务器发送的内容** — 记录本身，最新的在前，并带有每个条目的详细信息。

## 更新

模组每天会向 GitHub 查询一次本仓库的[最新版本](https://github.com/evgenmakarickij-wq/localpack-bypass/releases)。如果它比你正在使用的版本更新，标题界面会提示安装。接受后会做三件事：

1. 下载新的 JAR，并根据随该版本发布的 `update.json` 清单**校验其 SHA-256**。
2. 删除旧的 JAR，把新的移入 `mods/`。
3. 你重新启动游戏。

在 Windows 上，正在加载的 JAR 无法删除，因此会生成一段简短的脚本等待游戏退出，替你完成第 2 步；发生这种情况时模组会告诉你。在 Android、Linux 和 macOS 上，正在运行的 JAR 不会被锁定，会直接替换。更新器接触的所有内容都位于 `mods/` 和 `config/localpack-bypass/` 下。

该检查是对 GitHub API 的普通匿名请求（无需令牌、无需账号），模组没有遥测，也没有自己的服务器。跳过的版本会被记住，`-Dlocalpack.bypass.updateCheck=false` 可完全关闭更新。

**版本编号注意事项：** `0.0.1.2026-alpha-build-beta-release` 会被读取为 `[0, 0, 1, 2026]`，排序*低于*较早的 `0.0.3-beta`（`[0, 0, 3]`）。编号为 `0.0.3-beta` 或更高的构建不会自动收到此版本，必须手动替换。

## 要求

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 或更高版本
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows、macOS、Linux 或诸如 [PojavLauncher](https://pojavlauncherteam.github.io/) 之类的 Android 启动器：模组接触的每个文件都从启动器的游戏目录中解析，因此无需手动设置文件夹
- 可选：[Mod Menu](https://modrinth.com/mod/modmenu)，可显示你所用语言的模组名称和描述，并为模组提供一个设置界面

## 自行构建

每次推送到 `main` 时，模组都会由 [GitHub Actions 工作流](../.github/workflows/build.yml)构建，JAR 位于该次运行的构建产物中。推送诸如 `v0.0.3` 的标签还会发布一个[版本](https://github.com/evgenmakarickij-wq/localpack-bypass/releases)，其中包含 JAR 以及游戏内更新器读取的 `update.json` 清单。

## 代码来源

- **起源：** 本项目源自本仓库最初的原型，它会把服务器资源包的 SHA-1 与本地文件进行比对。
- **作者：** McChopik、Telemine3140。当前代码应项目作者的要求，使用 [Anthropic](https://www.anthropic.com) 的 AI 编程工具 [Claude Code](https://claude.com/claude-code) 重写并扩展。新增了单击下载、批量处理、自动启用、“拒绝并离开”、更新器、翻译、覆盖 Windows、macOS、Linux 和 Android 的路径处理，以及文档。没有从其他模组复制代码。
- **构建于：** 使用 Mojang 官方名称的 Minecraft 自身代码、[Fabric Loader 和 Fabric API](https://fabricmc.net)、[Fabric Loom](https://github.com/FabricMC/fabric-loom) 以及 [SpongePowered Mixin](https://github.com/SpongePowered/Mixin)。
- **状态：** 实验性。它未经充分测试，因此请自行承担风险使用。
- 与 Mojang、Microsoft、Fabric 或 Anthropic **无关联**。

## 许可证

下面的许可证文本以英文原文呈现；只有该英文措辞具有法律约束力，MIT 许可证的译文不能取代它。

This is an **open-source** project released under the **[MIT License](../LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

欢迎贡献、错误报告和拉取请求：[提交 issue](https://github.com/evgenmakarickij-wq/localpack-bypass/issues)。
