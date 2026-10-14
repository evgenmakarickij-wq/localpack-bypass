# localpack-bypass

<p>
  <a href="../LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Ngôn ngữ:** [English](../README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · **Tiếng Việt** · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Tác giả:** McChopik, Telemine3140

---

**localpack-bypass** là một mod [Fabric](https://fabricmc.net) phía máy khách, miễn phí và mã nguồn mở dành cho [Minecraft](https://www.minecraft.net) 26.2, giúp việc dùng gói tài nguyên của máy chủ trở nên nhẹ nhàng. Thay vì tải lại cùng một gói mỗi lần vào máy chủ, nó so sánh SHA-1 của gói mà máy chủ gửi với các tệp ZIP đã có trong thư mục `resourcepacks` của bạn. Nếu tìm thấy gói khớp, gói sẽ được bật một cách lặng lẽ, nên bạn vào mà không bị hỏi hay phải chờ. Nếu gói là mới hoặc đã thay đổi, một màn hình sẽ cho phép tải tất cả các gói cần thiết cùng một lúc; sau đó mod xác minh, bật chúng, lưu lựa chọn và chỉ tải lại tài nguyên một lần. Các gói đã tải vẫn được bật sau khi bạn rời máy chủ, giống như gói tài nguyên bình thường, còn nếu từ chối, bạn sẽ bị ngắt kết nối khỏi máy chủ như trong bản gốc. Màn hình và thông báo đi theo ngôn ngữ của trò chơi; mod đi kèm bản dịch cho 38 ngôn ngữ (xem danh sách ngôn ngữ ở trên).

## Cách hoạt động

- **Gói đã tải trước đó:** được bật lặng lẽ. Không hỏi, không tải.
- **Gói mới hoặc đã thay đổi:** một màn hình với **Tải tất cả gói tài nguyên**. Gói được lưu vào `resourcepacks/localpack-<sha1>.zip`, được xác minh bằng SHA-1, bật và lưu vào cài đặt của bạn.
- **Từ chối và rời đi:** bạn bị ngắt kết nối khỏi máy chủ.
- **Gói cũ:** những gói mod đã tải trước đó sẽ bị bỏ chọn khi một phiên bản mới hơn xuất hiện. Các tệp vẫn còn trên đĩa, nên có thể quay lại ngay lập tức.
- **Không có gì thất bại trong im lặng:** một lượt tải không thành công, một mã kiểm tra không khớp, hoặc một tệp mà trò chơi không chịu bật đều được báo trên màn hình, hoặc dưới dạng thông báo nếu không có màn hình nào.

## Cài đặt (Mod Menu)

Với [Mod Menu](https://modrinth.com/mod/modmenu) được cài đặt, mod xuất hiện trong danh sách mod với một màn hình cài đặt riêng. Không có phím tắt nào được thêm vào; mọi thứ đều nằm ở đó.

- **Thư mục cho các gói đã tải** — nơi lưu các gói. Để trống để dùng thư mục `resourcepacks` của chính trò chơi. Bất kỳ thư mục nào khác đều được, kể cả một ổ đĩa khác hay thư mục chia sẻ trên Android; khi đó các gói cũng được sao chép vào `resourcepacks` trước khi được bật, vì đó là thư mục duy nhất mà trò chơi tự đọc. Các nút sẽ mở một trong hai thư mục.
- **Ghi nhớ những máy chủ bạn đã đồng ý** — một máy chủ bạn đã từng lấy gói sẽ không bị hỏi lại. Câu trả lời cũng được ghi vào cài đặt *Server Resource Packs* theo từng máy chủ của chính trò chơi, bên cạnh Prompt, Enabled và Disabled.
- **Giữ các gói từ máy chủ khác** — bật tùy chọn này để giữ những gói bạn đã có. Tắt nó đi để bắt đầu mỗi máy chủ chỉ với các gói của máy chủ đó.
- **Chờ trước khi hỏi** — thời gian thu thập phần còn lại của một lô trước khi màn hình hiện ra, tính bằng mili giây hoặc giây. Máy chủ gửi tất cả các gói cùng một lúc, nên chờ đợi biến nhiều câu hỏi thành một.
- **Từ bỏ sau** và **Số lần thử** — một lượt tải có thể mất bao lâu, và một lượt thất bại được thử lại bao nhiêu lần. Một mã kiểm tra không khớp được tính là thất bại, vì đó là dáng vẻ của một quá trình truyền tải dở dang.
- **Ghi lại nhật ký** — mọi gói tài nguyên mà máy chủ gửi đều được ghi lại: máy chủ nào, địa chỉ nào, mã kiểm tra, kích thước, điều gì đã xảy ra với nó và mất bao lâu. Đó là một tệp trên máy tính của chính bạn; không có gì được gửi đi đâu cả.
- **Các gói đã tải** — những gói mà mod đã tải, được nhóm theo máy chủ mà chúng đến từ, cho biết mỗi gói đang bật, tắt hay đã biến mất khỏi thư mục. Nhấn vào một gói để bật hoặc tắt nó; một gói mà tệp đã biến mất có thể bị quên đi. **Quên tất cả** xóa những gì mod ghi nhớ và không bao giờ xóa tệp gói nào.
- **Những gì máy chủ đã gửi** — chính nhật ký đó, mới nhất trước, kèm chi tiết của từng mục.

## Cập nhật

Mỗi ngày một lần, mod hỏi GitHub về [bản phát hành mới nhất](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) của kho lưu trữ này. Nếu nó mới hơn phiên bản bạn đang dùng, màn hình tiêu đề sẽ mời bạn cài đặt. Chấp nhận sẽ làm ba việc:

1. Tệp JAR mới được tải về và **SHA-256 của nó được kiểm tra** dựa trên tệp kê khai `update.json` được phát hành kèm phiên bản đó.
2. Tệp JAR cũ bị xóa và tệp mới được chuyển vào `mods/`.
3. Bạn khởi động lại trò chơi.

Trên Windows, không thể xóa một tệp JAR đang được nạp, nên một tập lệnh ngắn được tạo ra sẽ chờ trò chơi thoát rồi thực hiện bước 2 giúp bạn; mod sẽ thông báo khi điều đó xảy ra. Trên Android, Linux và macOS, tệp JAR đang chạy không bị khóa và chỉ đơn giản được thay thế. Mọi thứ mà trình cập nhật chạm đến đều nằm trong `mods/` và `config/localpack-bypass/`.

Việc kiểm tra là một yêu cầu ẩn danh thông thường tới GitHub API (không cần mã thông báo, không cần tài khoản), mod không có dữ liệu đo lường và không có máy chủ riêng. Phiên bản bị bỏ qua sẽ được ghi nhớ, và `-Dlocalpack.bypass.updateCheck=false` tắt hoàn toàn việc cập nhật.

**Lưu ý về cách đánh số phiên bản:** `0.0.1.2026-alpha-build-beta-release` được đọc là `[0, 0, 1, 2026]`, xếp *dưới* `0.0.3-beta` (`[0, 0, 3]`) trước đó. Các bản dựng được đánh số `0.0.3-beta` trở lên sẽ không tự động nhận được phiên bản này và phải được thay thế thủ công.

## Yêu cầu

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 trở lên
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux hoặc trình khởi chạy Android như [PojavLauncher](https://pojavlauncherteam.github.io/): mọi tệp mà mod chạm đến đều được xác định từ thư mục trò chơi của trình khởi chạy, nên không cần thiết lập thư mục thủ công
- Tùy chọn: [Mod Menu](https://modrinth.com/mod/modmenu), hiển thị tên và mô tả mod bằng ngôn ngữ của bạn và mang đến cho mod một màn hình cài đặt

## Tự xây dựng

Mod được [quy trình GitHub Actions](../.github/workflows/build.yml) xây dựng mỗi khi đẩy lên `main`, tệp JAR nằm trong phần Artifacts của lần chạy. Đẩy một thẻ như `v0.0.3` cũng sẽ phát hành một [bản phát hành](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) kèm tệp JAR và tệp kê khai `update.json` mà trình cập nhật trong trò chơi đọc.

## Mã nguồn đến từ đâu

- **Khởi nguồn:** dự án bắt đầu từ nguyên mẫu ban đầu của kho lưu trữ này, vốn so sánh SHA-1 của gói máy chủ với các tệp cục bộ.
- **Tác giả:** McChopik, Telemine3140. Mã hiện tại được viết lại và mở rộng bằng [Claude Code](https://claude.com/claude-code), một công cụ lập trình AI của [Anthropic](https://www.anthropic.com), theo yêu cầu của các tác giả dự án. Nó bổ sung tải xuống bằng một cú nhấp, gộp nhóm, tự động bật, "từ chối và rời đi", trình cập nhật, các bản dịch, xử lý đường dẫn cho Windows, macOS, Linux và Android, cùng tài liệu. Không có mã nào được sao chép từ các mod khác.
- **Dựa trên:** mã của chính Minecraft với tên chính thức của Mojang, [Fabric Loader và Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) và [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Trạng thái:** thử nghiệm. Nó chưa được kiểm thử rộng rãi, nên hãy dùng với rủi ro của riêng bạn.
- **Không liên kết** với Mojang, Microsoft, Fabric hay Anthropic.

## Giấy phép

Văn bản giấy phép bên dưới được trình bày nguyên bản bằng tiếng Anh; chỉ cách diễn đạt tiếng Anh đó mới có tính ràng buộc pháp lý, và một bản dịch của giấy phép MIT sẽ không thay thế được nó.

This is an **open-source** project released under the **[MIT License](../LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Hoan nghênh đóng góp, báo lỗi và yêu cầu kéo: [mở một issue](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
