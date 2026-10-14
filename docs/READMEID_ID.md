# localpack-bypass

<p>
  <a href="../LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Bahasa:** [English](../README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · **Bahasa Indonesia** · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Penulis:** McChopik, Telemine3140

---

**localpack-bypass** adalah mod [Fabric](https://fabricmc.net) sisi klien yang gratis dan bersumber terbuka untuk [Minecraft](https://www.minecraft.net) 26.2, yang membuat paket sumber daya server menjadi tidak merepotkan. Alih-alih mengunduh ulang paket yang sama setiap kali bergabung, mod ini membandingkan SHA-1 dari paket yang dikirim server dengan berkas ZIP yang sudah ada di folder `resourcepacks` Anda. Jika ditemukan paket yang cocok, paket itu diaktifkan secara diam-diam, sehingga Anda bergabung tanpa diminta dan tanpa menunggu. Jika paket baru atau berubah, satu layar menawarkan untuk mengunduh semua paket yang dibutuhkan sekaligus; mod lalu memverifikasinya, mengaktifkannya, menyimpan pilihan, dan memuat ulang sumber daya hanya sekali. Paket yang telah diunduh tetap aktif setelah Anda meninggalkan server, seperti paket sumber daya biasa, sedangkan menolak akan memutuskan Anda dari server, seperti pada versi vanilla. Layar dan pesan mengikuti bahasa permainan; mod ini menyertakan terjemahan untuk 38 bahasa (lihat daftar bahasa di atas).

## Cara kerjanya

- **Paket sudah diunduh:** diaktifkan secara diam-diam. Tanpa permintaan, tanpa unduhan.
- **Paket baru atau berubah:** satu layar dengan **Unduh semua paket sumber daya**. Paket disimpan ke `resourcepacks/localpack-<sha1>.zip`, diverifikasi dengan SHA-1, diaktifkan, dan disimpan ke opsi Anda.
- **Menolak dan keluar:** Anda diputuskan dari server.
- **Paket lama:** paket yang sebelumnya diunduh oleh mod akan dibatalkan pilihannya saat versi yang lebih baru datang. Berkasnya tetap ada di disk, jadi berpindah kembali bisa seketika.
- **Tidak ada yang gagal secara diam-diam:** unduhan yang tidak berhasil, checksum yang tidak cocok, atau berkas yang tidak mau diaktifkan oleh permainan dilaporkan di layar, atau sebagai notifikasi jika tidak ada layar yang tersedia.

## Pengaturan (Mod Menu)

Dengan [Mod Menu](https://modrinth.com/mod/modmenu) terpasang, mod muncul di daftar mod dengan layar pengaturannya sendiri. Tidak ada tombol pintas yang ditambahkan; semuanya ada di sana.

- **Folder untuk paket yang diunduh** — tempat paket disimpan. Biarkan kosong untuk memakai folder `resourcepacks` milik permainan sendiri. Folder lain apa pun bisa dipakai, termasuk drive lain atau folder bersama Android; paket kemudian juga disalin ke `resourcepacks` sebelum diaktifkan, karena hanya folder itulah yang dibaca oleh permainan sendiri. Tombol membuka salah satu folder.
- **Ingat server yang telah Anda setujui** — server yang paketnya sudah pernah Anda ambil tidak akan ditanyakan lagi. Jawabannya juga ditulis ke pengaturan *Server Resource Packs* per server milik permainan sendiri, di samping Prompt, Enabled, dan Disabled.
- **Simpan paket dari server lain** — biarkan ini aktif untuk menyimpan paket yang sudah Anda miliki. Matikan untuk memulai setiap server hanya dengan paket dari server itu.
- **Tunggu sebelum bertanya** — berapa lama menunggu sisa satu kumpulan sebelum layarnya muncul, dalam milidetik atau detik. Server mengirim semua paketnya sekaligus, jadi menunggu mengubah beberapa pertanyaan menjadi satu.
- **Menyerah setelah** dan **Percobaan** — berapa lama satu unduhan boleh berlangsung, dan berapa kali unduhan yang gagal dicoba lagi. Checksum yang tidak cocok dianggap sebagai kegagalan, karena seperti itulah transfer yang belum selesai.
- **Simpan catatan** — setiap paket sumber daya yang dikirim server dicatat: server mana, alamat mana, checksum, ukuran, apa yang terjadi padanya dan berapa lama prosesnya. Itu adalah berkas di komputer Anda sendiri; tidak ada yang dikirim ke mana pun.
- **Paket yang diunduh** — paket yang telah diunduh mod, dikelompokkan menurut server asalnya, menampilkan apakah masing-masing aktif, nonaktif, atau hilang dari folder. Tekan salah satunya untuk mengaktifkan atau menonaktifkannya; paket yang berkasnya hilang bisa dilupakan. **Lupakan semuanya** menghapus apa yang diingat mod dan tidak pernah menghapus berkas paket.
- **Apa yang dikirim server** — catatannya sendiri, yang terbaru lebih dulu, dengan rincian setiap entri.

## Pembaruan

Sekali sehari mod menanyakan GitHub tentang [rilis terbaru](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) dari repositori ini. Jika lebih baru daripada versi yang Anda jalankan, layar judul menawarkan untuk memasangnya. Menerima akan melakukan tiga hal:

1. Berkas JAR baru diunduh dan **SHA-256-nya diperiksa** terhadap manifes `update.json` yang diterbitkan bersama rilis tersebut.
2. Berkas JAR lama dihapus dan yang baru dipindahkan ke `mods/`.
3. Anda memulai ulang permainan.

Di Windows, berkas JAR yang sedang dimuat tidak dapat dihapus, jadi skrip singkat yang dihasilkan akan menunggu permainan keluar dan melakukan langkah 2 untuk Anda; mod akan memberi tahu saat itu terjadi. Di Android, Linux, dan macOS, berkas JAR yang berjalan tidak terkunci dan langsung diganti. Semua yang disentuh oleh updater berada di bawah `mods/` dan `config/localpack-bypass/`.

Pemeriksaan ini adalah permintaan anonim biasa ke GitHub API (tanpa token, tanpa akun), mod tidak memiliki telemetri maupun server sendiri. Versi yang dilewati akan diingat, dan `-Dlocalpack.bypass.updateCheck=false` menonaktifkan pembaruan sepenuhnya.

**Catatan tentang penomoran versi:** `0.0.1.2026-alpha-build-beta-release` dibaca sebagai `[0, 0, 1, 2026]`, yang berada *di bawah* `0.0.3-beta` yang lebih dahulu (`[0, 0, 3]`). Versi bernomor `0.0.3-beta` atau lebih tinggi tidak akan otomatis ditawari versi ini dan harus diganti secara manual.

## Persyaratan

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 atau lebih baru
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux, atau peluncur Android seperti [PojavLauncher](https://pojavlauncherteam.github.io/): setiap berkas yang disentuh mod diselesaikan dari direktori permainan peluncur, jadi tidak ada folder yang perlu disiapkan secara manual
- Opsional: [Mod Menu](https://modrinth.com/mod/modmenu), yang menampilkan nama dan deskripsi mod dalam bahasa Anda dan memberi mod ini layar pengaturan

## Membangun sendiri

Mod dibangun oleh [alur kerja GitHub Actions](../.github/workflows/build.yml) pada setiap dorongan ke `main`, dengan berkas JAR tersedia di bagian Artifacts dari proses tersebut. Dorong tag seperti `v0.0.3` untuk juga menerbitkan sebuah [rilis](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) beserta berkas JAR dan manifes `update.json` yang dibaca oleh updater dalam permainan.

## Asal kode

- **Asal:** proyek ini dimulai dari prototipe asli repositori ini, yang membandingkan SHA-1 dari paket server dengan berkas lokal.
- **Penulis:** McChopik, Telemine3140. Kode saat ini ditulis ulang dan diperluas dengan [Claude Code](https://claude.com/claude-code), alat pemrograman AI dari [Anthropic](https://www.anthropic.com), atas permintaan para penulis proyek. Ini menambahkan unduhan sekali klik, pengelompokan, pengaktifan otomatis, "menolak dan keluar", updater, terjemahan, penanganan jalur yang mencakup Windows, macOS, Linux, dan Android, serta dokumentasi. Tidak ada kode yang disalin dari mod lain.
- **Dibangun di atas:** kode Minecraft sendiri dengan nama resmi Mojang, [Fabric Loader dan Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom), dan [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Status:** eksperimental. Ini belum diuji secara menyeluruh, jadi gunakan dengan risiko Anda sendiri.
- **Tidak berafiliasi** dengan Mojang, Microsoft, Fabric, atau Anthropic.

## Lisensi

Teks lisensi di bawah ini disajikan dalam bahasa Inggris aslinya; hanya kata-kata berbahasa Inggris itulah yang mengikat secara hukum, dan terjemahan lisensi MIT tidak akan menggantikannya.

This is an **open-source** project released under the **[MIT License](../LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Kontribusi, laporan bug, dan permintaan tarik (pull request) dipersilakan: [buka sebuah issue](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
