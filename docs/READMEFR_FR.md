# localpack-bypass

<p>
  <a href="../LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Langues:** [English](../README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · **Français** · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Auteurs:** McChopik, Telemine3140

---

**localpack-bypass** est un mod [Fabric](https://fabricmc.net) pour [Minecraft](https://www.minecraft.net) 26.2 gratuit, open source et côté client qui rend les packs de ressources de serveur indolores. Au lieu de retélécharger le même pack à chaque connexion, il compare le SHA-1 du pack envoyé par le serveur aux fichiers ZIP déjà présents dans votre dossier `resourcepacks`. Si un pack correspondant est trouvé, il est activé en silence, ce qui vous permet de rejoindre le serveur sans invite ni attente. Si un pack est nouveau ou a changé, un seul écran propose de télécharger tous les packs nécessaires d'un coup ; le mod les vérifie ensuite, les active, enregistre la sélection et ne recharge les ressources qu'une seule fois. Les packs téléchargés restent actifs après avoir quitté le serveur, comme les packs de ressources normaux, tandis que refuser vous déconnecte du serveur, comme dans le jeu vanilla. Les écrans et les messages suivent la langue du jeu ; le mod inclut des traductions pour 38 langues (voir la liste ci-dessus).

## Comment ça marche

- **Pack déjà téléchargé :** il est activé en silence. Aucune invite, aucun téléchargement.
- **Pack nouveau ou modifié :** un écran avec **Télécharger tous les packs de ressources**. Les packs sont enregistrés dans `resourcepacks/localpack-<sha1>.zip`, vérifiés par SHA-1, activés et enregistrés dans vos options.
- **Refuser et quitter :** vous êtes déconnecté du serveur.
- **Packs plus anciens :** les packs précédemment téléchargés par le mod sont décochés lorsqu'une version plus récente arrive. Les fichiers restent sur le disque, revenir en arrière est donc instantané.

## Mises à jour

Une fois par jour, le mod interroge GitHub pour obtenir la [dernière version](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) de ce dépôt. Si elle est plus récente que la version que vous utilisez, l'écran titre propose de l'installer. Accepter fait trois choses :

1. Le nouveau fichier JAR est téléchargé et son **SHA-256 est vérifié** par rapport au manifeste `update.json` publié avec cette version.
2. L'ancien fichier JAR est supprimé et le nouveau est déplacé vers `mods/`.
3. Vous redémarrez le jeu.

Sous Windows, un fichier JAR en cours de chargement ne peut pas être supprimé, c'est pourquoi un petit script généré attend la fermeture du jeu et effectue l'étape 2 à votre place ; le mod vous le signale quand cela se produit. Sous Android, Linux et macOS, le fichier JAR en cours d'exécution n'est pas verrouillé et est simplement remplacé. Tout ce que touche le programme de mise à jour se trouve dans `mods/` et `config/localpack-bypass/`.

La vérification est une simple requête anonyme à l'API GitHub (aucun jeton, aucun compte), le mod n'a ni télémétrie ni serveur propre. Une version ignorée est mémorisée, et `-Dlocalpack.bypass.updateCheck=false` désactive entièrement les mises à jour.

## Prérequis

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 ou plus récent
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux ou un lanceur Android tel que [PojavLauncher](https://pojavlauncherteam.github.io/) : chaque fichier que touche le mod est résolu depuis le dossier de jeu du lanceur, aucun dossier n'est donc à configurer à la main
- Facultatif : [Mod Menu](https://modrinth.com/mod/modmenu), qui affiche le nom et la description du mod dans votre langue

## Compiler soi-même

Le mod est compilé à chaque push vers `main` par le [workflow GitHub Actions](../.github/workflows/build.yml), le fichier JAR étant disponible dans les artefacts de l'exécution. Une étiquette comme `v0.0.3` publie en plus une [version](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) avec le fichier JAR et le manifeste `update.json` que le programme de mise à jour lit dans le jeu.

## D'où vient le code

- **Origine :** le projet est né du prototype d'origine de ce dépôt, qui comparait le SHA-1 du pack du serveur aux fichiers locaux.
- **Auteurs :** McChopik, Telemine3140. Le code actuel a été réécrit et étendu avec [Claude Code](https://claude.com/claude-code), un outil de programmation par IA d'[Anthropic](https://www.anthropic.com), à la demande des auteurs du projet. Cela a ajouté le téléchargement en un clic, le regroupement, l'activation automatique, « Refuser et quitter », le programme de mise à jour, les traductions, la gestion des chemins pour Windows, macOS, Linux et Android, ainsi que la documentation. Aucun code n'a été copié d'autres mods.
- **Construit sur :** le propre code de Minecraft avec les noms officiels de Mojang, [Fabric Loader et Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) et [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Statut :** expérimental. Il n'a pas été testé en profondeur, utilisez-le à vos propres risques.
- **Non affilié** à Mojang, Microsoft, Fabric ou Anthropic.

## Licence

Le texte de licence ci-dessous est reproduit dans son anglais d'origine ; seule cette formulation en anglais fait foi juridiquement, et une traduction de la licence MIT ne la remplacerait pas.

This is an **open-source** project released under the **[MIT License](../LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Les contributions, les rapports de bogues et les pull requests sont les bienvenus : [ouvrez une issue](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
