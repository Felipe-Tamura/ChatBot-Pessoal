package entities;

import java.util.Scanner;
import java.util.Set;
import java.util.Random;
import java.util.Map;
import java.util.Properties;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.Properties;

/**
 * Chatbot interativo com sistema de aprendizado dinâmico.
 * 
 * Este chatbot permite que usuários ensinem novos conhecimentos por meio
 * de palavras-chave e respostas, criando uma base de conhecimento personalizada.
 * Inclui funcionalidades de edição, remoção e listagem do conhecimento adquirido.
 * 
 * @author Felipe Tamura
 * @version 1.0
 * @since 2025
 */
public class Chatbot{

	private String CAMINHO_CONHECIMENTO = "src/data/conhecimento.json";
	private Scanner sc;
	private Random rnd;
	private String nomeUsuario;
	private boolean executando;
	private Map<String, List<String>> conhecimento;

	
	
	/**
	 * Construtor da classe.
	 */
	public Chatbot() {
		// Inicialização dos métodos
		sc = new Scanner(System.in);
		rnd = new Random();
		conhecimento = new HashMap<>();
		executando = true;
		carregarConhecimento();
	}

	/**
	 * Inicialização do chatbot.
	 * 
	 * Solicita o nome do usuário e mostra a funcionalidade do bot.
	 * Executa o loop para iniciar a conversa.
	 * 
	 * @see #consoleAjuda();
	 * @see #executarLoop();
	 */
	public void iniciar() {
		// Solicita o nome do usuário
		System.out.println("=== CHATBOT ===");
		System.out.print("Digite seu nome: ");
		nomeUsuario = sc.nextLine();
		
		// Mostra as funcionalidades do bot
		consoleAjuda();
		
		// Executa o loop
		executarLoop();
		
		// Fecha/Finaliza funcionalidades
		sc.close();
	}

	/**
	 * Mantém o bot em funcionamento para as conversas.
	 * 
	 * @see #processarMensagem(String)
	 */
	private void executarLoop() {
		while (executando) {
			System.out.printf("%n%s: ", nomeUsuario);
			String entrada = sc.nextLine().trim();

			// Verifica entrada preenchida
			if (!entrada.isEmpty()) processarMensagem(entrada); // Processa a mensagem
		}
	}

	/**
	 * Processa e responde mensagens do usuário.
	 * 
	 * Verifica se a mensagem contém comandos especiais ou gera resposta
	 * baseada no conhecimento adquirido.
	 * Aplica normalização de texto antes do processamento.
	 * 
	 * @param entrada mensagem digitada pelo usuário
	 * @see #tratarMensagem(String)
	 * @see #verificarComandosEspeciais(String)
	 * @see #gerarResposta(String)
	 */
	private void processarMensagem(String entrada) {
		String msg = tratarMensagem(entrada);
		// Verifica comando especial
		if (!verificarComandosEspeciais(msg)) {
			// Gera resposta com base no conhecimento do bot
			System.out.printf("%nChatbot: %s", gerarResposta(msg));
		}
	}

	/**
	 * Comandos especiais do bot.
	 * 
	 * Verifica qual comando o usuário envia ao bot.
	 * Executa funcionalidade de acordo com o comando enviado.
	 * 
	 * @param mensagem mensagem digitada pelo usuário
	 * @return validação de comando especial
	 * @see #ensinarChat()
	 * @see #consoleAjuda()
	 * @see #listarConhecimento()
	 * @see #editarConhecimento()
	 * @see #removerConhecimento()
	 */
	private boolean verificarComandosEspeciais(String mensagem) {
		// Opção de sair do chat com o bot
		if (mensagem.contains("sair") || mensagem.contains("exit")) {
			String[] resposta = {
					String.format("Foi um prazer conversar com você %s, até logo!", nomeUsuario),
					String.format("Até mais %s!", nomeUsuario),
					"Vejo você depois!"
			};

			System.out.println(resposta[rnd.nextInt(resposta.length)]);
			executando = false; // Parar loop
			return true;
		}	

		// Ensina o bot
		if (mensagem.contains("ensinar")) {
			ensinarChat();
			return true;
		}

		// Mostra as funcionalidades do bot
		if (mensagem.contains("ajuda") || mensagem.contains("help")) {
			consoleAjuda();
			return true;
		}
		
		// Lista o conhecimento do bot
		if (mensagem.contains("listar") || mensagem.contains("conhecimento")) {
			if (!listarConhecimento()) {
				String[] respostas = {
						"Meu conhecimento está vazio no momento, poderia me ensinar algo novo?",
						"Minha memória está vazia, considere me passar um pouco de conhecimento, por favor!",
						"Estou confuso, posso jurar que eu tinha conhecimento suficiente na minha memória!",
						"Ainda não sei sobre nada, me ensine!"
				};
				System.out.printf("Chatbot: %s%n", respostas[rnd.nextInt(respostas.length)]);
			}
			return true;
		}
		
		// Edita o conhecimento do bot
		if (mensagem.contains("editar") || mensagem.contains("edit")) {
			editarConhecimento();
			return true;
		}
		
		// Remove o conhecimento do bot
		if (mensagem.contains("esquecer") || mensagem.contains("remover")) {
			removerConhecimento();
			return true;
		}

		return false;
	}

	/**
	 * Gera resposta contextual baseada na mensagem do usuário.
	 * 
	 * Utiliza sistema de pontuação para encontrar a melhor correspondência
	 * entre palavras da mensagem e palavras-chave do conhecimento.
	 * Se nenhuma correspondência for encontrada, retorna resposta padrão
	 * incentivando o usuário a ensinar sobre o assunto.
	 * 
	 * @param mensagem texto normalizado enviado pelo usuário
	 * @return resposta gerada pelo chatbot
	 * @see #extrairPalavras(String)
	 * @see #tratarMensagem(String)
	 */
	private String gerarResposta(String mensagem) {
		// Saudação do usuário
		if (mensagem.contains("oi") || mensagem.contains("olá") || mensagem.contains("hey")) {
			String[] resposta = {
					String.format("Olá, como vai %s?", nomeUsuario),
					"Opa, tudo bom?",
					String.format("Tudo tranquilo %s?", nomeUsuario)
			};
			return resposta[rnd.nextInt(resposta.length)];
		}		

		// Agradecimento do usuário
		if (mensagem.contains("obrigado")) {
			String[] resposta = {
					"De nada, qualquer coisa estou aqui para te responder",
					"Qualquer coisa pode falar comigo que nós desvendamos juntos"
			};
			return resposta[rnd.nextInt(resposta.length)];
		}

		// Cria lista de palavras da mensagem
		List<String> palavrasMensagem = extrairPalavras(mensagem);

		int maiorScore = 0;
		List<String> palavrasComMelhorScore = new ArrayList<>();
		
		for (String palavraChave: conhecimento.keySet()) { // Iterando palavras chave
			// Verifica lista de palavras da mensagem com palavras chave
			int score = palavrasMensagem.contains(palavraChave) ? 1 : 0;
			// Adiciona palavra com melhor pontuação
			if (score > maiorScore) {
				maiorScore = score;
				palavrasComMelhorScore.clear();
				palavrasComMelhorScore.add(palavraChave);
			
			// Adiciona mais de um palavra com pontuação igual
			}else if (score == maiorScore && score > 0) {
				palavrasComMelhorScore.add(palavraChave);
			}
		}
		
		if (maiorScore > 0) {
			// Coleta palavra dinamicamente 
			String palavraEscolhida = palavrasComMelhorScore.get(
					rnd.nextInt(palavrasComMelhorScore.size())
			);
			// Obtém uma lista de respostas
			List<String> respostas = conhecimento.get(palavraEscolhida);
			// Responde dinâmicamente o usuário
			return respostas.get(rnd.nextInt(respostas.size()));
		}

		// Resposta padrão
		String[] defaultResponse = {
				"Não entendo sobre esse assunto, poderia me ensinar sobre?",
				"Hmm, interessante, pode me falar mais sobre?",
				"Que tal me ensinar mais sobre isso?"
		};		
		return defaultResponse[rnd.nextInt(defaultResponse.length)];
	}

	/**
	 * Mostra as funcionalidades deste bot.
	 */
	private void consoleAjuda() {
		// Informação sobre como o chatbot funciona
		System.out.println("\n=== MENU DE AJUDA ===");
		String[] funcionalidades = {
				"- sair/exit: encerra o chatbot",
				"- ensinar: ensina o chatbot algo novo",
				"- ajuda/help: mostra o menu de ajuda",
				"- listar/conhecimento: mostra a lista de conhecimento do bot",
				"- editar: edita o conhecimento do bot",
				"- esquecer/remover: remove o conhecimento do bot"
		};

		// Imprime as funcionalidades do bot
		for (String item: funcionalidades) {
			System.out.println(item);
		}

		System.out.println("======");
		System.out.println("\nComo usar este chat?");
		System.out.println("Primeiro você precisa verificar se ele entende do assunto que "
				+ "você está querendo discutir com ele.");
		System.out.println("Caso ele diga que não entende, por favor, ensine a ele como conversar"
				+ " sobre este assunto!");
		System.out.println("\nPense que ele acabou de nascer e que o mundo é "
				+ "um mistério para ele :)");
		System.out.println("Acima estão as principais funcionalidades deste chat!");
		System.out.println("\nNão esqueça de visitar meu Github para novas atualizações:"
				+ " https://github.com/Felipe-Tamura");

	}

	/**
	 * Adiciona um novo conhecimento na base do bot.
	 * 
	 * Com o comando 'ensinar', o bot coleta os novos dados que o usuário digita para ele.
	 * 
	 * @see #tratarMensagem(String)
	 * @see #extrairPalavras(String)
	 */
	private void ensinarChat() {
		// Solicita palavra chave e resposta
		System.out.print("Chatbot: Palavra-Chave (ex: tempo de hoje): ");
		String palavraChave = tratarMensagem(sc.nextLine().toLowerCase().trim());
		System.out.print("Chatbot: Resposta da palavra-chave: ");
		String respostaConhecimento = tratarMensagem(sc.nextLine());

		// Verifica dados existentes no input
		if (validarPalavra(palavraChave)) {
			return;
		}
		
		if (respostaConhecimento.isEmpty()) {
			System.out.println("Chatbot: Não posso aceitar conhecimento vazio!");
			return;
		}

		// Extrai palavras chave da mensagem
		List<String> possiveisChave = extrairPalavras(palavraChave);
		
		System.out.printf("Chatbot: Encontrei %d palavras-chave%n", possiveisChave.size()); // Informa a quantidade de palavras-chave

		List<String> palavraNova = new ArrayList<>();
		List<String> palavraExistente = new ArrayList<>();
		boolean respostaNova = false;
		boolean respostaExistente = false;
		List<String> respostaDuplicada = new ArrayList<>();

		// Verifica palavras-chave no conhecimento e adiciona a lista
		for (String msg: possiveisChave) {
			if (conhecimento.containsKey(msg)) {
				palavraExistente.add(msg);
				// Verificando se resposta já existe na base do conhecimento do bot
				if (conhecimento.get(msg).contains(respostaConhecimento)) {
					respostaExistente = true;
					respostaDuplicada.add(msg);
				}else {
					respostaNova = true;
				}
			}else {
				palavraNova.add(msg);
				respostaNova = true;
			}
		}
		
		// Retorna resposta com base no cenário atual
		if (!palavraNova.isEmpty() && palavraExistente.isEmpty()) {
			System.out.println("Chatbot: Novas palavras: ");
			palavraNova.forEach(palavra -> System.out.printf("'%s'%n",palavra)); // Expressão lambda
		}else if (!palavraNova.isEmpty() && !palavraExistente.isEmpty()) {
			System.out.println("Chatbot: Algumas já existem (expandindo conhecimento)");
			System.out.println("Chatbot: Novas palavras: ");
			palavraNova.forEach(palavra -> System.out.printf("'%s'%n",palavra));

			System.out.println("Chatbot: Palavras existentes: ");
			palavraExistente.forEach(palavra -> System.out.printf("'%s'%n", palavra));

			if (respostaExistente && !respostaDuplicada.isEmpty()) {
				System.out.printf("Chatbot: A resposta '%s' já existe nas seguintes palavras-chave:%n", respostaConhecimento);
				respostaDuplicada.forEach(palavra -> System.out.printf("'%s'%n", palavra));
			}
		}else if (palavraNova.isEmpty() && !palavraExistente.isEmpty()) {
			System.out.println("Chatbot: Palavras existentes: ");
			palavraExistente.forEach(palavra -> System.out.printf("'%s'%n", palavra));

			if (respostaExistente && !respostaDuplicada.isEmpty()) {
				System.out.printf("Chatbot: A resposta '%s' já existe nas seguintes palavras-chave:%n", respostaConhecimento);
				respostaDuplicada.forEach(palavra -> System.out.printf("'%s'%n", palavra));
			}
		}
	
		// Adiciona respostas à palavra chave
		for (String chave: possiveisChave) {
			if (respostaNova && !respostaDuplicada.contains(chave)) {
				conhecimento.computeIfAbsent(chave, k -> new ArrayList<>()).add(respostaConhecimento);
			}
		}
		
		if (respostaNova) {
			String[] respostas = { // Lista dinâmica de resposta
					String.format("Obrigado, agora eu sei um pouco sobre '%s'", palavraChave),
					String.format("Perfeito! Agora posso conversar sobre '%s' com você", palavraChave),
					String.format("Eba! Aprendi algo novo sobre '%s', muito obrigado!", palavraChave),
					String.format("Que legal! Agora '%s' faz parte do meu conhecimento", palavraChave)
			};

			System.out.printf("Chatbot: %s%n", respostas[rnd.nextInt(respostas.length)]);
			salvarConhecimento();
		}
	}

	/**
	 * Valida palavra para conhecimento do bot
	 * 
	 * Evita dados vazios, palavras muito curtas e valida digitos numéricos
	 * 
	 * @param palavra - texto original a ser validado
	 * @return validação final com base nos critérios
	 * @see validarNumero()
	 */
	private boolean validarPalavra(String palavra) {
		// Valida palavra vazia
		if (palavra.isEmpty() || palavra.trim().isEmpty()) {
			System.out.println(
					"Chatbot: Encontrei dados vazios, não posso aceitar palavra-chave vazia!"
					);
			return true;
		}
		
		// Valida palavra curta
		if (palavra.length() < 2) {
			System.out.println("Chatbot: Palavra-chave muito curta!");
			return true;
		}
		
		// Valida digito
		if (palavra.matches("\\d+")) {
			try {
				return validarNumero(palavra);
			}catch (InputMismatchException e) {
				System.out.println("Chatbot: Entrada inválida: " + e.getMessage());
				return true;
			}catch (NumberFormatException e) {
				System.out.println("Chatbot: Formato inválido: " + e.getMessage());
				return true;
			}
		}
		
		// Retorno padrão
		return false;
	}
	
	/**
	 * Verifica dígitos para conhecimento do bot
	 * 
	 * Evita dígito genéricos e sequênciais
	 * 
	 * @param palavra - texto original a ser validado
	 * @return decisão de números válidos
	 */
	private boolean validarNumero(String palavra) {
		// Converte texto para número
		int numero = Integer.parseInt(palavra);
		// Separa digitos em lista
		String[] digito = palavra.split("");
		// Sequência e Repetição
		boolean isRep = true;
		boolean isSeqCre = false;
		boolean isSeqDec = false;
		
		// Verifica idade
		if (numero >= 1 && numero <= 120) {
			return false;
		}
		
		// Verifica ano
		if (numero >= 1900 && numero <= 2100) {
			return false;
		}

		// Verifica tamanho dos dígitos
		if (palavra.length() > 5) {
			return true;
		}
		
		// Verifica digito sequencial crescente
		for (int i = 0; i < palavra.length() - 1; i++) {
			if ((Integer.parseInt(digito[i]) + 1) == Integer.parseInt(digito[i+1])) {
				isSeqCre = true;
			}
		}
		
		// Verifica digito sequencial decrescente
		for (int i = 0; i < palavra.length() - 1; i++) {
			if ((Integer.parseInt(digito[i]) - 1) == Integer.parseInt(digito[i+1])) {
				isSeqDec = true;
			}
		}
		
		// Verifica digito repetido
		for (int i = 0; i < palavra.length(); i++) {
			if (!digito[i].equals(digito[0])) {
				isRep = false;
			}
		}
		
		
		//Valida Sequência ou Repetição
		if (isRep || isSeqCre || isSeqDec) {
			System.out.println(
					"Chatbot: Valores sequênciais ou repetidos não posso aceitar como palavra chave!"
					);
			return true;
		}

		// Retorno padrão
		return true;
	}

	/**
	 * Normaliza texto removendo acentos e pontuação.
	 * 
	 * Converte para minúsculas, substitui caracteres acentuados
	 * pelos equivalentes sem acento e remove pontuação específica.
	 * 
	 * @param entrada - texto original a ser normalizado
	 * @return texto normalizado em minúsculas sem acentos
	 * @see #criarMapaAcentos()
	 */
	private String tratarMensagem(String entrada) {
		// Padrão das mensagens
		String res = entrada.toLowerCase();
		
		Map<String, List<String>> acentos = criarMapaAcentos();
		
		// Modifica acentos por letras
		for (Map.Entry<String, List<String>> acento: acentos.entrySet()) {
			String letraBase = acento.getKey();
			for (String letra: acento.getValue()) {
				res = res.replace(letra, letraBase);
			}
		}
		
		// Retira pontuação
		res = res.replaceAll("[?!]", "");

		return res;
	}
	
	/**
	 * Criação de mapa de acentos para tratamento na mensagem
	 * 
	 * @return lista de acentos
	 */
	private Map<String, List<String>> criarMapaAcentos(){
		// Cria lista de acentos
		Map<String, List<String>> acentos = new HashMap<>();
		
		// Adiciona acentos mais usados
		acentos.put("c", Arrays.asList("ç"));
		acentos.put("a", Arrays.asList("ã", "â", "á", "à"));
		acentos.put("i", Arrays.asList("í", "ì"));
		acentos.put("u", Arrays.asList("ú", "ù"));
		acentos.put("e", Arrays.asList("ê", "é", "è"));
		acentos.put("o", Arrays.asList("õ", "ô", "ó", "ò"));
		
		return acentos;
	}

	/**
	 * Extrai palavras relevantes removendo stop words.
	 * 
	 * Remove palavras comuns (artigos, preposições, etc.) e palavras vazias,
	 * mantendo apenas termos significativos para análise de contexto.
	 * 
	 * @param mensagem texto normalizado para extração
	 * @return lista de palavras relevantes
	 */
	private List<String> extrairPalavras(String mensagem){
		// Lista de palavras genericas
		Set<String> STOP_WORDS = new HashSet<>(
				Arrays.asList( "o", "a", "os", "as", "de", "da", "do", "para", "com", "em", "por",
				"que", "um", "uma", "é", "foi", "ser", "ter", "como", "mais", "muito", "bem", "já",
				"ainda", "mas", "ou", "se", "me", "te", "nos", "lhe"
		));
		String[] temp = mensagem.split(" "); // Transforma mensagem em lista
		
		// Cria lista auxiliar
		List<String> aux = new ArrayList<>();
		// Adiciona palavras relevantes ao auxiliar
		for (String item: temp) {
			if (!STOP_WORDS.contains(item) && !item.isEmpty()) {
				aux.add(item);
			}
		}
		
		return aux;
	}

	/**
	 * Mostra ao usuário todo o conhecimento do chat neste momento.
	 * 
	 * @return lista de conhecimento.
	 */
	private boolean listarConhecimento() {
		// Verifica conhecimento vazio
		if (conhecimento.isEmpty()) return false;
		System.out.printf(
				"Chatbot: Eu possuo %d palavra(s)-chave(s) no meu conhecimento atualmente.%n",
				conhecimento.size()
		);
		int temp = 1;
		// Lista conhecimento com quantidade de resposta
		for (String palavraChave: conhecimento.keySet()) {
			System.out.printf("%d. %s - (%d resposta(s)).%n",
					temp,
					palavraChave,
					conhecimento.get(palavraChave).size()
			);
			temp++;
		}
		return true;
	}
	
	/**
	 * Edição do conhecimento do bot.
	 * 
	 * Verifica qual a palavra-chave e qual a resposta que o usuário quer editar.
	 * 
	 * @see #listarConhecimento()
	 * @see #salvarConhecimento()
	 */
	private void editarConhecimento() {
		try {
			// Verifica conhecimento do bot
			if (listarConhecimento()) {
				System.out.print(
						"\nChatbot: Qual conhecimento você deseja editar? Digite a palavra chave: "
				);
				String palavraChave = tratarMensagem(sc.nextLine());
				int temp = 1; // Variável temporária
				
				// Verifica palavra chave existente no conhecimento
				if (conhecimento.containsKey(palavraChave)) {
					// Lista resposta do conhecimento
					for (String lista: conhecimento.get(palavraChave)) {
						System.out.println("\n" + temp + ". " + lista);
						temp++;
					}
					System.out.print(
							"\nChatbot: Qual dessas respostas você deseja alterar? Digite o número: "
					);
					
					int indexFrase = sc.nextInt() - 1;
					sc.nextLine();
					
					// Verifica resposta válida
					if (indexFrase < 0 || indexFrase > (conhecimento.get(palavraChave).size() - 1)) {
						System.out.println("Chatbot: Resposta não encontrada!");
						return;
					}
					
					System.out.printf(
							"Chatbot: Resposta escolhida: %s",
							conhecimento.get(palavraChave).get(indexFrase)
					);
					System.out.println("\nChatbot: Agora me diga a resposta editada: ");
					String respostaEditada = tratarMensagem(sc.nextLine());
					conhecimento.get(palavraChave).remove(indexFrase); // Remove resposta antiga
					conhecimento.get(palavraChave).add(respostaEditada); // Adiciona resposta nova
					System.out.println("Chatbot: Resposta editada com sucesso!");
					salvarConhecimento();
				}else {
					System.out.println("Chatbot: palavra-chave não encontrada");
				}
				
			}else {
				System.out.println("Chatbot: Conhecimento vazio!");
			}
		} catch (InputMismatchException e) { // Trata input de número com texto
			System.out.printf("Chatbot: Algo deu errado: %s.%nTente novamente!%n", e.getMessage());
			sc.nextLine(); // Limpa o buffer
		}
	}

	/**
	 * Remove conhecimento do chatbot de forma interativa.
	 * 
	 * Oferece duas opções: remover palavra-chave completa ou apenas
	 * uma resposta específica. Inclui confirmações para prevenir
	 * remoções acidentais.
	 * 
	 * @throws InputMismatchException quando usuário digita texto onde esperava número
	 */
	private void removerConhecimento() {
		try {
			// Verifica lista de conhecimento
			if (listarConhecimento()) {
				System.out.println("\nChatbot: Você quer:"
						+ "\n1. Remover uma palavra-chave inteira."
						+ "\n2. Remover apenas uma resposta específica."
						+ "\n0. Sair."
				);
				int decisao = sc.nextInt();
				sc.nextLine(); // Limpa buffer
				// Variáveis temporárias
				String palavraChave;
				int temp = 1;
				// Switch de decisão
				switch (decisao) {
					case 1: // Remove palavra chave do conhecimento
						System.out.print("Chatbot: Digite uma da(s) palavra(s)-chave(s) da lista: ");
						palavraChave = tratarMensagem(sc.nextLine());
						// Verifica existência da palavra chave no conhecimento
						if (conhecimento.containsKey(palavraChave)) {
							System.out.println(
									"Tem certeza que deseja excluir essa palavra chave? [S]im [N]ão"
									);
							String respostaTemp = tratarMensagem(sc.nextLine());
							// Decisão do usuário
							if (respostaTemp.isEmpty() ||
									respostaTemp.contains("sim") ||
									respostaTemp.contains("s")) {
								// Remove palavra chave do conhecimento
								conhecimento.remove(palavraChave);
								System.out.println("\nChatbot: Palavra-chave removida com sucesso!");
							}else if (respostaTemp.contains("não") || respostaTemp.contains("n")) {
								System.out.println("\nChatbot: Nada foi removido! Retornando...");
							}else {
								System.out.println("\nChatbot: Resposta inválida! Retornando...");
							}
						}else {
							System.out.println("Chatbot: palavra-chave não encontrada");
						}
						break;
						
					case 2: // Remove respostas do conhecimento
						System.out.print("Chatbot: Digite uma da(s) palavra(s)-chave(s) da lista: ");
						palavraChave = tratarMensagem(sc.nextLine());
						// Verifica existência da palavra chave no conhecimento
						if (conhecimento.containsKey(palavraChave)) {
							// Lista respostas da palavra chave
							for (String lista: conhecimento.get(palavraChave)) {
								System.out.println("\n" + temp + ". " + lista);
								temp++;
							}
							
							System.out.print("\nChatbot: Digite o número da resposta: ");
							int indexFrase = sc.nextInt() - 1;
							sc.nextLine();
							
							// Verifica número válido para lista de conhecimento
							if (indexFrase < 0 || indexFrase > (conhecimento.get(palavraChave).size() - 1)) {
								System.out.println("Chatbot: Resposta não encontrada!");
								return;
							}
							
							// Verifica se palavra-chave irá ficar vazia
							if ((conhecimento.get(palavraChave).size() - 1) == 0) {
								System.out.println(
										"Chatbot: Ao remover essa resposta, você estará removendo "
										+ "a palavra-chave do conhecimento"
										+ "\nDeseja continuar? [S]im [N]ão"
								);
								String respostaTemp = tratarMensagem(sc.nextLine());
								
								// Decisão do usuário
								if (respostaTemp.isEmpty() ||
										respostaTemp.contains("sim") ||
										respostaTemp.contains("s")) {
									conhecimento.remove(palavraChave); // Remove palavra chave do conhecimento
									System.out.println(
											"\nChatbot: Palavra-chave removida do conhecimento!"
											);
									salvarConhecimento();
									return;
								}else if (respostaTemp.contains("não") ||
										respostaTemp.contains("n")) {
									System.out.println("\nChatbot: Nada foi removido! Retornando...");
								}else {
									System.out.println("\nChatbot: Resposta inválida! Retornando...");
								}
							}
							
							System.out.printf(
									"Chatbot: Resposta escolhida: %s",
									conhecimento.get(palavraChave).get(indexFrase)
									);
							conhecimento.get(palavraChave).remove(indexFrase); // Remove resposta da palavra chave
							System.out.println("\nChatbot: Resposta removida com sucesso!");
							salvarConhecimento();
						}else {
							System.out.println("Chatbot: palavra-chave não encontrada");
						}
						break;
						
					default:
						break;
				}
			}
		} catch (InputMismatchException e) { // Trata input de número com texto
			System.out.printf("Chatbot: Você digitou uma decisão incorreta e gerou o erro %s.%nTente novamente!%n", e.getMessage());
			sc.nextLine(); // Limpa o buffer
		}
	}
	
	/**
	 * Verifica diretorio existente
	 * 
	 * Instancia o caminho com File e verifica se ele existe, caso não exista ele cria o caminho
	 */
	private void verificarDiretorio() {
		// Instancia do arquivo
		File arquivo = new File(CAMINHO_CONHECIMENTO);
		// Instancia do diretorio
		File diretorio = arquivo.getParentFile();
		
		// Verifica diretorio existente
		if (!diretorio.exists()) {
			diretorio.mkdirs();
		}
	}
	
	/**
	 * Salva o conhecimento do chatbot.
	 * 
	 * O arquivo padrão do conhecimento é JSON. Realiza configuração para o formato
	 * JSON da base de conhecimento do chatbot
	 */
 	private void salvarConhecimento() {
 		verificarDiretorio(); // Verifica diretorio antes de salvar o arquivo
		StringBuilder json = new StringBuilder();
		
		// Inicio do arquivo
		json.append("{");

		int temp = 1;
		// Itera sobre cada palavra-chave e suas respectivas respostas
		for (String con: conhecimento.keySet()) {

			// Adiciona a palavrachave
			json.append("\n\"" + con + "\": [");
			
			// Verifica se palavra-chave contém apenas 1 resposta
			if (conhecimento.get(con).size() == 1) {
				// Adiciona resposta sem vírgula para o JSON
				json.append("\"" + conhecimento.get(con).get(0) + "\"");
			}else {
				// Itera sobre cada resposta da palavra-chave menos a última resposta
				for (int i = 0; i < conhecimento.get(con).size() - 1; i++) {
					// Adiciona resposta com vírgula para o JSON
					json.append("\"" + conhecimento.get(con).get(i) + "\",");
				}
				// Adiciona a última resposta da palavra-chave
				json.append("\"" + conhecimento.get(con).get(conhecimento.get(con).size() - 1) + "\"");
			}

			// Verifica quantidade de palavra chave no conhecimento
			if (temp < conhecimento.size()){
				// Adiciona final da resposta para o JSON com vírgula
				json.append("],");
			}else {
				// Adiciona final da resposta para o JSON sem vírgula
				json.append("]");
			}

			temp++;
		}
		
		// Final do arquivo
		json.append("\n}");
		
		// Escrevendo no arquivo
		try (FileWriter writer = new FileWriter(CAMINHO_CONHECIMENTO)){
			writer.write(json.toString());
		}catch (IOException e) {
			System.out.println("Chatbot: Erro ao salvar: " + e.getMessage());
		}
	}

 	/**
 	 * Carrega conhecimento do arquivo JSON
 	 * 
 	 * Verifica a existencia do arquivo com o conhecimento base, caso contrário inicia com
 	 * conhecimento vazio.
 	 * 
 	 */
 	private void carregarConhecimento() {
 		// Coleta o arquivo no caminho
 		File arquivo = new File(CAMINHO_CONHECIMENTO);
 		
 		// Verifica se o arquivo existe
 		if (!arquivo.exists()) {
 			System.out.println("Chatbot: Arquivo de conhecimento vazio, conhecimento iniciando do zero!");
 			return;
 		}
 		
 		try {
 			// Coleta os dados do arquivo
 			String jsonContent = lerArquivo(arquivo);
 			// Verifica se os dados não estão vazios
 			if (!jsonContent.trim().isEmpty()) {
 				// Analisa o conteúdo
 				analiseJson(jsonContent);
 			}
 		}catch (IOException e) {
 			System.out.println("Chatbot: Erro ao carregar conhecimento: " + e.getMessage());
 		}
 	}
 		
 	/**
 	 * Realiza leitura do arquivo JSON
 	 * 
 	 * Adiciona todo o conteúdo do arquivo em um StringBuilder
 	 * 
 	 * @param arquivo - para leitura e tratamento dos dados
 	 * @return String em formato JSON
 	 * @throws IOException - Erro na leitura
 	 */
 	private String lerArquivo(File arquivo) throws IOException {
 		StringBuilder content = new StringBuilder();
 		
 		try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))){ // Tenta ler o arquivo
 			String linha;
 			// Verifica se a linha não está vazia
 			while ((linha = reader.readLine()) != null) {
 				// Adiciona o conteúdo da linha
 				content.append(linha).append("\n");
 			}
 		}catch (IOException e) {
 			System.out.println("Chatbot: Não consegui ler o arquivo: " + e.getMessage());
 		}
 		
 		return content.toString();
 	}
 	
 	/**
 	 * Analise de arquivo JSON
 	 * 
 	 * Analisa e adiciona o conteúdo do JSON no conhecimento do bot
 	 * 
 	 * @param jsonContent - String em formato JSON
 	 */
 	private void analiseJson(String jsonContent) {
 		// Remove quebras de linha e espaços extras
 		String json = jsonContent.replaceAll("\\s+", " ").trim();
 		// Retira 2 caracteres inicial e 3 final
 		json = json.substring(2, json.length() - 3);
 		
 		// Separa JSON pela string delimitadora
 		String[] chaves = json.split("], ");
 		for(int i = 0; i < chaves.length; i++) {
 			// Coleta cada chave da lista
 			String chave = chaves[i];

 			// Adiciona novamente o colchete retirado anteriormente
			chave += "]";
 			
			// Obtém inicio e fim da chave
 			int inicioChave = chave.indexOf("\"") + 1;
 			int fimChave = chave.indexOf("\"", inicioChave);
 			// Obtém palavra-chave padrão
 			String chaveDefault = chave.substring(inicioChave, fimChave);
 			
 			// Obtém o inicio e fim da resposta
 			int inicioResposta = chave.indexOf("[") + 1;
 			int fimResposta = chave.indexOf("]");
 			// Obtém respostas padrão da palavra-chave
 			String[] respostaDefault = chave.substring(inicioResposta, fimResposta).split("\", \"");
 			// Itera sobre as respostas padrão
 			List<String> listaRespostas = new ArrayList<>();
 			for (String resp: respostaDefault) {
 				// Itera sobre as respostas retirando vírgula e aspas
 				for (String r: resp.split(",\"")) {
 					// Adiciona na lista retirando as aspas restantes
 					listaRespostas.add(r.replaceAll("\"", ""));
 				}
 			}
 			
 			// Adiciona no conhecimento
 			conhecimento.put(chaveDefault, listaRespostas);
 		} 	}
}