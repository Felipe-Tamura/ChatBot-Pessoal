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
- `editar` - Edita a resposta das palavras-chaves aprendidas
- `esquecer` ou `remover` - Remove respostas ou palavras-chaves aprendidas

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
   git clone https://github.com/Felipe-Tamura/ChatBot-Pessoal.git
   cd ChatBot-Pessoal
   ```

2. **Compile o código**
   ```bash
   mkdir bin
   javac -d bin src/entities/Chatbot.java src/application/Program.java
   ```

3. **Execute o programa**
   ```bash
   java -cp bin application.Program
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
- editar: edita o conhecimento do bot
- esquecer/remover: remove o conhecimento do bot

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
│   │   └── Chatbot.java          # Classe principal do chatbot
│   ├── data/
│   │   └── conhecimento.java     # Arquivo de conhecimento do chatbot
│   └── application/
│       └── Program.java          # Classe principal para execução
├── .gitignore                    # Arquivos ignorados pelo Git
└── README.md                     # Este arquivo
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
- Linkedin: [@Felipe Tamura](https://www.linkedin.com/in/felipe-tamura-b35373215)

---

⭐ Se este projeto foi útil para você, considere dar uma estrela no repositório!
