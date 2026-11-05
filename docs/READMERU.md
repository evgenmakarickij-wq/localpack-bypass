# localpack-bypass

<p>
  <a href="../LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Языки:** [English](../README.md) · [Українська](READMEUA.md) · **Русский** · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Авторы:** McChopik, Telemine3140

---

**localpack-bypass** — бесплатный клиентский [Fabric](https://fabricmc.net)-мод с открытым исходным кодом для [Minecraft](https://www.minecraft.net) 26.2. Он сохраняет серверные ресурспаки в обычной папке `resourcepacks` и повторно использует их без новой загрузки. Согласие игрока, обязательные паки, SHA-1, порядок паков, отключение и перезагрузку ресурсов полностью обрабатывает сама игра.

## Как это работает

- **Пак уже скачан:** локальный файл подкладывается во внутренний кэш Minecraft, после чего игра заново проверяет его SHA-1.
- **Новый или изменённый пак:** Minecraft скачивает его обычным способом с лимитом 250 МиБ, настройками прокси и проверкой хеша. Проверенный файл сохраняется как `resourcepacks/localpack-<sha1>.zip`.
- **Согласие и обязательные паки:** используются стандартные настройки сервера «Спрашивать», «Включено» и «Отключено».
- **Срок действия:** пак удаляется при команде `pop` или выходе с сервера. UUID, замена и ошибки reload обрабатываются игрой.
- **Ручной выбор сохраняется:** мод никогда не меняет `options.txt`. Если архив выключен игроком в меню ресурспаков, мод не включает его обратно.

## Настройки (Mod Menu)

С установленным [Mod Menu](https://modrinth.com/mod/modmenu) мод появляется в списке модов и получает собственный экран настроек. Горячих клавиш не добавлено — всё находится там.

- **Папка для скачанных паков** — где хранится дополнительная архивная копия. Относительный путь считается от папки игры.
- **Вести запись** — сохраняет сервер и результат. URL, хеш, размер и диагностика записываются только при включённом пункте **«Записывать и подробности»**.
- **Скачанные паки** — паки, которые скачал мод, сгруппированные по серверу, с которого они пришли, со статусом: включён, выключен или файл пропал. Нажмите, чтобы включить или выключить; пак, чей файл пропал, можно забыть. **Забыть всё** стирает память мода и **никогда не удаляет файлы паков**.
- **Что присылали серверы** — сама запись, самые новые сверху, с подробностями каждой строки.

## Требования

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 или новее
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux или Android-лаунчер (например, [PojavLauncher](https://pojavlauncherteam.github.io/)): все пути мод берёт из игровой папки лаунчера, так что ни одну папку не нужно указывать вручную
- По желанию: [Mod Menu](https://modrinth.com/mod/modmenu), который показывает название и описание мода на вашем языке и даёт моду [экран настроек](#настройки-mod-menu)

## Сборка

Мод собирает [рабочий процесс GitHub Actions](../.github/workflows/build.yml) при каждом пуше в `main`. Jar можно скачать из раздела **Artifacts** запуска. Обычные коммиты никогда не создают релиз.

## Откуда код

- **Начало:** проект вырос из исходного прототипа в этом репозитории, который сравнивал SHA-1 пака сервера с локальными файлами.
- **Авторы:** McChopik, Telemine3140. Текущая реализация сохраняет стандартный протокол серверных ресурспаков Minecraft и добавляет вокруг его проверенной загрузки постоянный локальный кэш. Код из других модов не копировался.
- **Использовано:** собственный код Minecraft с официальными названиями Mojang, [Fabric Loader и Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) и [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Статус:** экспериментальный. Мод тестировался слабо, так что используйте его на свой страх и риск.
- **Не связан** с Mojang, Microsoft, Fabric или Anthropic.

## Лицензия

Текст ниже — перевод для удобства; юридическую силу имеет только оригинальный английский текст лицензии в файле [LICENSE](../LICENSE).

Это проект с **открытым исходным кодом** под лицензией **[MIT](../LICENSE)** ([краткое описание](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. Его можно использовать, копировать, изменять, объединять, публиковать и распространять, в том числе коммерчески, при условии сохранения уведомления об авторских правах и лицензии. Программа предоставляется «как есть», без каких-либо гарантий.

Предложения, сообщения об ошибках и pull request'ы приветствуются: [создать issue](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
