# localpack-bypass

<p>
  <a href="LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Языки:** [English](README.md) · [Українська](READMEUA.md) · **Русский**

**Авторы:** Claude, McChopik, Telemine3140

---

**localpack-bypass** — бесплатный клиентский [Fabric](https://fabricmc.net)-мод с открытым исходным кодом для [Minecraft](https://www.minecraft.net) 26.2, который упрощает работу с серверными ресурспаками. Вместо того чтобы скачивать один и тот же пак при каждом входе, он сравнивает SHA-1 пака сервера с ZIP-файлами в вашей папке `resourcepacks`. Если подходящий пак найден, он включается без запросов, и вы заходите на сервер без ожидания. Если пак новый или изменился, появляется одно окно с предложением скачать все нужные паки сразу; после этого мод проверяет их, включает, сохраняет выбор и перезагружает ресурсы только один раз. Скачанные паки остаются включёнными после выхода с сервера, как обычные ресурспаки, а отказ отключает вас от сервера, как в ванили. Окно и сообщения подстраиваются под язык игры: English, Русский, Українська.

## Как это работает

- **Пак уже скачан:** он включается без запросов. Ни окна, ни загрузки.
- **Новый или изменённый пак:** одно окно с кнопкой **«Скачать все ресурспаки»**. Паки сохраняются в `resourcepacks/localpack-<sha1>.zip`, проверяются по SHA-1, включаются и сохраняются в настройках.
- **Отказаться и выйти:** вас отключают от сервера.
- **Старые паки:** паки, ранее скачанные модом, отключаются, когда приходит новая версия. Файлы остаются на диске, так что вернуться к старой версии можно мгновенно.

## Требования

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 или новее
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- По желанию: [Mod Menu](https://modrinth.com/mod/modmenu), который показывает название и описание мода на вашем языке

## Сборка

```
gradle build
```

Готовый jar появляется в `build/libs/`. [Рабочий процесс GitHub Actions](.github/workflows/build.yml) также собирает его при каждом пуше в `main`; jar можно скачать из раздела **Artifacts** запуска.

## Откуда код

- **Начало:** проект вырос из исходного прототипа в этом репозитории, который сравнивал SHA-1 пака сервера с локальными файлами.
- **Авторы:** Claude, McChopik, Telemine3140. Текущий код переписан и расширен [Claude](https://claude.ai) (ИИ от [Anthropic](https://www.anthropic.com)) по просьбе авторов проекта. Он добавил скачивание одной кнопкой, обработку пачкой, автоматическое включение, «отказаться и выйти», переводы и документацию. Код из других модов не копировался.
- **Использовано:** собственный код Minecraft с официальными названиями Mojang, [Fabric Loader и Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) и [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Статус:** экспериментальный. Мод тестировался слабо, так что используйте его на свой страх и риск.
- **Не связан** с Mojang, Microsoft, Fabric или Anthropic.

## Лицензия

Это проект с **открытым исходным кодом** под лицензией **[MIT](LICENSE)** ([краткое описание](https://opensource.org/license/mit)). Copyright (c) 2026 Claude, McChopik, Telemine3140. Его можно использовать, копировать, изменять, объединять, публиковать и распространять, в том числе коммерчески, при условии сохранения уведомления об авторских правах и лицензии. Программа предоставляется «как есть», без каких-либо гарантий.

Предложения, сообщения об ошибках и pull request'ы приветствуются: [создать issue](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
