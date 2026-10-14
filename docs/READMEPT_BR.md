# localpack-bypass

<p>
  <a href="../LICENSE"><img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT"></a>
  <a href="https://github.com/evgenmakarickij-wq/localpack-bypass"><img src="https://img.shields.io/badge/open%20source-yes-brightgreen.svg" alt="Open Source"></a>
  <a href="https://www.minecraft.net"><img src="https://img.shields.io/badge/Minecraft-26.2-blue.svg" alt="Minecraft 26.2"></a>
  <a href="https://fabricmc.net"><img src="https://img.shields.io/badge/mod%20loader-Fabric-beige.svg" alt="Fabric"></a>
</p>

**Idiomas:** [English](../README.md) · [Українська](READMEUA.md) · [Русский](READMERU.md) · [Беларуская](READMEBE_BY.md) · [Čeština](READMECS_CZ.md) · [Deutsch](READMEDE_DE.md) · [Polski](READMEPL_PL.md) · [Slovenčina](READMESK_SK.md) · [العربية](READMEAR_SA.md) · [Български](READMEBG_BG.md) · [Català](READMECA_ES.md) · [Dansk](READMEDA_DK.md) · [Ελληνικά](READMEEL_GR.md) · [Español](READMEES_ES.md) · [Eesti](READMEET_EE.md) · [Suomi](READMEFI_FI.md) · [Français](READMEFR_FR.md) · [Hrvatski](READMEHR_HR.md) · [Magyar](READMEHU_HU.md) · [Bahasa Indonesia](READMEID_ID.md) · [Íslenska](READMEIS_IS.md) · [Italiano](READMEIT_IT.md) · [日本語](READMEJA_JP.md) · [한국어](READMEKO_KR.md) · [Lietuvių](READMELT_LT.md) · [Latviešu](READMELV_LV.md) · [Nederlands](READMENL_NL.md) · [Norsk](READMENO_NO.md) · **Português (Brasil)** · [Română](READMERO_RO.md) · [Slovenščina](READMESL_SI.md) · [Shqip](READMESQ_AL.md) · [Српски](READMESR_SP.md) · [Svenska](READMESV_SE.md) · [Türkçe](READMETR_TR.md) · [Tiếng Việt](READMEVI_VN.md) · [简体中文](READMEZH_CN.md) · [繁體中文](READMEZH_TW.md)

**Autores:** McChopik, Telemine3140

---

**localpack-bypass** é um mod de [Fabric](https://fabricmc.net) para [Minecraft](https://www.minecraft.net) 26.2 gratuito, de código aberto e do lado do cliente que torna os pacotes de recursos do servidor indolores. Em vez de baixar o mesmo pacote toda vez que você entra, ele compara o SHA-1 do pacote que o servidor envia com os arquivos ZIP que já estão na sua pasta `resourcepacks`. Se um pacote correspondente for encontrado, ele é ativado em silêncio, para que você entre sem avisos nem espera. Se um pacote for novo ou tiver mudado, uma única tela oferece baixar todos os pacotes necessários de uma vez; depois o mod os verifica, os ativa, salva a seleção e recarrega os recursos apenas uma vez. Os pacotes baixados permanecem ativos após você sair do servidor, como os pacotes de recursos normais, enquanto recusar desconecta você do servidor, como no jogo original. As telas e as mensagens seguem o idioma do jogo; o mod inclui traduções para 38 idiomas (veja a lista acima).

## Como funciona

- **Pacote já baixado:** ele é ativado em silêncio. Sem avisos, sem download.
- **Pacote novo ou alterado:** uma tela com **Baixar todos os pacotes de recursos**. Os pacotes são salvos em `resourcepacks/localpack-<sha1>.zip`, verificados por SHA-1, ativados e salvos nas suas opções.
- **Recusar e sair:** você é desconectado do servidor.
- **Pacotes antigos:** os pacotes baixados anteriormente pelo mod são desmarcados quando chega uma versão mais nova. Os arquivos permanecem no disco, então voltar atrás é instantâneo.
- **Nada falha em silêncio:** um download que não funcionou, uma soma de verificação que não bateu ou um arquivo que o jogo não quis ativar são informados na tela, ou como uma notificação se não houver tela disponível.

## Configurações (Mod Menu)

Com o [Mod Menu](https://modrinth.com/mod/modmenu) instalado, o mod aparece na lista de mods com uma tela de configurações própria. Nenhuma tecla de atalho foi adicionada; tudo está lá.

- **Pasta dos pacotes baixados** — onde os pacotes são guardados. Deixe-a vazia para usar a própria pasta `resourcepacks` do jogo. Qualquer outra pasta funciona, inclusive outro disco ou uma pasta compartilhada do Android; os pacotes passam então a ser copiados também para `resourcepacks` antes de serem ativados, porque essa é a única pasta que o próprio jogo lê. Os botões abrem uma ou outra pasta.
- **Lembrar servidores em que você disse sim** — um servidor de onde você já pegou pacotes não é perguntado de novo. A resposta também é gravada na própria configuração por servidor do jogo, *Server Resource Packs*, ao lado de Prompt, Enabled e Disabled.
- **Manter pacotes de outros servidores** — deixe ligado para manter os pacotes que você já tem. Desligue para começar cada servidor apenas com os pacotes daquele servidor.
- **Esperar antes de perguntar** — por quanto tempo coletar o resto de um lote antes que a tela apareça, em milissegundos ou segundos. Os servidores enviam todos os seus pacotes de uma vez, então esperar transforma várias perguntas em uma.
- **Desistir depois de** e **Tentativas** — quanto tempo um download pode levar e quantas vezes um que falhou é tentado de novo. Uma soma de verificação que não bate conta como falha, porque é assim que se parece uma transferência pela metade.
- **Manter um registro** — cada pacote de recursos que um servidor envia é registrado: qual servidor, qual endereço, a soma de verificação, o tamanho, o que aconteceu com ele e quanto tempo levou. É um arquivo no seu próprio computador; nada é enviado a lugar nenhum.
- **Pacotes baixados** — os pacotes que o mod baixou, agrupados pelo servidor de onde vieram, mostrando se cada um está ligado, desligado ou sumiu da pasta. Pressione um para ligá-lo ou desligá-lo; um pacote cujo arquivo desapareceu pode ser esquecido. **Esquecer tudo** limpa o que o mod lembra e nunca apaga um arquivo de pacote.
- **O que os servidores enviaram** — o próprio registro, do mais recente para o mais antigo, com os detalhes de cada entrada.

## Atualizações

Uma vez por dia o mod pergunta ao GitHub pela [última versão](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) deste repositório. Se ela for mais nova que a versão que você usa, a tela de título oferece instalá-la. Aceitar faz três coisas:

1. O novo arquivo JAR é baixado e o seu **SHA-256 é verificado** com o manifesto `update.json` publicado com essa versão.
2. O arquivo JAR antigo é excluído e o novo é movido para `mods/`.
3. Você reinicia o jogo.

No Windows não é possível excluir um arquivo JAR que está carregado no momento, por isso um pequeno script gerado espera o jogo fechar e executa o passo 2 por você; o mod avisa quando isso acontece. No Android, Linux e macOS o arquivo JAR em execução não fica bloqueado e é simplesmente substituído. Tudo o que o atualizador toca está em `mods/` e `config/localpack-bypass/`.

A verificação é uma solicitação anônima comum à API do GitHub (sem token, sem conta); o mod não tem telemetria nem servidor próprio. Uma versão ignorada é lembrada, e `-Dlocalpack.bypass.updateCheck=false` desativa as atualizações por completo.

**Ressalva sobre a numeração de versões:** `0.0.1.2026-alpha-build-beta-release` é lido como `[0, 0, 1, 2026]`, que se ordena *abaixo* da anterior `0.0.3-beta` (`[0, 0, 3]`). Builds numeradas `0.0.3-beta` ou superiores não receberão esta automaticamente e terão de ser substituídas à mão.

## Requisitos

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 ou mais recente
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25
- Windows, macOS, Linux ou um launcher de Android como o [PojavLauncher](https://pojavlauncherteam.github.io/): cada arquivo que o mod toca é obtido do diretório de jogo do launcher, então não é preciso configurar nenhuma pasta à mão
- Opcional: [Mod Menu](https://modrinth.com/mod/modmenu), que mostra o nome e a descrição do mod no seu idioma e dá ao mod uma tela de configurações

## Compilar você mesmo

O mod é compilado a cada push para a `main` pelo [fluxo de trabalho do GitHub Actions](../.github/workflows/build.yml) e o arquivo JAR fica disponível nos artefatos da execução. Uma tag como `v0.0.3` também publica uma [versão](https://github.com/evgenmakarickij-wq/localpack-bypass/releases) com o arquivo JAR e o manifesto `update.json` que o atualizador lê dentro do jogo.

## De onde vem o código

- **Origem:** o projeto surgiu do protótipo original deste repositório, que comparava o SHA-1 do pacote do servidor com os arquivos locais.
- **Autores:** McChopik, Telemine3140. O código atual foi reescrito e ampliado com o [Claude Code](https://claude.com/claude-code), uma ferramenta de programação com IA da [Anthropic](https://www.anthropic.com), a pedido dos autores do projeto. Foram adicionados o download com um clique, o agrupamento, a ativação automática, «Recusar e sair», o atualizador, as traduções, o tratamento de caminhos para Windows, macOS, Linux e Android, e a documentação. Nenhum código foi copiado de outros mods.
- **Construído sobre:** o próprio código do Minecraft com os nomes oficiais da Mojang, [Fabric Loader e Fabric API](https://fabricmc.net), [Fabric Loom](https://github.com/FabricMC/fabric-loom) e [SpongePowered Mixin](https://github.com/SpongePowered/Mixin).
- **Status:** experimental. Não foi testado extensivamente, então use por sua conta e risco.
- **Sem vínculo** com Mojang, Microsoft, Fabric ou Anthropic.

## Licença

O texto da licença abaixo é reproduzido no seu inglês original; somente essa redação em inglês tem validade legal, e uma tradução da licença MIT não a substituiria.

This is an **open-source** project released under the **[MIT License](../LICENSE)** ([summary](https://opensource.org/license/mit)). Copyright (c) 2026 McChopik, Telemine3140. You may use, copy, modify, merge, publish and distribute it, including commercially, as long as the copyright and license notice are kept. The software is provided "as is", without warranty of any kind.

Contribuições, relatórios de erros e pull requests são bem-vindos: [abra uma issue](https://github.com/evgenmakarickij-wq/localpack-bypass/issues).
