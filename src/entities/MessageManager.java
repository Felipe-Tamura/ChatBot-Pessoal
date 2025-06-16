package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MessageManager {
	
	/**
	 * Valida palavra para conhecimento do bot
	 * 
	 * Evita dados vazios, palavras muito curtas e valida digitos numéricos
	 * 
	 * @param palavra - texto original a ser validado
	 * @return validação final com base nos critérios
	 * @see validarNumero()
	 */
	public boolean validarPalavra(String palavra) {
		// Valida palavra vazia
		if (palavra.isEmpty() || palavra.trim().isEmpty()) {
			System.out.println("Dados vazios não são válidos!");
			return true;
		}
		
		// Valida palavra curta
		if (palavra.length() < 2) {
			System.out.println("Palavra-chave curta!");
			return true;
		}
		
		// Valida digito
		if (palavra.matches("\\d+")) {
			try {
				return validarNumero(palavra);
			}catch (InputMismatchException e) {
				System.out.println("Entrada de dados invália: " + e.getMessage());
				return true;
			}catch (NumberFormatException e) {
				System.out.println("Formato inválido: " + e.getMessage());
				return true;
			}
		}
		
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
	public boolean validarNumero(String palavra) {
		// Converte texto para numero
		int numero = Integer.parseInt(palavra);
		// Separa digitos em linha
		String[] digito = palavra.split("");
		// Sequencia e repetição
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
			return false;
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
	public String tratarMensagem(String entrada) {
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
	public Map<String, List<String>> criarMapaAcentos(){
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
	public List<String> extrairPalavras(String mensagem){
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
}
