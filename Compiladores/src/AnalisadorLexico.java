import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnalisadorLexico {

	public static List<String> listaReservadas = new ArrayList<>(Arrays.asList(new String [] {"int", "real", "texto", "}", "{", "mostra", "le", "se", "enquanto"}));
	public static List<Simbolo> listaSimbolos = new ArrayList<>();
	public static List<String> listaOperadores = new ArrayList<>(Arrays.asList(new String [] {"+", "-", "/", "*", "=", "<", ">", "<=", ">=", "==", "!=", "."}));
	
	
	public void ReconhecerLinha(String linha, int indexLinha){
		linha += " ";
		String sim = ""; int quantidadeListaSimbolos = 0; int coluna = 0;
		boolean isString = false;
		boolean isFinal = false; //Verifica se ele é final "
		for(int i = 0; i < linha.length(); i++){
			if(linha.charAt(i) == '#'){ break; }
			
			if(linha.charAt(i) == '$'){
				Token token = new Token();
				token.setImagem("$");
				token.setClasse("FIM");
				token.setLinha(indexLinha);
				token.setColuna(coluna);
				token.setIndice(Token.lista.size());
				Token.lista.add(token);
				break;
			}
			
			if(linha.charAt(i) == '"'){ 
				isString = true; 
				}
			/**
			 * Ele trava na primeira aspa, pula ela e concatena tudo que tem dentro da string até achar a próxima aspa, achando ele continua
			 * o precesso normalmente e armazena a string encontrada.
			 * 
			 * isString fala que ele achou uma string, isFinal mostra que ele chegou no final da string encontrada ou seja achou outra aspa após
			 * ter achado a primeira
			 */
			coluna++;
			if(isString){
				if(isFinal){
					if(linha.charAt(i) == '"'){
						isFinal = false; 
						isString = false; 
						Token token = new Token();
						token.setImagem(sim);
						token.setClasse("CLS");
						token.setLinha(indexLinha);
						token.setColuna(coluna);
						token.setIndice(-1);
						Token.lista.add(token);
					}
					sim += String.valueOf(linha.charAt(i));
					
				}else{
					isFinal = true;
				}
			}else{
				if(linha.charAt(i) != ' ' && linha.charAt(i) != '"'){
					sim += String.valueOf(linha.charAt(i));
					
				}else if(linha.charAt(i) != '"'){
				
					if(sim != null){
						if(IsContanteLiteralInteiro(sim)){
							Token token = new Token();
							token.setImagem(sim);
							token.setClasse("CLI");
							token.setLinha(indexLinha);
							token.setColuna(coluna);
							token.setIndice(-1);
							Token.lista.add(token);
							
						}else if(IsContanteLiteralReal(sim)){
							Token token = new Token();
							token.setImagem(sim);
							token.setClasse("CLR");
							token.setLinha(indexLinha);
							token.setColuna(coluna);
							token.setIndice(-1);
							Token.lista.add(token);
							
						}else if(listaReservadas.contains(sim)){
							if(IsDelimitador(sim)){
								Token token = new Token();
								token.setImagem(sim);
								token.setClasse("DE");
								token.setLinha(indexLinha);
								token.setColuna(coluna);
								token.setIndice(-1);
								Token.lista.add(token);
							}else{
								Token token = new Token();
								token.setImagem(sim);
								token.setClasse("PR");
								token.setLinha(indexLinha);
								token.setColuna(coluna);
								token.setIndice(-1);
								Token.lista.add(token);
							}
						}else if(IsIdentificador(sim)){
							if(!ContemSimbolo(sim)){
								quantidadeListaSimbolos = listaSimbolos.size();
								Simbolo objSimbolo = new Simbolo();
								objSimbolo.setIndice(quantidadeListaSimbolos - 1);
								objSimbolo.setImagem(sim);
								listaSimbolos.add(objSimbolo);
							}
							
							Token token = new Token();
							token.setImagem(sim);
							token.setClasse("ID");
							token.setLinha(indexLinha);
							token.setColuna(coluna);
							token.setIndice(listaSimbolos.size() - 1);
							Token.lista.add(token);
					
						}else if(IsOperador(sim)){
							Token token = new Token();
							token.setImagem(sim);
							token.setClasse("OP");
							token.setLinha(indexLinha);
							token.setColuna(coluna);
							token.setIndice(-1);
							Token.lista.add(token);
							
						}
						sim = "";
					}else{
						System.out.println("ERRO!!");
					}
				}
			}
		}
	}
	
	private boolean ContemSimbolo(String sim) {
		for(Simbolo s: listaSimbolos){
			if(s.getImagem().equals(sim)){
				return true;
			}
		}
		return false;
		
	}

	public void IsString(){
		
	}
	
	public boolean IsDelimitador(String sim){
		if(sim.equals("}") || sim.equals("{")){
			return true;
		}
		return false;
	}
	
	public boolean IsOperador(String sim){
		if(listaOperadores.contains(sim)){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean IsContanteLiteralInteiro(String sim){
		if(sim.matches("\\d+")){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean IsContanteLiteralReal(String sim){
		if(sim.matches("\\d+.\\d+")){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean IsIdentificador(String sim){
		if(sim.matches("\\w{1}\\w*\\d*")){
			return true;
		}else{
			return false;
		}
	}
	
}