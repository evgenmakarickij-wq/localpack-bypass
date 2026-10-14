# localpack-bypass

<p>
  <a href="LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Lingue:** [English](README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · **Italiano** · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Autori:** McChopik, Telemine3140

---

**localpack-bypass** è un mod [Fabric](https://fabricmc.net) per [Minecraft](https://www.minecraft.net) 26.2 gratuito, open source e lato client che rende indolori i pacchetti di risorse del server. Invece di riscaricare ogni volta lo stesso pacchetto quando entri, confronta lo SHA-1 del pacchetto inviato dal server con i file ZIP già presenti nella tua cartella `resourcepacks`. Se viene trovato un pacchetto corrispondente, viene attivato in silenzio, così entri senza richieste né attese. Se un pacchetto è nuovo o è cambiato, una sola schermata propone di scaricare tutti i pacchetti necessari in una volta; in seguito la mod li verifica, li attiva, salva la selezione e ricarica le risorse una sola volta. I pacchetti scaricati restano attivi dopo che esci dal server, come i normali pacchetti di risorse, mentre rifiutare ti disconnette dal server, come nel gioco vanilla. Le schermate e i messaggi seguono la lingua del gioco; la mod include traduzioni per 38 lingue (vedi l'elenco qui sopra).

## Come funziona

- **Pacchetto già scaricato:** viene attivato in silenzio. Nessuna richiesta, nessun download.
- **Pacchetto nuovo o modificato:** una schermata con **Scarica tutti i pacchetti di risorse**. I pacchetti vengono salvati in `resourcepacks/localpack-<sha1>.zip`, verificati tramite SHA-1, attivati e salvati nelle tue opzioni.
- **Rifiuta ed esci:** vieni disconnesso dal server.
- **Pacchetti più vecchi:** i pacchetti scaricati in precedenza dalla mod vengono deselezionati quando arriva una versione più recente. I file restano sul disco, quindi tornare indietro è immediato.

## Aggiornamenti

Una volta al giorno la mod chiede a GitHub l'[ultima versione](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) di questo repository. Se è più recente della versione che usi, la schermata del titolo propone di installarla. Accettare fa tre cose:

1. Il nuovo file JAR viene scaricato e il suo **SHA-256 viene verificato** rispetto al manifesto `update.json` pubblicato con quella versione.
2. Il vecchio file JAR viene eliminato e quello nuovo viene spostato in `mods/`.
3. Riavvii il gioco.

Su Windows un file JAR attualmente caricato non può essere eliminato, perciò un breve script generato attende la chiusura del gioco ed esegue il passo 2 al posto tuo; la mod te lo segnala quando accade. Su Android, Linux e macOS il file JAR in esecuzione non è bloccato e viene semplicemente sostituito. Tutto ciò che l'updater tocca si trova in `mods/` e `config/localpack-bypass/`.

Il controllo è una normale richiesta anonima all'API di GitHub (nessun token, nessun account), la mod non ha telemetria né un server proprio. Una versione saltata viene ricordata, e `-Dlocalpack.bypass.updateCheck=false` disattiva del tutto gli aggiornamenti.

## Requisiti

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 o successivo
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux o un launcher Android come [PojavLauncher](https://pojavlauncherteam.github.io/): ogni file che la mod tocca viene ricavato dalla cartella di gioco del launcher, quindi non serve configurare nessuna cartella a mano
- Facoltativo: [Mod Menu](https://modrinth.com/mod/modmenu), che mostra il nome e la descrizione della mod nella tua lingua

## Compilarlo da soli

La mod viene compilata a ogni push su `main` dal [workflow di GitHub Actions](.github/workflows/build.yml), con il file JAR disponibile tra gli artefatti dell'esecuzione. Un tag come `v0.0.3` pubblica inoltre una [versione](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) con il file JAR e il manifesto `update.json` che l'updater legge nel gioco.

## Da dove viene il codice

- **Origine:** il progetto è nato dal prototipo originale di questo repository, che confrontava lo SHA-1 del pacchetto del server con i file locali.
- **Autori:** McChopik, Telemine3140. Il codice attuale è stato riscritto ed esteso con [Claude Code](https://claude.com/claude-code), uno strumento di programmazione con IA di [Anthropic](https://www.anthropic.com), su richiesta degli autori del progetto. Sono stati aggiunti il download con un clic, il raggruppamento, l'attivazione automatica, «Rifiuta ed esci», l'updater, le traduzioni, la gestione dei percorsi per Windows, macOS, Linux e Android e la documentazione. Non è stato copiato codice da altre mod.
- **Costruita su:** il codice di Minecraft con i nomi ufficiali di Mojang, [Fabric Loader e Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) e [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Stato:** sperimentale. Non è stato testato a fondo, quindi usalo a tuo rischio.
- **Non affiliata** a Mojang, Microsoft, Fabric o Anthropic.

## Licenza

Il testo della licenza riportato di seguito è riprodotto nel suo inglese originale; solo quella formulazione in inglese ha valore legale, e una traduzione della licenza MIT non la sostituirebbe.

This is an **open-source** project released under the **[MIT License](LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Contributi, segnalazioni di bug e pull request sono benvenuti: [apri una issue](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
