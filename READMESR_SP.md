# localpack-bypass

<p>
  <a href="LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Језици:** [English](README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · **Српски** · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Аутори:** McChopik, Telemine3140

---

**localpack-bypass** је бесплатан мод са отвореним кодом за клијентску страну за [Fabric](https://fabricmc.net) и [Minecraft](https://www.minecraft.net) 26.2, који олакшава серверске пакете ресурса. Уместо да при сваком придруживању поново преузимаш исти пакет, он пореди SHA-1 пакета који сервер шаље са ZIP фајловима који већ леже у фасцикли `resourcepacks`. Ако нађе одговарајући пакет, тихо га активира, тако да се придружујеш без питања и без чекања. Ако је пакет нов или се променио, један екран нуди да преузмеш све потребне пакете одједном; мод их затим проверава, активира, чува избор и поново учитава ресурсе само једном. Преузети пакети остају активни након напуштања сервера, као обични пакети ресурса, док одбијање, као у обичној игри, прекида везу са сервером. Екрани и поруке прате језик игре; мод доноси преводе за 38 језика (погледај списак изнад).

## Како функционише

- **Пакет је већ преузет:** тихо се активира. Без питања, без преузимања.
- **Нов или измењен пакет:** екран са **Преузми све пакете ресурса**. Пакети се чувају под `resourcepacks/localpack-<sha1>.zip`, проверавају се преко SHA-1, активирају се и чувају у твојим подешавањима.
- **Одбиј и изађи:** бићеш прекинут са сервера.
- **Старији пакети:** претходно преузети пакети од мода се искључују када стигне новија верзија. Фајлови остају на диску, тако да је повратак тренутан.

## Ажурирања

Једном дневно мод пита GitHub за [најновију верзију](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) овог репозиторијума. Ако је новија од верзије коју користиш, почетни екран нуди да је инсталираш. Прихватање чини три ствари:

1. Нови JAR фајл се преузима и **његов SHA-256 се проверава**, и то против манифеста `update.json`, који се објављује са овом верзијом.
2. Стари JAR фајл се брише, а нови се премешта у `mods/`.
3. Поново покренеш игру.

У Windows-у JAR фајл који је управо учитан не може се избрисати, зато кратка генерисана скрипта чека затварање игре и обавља корак 2 уместо тебе; мод ти то саопшти када се то догоди. На Android-у, Linux-у и macOS-у JAR фајл који се извршава није закључан и једноставно се замењује. Све чега се ажурирање дотакне лежи у `mods/` и `config/localpack-bypass/`.

Упит је обичан анониман захтев ка GitHub API-ју (без токена, без налога), мод нема телеметрију ни сопствени сервер. Прескочена верзија се памти, а `-Dlocalpack.bypass.updateCheck=false` потпуно искључује ажурирања.

## Захтеви

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 или новији
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux или Android покретач као [PojavLauncher](https://pojavlauncherteam.github.io/): сваки фајл чега се мод дотакне утврђује се из директоријума игре покретача, тако да није потребно ручно подешавати фасциклу
- Опционо: [Mod Menu](https://modrinth.com/mod/modmenu), које приказује назив и опис мода на твом језику

## Сопствена изградња

Мод се изграђује при сваком push-у на `main` од стране [GitHub Actions радног тока](.github/workflows/build.yml), JAR фајл лежи међу артефактима покретања. Ознака као `v0.0.3` додатно објављује [верзију](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) са JAR фајлом и манифестом `update.json`, који ажурирање чита у игри.

## Одакле потиче код

- **Порекло:** пројекат је настао из првобитног прототипа овог репозиторијума, који је поредио SHA-1 серверског пакета са локалним фајловима.
- **Аутори:** McChopik, Telemine3140. Данашњи код је поново написан и проширен помоћу [Claude Code](https://claude.com/claude-code), алата за програмирање вештачке интелигенције од [Anthropic](https://www.anthropic.com), на жељу аутора пројекта. Додати су преузимање једним кликом, груписање, аутоматско активирање, „одбиј и изађи“, ажурирање, преводи, руковање путањама за Windows, macOS, Linux и Android, као и документација. Код из других модова није копиран.
- **Изграђено на:** сопственом коду Minecraft-а са званичним именима од Mojang, [Fabric Loader и Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) и [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Статус:** експериментално. Није темељно тестирано, па га користи на сопствену одговорност.
- **Није повезан** са Mojang, Microsoft, Fabric или Anthropic.

## Лиценца

Текст лиценце испод је овде приказан у оригиналном енглеском облику; правно обавезујућа је само та енглеска формулација, превод MIT лиценце је не би заменио.

This is an **open-source** project released under the **[MIT License](LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Доприноси, пријаве грешака и pull request-ови су добродошли: [отвори issue](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
