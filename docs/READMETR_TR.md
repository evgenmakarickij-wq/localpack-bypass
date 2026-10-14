# localpack-bypass

<p>
  <a href="../LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Diller:** [English](../README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · **Türkçe** · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Yazarlar:** McChopik, Telemine3140

---

**localpack-bypass**, sunucu kaynak paketlerini sorunsuz hale getiren, [Minecraft](https://www.minecraft.net) 26.2 için ücretsiz, açık kaynaklı, istemci tarafı bir [Fabric](https://fabricmc.net) modudur. Her katılışta aynı paketi yeniden indirmek yerine, sunucunun gönderdiği paketin SHA-1'ini `resourcepacks` klasöründe zaten bulunan ZIP dosyalarıyla karşılaştırır. Eşleşen bir paket bulunursa sessizce etkinleştirilir, böylece soru sorulmadan ve beklemeden katılırsın. Paket yeniyse veya değişmişse, tek bir ekran gerekli tüm paketleri bir kerede indirmeyi sunar; mod ardından bunları doğrular, etkinleştirir, seçimi kaydeder ve kaynakları yalnızca bir kez yeniden yükler. İndirilen paketler, sunucudan ayrıldıktan sonra normal kaynak paketleri gibi etkin kalır; reddetmek ise seni vanilla oyunda olduğu gibi sunucudan ayırır. Ekranlar ve mesajlar oyun dilini izler; mod 38 dil için çeviri içerir (yukarıdaki listeye bak).

## Nasıl çalışır

- **Paket zaten indirilmiş:** sessizce etkinleştirilir. Soru yok, indirme yok.
- **Yeni veya değiştirilmiş paket:** **Tüm kaynak paketlerini indir** bulunan bir ekran. Paketler `resourcepacks/localpack-<sha1>.zip` altına kaydedilir, SHA-1 ile doğrulanır, etkinleştirilir ve ayarlarına kaydedilir.
- **Reddet ve çık:** sunucudan bağlantın kesilir.
- **Eski paketler:** modun daha önce indirdiği paketler, daha yeni bir sürüm geldiğinde seçimden çıkarılır. Dosyalar diskte kalır, bu yüzden geri dönmek anında mümkündür.
- **Hiçbir şey sessizce başarısız olmaz:** çalışmayan bir indirme, eşleşmeyen bir sağlama toplamı veya oyunun açmadığı bir dosya ekranda bildirilir; ekran yoksa bildirim olarak gösterilir.

## Ayarlar (Mod Menu)

[Mod Menu](https://modrinth.com/mod/modmenu) yüklüyse mod, mod listesinde kendine ait bir ayar ekranıyla görünür. Hiç kısayol tuşu eklenmedi; her şey orada.

- **İndirilen paketler için klasör** — paketlerin tutulduğu yer. Oyunun kendi `resourcepacks` klasörü için boş bırak. Başka herhangi bir klasör çalışır; farklı bir sürücü veya bir Android paylaşımlı klasörü de dahil. Paketler o zaman açılmadan önce `resourcepacks` içine de kopyalanır, çünkü oyunun kendisinin okuduğu tek klasör orasıdır. Düğmeler iki klasörden birini açar.
- **Evet dediğin sunucuları hatırla** — daha önce paket aldığın bir sunucu bir daha sorulmaz. Cevap, oyunun kendi sunucu başına *Server Resource Packs* ayarına da yazılır; Prompt, Enabled ve Disabled'ın yanına.
- **Diğer sunucuların paketlerini koru** — zaten sahip olduğun paketleri korumak için bunu açık bırak. Her sunucuya yalnızca o sunucunun paketleriyle başlamak için kapat.
- **Sormadan önce bekle** — ekran görünmeden önce bir grubun geri kalanının toplanması için geçen süre; milisaniye veya saniye cinsinden. Sunucular tüm paketlerini bir kerede gönderir, bu yüzden beklemek birkaç soruyu tek soruya dönüştürür.
- **Sonra vazgeç** ve **Deneme sayısı** — bir indirmenin en fazla ne kadar sürebileceği ve başarısız olanın kaç kez yeniden deneneceği. Eşleşmeyen bir sağlama toplamı başarısızlık sayılır, çünkü yarım kalmış bir aktarım böyle görünür.
- **Kayıt tut** — bir sunucunun gönderdiği her kaynak paketi kaydedilir: hangi sunucu, hangi adres, sağlama toplamı, boyut, ona ne olduğu ve ne kadar sürdüğü. Bu senin kendi bilgisayarındaki bir dosyadır; hiçbir yere gönderilmez.
- **İndirilen paketler** — modun indirdiği paketler, geldikleri sunucuya göre gruplanmış olarak; her birinin açık, kapalı veya klasörden kaybolmuş olup olmadığını gösterir. Açıp kapatmak için birine bas; dosyası kaybolan bir paket unutulabilir. **Her şeyi unut**, modun hatırladıklarını temizler ve hiçbir zaman bir paket dosyasını silmez.
- **Sunucuların gönderdikleri** — kaydın kendisi, en yeniden eskiye, her girdinin ayrıntılarıyla.

## Güncellemeler

Mod günde bir kez GitHub'dan bu deponun [en son sürümünü](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) sorar. Kullandığın sürümden daha yeniyse ana menü onu kurmayı sunar. Kabul etmek üç şey yapar:

1. Yeni JAR dosyası indirilir ve **SHA-256'sı doğrulanır**; bu doğrulama, o sürümle yayımlanan `update.json` bildirimine karşı yapılır.
2. Eski JAR dosyası silinir ve yenisi `mods/` klasörüne taşınır.
3. Oyunu yeniden başlatırsın.

Windows'ta o anda yüklü olan bir JAR dosyası silinemez, bu yüzden kısa bir oluşturulmuş betik oyunun kapanmasını bekler ve 2. adımı senin yerine yapar; bu olduğunda mod sana söyler. Android, Linux ve macOS'ta çalışan JAR dosyası kilitli değildir ve basitçe değiştirilir. Güncelleyicinin dokunduğu her şey `mods/` ve `config/localpack-bypass/` altındadır.

Sorgu, GitHub API'sine yapılan sıradan anonim bir istektir (jeton yok, hesap yok); modun telemetrisi ve kendi sunucusu yoktur. Atlanan bir sürüm hatırlanır ve `-Dlocalpack.bypass.updateCheck=false` güncellemeleri tamamen kapatır.

**Sürüm numaralandırma uyarısı:** `0.0.1.2026-alpha-build-beta-release` sürümü `[0, 0, 1, 2026]` olarak okunur ve bu, önceki `0.0.3-beta` (`[0, 0, 3]`) sürümünün *altında* sıralanır. `0.0.3-beta` veya üzeri numaralı yapılara bu sürüm otomatik olarak sunulmaz ve elle değiştirilmeleri gerekir.

## Gereksinimler

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 veya üzeri
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux veya [PojavLauncher](https://pojavlauncherteam.github.io/) gibi bir Android başlatıcısı: modun dokunduğu her dosya, başlatıcının oyun dizininden belirlenir, bu yüzden elle klasör ayarlamak gerekmez
- İsteğe bağlı: mod adını ve açıklamasını kendi dilinde gösteren ve moda bir ayar ekranı kazandıran [Mod Menu](https://modrinth.com/mod/modmenu)

## Kendin derle

Mod, `main` dalına yapılan her push'ta [GitHub Actions iş akışı](../.github/workflows/build.yml) tarafından derlenir; JAR dosyası o çalıştırmanın artifact'leri arasında bulunur. `v0.0.3` gibi bir etiket ayrıca, JAR dosyasını ve oyundaki güncelleyicinin okuduğu `update.json` bildirimini içeren bir [sürüm](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) yayımlar.

## Kod nereden geliyor

- **Köken:** proje, sunucu paketinin SHA-1'ini yerel dosyalarla karşılaştıran bu deponun ilk prototipinden doğdu.
- **Yazarlar:** McChopik, Telemine3140. Bugünkü kod, proje yazarlarının isteğiyle [Anthropic](https://www.anthropic.com)'in yapay zekâ programlama aracı [Claude Code](https://claude.com/claude-code) ile yeniden yazıldı ve genişletildi. Tek tıkla indirme, paketleme, otomatik etkinleştirme, «reddet ve çık», güncelleyici, çeviriler, Windows, macOS, Linux ve Android için yol işleme ve belgeler eklendi. Başka modlardan kod kopyalanmadı.
- **Üzerine inşa edildiği:** Mojang'ın resmî adlarını taşıyan Minecraft'ın kendi kodu, [Fabric Loader ve Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) ve [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Durum:** deneysel. Kapsamlı test edilmedi, bu yüzden kendi sorumluluğunda kullan.
- Mojang, Microsoft, Fabric veya Anthropic ile **bağlantılı değildir**.

## Lisans

Aşağıdaki lisans metni burada özgün İngilizce haliyle aktarılmıştır; hukuken bağlayıcı olan yalnızca bu İngilizce metindir ve MIT lisansının bir çevirisi onun yerini almaz.

This is an **open-source** project released under the **[MIT License](../LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Katkılar, hata bildirimleri ve pull request'ler memnuniyetle karşılanır: [bir issue aç](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
