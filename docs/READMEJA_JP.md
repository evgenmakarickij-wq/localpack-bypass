# localpack-bypass

<p>
  <a href="../LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**言語:** [English](../README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · **日本語** · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**作者:** McChopik, Telemine3140

---

**localpack-bypass** は、サーバーのリソースパックを手間なく扱えるようにする、[Minecraft](https://www.minecraft.net) 26.2 向けの無料・オープンソースのクライアントサイド [Fabric](https://fabricmc.net) Mod です。参加するたびに同じパックを再ダウンロードする代わりに、サーバーが送信するパックの SHA-1 を、`resourcepacks` フォルダーにある ZIP ファイルと比較します。一致するパックが見つかれば通知なしで有効化されるため、確認も待ち時間もなく参加できます。パックが新規または変更されている場合は、1 つの画面で必要なパックをまとめてダウンロードできます。その後 Mod が検証・有効化し、選択を保存して、リソースの再読み込みを 1 回だけ行います。ダウンロードしたパックは、通常のリソースパックと同様にサーバーを退出した後も有効なままです。一方、拒否するとバニラと同様にサーバーから切断されます。画面とメッセージはゲームの言語に従います。Mod には 38 の言語の翻訳が含まれています（上の言語一覧を参照）。

## 仕組み

- **パックがダウンロード済みの場合:** 通知なしで有効化されます。確認もダウンロードもありません。
- **新規または変更されたパックの場合:** **すべてのリソースパックをダウンロード** がある画面が 1 つ表示されます。パックは `resourcepacks/localpack-<sha1>.zip` に保存され、SHA-1 で検証・有効化され、オプションに保存されます。
- **拒否して退出:** サーバーから切断されます。
- **古いパック:** Mod が以前ダウンロードしたパックは、新しいバージョンが届くと選択が解除されます。ファイルはディスクに残るため、いつでもすぐに戻せます。
- **何も黙って失敗しない:** うまくいかなかったダウンロード、一致しなかったチェックサム、ゲームが有効化しなかったファイルは、画面上に、または画面を利用できない場合は通知として報告されます。

## 設定 (Mod Menu)

[Mod Menu](https://modrinth.com/mod/modmenu) がインストールされていると、Mod は Mod 一覧に表示され、独自の設定画面を持ちます。ホットキーは追加されておらず、すべてそこにあります。

- **ダウンロードしたパックのフォルダー** — パックが保存される場所です。空のままにすると、ゲーム自身の `resourcepacks` フォルダーが使われます。別のドライブや Android の共有フォルダーなど、他のフォルダーでもかまいません。その場合、パックは有効化される前に `resourcepacks` にもコピーされます。ゲーム自身が読むのはそのフォルダーだけだからです。ボタンでどちらのフォルダーでも開けます。
- **「はい」と言ったサーバーを記憶する** — すでにパックを受け取ったことのあるサーバーには、再び確認されません。その回答は、ゲーム自身のサーバーごとの *サーバーリソースパック* 設定にも、「確認」「有効」「無効」の隣に書き込まれます。
- **他のサーバーのパックを保持する** — これをオンにすると、すでにあるパックを保持します。オフにすると、各サーバーをそのサーバーのパックだけで開始します。
- **確認前に待機する** — 画面が表示される前にバッチの残りを集める時間を、ミリ秒または秒で指定します。サーバーはすべてのパックを一度に送るので、待つことで複数の質問が 1 つになります。
- **あきらめるまでの時間** と **試行回数** — 1 回のダウンロードに許される時間と、失敗したダウンロードを再試行する回数です。一致しないチェックサムは失敗とみなされます。途中で終わった転送はそう見えるからです。
- **記録を保持する** — サーバーが送るすべてのリソースパックが記録されます。どのサーバー、どのアドレス、チェックサム、サイズ、どうなったか、どれだけかかったかです。それは自分のコンピューター上のファイルで、どこにも送信されません。
- **ダウンロード済みのパック** — Mod がダウンロードしたパックを、取得元のサーバーごとにまとめ、それぞれがオンか、オフか、フォルダーから消えているかを示します。1 つ押すとオンまたはオフになります。ファイルが消えたパックは忘れることができます。**すべて忘れる** は Mod が記憶している内容を消去し、パックのファイルを決して削除しません。
- **サーバーが送ったもの** — 記録そのもので、新しい順に、すべての項目の詳細が付きます。

## アップデート

Mod は 1 日 1 回、このリポジトリの[最新リリース](https://github.com/evgenmakarickij-wq/localpack-bypass/releases)を GitHub に問い合わせます。使用中のバージョンより新しい場合は、タイトル画面でインストールを提案します。承諾すると次の 3 つが行われます:

1. 新しい JAR がダウンロードされ、そのリリースで公開される `update.json` マニフェストに対して **SHA-256 が検証されます**。
2. 古い JAR が削除され、新しい JAR が `mods/` に移動されます。
3. ゲームを再起動します。

Windows では、読み込まれている JAR を削除できないため、短い生成スクリプトがゲームの終了を待って手順 2 を実行します。その場合は Mod が知らせます。Android、Linux、macOS では、実行中の JAR はロックされないため、そのまま置き換えられます。アップデーターが触れるものはすべて `mods/` と `config/localpack-bypass/` の下にあります。

この確認は GitHub API への通常の匿名リクエストです（トークンもアカウントも不要）。Mod にはテレメトリーも独自のサーバーもありません。スキップしたバージョンは記憶され、`-Dlocalpack.bypass.updateCheck=false` でアップデートを完全に無効にできます。

**バージョン番号に関する注意:** `0.0.1.2026-alpha-build-beta-release` は `[0, 0, 1, 2026]` として読まれ、以前の `0.0.3-beta`（`[0, 0, 3]`）より *下* に並びます。`0.0.3-beta` 以上の番号のビルドには、これが自動的に提供されず、手動で置き換える必要があります。

## 必要条件

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 以降
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows、macOS、Linux、または [PojavLauncher](https://pojavlauncherteam.github.io/) などの Android ランチャー: Mod が触れるすべてのファイルはランチャーのゲームディレクトリから解決されるため、フォルダーを手動で設定する必要はありません
- 任意: [Mod Menu](https://modrinth.com/mod/modmenu)。Mod の名前と説明をあなたの言語で表示し、Mod に設定画面を追加します

## 自分でビルドする

Mod は `main` へのプッシュごとに [GitHub Actions ワークフロー](../.github/workflows/build.yml) によってビルドされ、JAR はその実行のアーティファクトにあります。`v0.0.3` のようなタグをプッシュすると、JAR と、ゲーム内アップデーターが読む `update.json` マニフェストを含む[リリース](https://github.com/evgenmakarickij-wq/localpack-bypass/releases)も公開されます。

## コードの由来

- **起源:** このプロジェクトは、サーバーのパックの SHA-1 をローカルファイルと比較していた、このリポジトリの元のプロトタイプから始まりました。
- **作者:** McChopik、Telemine3140。現在のコードは、プロジェクトの作者の要望により、[Anthropic](https://www.anthropic.com) の AI コーディングツール [Claude Code](https://claude.com/claude-code) を使って書き直され、拡張されました。ワンクリックのダウンロード、バッチ処理、自動有効化、「拒否して退出」、アップデーター、翻訳、Windows・macOS・Linux・Android に対応するパス処理、そしてドキュメントが追加されました。他の Mod からコードをコピーしてはいません。
- **基盤:** Mojang の公式名を付けた Minecraft 本体のコード、[Fabric Loader と Fabric API](https://fabricmc.net)、[Fabric Loom](https://github.com/FabricMC/fabric-loom)、[SpongePowered Mixin](https://github.com/SpongePowered/Mixin)。
- **状態:** 実験的。広範なテストは行われていないため、自己責任で使用してください。
- Mojang、Microsoft、Fabric、Anthropic とは**無関係**です。

## ライセンス

以下のライセンス本文は原文の英語のまま掲載しています。法的に拘束力があるのはこの英語の文言のみであり、MIT ライセンスの翻訳はこれを置き換えるものではありません。

This is an **open-source** project released under the **[MIT License](../LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

コントリビューション、バグ報告、プルリクエストを歓迎します: [Issue を作成](https://github.com/evgenmakarickij-wq/localpack-bypass/issues)。
