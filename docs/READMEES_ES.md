# localpack-bypass

<p>
  <a href="../LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Idiomas:** [English](../README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · **Español** · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · [Português (Brasil)](READMEPT_BR.md) · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Autores:** McChopik, Telemine3140

---

**localpack-bypass** es un mod de [Fabric](https://fabricmc.net) para [Minecraft](https://www.minecraft.net) 26.2 gratuito, de código abierto y del lado del cliente que hace que los paquetes de recursos del servidor dejen de ser un fastidio. En lugar de descargar el mismo paquete cada vez que entras, compara el SHA-1 del paquete que envía el servidor con los archivos ZIP que ya están en tu carpeta `resourcepacks`. Si se encuentra un paquete que coincide, se activa en silencio, así entras sin avisos ni esperas. Si un paquete es nuevo o ha cambiado, una sola pantalla ofrece descargar todos los paquetes necesarios de una vez; después el mod los verifica, los activa, guarda la selección y recarga los recursos una sola vez. Los paquetes descargados siguen activos tras salir del servidor, como los paquetes de recursos normales, mientras que rechazarlos te desconecta del servidor, igual que en el juego original. Las pantallas y los mensajes siguen el idioma del juego; el mod incluye traducciones para 38 idiomas (consulta la lista de arriba).

## Cómo funciona

- **Paquete ya descargado:** se activa en silencio. Sin avisos, sin descargas.
- **Paquete nuevo o modificado:** una pantalla con **Descargar todos los paquetes de recursos**. Los paquetes se guardan en `resourcepacks/localpack-<sha1>.zip`, se verifican por SHA-1, se activan y se guardan en tus opciones.
- **Rechazar y salir:** te desconectas del servidor.
- **Paquetes antiguos:** los paquetes descargados antes por el mod se desmarcan cuando llega una versión más reciente. Los archivos permanecen en el disco, así que volver atrás es instantáneo.

## Actualizaciones

Una vez al día el mod le pregunta a GitHub por la [última versión](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) de este repositorio. Si es más nueva que la versión que usas, la pantalla de título ofrece instalarla. Aceptar hace tres cosas:

1. El nuevo archivo JAR se descarga y se **verifica su SHA-256** con el manifiesto `update.json` publicado con esa versión.
2. El archivo JAR antiguo se elimina y el nuevo se mueve a `mods/`.
3. Reinicias el juego.

En Windows no se puede eliminar un archivo JAR que está cargado en ese momento, así que un pequeño script generado espera a que el juego se cierre y hace el paso 2 por ti; el mod te lo avisa cuando ocurre. En Android, Linux y macOS el archivo JAR en ejecución no está bloqueado y simplemente se reemplaza. Todo lo que toca el actualizador está en `mods/` y `config/localpack-bypass/`.

La comprobación es una petición anónima normal a la API de GitHub (sin token ni cuenta); el mod no tiene telemetría ni servidor propio. Una versión omitida se recuerda, y `-Dlocalpack.bypass.updateCheck=false` desactiva las actualizaciones por completo.

## Requisitos

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 o posterior
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux o un lanzador de Android como [PojavLauncher](https://pojavlauncherteam.github.io/): cada archivo que toca el mod se obtiene del directorio de juego del lanzador, así que no hay que configurar ninguna carpeta a mano
- Opcional: [Mod Menu](https://modrinth.com/mod/modmenu), que muestra el nombre y la descripción del mod en tu idioma

## Compilarlo tú mismo

El mod se compila en cada push a `main` mediante el [flujo de trabajo de GitHub Actions](../.github/workflows/build.yml) y el archivo JAR está disponible en los artefactos de la ejecución. Una etiqueta como `v0.0.3` publica además una [versión](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) con el archivo JAR y el manifiesto `update.json` que lee el actualizador dentro del juego.

## De dónde viene el código

- **Origen:** el proyecto partió del prototipo original de este repositorio, que comparaba el SHA-1 del paquete del servidor con los archivos locales.
- **Autores:** McChopik, Telemine3140. El código actual se reescribió y amplió con [Claude Code](https://claude.com/claude-code), una herramienta de programación con IA de [Anthropic](https://www.anthropic.com), a petición de los autores del proyecto. Se añadieron la descarga con un clic, el agrupamiento, la activación automática, «Rechazar y salir», el actualizador, las traducciones, el tratamiento de rutas para Windows, macOS, Linux y Android, y la documentación. No se copió código de otros mods.
- **Construido sobre:** el propio código de Minecraft con los nombres oficiales de Mojang, [Fabric Loader y Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) y [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Estado:** experimental. No se ha probado a fondo, así que úsalo bajo tu propio riesgo.
- **Sin relación** con Mojang, Microsoft, Fabric ni Anthropic.

## Licencia

El texto de la licencia que aparece abajo se reproduce en su inglés original; solo esa redacción en inglés tiene validez legal, y una traducción de la licencia MIT no la sustituiría.

This is an **open-source** project released under the **[MIT License](../LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Las contribuciones, los informes de errores y las solicitudes de cambio son bienvenidos: [abre una incidencia](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
