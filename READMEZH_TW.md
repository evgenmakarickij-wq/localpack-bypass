# localpack-bypass

<p>
  <a href="LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**語言:** [English](README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · **繁體中文**

**作者:** McChopik, Telemine3140

---

**localpack-bypass** 是一個免費、開源的用戶端 [Fabric](https://fabricmc.net) 模組，適用於 [Minecraft](https://www.minecraft.net) 26.2，讓伺服器資源包不再麻煩。它不再讓你每次加入時都重新下載同一個資源包，而是把伺服器傳送的資源包的 SHA-1 與你 `resourcepacks` 資料夾中既有的 ZIP 檔案進行比對。如果找到相符的資源包，就會靜默啟用，讓你無須確認、無須等待即可加入。如果資源包是新的或已變更，一個畫面會讓你一次下載所有需要的資源包；接著模組會驗證它們、啟用它們、儲存選擇，並只重新載入一次資源。下載的資源包在你離開伺服器後仍像一般資源包一樣保持啟用，而拒絕則會像原版一樣讓你與伺服器中斷連線。畫面與訊息會跟隨遊戲語言；本模組附帶 38 種語言的翻譯（請見上方的語言清單）。

## 運作方式

- **資源包已下載：** 會靜默啟用。沒有提示，也不會下載。
- **新的或已變更的資源包：** 一個帶有 **下載所有資源包** 的畫面。資源包會儲存到 `resourcepacks/localpack-<sha1>.zip`，經 SHA-1 驗證、啟用，並儲存到你的設定中。
- **拒絕並離開：** 你會與伺服器中斷連線。
- **較舊的資源包：** 當有更新的版本到來時，模組先前下載的資源包會被取消勾選。檔案仍保留在磁碟上，因此隨時可以立刻切換回來。

## 更新

模組每天會向 GitHub 查詢一次本儲存庫的[最新版本](https://github.com/evgenmakarickij-wq/localpack-bypass/releases)。如果它比你正在使用的版本更新，標題畫面會提示安裝。接受後會做三件事：

1. 下載新的 JAR，並依據隨該版本發布的 `update.json` 清單**檢查其 SHA-256**。
2. 刪除舊的 JAR，把新的移入 `mods/`。
3. 你重新啟動遊戲。

在 Windows 上，正在載入的 JAR 無法刪除，因此會產生一段簡短的指令碼等待遊戲結束，替你完成第 2 步；發生這種情況時模組會告訴你。在 Android、Linux 與 macOS 上，正在執行的 JAR 不會被鎖定，會直接取代。更新器所接觸的所有內容都位於 `mods/` 與 `config/localpack-bypass/` 之下。

這項檢查是對 GitHub API 的一般匿名要求（不需要權杖、不需要帳號），模組沒有遙測，也沒有自己的伺服器。略過的版本會被記住，`-Dlocalpack.bypass.updateCheck=false` 可完全停用更新。

## 需求

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 或更新版本
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows、macOS、Linux 或諸如 [PojavLauncher](https://pojavlauncherteam.github.io/) 之類的 Android 啟動器：模組接觸的每個檔案都從啟動器的遊戲目錄解析，因此無須手動設定資料夾
- 選用：[Mod Menu](https://modrinth.com/mod/modmenu)，可顯示你所用語言的模組名稱與說明

## 自行建置

每次推送到 `main` 時，模組都會由 [GitHub Actions 工作流程](.github/workflows/build.yml)建置，JAR 位於該次執行的成品中。推送諸如 `v0.0.3` 的標籤還會發布一個[版本](https://github.com/evgenmakarickij-wq/localpack-bypass/releases)，其中包含 JAR 以及遊戲內更新器所讀取的 `update.json` 清單。

## 程式碼來源

- **起源：** 本專案源自本儲存庫最初的雛型，它會把伺服器資源包的 SHA-1 與本機檔案進行比對。
- **作者：** McChopik、Telemine3140。目前的程式碼應專案作者的要求，使用 [Anthropic](https://www.anthropic.com) 的 AI 程式設計工具 [Claude Code](https://claude.com/claude-code) 重寫並擴充。新增了單鍵下載、批次處理、自動啟用、「拒絕並離開」、更新器、翻譯、涵蓋 Windows、macOS、Linux 與 Android 的路徑處理，以及文件。沒有從其他模組複製程式碼。
- **建構於：** 使用 Mojang 官方名稱的 Minecraft 自身程式碼、[Fabric Loader 與 Fabric API](https://fabricmc.net)、[Fabric Loom](https://github.com/FabricMC/fabric-loom) 以及 [SpongePowered Mixin](https://github.com/SpongePowered/Mixin)。
- **狀態：** 實驗性。它未經充分測試，因此請自行承擔風險使用。
- 與 Mojang、Microsoft、Fabric 或 Anthropic **無關**。

## 授權條款

下方的授權條款文字以英文原文呈現；只有該英文措辭具有法律約束力，MIT 授權條款的譯文無法取代它。

This is an **open-source** project released under the **[MIT License](LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

歡迎貢獻、錯誤回報與提取要求：[開啟 issue](https://github.com/evgenmakarickij-wq/localpack-bypass/issues)。
