# localpack-bypass

<p>
  <a href="../LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Документація:** [English](../README.md) · **Українська** · [Русский](READMERU.md). Інтерфейс гри й надалі містить усі 38 перекладів.

**Автори:** McChopik, Telemine3140

---

**localpack-bypass** — безкоштовний клієнтський [Fabric](https://fabricmc.net)-мод із відкритим вихідним кодом для [Minecraft](https://www.minecraft.net) 26.2. Він зберігає серверні ресурспаки у звичайній теці `resourcepacks` і повторно використовує їх без нового завантаження. Згоду гравця, обов'язкові паки, SHA-1, порядок паків, вимкнення та перезавантаження ресурсів повністю обробляє сама гра.

## Як це працює

- **Пак уже завантажено:** локальний файл підкладається у внутрішній кеш Minecraft, після чого гра знову перевіряє його SHA-1.
- **Новий або змінений пак:** Minecraft завантажує його звичайним способом із лімітом 250 МіБ, налаштуваннями проксі та перевіркою хешу. Перевірений файл зберігається як `resourcepacks/localpack-<sha1>.zip`.
- **Згода й обов'язкові паки:** використовуються стандартні налаштування сервера «Запитувати», «Увімкнено» та «Вимкнено».
- **Термін дії:** пак видаляється після команди `pop` або виходу із сервера. UUID, заміну й помилки перезавантаження обробляє гра.
- **Ручний вибір зберігається:** мод ніколи не змінює `options.txt`. Якщо архів вимкнено гравцем у меню ресурспаків, мод не вмикає його назад.

## Налаштування (Mod Menu)

Зі встановленим [Mod Menu](https://modrinth.com/mod/modmenu) мод має власний екран налаштувань.

- **Тека для завантажених паків** — де зберігається додаткова архівна копія. Відносний шлях обчислюється від теки гри.
- **Вести запис** — зберігає сервер і результат. URL, хеш, розмір та діагностика записуються лише коли ввімкнено **«Записувати й подробиці»**.
- **Завантажені паки** — показує архіви за серверами та їхній стан. Пак можна вручну ввімкнути чи вимкнути; мод не змінить цей вибір самостійно.
- **Що надсилали сервери** — журнал завантажених або повторно використаних паків, найновіші записи зверху.

## Вимоги

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 або новіший
- [Fabric API](https://modrinth.com/mod/fabric-api) 0.158.0 або новіший
- Java 25
- Windows, macOS, Linux або Android-лаунчер, наприклад [PojavLauncher](https://pojavlauncherteam.github.io/)
- За бажанням: [Mod Menu](https://modrinth.com/mod/modmenu)

## Збірка

Мод збирається [GitHub Actions](../.github/workflows/build.yml) після кожного push у `main`. JAR доступний у розділі **Artifacts** відповідного запуску. Звичайні коміти ніколи не створюють реліз.

## Звідки код

- **Початок:** проєкт виріс із прототипу, який порівнював SHA-1 серверного пака з локальними файлами.
- **Поточна реалізація:** зберігає стандартний протокол серверних ресурспаків Minecraft і додає постійний локальний кеш навколо перевіреного завантажувача гри.
- **Використано:** Minecraft з офіційними назвами Mojang, [Fabric Loader і Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) та [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Статус:** експериментальний. Використовуйте на власний ризик.
- **Не пов'язаний** із Mojang, Microsoft або Fabric.

## Ліцензія

Проєкт має відкритий вихідний код і поширюється за ліцензією **[MIT](../LICENSE)**. Юридичну силу має англійський текст у файлі `LICENSE`.

Пропозиції, повідомлення про помилки та pull request'и вітаються: [створити issue](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
