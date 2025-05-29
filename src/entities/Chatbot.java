package entities;

import java.util.Scanner;
import java.util.Set;
import java.util.Random;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.ArrayList;
import java.util.Arrays;

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

	private Scanner sc;
	private Random rnd;
	private String nomeUsuario;
	private boolean executando;
	private Map<String, List<String>> conhecimento;

	/**
	 * Construtor da classe.
	 */
	public Chatbot() {
		// Inicializaão dos métodos
		sc = new Scanner(System.in);
		rnd = new Random();
		conhecimento = new HashMap<>();
		executando = true;
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
		System.out.print("Chatbot: Me diga a palavra-chave para o assunto principal, por favor (ex: tempo): ");
		String palavraChave = tratarMensagem(sc.nextLine().toLowerCase().trim());
		System.out.print("Chatbot: Me diga agora como responder a esta palavra chave, por favor: ");
		String respostaConhecimento = tratarMensagem(sc.nextLine());

		// Extrai palavras chave da mensagem
		List<String> possiveisChave = extrairPalavras(palavraChave);
		
		System.out.printf("Chatbot: Encontrei %d palavras-chave%n", possiveisChave.size()); // Informa a quantidade de palavras-chave

		List<String> palavraNova = new ArrayList<>();
		List<String> palavraExistente = new ArrayList<>();

		// Verifica palavras-chave no conhecimento e adiciona a lista
		for (String msg: possiveisChave) {
			if (conhecimento.containsKey(msg)) {
				palavraExistente.add(msg);
			}else {
				palavraNova.add(msg);
			}
		}
		
		// Retorna resposta com base no cenário atual
		if (!palavraNova.isEmpty() && palavraExistente.isEmpty()) {
			System.out.println("Chatbot: Todas as palavras são novas");
			palavraNova.forEach(palavra -> System.out.printf("'%s'%n",palavra)); // Expressão lambda
		}else if (!palavraNova.isEmpty() && !palavraExistente.isEmpty()) {
			System.out.println("Chatbot: Algumas já existem (expandindo conhecimento");
			System.out.println("Chatbot: Novas palavras: ");
			palavraNova.forEach(palavra -> System.out.printf("'%s'%n",palavra));
			System.out.println("Chatbot: Palavras existentes: ");
			palavraExistente.forEach(palavra -> System.out.printf("'%s'%n", palavra));
		}else if (palavraNova.isEmpty() && !palavraExistente.isEmpty()) {
			System.out.println("Chatbot: Todas já existem (apenas adicionando respostas");
			System.out.println("Chatbot: Palavras existentes: ");
			palavraExistente.forEach(palavra -> System.out.printf("'%s'%n", palavra));
		}
	
		// Adiciona respostas à palavra chave
		for (String chave: possiveisChave) {
			conhecimento.computeIfAbsent(chave, k -> new ArrayList<>()).add(respostaConhecimento);
		}
		
		String[] respostas = { // Lista dinâmica de resposta
				String.format("Obrigado, agora eu sei um pouco sobre '%s'", palavraChave),
				String.format("Perfeito! Agora posso conversar sobre '%s' com você", palavraChave),
				String.format("Eba! Aprendi algo novo sobre '%s', muito obrigado!", palavraChave),
				String.format("Que legal! Agora '%s' faz parte do meu conhecimento", palavraChave)
		};

		System.out.printf("Chatbot: %s%n", respostas[rnd.nextInt(respostas.length)]);
	}

	/**
	 * Normaliza texto removendo acentos e pontuação.
	 * 
	 * Converte para minúsculas, substitui caracteres acentuados
	 * pelos equivalentes sem acento e remove pontuação específica.
	 * 
	 * @param entrada texto original a ser normalizado
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
	 */
	private void editarConhecimento() {
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
			}else {
				System.out.println("Chatbot: palavra-chave não encontrada");
			}
			
		}else {
			System.out.println("Chatbot: Conhecimento vazio!");
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
							System.out.println("Tem certeza que deseja excluir essa palavra chave? [S]im [N]ão");
							String respostaTemp = tratarMensagem(sc.nextLine());
							// Decisão do usuário
							if (respostaTemp.isEmpty() || respostaTemp.contains("sim") || respostaTemp.contains("s")) {
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
										"Chatbot: Ao remover essa resposta, você estará removendo a palavra-chave do conhecimento"
										+ "\nDeseja continuar? [S]im [N]ão"
								);
								String respostaTemp = tratarMensagem(sc.nextLine());
								
								// Decisão do usuário
								if (respostaTemp.isEmpty() || respostaTemp.contains("sim") || respostaTemp.contains("s")) {
									conhecimento.remove(palavraChave); // Remove palavra chave do conhecimento
									System.out.println("\nChatbot: Palavra-chave removida do conhecimento!");
									return;
								}else if (respostaTemp.contains("não") || respostaTemp.contains("n")) {
									System.out.println("\nChatbot: Nada foi removido! Retornando...");
								}else {
									System.out.println("\nChatbot: Resposta inválida! Retornando...");
								}
							}
							
							System.out.printf("Chatbot: Resposta escolhida: %s", conhecimento.get(palavraChave).get(indexFrase));
							conhecimento.get(palavraChave).remove(indexFrase); // Remove resposta da palavra chave
							System.out.println("\nChatbot: Resposta removida com sucesso!");
							
						}else {
							System.out.println("Chatbot: palavra-chave não encontrada");
						}
						break;
						
					default:
						break;
				}
			}
		} catch (InputMismatchException e) { // Trata input de número com texto
			System.out.println("Chatbot: Você digitou uma decisão incorreta, tente novamente!");
			sc.nextLine(); // Limpa o buffer
		}
	}
}