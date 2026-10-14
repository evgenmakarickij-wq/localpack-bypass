# localpack-bypass

<p>
  <a href="../LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Γλώσσες:** [English](../README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · **Ελληνικά** · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Συγγραφείς:** McChopik, Telemine3140

---

Το **localpack-bypass** είναι ένα δωρεάν, ανοιχτού κώδικα mod [Fabric](https://fabricmc.net) από την πλευρά του πελάτη για το [Minecraft](https://www.minecraft.net) 26.2, που κάνει τα πακέτα πόρων του διακομιστή ανώδυνα. Αντί να κατεβάζεις ξανά το ίδιο πακέτο κάθε φορά που συνδέεσαι, συγκρίνει το SHA-1 του πακέτου που στέλνει ο διακομιστής με τα αρχεία ZIP που βρίσκονται ήδη στον φάκελο `resourcepacks`. Αν βρεθεί ταιριαστό πακέτο, ενεργοποιείται αθόρυβα, ώστε να συνδέεσαι χωρίς ερωτήσεις και χωρίς αναμονή. Αν ένα πακέτο είναι νέο ή έχει αλλάξει, μία οθόνη προσφέρει λήψη όλων των απαραίτητων πακέτων με μία φορά· μετά το mod τα επαληθεύει, τα ενεργοποιεί, αποθηκεύει την επιλογή και φορτώνει ξανά τους πόρους μόνο μία φορά. Τα κατεβασμένα πακέτα παραμένουν ενεργά αφού φύγεις από τον διακομιστή, όπως τα κανονικά πακέτα πόρων, ενώ η άρνηση σε αποσυνδέει από τον διακομιστή όπως στο vanilla παιχνίδι. Οι οθόνες και τα μηνύματα ακολουθούν τη γλώσσα του παιχνιδιού· το mod προσφέρει μεταφράσεις για 38 γλώσσες (δείτε τη λίστα παραπάνω).

## Πώς λειτουργεί

- **Το πακέτο έχει ήδη κατέβει:** ενεργοποιείται αθόρυβα. Καμία ερώτηση, καμία λήψη.
- **Νέο ή τροποποιημένο πακέτο:** μια οθόνη με **Λήψη όλων των πακέτων πόρων**. Τα πακέτα αποθηκεύονται στο `resourcepacks/localpack-<sha1>.zip`, επαληθεύονται με SHA-1, ενεργοποιούνται και αποθηκεύονται στις ρυθμίσεις σου.
- **Άρνηση και αποχώρηση:** αποσυνδέεσαι από τον διακομιστή.
- **Παλαιότερα πακέτα:** πακέτα που είχε κατεβάσει προηγουμένως το mod αποεπιλέγονται όταν φτάσει νεότερη έκδοση. Τα αρχεία παραμένουν στον δίσκο, οπότε η επιστροφή είναι άμεση.

## Ενημερώσεις

Μία φορά την ημέρα το mod ρωτά το GitHub για τη [νεότερη έκδοση](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) αυτού του αποθετηρίου. Αν είναι νεότερη από την έκδοση που χρησιμοποιείς, η αρχική οθόνη προσφέρει να την εγκαταστήσεις. Η αποδοχή κάνει τρία πράγματα:

1. Το νέο αρχείο JAR κατεβαίνει και το **SHA-256 του επαληθεύεται** έναντι του manifest `update.json` που δημοσιεύεται με αυτή την έκδοση.
2. Το παλιό αρχείο JAR διαγράφεται και το νέο μετακινείται στο `mods/`.
3. Κάνεις επανεκκίνηση του παιχνιδιού.

Στα Windows ένα αρχείο JAR που είναι φορτωμένο αυτή τη στιγμή δεν μπορεί να διαγραφεί, γι' αυτό ένα σύντομο παραγόμενο script περιμένει να κλείσει το παιχνίδι και εκτελεί το βήμα 2 για σένα· το mod σου το λέει όταν συμβεί αυτό. Στο Android, το Linux και το macOS το ενεργό αρχείο JAR δεν είναι κλειδωμένο και αντικαθίσταται απλώς. Ό,τι αγγίζει ο ενημερωτής βρίσκεται στο `mods/` και στο `config/localpack-bypass/`.

Το αίτημα είναι ένα συνηθισμένο ανώνυμο αίτημα στο API του GitHub (χωρίς token, χωρίς λογαριασμό), το mod δεν έχει τηλεμετρία ούτε δικό του διακομιστή. Μια έκδοση που παραλείφθηκε θυμάται, και το `-Dlocalpack.bypass.updateCheck=false` απενεργοποιεί εντελώς τις ενημερώσεις.

## Απαιτήσεις

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 ή νεότερο
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux ή έναν launcher για Android όπως το [PojavLauncher](https://pojavlauncherteam.github.io/): κάθε αρχείο που αγγίζει το mod καθορίζεται από τον φάκελο παιχνιδιού του launcher, οπότε δεν χρειάζεται να ρυθμίσεις φάκελο με το χέρι
- Προαιρετικά: [Mod Menu](https://modrinth.com/mod/modmenu), που εμφανίζει το όνομα και την περιγραφή του mod στη γλώσσα σου

## Χτίσιμο μόνος σου

Το mod χτίζεται σε κάθε push στο `main` από τη [ροή εργασιών του GitHub Actions](../.github/workflows/build.yml), και το αρχείο JAR βρίσκεται στα artifacts αυτής της εκτέλεσης. Μια ετικέτα όπως `v0.0.3` δημοσιεύει επιπλέον μια [έκδοση](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) με το αρχείο JAR και το manifest `update.json` που διαβάζει ο ενημερωτής στο παιχνίδι.

## Από πού προέρχεται ο κώδικας

- **Προέλευση:** το έργο προέκυψε από το αρχικό πρωτότυπο αυτού του αποθετηρίου, που συνέκρινε το SHA-1 του πακέτου του διακομιστή με τοπικά αρχεία.
- **Συγγραφείς:** McChopik, Telemine3140. Ο σημερινός κώδικας ξαναγράφηκε και επεκτάθηκε με το [Claude Code](https://claude.com/claude-code), ένα εργαλείο προγραμματισμού με τεχνητή νοημοσύνη της [Anthropic](https://www.anthropic.com), με επιθυμία των συγγραφέων του έργου. Προστέθηκαν η λήψη με ένα κλικ, η ομαδοποίηση, η αυτόματη ενεργοποίηση, το «άρνηση και αποχώρηση», ο ενημερωτής, οι μεταφράσεις, ο χειρισμός διαδρομών για Windows, macOS, Linux και Android, καθώς και η τεκμηρίωση. Κώδικας από άλλα mod δεν αντιγράφηκε.
- **Χτισμένο πάνω σε:** τον ίδιο τον κώδικα του Minecraft με τα επίσημα ονόματα της Mojang, το [Fabric Loader και το Fabric API](https://fabricmc.net), το [Fabric Loom](https://github.com/FabricMC/fabric-loom) και το [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Κατάσταση:** πειραματικό. Δεν έχει δοκιμαστεί εκτενώς, οπότε χρησιμοποίησέ το με δική σου ευθύνη.
- **Δεν συνδέεται** με τη Mojang, τη Microsoft, το Fabric ή την Anthropic.

## Άδεια

Το κείμενο της άδειας παρακάτω παρατίθεται εδώ στο αρχικό του αγγλικό κείμενο· νομικά δεσμευτικό είναι μόνο αυτό το αγγλικό κείμενο, και μια μετάφραση της άδειας MIT δεν θα το αντικαθιστούσε.

This is an **open-source** project released under the **[MIT License](../LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Οι συνεισφορές, οι αναφορές σφαλμάτων και τα pull requests είναι ευπρόσδεκτα: [άνοιξε ένα issue](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
