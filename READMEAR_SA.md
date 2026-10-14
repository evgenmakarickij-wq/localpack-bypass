# localpack-bypass

<p>
  <a href="LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**اللغات:** [English](README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · **العربية** · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**المؤلفون:** McChopik, Telemine3140

---

**localpack-bypass** هو مود [Fabric](https://fabricmc.net) مجاني ومفتوح المصدر ومن جانب العميل من أجل [Minecraft](https://www.minecraft.net) 26.2، يجعل حزم موارد الخادم بلا عناء. فبدل تنزيل الحزمة نفسها من جديد عند كل دخول، يقارن SHA-1 الخاص بالحزمة التي يرسلها الخادم بملفات ZIP الموجودة بالفعل في مجلد `resourcepacks`. وإذا وُجدت حزمة مطابقة، تُفعَّل بهدوء، فتنضم دون أسئلة ودون انتظار. وإذا كانت الحزمة جديدة أو تغيّرت، تعرض شاشة واحدة تنزيل كل الحزم المطلوبة دفعة واحدة؛ ثم يتحقق منها المود ويفعّلها ويحفظ الاختيار ويعيد تحميل الموارد مرة واحدة فقط. تبقى الحزم المنزّلة مفعّلة بعد مغادرة الخادم، مثل حزم الموارد العادية، بينما يؤدي الرفض إلى فصلك عن الخادم كما في اللعبة الأصلية. تتبع الشاشات والرسائل لغة اللعبة؛ ويوفّر المود ترجمات لـ38 لغة (انظر قائمة اللغات أعلاه).

## كيف يعمل

- **الحزمة منزّلة مسبقًا:** تُفعَّل بهدوء. لا سؤال ولا تنزيل.
- **حزمة جديدة أو معدَّلة:** شاشة فيها **تنزيل كل حزم الموارد**. تُحفظ الحزم في `resourcepacks/localpack-<sha1>.zip`، وتُتحقَّق عبر SHA-1، وتُفعَّل، وتُحفظ في إعداداتك.
- **الرفض والخروج:** يتم فصلك عن الخادم.
- **الحزم الأقدم:** تُلغى الحزم التي نزّلها المود سابقًا عند وصول إصدار أحدث. تبقى الملفات على القرص، لذا يمكن الرجوع فورًا.

## التحديثات

مرة كل يوم يسأل المود GitHub عن [أحدث إصدار](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) لهذا المستودع. وإذا كان أحدث من الإصدار الذي تستخدمه، تعرض شاشة العنوان تثبيته. الموافقة تفعل ثلاثة أشياء:

1. يُنزَّل ملف JAR الجديد و**يُتحقَّق من SHA-256 الخاص به** مقابل بيان `update.json` المنشور مع هذا الإصدار.
2. يُحذف ملف JAR القديم ويُنقل الجديد إلى `mods/`.
3. تعيد تشغيل اللعبة.

في Windows لا يمكن حذف ملف JAR محمّل حاليًا، لذلك ينتظر نص برمجي قصير مولَّد إغلاق اللعبة وينفّذ الخطوة 2 بدلًا عنك؛ ويخبرك المود عندما يحدث ذلك. في Android وLinux وmacOS لا يكون ملف JAR قيد التشغيل مقفلًا ويُستبدل ببساطة. وكل ما يلمسه المحدِّث يقع تحت `mods/` و`config/localpack-bypass/`.

الاستعلام طلب مجهول عادي إلى GitHub API (بلا رمز ولا حساب)، وليس للمود أي قياسات عن بُعد ولا خادم خاص به. يُتذكَّر الإصدار المتخطّى، ويُعطّل `-Dlocalpack.bypass.updateCheck=false` التحديثات كليًا.

## المتطلبات

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 أو أحدث
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows أو macOS أو Linux أو مشغّل Android مثل [PojavLauncher](https://pojavlauncherteam.github.io/): يُحدَّد كل ملف يلمسه المود من مجلد اللعبة الخاص بالمشغّل، لذا لا حاجة لإعداد مجلد يدويًا
- اختياري: [Mod Menu](https://modrinth.com/mod/modmenu)، الذي يعرض اسم المود ووصفه بلغتك

## البناء بنفسك

يُبنى المود عند كل دفع (push) إلى `main` عبر [سير عمل GitHub Actions](.github/workflows/build.yml)، ويوجد ملف JAR ضمن مخرجات تلك التشغيلة. كما ينشر وسم مثل `v0.0.3` [إصدارًا](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) يضم ملف JAR وبيان `update.json` الذي يقرأه المحدِّث داخل اللعبة.

## من أين يأتي الكود

- **الأصل:** نشأ المشروع من النموذج الأولي الأصلي لهذا المستودع، الذي كان يقارن SHA-1 الخاص بحزمة الخادم بالملفات المحلية.
- **المؤلفون:** McChopik وTelemine3140. أُعيدت كتابة الكود الحالي وتوسيعه بطلب من مؤلفي المشروع باستخدام [Claude Code](https://claude.com/claude-code)، وهي أداة برمجة بالذكاء الاصطناعي من [Anthropic](https://www.anthropic.com). وقد أُضيف التنزيل بنقرة واحدة، والتجميع، والتفعيل التلقائي، وخيار «الرفض والخروج»، والمحدِّث، والترجمات، ومعالجة المسارات لـWindows وmacOS وLinux وAndroid، والتوثيق. ولم يُنسخ أي كود من مودات أخرى.
- **مبني على:** كود Minecraft نفسه بالأسماء الرسمية من Mojang، و[Fabric Loader وFabric API](https://fabricmc.net)، و[Fabric Loom](https://github.com/FabricMC/fabric-loom)، و[SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **الحالة:** تجريبي. لم يُختبر على نطاق واسع، لذا استخدمه على مسؤوليتك.
- **غير مرتبط** بـMojang أو Microsoft أو Fabric أو Anthropic.

## الترخيص

نص الترخيص أدناه مُورَد هنا بصيغته الإنجليزية الأصلية؛ ولا يُلزِم قانونيًا إلا هذا النص الإنجليزي، ولن تحل ترجمة ترخيص MIT محلّه.

This is an **open-source** project released under the **[MIT License](LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

المساهمات وبلاغات الأخطاء وطلبات السحب مرحّب بها: [افتح issue](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
