# Chatbot Pessoal

Um chatbot inteligente desenvolvido em Java que aprende dinamicamente através de interações com o usuário. O sistema utiliza correspondência de palavras-chave e permite que o usuário ensine novos conhecimentos ao bot durante a conversa.

## Características

- **Aprendizado dinâmico**: O chatbot pode aprender novos assuntos durante a conversa
- **Processamento de texto**: Remove acentos e caracteres especiais para melhor correspondência
- **Respostas variadas**: Sistema de respostas aleatórias para tornar a conversa mais natural
- **Comandos especiais**: Funcionalidades administrativas integradas
- **Interface amigável**: Menu de ajuda e instruções claras

## Funcionalidades

### Comandos Disponíveis
- `sair` ou `exit` - Encerra o chatbot
- `ensinar` - Ensina o chatbot sobre um novo assunto
- `ajuda` ou `help` - Exibe o menu de ajuda
- `listar` ou `conhecimento` - Mostra todas as palavras-chave aprendidas

### Capacidades do Chatbot
- Reconhecimento de saudações (`oi`, `olá`, `hey`)
- Resposta a agradecimentos (`obrigado`)
- Sistema de correspondência por palavras-chave
- Remoção automática de stop words
- Tratamento de acentos e pontuação

## Tecnologias Utilizadas

- **Java** - Linguagem principal
- **HashMap** - Armazenamento do conhecimento
- **Scanner** - Interface de entrada do usuário
- **Random** - Geração de respostas variadas

## Como Executar

### Pré-requisitos
- Java 8 ou superior instalado
- Terminal ou IDE Java

### Passos para execução

1. **Clone o repositório**
   ```bash
   git clone https://github.com/seu-usuario/chatbot-pessoal.git
   cd chatbot-pessoal
   ```

2. **Compile o código**
   ```bash
   javac -d . src/entities/Chatbot.java Main.java
   ```

3. **Execute o programa**
   ```bash
   java Main
   ```

### Exemplo de uso
```
=== CHABOT ===
Digite seu nome: João

=== MENU DE AJUDA ===
- sair/exit: encerra o chatbot
- ensinar: ensina o chatbot algo novo
- ajuda/help: mostra o menu de ajuda
- listar/conhecimento: mostra a lista de conhecimento do bot

João: oi
Chatbot: Olá, como vai João?

João: ensinar
Me diga a palavra-chave para o assunto principal, por favor (ex: tempo): futebol
Me diga agora como responder a esta palavra chave, por favor: Futebol é um esporte muito popular no Brasil!
Chatbot: Obrigado, agora eu sei um pouco sobre 'futebol'

João: me fale sobre futebol
Chatbot: Futebol é um esporte muito popular no Brasil!
```

## Estrutura do Projeto

```
chatbot-pessoal/
├── src/
│   ├── entities/
│   │   └── Chatbot.java    # Classe principal do chatbot
│   └── application/
│       └── Program.java    # Classe principal para execução
├── .gitignore             # Arquivos ignorados pelo Git
└── README.md              # Este arquivo
```

## Como Funciona

### Sistema de Conhecimento
O chatbot utiliza um `HashMap<String, List<String>>` para armazenar:
- **Chave**: Palavra-chave do assunto
- **Valor**: Lista de possíveis respostas para aquela palavra-chave

### Processamento de Mensagens
1. **Normalização**: Remove acentos e converte para minúsculas
2. **Limpeza**: Remove pontuação desnecessária
3. **Extração**: Separa palavras relevantes (remove stop words)
4. **Correspondência**: Busca por palavras-chave conhecidas
5. **Resposta**: Retorna uma resposta aleatória relacionada

### Sistema de Pontuação
O algoritmo calcula um score baseado na correspondência de palavras-chave:
- Encontrou palavra-chave = 1 ponto
- Escolhe a palavra-chave com melhor score
- Em caso de empate, escolhe aleatoriamente

## 📋 Tópicos de Desenvolvimento

### Tópico 1: Normalização de Texto
- [x] Função dedicada para limpar e normalizar entradas
- [x] Remoção de acentos e caracteres especiais  
- [x] Conversão para lowercase
- [x] Remoção de pontuação (!?)
- [x] Divisão de frases em palavras individuais

**Implementado em**: `tratarMensagem()` e `extrairPalavras()`

### Tópico 2: Sistema de Correspondência  
- [x] Busca por palavras individuais
- [x] Sistema de pontuação para determinar melhor correspondência
- [x] Tratamento de stop words (palavras irrelevantes)
- [x] Seleção aleatória entre palavras com mesmo score

**Implementado em**: `gerarResposta()` com sistema de scoring

### Tópico 3: Estrutura de Dados
- [x] Uso de palavras individuais como chaves
- [x] Múltiplas respostas por palavra-chave
- [x] HashMap para armazenamento eficiente
- [x] Sistema de conhecimento expandível

**Implementado em**: `Map<String, List<String>> conhecimento`

### Tópico 4: Experiência do Usuário
- [x] Comando para listar conhecimento (`listar/conhecimento`)
- [ ] Sistema para editar conhecimento (`editar`) 
- [ ] Sistema para remover conhecimento (`esquecer/remover`)
- [ ] Melhorar feedback de aprendizado (mostrar quantidade de respostas)
- [ ] Sistema de contexto básico (lembrar última palavra-chave)

**Status**: Implementando sistema de edição de conhecimento

### Tópico 5: Robustez
- [ ] Validação rigorosa de inputs do usuário
- [ ] Tratamento de casos onde entrada fica vazia após normalização
- [ ] Prevenção de spam no aprendizado (limites, cooldown)
- [ ] Tratamento de exceções e erros inesperados
- [ ] Validação de comandos malformados

### Tópico 6: Funcionalidades Avançadas
- [ ] Sistema de persistência (salvar conhecimento em arquivo)
- [ ] Import/Export de conhecimento
- [ ] Sistema de sinônimos
- [ ] Respostas contextuais mais inteligentes
- [ ] Histórico de conversas

### Tópico 7: Interface e UX
- [ ] Melhorar layout do console
- [ ] Sistema de cores no terminal
- [ ] Comandos com autocompletar
- [ ] Sistema de ajuda contextual
- [ ] Interface mais intuitiva

## Como Contribuir

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'feat: adiciona feature incrível'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## Autor

**Felipe Tamura**
- GitHub: [@Felipe-Tamura](https://github.com/Felipe-Tamura)

---

⭐ Se este projeto foi útil para você, considere dar uma estrela no repositório!
