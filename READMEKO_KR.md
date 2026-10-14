# localpack-bypass

<p>
  <a href="LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**언어:** [English](README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · **한국어** · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**제작자:** McChopik, Telemine3140

---

**localpack-bypass**는 서버 리소스 팩을 번거롭지 않게 만들어 주는, [Minecraft](https://www.minecraft.net) 26.2용 무료 오픈 소스 클라이언트 측 [Fabric](https://fabricmc.net) 모드입니다. 서버에 접속할 때마다 같은 팩을 다시 다운로드하는 대신, 서버가 보낸 팩의 SHA-1을 `resourcepacks` 폴더에 이미 있는 ZIP 파일과 비교합니다. 일치하는 팩을 찾으면 알림 없이 활성화되므로, 아무런 확인이나 대기 없이 접속할 수 있습니다. 팩이 새로 추가되었거나 변경되었다면, 하나의 화면에서 필요한 팩을 한 번에 모두 다운로드할 수 있습니다. 이후 모드가 이를 검증하고 활성화하며, 선택을 저장하고 리소스를 한 번만 다시 불러옵니다. 다운로드한 팩은 일반 리소스 팩처럼 서버를 나간 뒤에도 계속 활성화되어 있습니다. 반면 거부하면 바닐라와 마찬가지로 서버에서 연결이 끊깁니다. 화면과 메시지는 게임 언어를 따르며, 모드에는 38개 언어의 번역이 포함되어 있습니다(위의 언어 목록 참조).

## 작동 방식

- **이미 다운로드한 팩:** 알림 없이 활성화됩니다. 확인도 다운로드도 없습니다.
- **새로 추가되었거나 변경된 팩:** **모든 리소스 팩 다운로드**가 있는 화면 하나가 표시됩니다. 팩은 `resourcepacks/localpack-<sha1>.zip`에 저장되고, SHA-1로 검증되고, 활성화되어 옵션에 저장됩니다.
- **거부하고 나가기:** 서버에서 연결이 끊깁니다.
- **오래된 팩:** 모드가 이전에 다운로드한 팩은 더 새로운 버전이 도착하면 선택이 해제됩니다. 파일은 디스크에 남아 있으므로 언제든지 즉시 되돌릴 수 있습니다.

## 업데이트

모드는 하루에 한 번 GitHub에 이 저장소의 [최신 릴리스](https://github.com/evgenmakarickij-wq/localpack-bypass/releases)를 요청합니다. 사용 중인 버전보다 새로우면 타이틀 화면에서 설치를 제안합니다. 수락하면 세 가지가 일어납니다:

1. 새 JAR이 다운로드되고, 해당 릴리스와 함께 게시된 `update.json` 매니페스트에 대해 **SHA-256이 검사됩니다**.
2. 이전 JAR이 삭제되고 새 JAR이 `mods/`로 이동됩니다.
3. 게임을 다시 시작합니다.

Windows에서는 현재 로드된 JAR을 삭제할 수 없으므로, 짧은 생성 스크립트가 게임이 종료되기를 기다렸다가 2단계를 대신 수행합니다. 이 경우 모드가 알려 줍니다. Android, Linux, macOS에서는 실행 중인 JAR이 잠기지 않으므로 그냥 교체됩니다. 업데이터가 건드리는 모든 것은 `mods/`와 `config/localpack-bypass/` 아래에 있습니다.

이 확인은 GitHub API에 대한 일반적인 익명 요청입니다(토큰도 계정도 필요 없습니다). 모드에는 텔레메트리도 자체 서버도 없습니다. 건너뛴 버전은 기억되며, `-Dlocalpack.bypass.updateCheck=false`로 업데이트를 완전히 끌 수 있습니다.

## 요구 사항

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 이상
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux 또는 [PojavLauncher](https://pojavlauncherteam.github.io/) 같은 Android 런처: 모드가 건드리는 모든 파일은 런처의 게임 디렉터리에서 확인되므로 폴더를 직접 설정할 필요가 없습니다
- 선택 사항: [Mod Menu](https://modrinth.com/mod/modmenu), 모드 이름과 설명을 사용자의 언어로 표시합니다

## 직접 빌드하기

모드는 `main`에 푸시할 때마다 [GitHub Actions 워크플로](.github/workflows/build.yml)에서 빌드되며, JAR은 그 실행의 아티팩트에서 확인할 수 있습니다. `v0.0.3` 같은 태그를 푸시하면 JAR과 게임 내 업데이터가 읽는 `update.json` 매니페스트가 포함된 [릴리스](https://github.com/evgenmakarickij-wq/localpack-bypass/releases)도 함께 게시됩니다.

## 코드의 출처

- **기원:** 이 프로젝트는 서버 팩의 SHA-1을 로컬 파일과 비교하던 이 저장소의 원래 프로토타입에서 시작되었습니다.
- **제작자:** McChopik, Telemine3140. 현재 코드는 프로젝트 제작자의 요청에 따라 [Anthropic](https://www.anthropic.com)의 AI 코딩 도구인 [Claude Code](https://claude.com/claude-code)로 다시 작성되고 확장되었습니다. 원클릭 다운로드, 일괄 처리, 자동 활성화, "거부하고 나가기", 업데이터, 번역, Windows·macOS·Linux·Android를 아우르는 경로 처리, 그리고 문서가 추가되었습니다. 다른 모드에서 코드를 복사하지 않았습니다.
- **기반:** Mojang의 공식 이름을 사용한 Minecraft 자체 코드, [Fabric Loader와 Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom), [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **상태:** 실험적입니다. 광범위하게 테스트되지 않았으므로 사용에 따른 위험은 본인이 감수하세요.
- Mojang, Microsoft, Fabric, Anthropic과 **관련이 없습니다**.

## 라이선스

아래의 라이선스 본문은 원문 그대로 영어로 실려 있습니다. 법적 구속력이 있는 것은 이 영어 문구뿐이며, MIT 라이선스의 번역본이 이를 대체하지는 않습니다.

This is an **open-source** project released under the **[MIT License](LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

기여, 버그 보고, 풀 리퀘스트를 환영합니다: [이슈 열기](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
