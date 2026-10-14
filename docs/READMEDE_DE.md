# localpack-bypass

<p>
  <a href="../LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Sprachen:** [English](../README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · **Deutsch** · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Autoren:** McChopik, Telemine3140

---

**localpack-bypass** ist ein kostenloses, quelloffenes, clientseitiges [Fabric](https://fabricmc.net)-Mod für [Minecraft](https://www.minecraft.net) 26.2, das Server-Ressourcenpakete schmerzlos macht. Statt bei jedem Beitritt dasselbe Paket erneut herunterzuladen, vergleicht es die SHA-1 des Pakets, das der Server sendet, mit den ZIP-Dateien, die bereits im Ordner `resourcepacks` liegen. Wird ein passendes Paket gefunden, wird es still aktiviert, sodass du ohne Nachfragen und ohne Warten beitrittst. Ist ein Paket neu oder hat es sich geändert, bietet ein einzelner Bildschirm an, alle benötigten Pakete auf einmal herunterzuladen; das Mod prüft sie danach, aktiviert sie, speichert die Auswahl und lädt die Ressourcen nur einmal neu. Heruntergeladene Pakete bleiben nach dem Verlassen des Servers aktiv, wie normale Ressourcenpakete, während ein Ablehnen dich wie im Vanilla-Spiel vom Server trennt. Bildschirme und Meldungen folgen der Spielsprache; das Mod bringt Übersetzungen für 38 Sprachen mit (siehe die Liste oben).

## So funktioniert es

- **Paket bereits heruntergeladen:** es wird still aktiviert. Keine Nachfrage, kein Download.
- **Neues oder geändertes Paket:** ein Bildschirm mit **Alle Ressourcenpakete herunterladen**. Pakete werden unter `resourcepacks/localpack-<sha1>.zip` gespeichert, per SHA-1 geprüft, aktiviert und in deinen Optionen gespeichert.
- **Ablehnen und verlassen:** du wirst vom Server getrennt.
- **Ältere Pakete:** zuvor vom Mod heruntergeladene Pakete werden abgewählt, wenn eine neuere Version eintrifft. Die Dateien bleiben auf der Festplatte, ein Zurückwechseln ist also sofort möglich.

## Aktualisierungen

Einmal pro Tag fragt das Mod GitHub nach der [neuesten Version](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) dieses Repositorys. Ist sie neuer als die Version, die du verwendest, bietet der Titelbildschirm an, sie zu installieren. Ein Zusagen bewirkt drei Dinge:

1. Die neue JAR-Datei wird heruntergeladen und ihre **SHA-256 wird geprüft**, und zwar gegen das `update.json`-Manifest, das mit dieser Version veröffentlicht wird.
2. Die alte JAR-Datei wird gelöscht und die neue nach `mods/` verschoben.
3. Du startest das Spiel neu.

Unter Windows lässt sich eine JAR-Datei, die gerade geladen ist, nicht löschen, deshalb wartet ein kurzes erzeugtes Skript auf das Beenden des Spiels und erledigt Schritt 2 für dich; das Mod sagt es dir, wenn das passiert. Unter Android, Linux und macOS ist die laufende JAR-Datei nicht gesperrt und wird einfach ersetzt. Alles, was der Updater anfasst, liegt unter `mods/` und `config/localpack-bypass/`.

Die Abfrage ist eine gewöhnliche anonyme Anfrage an die GitHub-API (kein Token, kein Konto), das Mod hat keine Telemetrie und keinen eigenen Server. Eine übersprungene Version wird gemerkt, und `-Dlocalpack.bypass.updateCheck=false` schaltet Aktualisierungen vollständig ab.

## Voraussetzungen

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 oder neuer
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux oder ein Android-Launcher wie [PojavLauncher](https://pojavlauncherteam.github.io/): jede Datei, die das Mod anfasst, wird aus dem Spielverzeichnis des Launchers ermittelt, es muss also kein Ordner von Hand eingerichtet werden
- Optional: [Mod Menu](https://modrinth.com/mod/modmenu), das Mod-Name und -Beschreibung in deiner Sprache anzeigt

## Selbst bauen

Das Mod wird bei jedem Push nach `main` vom [GitHub-Actions-Workflow](../.github/workflows/build.yml) gebaut, die JAR-Datei liegt unter den Artefakten des Durchlaufs. Ein Tag wie `v0.0.3` veröffentlicht zusätzlich eine [Version](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) mit der JAR-Datei und dem `update.json`-Manifest, das der Updater im Spiel liest.

## Woher der Code kommt

- **Ursprung:** das Projekt entstand aus dem ursprünglichen Prototyp dieses Repositorys, der die SHA-1 des Serverpakets mit lokalen Dateien verglich.
- **Autoren:** McChopik, Telemine3140. Der heutige Code wurde mit [Claude Code](https://claude.com/claude-code), einem KI-Programmierwerkzeug von [Anthropic](https://www.anthropic.com), auf Wunsch der Projekt-Autoren neu geschrieben und erweitert. Hinzugekommen sind der Download mit einem Klick, die Bündelung, das automatische Aktivieren, „Ablehnen und verlassen“, der Updater, die Übersetzungen, die Pfadbehandlung für Windows, macOS, Linux und Android sowie die Dokumentation. Code aus anderen Mods wurde nicht kopiert.
- **Aufgebaut auf:** Minecrafts eigenem Code mit den offiziellen Namen von Mojang, [Fabric Loader und Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) und [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Status:** experimentell. Es wurde nicht ausgiebig getestet, benutze es also auf eigenes Risiko.
- **Nicht verbunden** mit Mojang, Microsoft, Fabric oder Anthropic.

## Lizenz

Der Lizenztext unten steht hier in seiner englischen Originalsprache; rechtlich bindend ist allein dieser englische Wortlaut, eine Übersetzung der MIT-Lizenz würde ihn nicht ersetzen.

This is an **open-source** project released under the **[MIT License](../LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Beiträge, Fehlerberichte und Pull Requests sind willkommen: [ein Issue eröffnen](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
