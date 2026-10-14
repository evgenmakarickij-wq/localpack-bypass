# localpack-bypass

<p>
  <a href="LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Мови:** [English](README.md) · **Українська** · [Русский](READMERU.md)

**Автори:** McChopik, Telemine3140

---

**localpack-bypass** — безкоштовний клієнтський [Fabric](https://fabricmc.net)-мод із відкритим вихідним кодом для [Minecraft](https://www.minecraft.net) 26.2, який спрощує роботу із серверними ресурспаками. Замість того щоб завантажувати той самий пак щоразу при вході, він порівнює SHA-1 пака сервера з ZIP-файлами у вашій теці `resourcepacks`. Якщо відповідний пак знайдено, він вмикається без запитів, і ви заходите на сервер без очікування. Якщо пак новий або змінився, з'являється одне вікно з пропозицією завантажити всі потрібні паки одразу; після цього мод перевіряє їх, вмикає, зберігає вибір і перезавантажує ресурси лише один раз. Завантажені паки лишаються ввімкненими після виходу із сервера, як звичайні ресурспаки, а відмова відключає вас від сервера, як у ванілі. Вікно й повідомлення підлаштовуються під мову гри: English, Deutsch, Polski, Čeština, Slovenčina, Беларуская, Русский, Українська.

## Як це працює

- **Пак уже завантажено:** він вмикається без запитів. Ні вікна, ні завантаження.
- **Новий або змінений пак:** одне вікно з кнопкою **«Завантажити всі ресурспаки»**. Паки зберігаються в `resourcepacks/localpack-<sha1>.zip`, перевіряються за SHA-1, вмикаються й зберігаються в налаштуваннях.
- **Відмовитись і вийти:** вас відключають від сервера.
- **Старі паки:** паки, раніше завантажені модом, вимикаються, коли приходить нова версія. Файли лишаються на диску, тож повернутися до старої версії можна миттєво.

## Вимоги

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 або новіший
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux або Android-лаунчер (наприклад, [PojavLauncher](https://pojavlauncherteam.github.io/)): усі шляхи мод бере з ігрової теки лаунчера, тож жодну теку не треба вказувати вручну
- За бажанням: [Mod Menu](https://modrinth.com/mod/modmenu), який показує назву й опис моду вашою мовою

## Збірка

Мод збирає [робочий процес GitHub Actions](.github/workflows/build.yml) при кожному пуші в `main`. Jar можна завантажити з розділу **Artifacts** запуску. Пуш тега вигляду `v0.0.2` додатково публікує [реліз](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) з jar і манифестом `update.json`, який читає оновлювач у грі.

## Оновлення

Раз на добу мод питає в GitHub [останній реліз](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) цього репозиторію. Якщо він новіший за вашу версію, на головному екрані з'явиться пропозиція оновитися. Після згоди відбувається три речі:

1. Новий jar завантажується, і його **SHA-256 звіряється** з манифестом `update.json`, опублікованим у тому ж релізі.
2. Старий jar видаляється, новий переноситься в `mods/`.
3. Ви перезапускаєте гру.

У Windows запущений jar видалити неможливо, тож короткий згенерований скрипт чекає на закриття гри й сам виконує крок 2; мод про це повідомляє. На Android, Linux і macOS запущений jar не заблокований, тож його просто замінено. Оновлювач торкається лише `mods/` і `config/localpack-bypass/`.

Перевірка — це звичайний анонімний запит до GitHub API (без токена й акаунта). Мод не має телеметрії та власного сервера. Пропущена версія запам'ятовується, а `-Dlocalpack.bypass.updateCheck=false` повністю вимикає оновлення.

## Звідки код

- **Початок:** проєкт виріс із початкового прототипу в цьому репозиторії, який порівнював SHA-1 пака сервера з локальними файлами.
- **Автори:** McChopik, Telemine3140. Поточний код переписано й розширено за допомогою [Claude Code](https://claude.com/claude-code) — інструмента для програмування на базі ШІ від [Anthropic](https://www.anthropic.com) — на прохання авторів проєкту. Він додав завантаження однією кнопкою, обробку пачкою, автоматичне ввімкнення, «відмовитись і вийти», оновлення мода, переклади, роботу зі шляхами для Windows, macOS, Linux і Android та документацію. Код з інших модів не копіювався.
- **Використано:** власний код Minecraft з офіційними назвами Mojang, [Fabric Loader і Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) та [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Статус:** експериментальний. Мод тестувався слабо, тож використовуйте його на власний ризик.
- **Не пов'язаний** з Mojang, Microsoft, Fabric чи Anthropic.

## Ліцензія

Це проєкт із **відкритим вихідним кодом** під ліцензією **[MIT](LICENSE)** ([короткий опис](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. Його можна використовувати, копіювати, змінювати, об'єднувати, публікувати та поширювати, зокрема комерційно, за умови збереження повідомлення про авторські права й ліцензію. Програма надається «як є», без жодних гарантій.

Пропозиції, повідомлення про помилки та pull request'и вітаються: [створити issue](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
