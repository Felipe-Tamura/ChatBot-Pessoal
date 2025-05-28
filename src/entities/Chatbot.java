package entities;

import java.util.Scanner;
import java.util.Set;
import java.util.Random;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;

public class Chatbot{

	private Scanner sc;
	private Random rnd;
	private String nomeUsuario;
	private boolean executando;
	private Map<String, List<String>> conhecimento;

	public Chatbot() {
		sc = new Scanner(System.in);
		rnd = new Random();
		conhecimento = new HashMap<>();
		executando = true;
	}

	public void iniciar() {
		System.out.println("=== CHABOT ===");
		System.out.print("Digite seu nome: ");
		nomeUsuario = sc.nextLine();
		consoleAjuda();
		executarLoop();
	}

	private void executarLoop() {
		while (executando) {
			System.out.printf("%n%s: ", nomeUsuario);
			String entrada = sc.nextLine().trim();

			if (!entrada.isEmpty()) processarMensagem(entrada);
		}
	}

	private void processarMensagem(String entrada) {
		String msg = tratarMensagem(entrada);
		if (!verificarComandosEspeciais(msg)) {
			System.out.printf("%nChatbot: %s", gerarResposta(msg));
		}
	}

	private boolean verificarComandosEspeciais(String mensagem) {
		if (mensagem.contains("sair") || mensagem.contains("exit")) {
			String[] resposta = {
					String.format("Foi um prazer conversar com você %s, até logo!", nomeUsuario),
					String.format("Até mais %s!", nomeUsuario),
					"Vejo você depois!"
			};

			System.out.println(resposta[rnd.nextInt(resposta.length)]);
			executando = false;
			return true;
		}	

		if (mensagem.contains("ensinar")) {
			ensinarChat();
			return true;
		}

		if (mensagem.contains("ajuda") || mensagem.contains("help")) {
			consoleAjuda();
			return true;
		}
		
		if (mensagem.contains("listar") || mensagem.contains("conhecimento")) {
			if (!listarConhecimento()) {
				String[] respostas = {
						"Meu conhecimento está vazio no momento, poderia me ensinar algo novo?",
						"Minha memória está vazia, considere me passar um pouco de conhecimento, por favor!",
						"Estou confuso, posso jurar que eu tinha conhecimento suficiente na minha memória!",
						"Ainda não sei sobre nada, me ensine!"
				};
				System.out.printf("Chabot: %s%n", respostas[rnd.nextInt(respostas.length)]);
			}
			return true;
		}
		
		if (mensagem.contains("editar")) {
			editarConhecimento();
			return true;
		}

		return false;
	}

	private String gerarResposta(String mensagem) {
		// Saudações
		if (mensagem.contains("oi") || mensagem.contains("olá") || mensagem.contains("hey")) {
			String[] resposta = {
					String.format("Olá, como vai %s?", nomeUsuario),
					"Opa, tudo bom?",
					String.format("Tudo tranquilo %s?", nomeUsuario)
			};
			return resposta[rnd.nextInt(resposta.length)];
		}		

		// Agradecimentos
		if (mensagem.contains("obrigado")) {
			String[] resposta = {
					"De nada, qualquer coisa estou aqui para te responder",
					"Qualquer coisa pode falar comigo que nós desvendamos juntos"
			};
			return resposta[rnd.nextInt(resposta.length)];
		}

		// Palavras do conhecimento
		List<String> palavrasMensagem = extrairPalavras(mensagem);

		int maiorScore = 0;
		List<String> palavrasComMelhorScore = new ArrayList<>();
		
		for (String palavraChave: conhecimento.keySet()) {
			int score = palavrasMensagem.contains(palavraChave) ? 1 : 0;
			if (score > maiorScore) {
				maiorScore = score;
				palavrasComMelhorScore.clear();
				palavrasComMelhorScore.add(palavraChave);
			}else if (score == maiorScore && score > 0) {
				palavrasComMelhorScore.add(palavraChave);
			}
		}
		
		if (maiorScore > 0) {
			String palavraEscolhida = palavrasComMelhorScore.get(
					rnd.nextInt(palavrasComMelhorScore.size())
			);
			List<String> respostas = conhecimento.get(palavraEscolhida);
			return respostas.get(rnd.nextInt(respostas.size()));
		}

		// Palavras padrão
		String[] defaultResponse = {
				"Não entendo sobre esse assunto, poderia me ensinar sobre?",
				"Hmm, interessante, pode me falar mais sobre?",
				"Que tal me ensinar mais sobre isso?"
		};		
		return defaultResponse[rnd.nextInt(defaultResponse.length)];
	}

	private void consoleAjuda() {
		System.out.println("\n=== MENU DE AJUDA ===");
		String[] funcionalidades = {
				"- sair/exit: encerra o chatbot",
				"- ensinar: ensina o chatbot algo novo",
				"- ajuda/help: mostra o menu de ajuda",
				"- listar/conhecimento: mostra a lista de conhecimento do bot"
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

	private void ensinarChat() {
		System.out.print("Chatbot: Me diga a plavar-chave para o assunto principal, por favor (ex: tempo): ");
		String palavraChave = tratarMensagem(sc.nextLine().toLowerCase().trim());
		System.out.print("Chatbot: Me diga agora como responder a esta palavra chave, por favor: ");
		String respostaConhecimento = tratarMensagem(sc.nextLine());

		List<String> possiveisChaves = extrairPalavras(palavraChave);
		for (String chave: possiveisChaves) {
			conhecimento.computeIfAbsent(chave, k -> new ArrayList<>()).add(respostaConhecimento);
		}
		
		String[] respostas = {
				String.format("Obrigado, agora eu sei um pouco sobre '%s'", palavraChave),
				String.format("Perfeito! Agora posso conversar sobre '%s' com você", palavraChave),
				String.format("Eba! Aprendi algo novo sobre '%s', muito obrigado!", palavraChave),
				String.format("Que legal! Agora '%s' faz parte do meu conhecimento", palavraChave)
		};

		System.out.printf("Chatbot: %s%n", respostas[rnd.nextInt(respostas.length)]);
	}

	private String tratarMensagem(String entrada) {
		String res = entrada.toLowerCase();

		Map<String, List<String>> acentos = new HashMap<>() {{
			put("c", Arrays.asList("ç"));
			put("a", Arrays.asList("ã", "â", "á", "à"));
			put("i", Arrays.asList("í", "ì"));
			put("u", Arrays.asList("ú", "ù"));
			put("e", Arrays.asList("ê", "é", "è"));
			put("o", Arrays.asList("õ", "ô", "ó", "ò"));
		}};
		
		for (Map.Entry<String, List<String>> acento: acentos.entrySet()) {
			String letraBase = acento.getKey();
			for (String letra: acento.getValue()) {
				res = res.replace(letra, letraBase);
			}
		}
		
		res = res.replaceAll("[?!]", "");

		return res;
	}

	private List<String> extrairPalavras(String mensagem){
		Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
				"o",
				"a",
				"os",
				"as",
				"de",
				"da",
				"do",
				"para",
				"com",
				"em",
				"por",
				"que",
				"um",
				"uma",
				"é",
				"foi",
				"ser",
				"ter",
				"como",
				"mais",
				"muito",
				"bem",
				"já",
				"ainda",
				"mas",
				"ou",
				"se",
				"me",
				"te",
				"nos",
				"lhe"
		));
		String[] temp = mensagem.split(" ");
		List<String> aux = new ArrayList<>();
		for (String item: temp) {
			if (!STOP_WORDS.contains(item) && !item.isEmpty()) {
				aux.add(item);
			}
		}
		return aux;
	}

	private boolean listarConhecimento() {
		if (conhecimento.isEmpty()) return false;
		System.out.printf(
				"Chatbot: Eu possuo %d palavra(s)-chave(s) no meu conhecimento atualmente.%n",
				conhecimento.size()
		);
		int temp = 1;
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
	
	private void editarConhecimento() {
		// Listar conhecimento que o bot possui
		if (listarConhecimento()) {
			System.out.print(
					"\nChatbot: Qual conhecimento você deseja editar? Digite a palavra chave: "
			);
			String palavraChave = tratarMensagem(sc.nextLine());
			int temp = 1;
			if (conhecimento.containsKey(palavraChave)) {
				//for (String palavra: conhecimento.keySet()) {
					//if (palavra.equals(palavraChave)) {
						for (String lista: conhecimento.get(palavraChave)) {
							System.out.println("\n" + temp + ". " + lista);
							temp++;
						}
						System.out.print(
								"\nChatbot: Qual dessas palavras você deseja alterar? Digite o número: "
						);
						
						int indexFrase = sc.nextInt() - 1;
						sc.nextLine();
						if (indexFrase < 0 || indexFrase > (conhecimento.get(palavraChave).size() - 1)) {
							System.out.println("Chatbot: Palavra não encontrada!");
							return;
						}
						
						System.out.printf("Chatbot: Resposta escolhida: %s", conhecimento.get(palavraChave).get(indexFrase));
						System.out.println("\nChatbot: Agora me diga a resposta editada: ");
						String respostaEditada = tratarMensagem(sc.nextLine());
						conhecimento.get(palavraChave).remove(indexFrase);
						conhecimento.get(palavraChave).add(respostaEditada);
						System.out.println("Chatbot: Resposta editada com sucesso!");
						//break;
					//}
				//}
			}else {
				System.out.println("Chatbot: palavra-chave não encontrada");
			}
			
		}else {
			System.out.println("Chatbot: Conhecimento vazio!");
		}
	}
}