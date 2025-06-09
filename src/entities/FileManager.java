package entities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class FileManager {
	private String CAMINHO_CONHECIMENTO = "src/data/conhecimento.json";
	
	/**
	 * Verifica diretorio existente
	 * 
	 * Instancia o caminho com File e verifica se ele existe, caso não exista ele cria o caminho
	 */
	private void verificarDiretorio() {
		// Instancia do arquivo
		File arquivo = new File(CAMINHO_CONHECIMENTO);
		// Verifica conteúdo do diretório
		File diretorio = arquivo.getParentFile();
		
		// Verifica se diretório existe
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
	public void salvarConhecimento(Map<String, List<String>> conhecimento) {
		verificarDiretorio(); // Verifica o diretório antes de salvar o arquivo
		StringBuilder json = new StringBuilder();
		
		// Define inicio do arquivo;
		json.append("{");
		
		int temp = 1;
		// Itera sobre cada palavra-chave e suas respostas
		for (String con: conhecimento.keySet()) {
			// Palavra-chave
			json.append("\n\"" + con + "\": [");
			// Verifica quantidade de respostas da palavra-chave
			if (conhecimento.get(con).size() == 1) {
				json.append("\"" + conhecimento.get(con).get(0) + "\"");
			}else {
				// Itera sobre respostas menos a última da lista
				for (int i = 0; i < conhecimento.get(con).size() - 1; i++) {
					// Adiciona resposta com vírgula
					json.append("\"" + conhecimento.get(con).get(i) + "\",");
				}
				// Adiciona última resposta
				json.append("\"" + conhecimento.get(con).get(conhecimento.get(con).size() - 1) + "\"");
			}
			
			// Verifica quantidade de palavras-chave
			if (temp < conhecimento.size()) {
				// Adiciona vírgula no final das respostas
				json.append("],");
			}else {
				// Final das respostas sem vírgula
				json.append("]");
			}
			
			temp++;
		}
		
		json.append("\n}");
		
		// Escreve no arquivo
		try (FileWriter writer = new FileWriter(CAMINHO_CONHECIMENTO)){
			writer.write(json.toString());
		}catch (IOException e) {
			System.out.println("Erro ao salvar: " + e.getMessage());
		}
	}
	
 	/**
 	 * Carrega conhecimento do arquivo JSON
 	 * 
 	 * Verifica a existencia do arquivo com o conhecimento base, caso contrário inicia com
 	 * conhecimento vazio.
 	 * 
 	 */
	public Map<String, List<String>> carregarConhecimento() {
		// Instancia do arquivo
		File arquivo = new File(CAMINHO_CONHECIMENTO);
		
		// Verifica se arquivo existe
		if (!arquivo.exists()) {
			System.out.println("Chatbot: Conhecimento vazio, iniciando do zero!");
			return new HashMap<>();
		}
		
		try {
			// Coleta dados do arquivo
			String jsonContent = lerArquivo(arquivo);
			// Verifica dados vazios
			if (!jsonContent.trim().isEmpty()) {
				// Análise de conteúdo
				return analiseJson(jsonContent);
			}
		}catch (IOException e) {
			System.out.println("Chatbot: Erro ao carregar conhecimento: " + e.getMessage());
		}
		
		return new HashMap<>();
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
		
		try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))){// Lê o arquivo
			String linha;
			// Verifica linha vazia
			while ((linha = reader.readLine()) != null) {
				// Adiciona conteúdo da linha
				content.append(linha).append("\n");
			}
		}catch (IOException e) {
			System.out.println("Chatbot: Não consegui ler o arquivo! " + e.getMessage());
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
	private Map<String, List<String>> analiseJson(String jsonContent) {
		Map<String, List<String>> conhecimento = new HashMap<>();
		// Remove quebra de linhas e espaços
		String json = jsonContent.replaceAll("\\s+",  " ").trim();

		// Retira 2 caracteres inciais e 3 finais
		json = json.substring(2, json.length() - 3);

		// Separa JSON pela string delimitadora
		String[] chaves = json.split("], ");
		for (int i = 0; i < chaves.length; i++) {
			// Separa palavra-chave
			String chave = chaves[i];

			// Adiciona colchete retirado anteriormente
			chave += "]";

			// Obtém inicio e fim da chave
			int inicioChave = chave.indexOf("\"") + 1;
			int fimChave = chave.indexOf("\"", inicioChave);
			// Palavra-chave padrão
			String chaveDefault = chave.substring(inicioChave, fimChave);
			
			// Obtém o inicio e fim da resposta
			int inicioResposta = chave.indexOf("[") + 1;
			int fimResposta = chave.indexOf("]");
			// Resposta padrão
			String[] respostaDefault = chave.substring(inicioResposta, fimResposta).split("\", \"");
			
			// Listar respostas
			List<String> listaRespostas = new ArrayList<>();
			for (String resp: respostaDefault) {
				// Retira vírgula e aspas
				for (String r: resp.split(",\"")) {
					// Retira aspas restantes
					listaRespostas.add(r.replaceAll("\"", ""));
				}
			}
			
			// Adiciona no conhecimento
			conhecimento.put(chaveDefault, listaRespostas);
		}
		// retorna o conhecimento
		return conhecimento;
	}
}
