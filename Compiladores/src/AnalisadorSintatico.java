import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.plaf.synth.SynthSeparatorUI;

public class AnalisadorSintatico {

	private Token token;
	private int pToken = 0;
	private List<Token> listaToken;
	public List<String> erros = new ArrayList<>();
	public static List<String> listaTipo = new ArrayList<>(Arrays.asList(new String [] {"int", "real", "texto", "logico"}));
	public static List<String> listaOperadores = new ArrayList<>(Arrays.asList(new String [] {"=", "<", ">", "<=", ">=", "==", "!=", "."}));
	public static List<String> listaOpArit = new ArrayList<>(Arrays.asList(new String [] {"+", "-", "*", "/", "."}));
	public No raiz;
	
	public AnalisadorSintatico(List<Token> listaToken){
		this.listaToken = listaToken;
	}
	
	public void leToken(){
		token = listaToken.get(pToken++);
		System.out.println(token.getImagem());
	}
	
	public boolean TemErros(){
		return !erros.isEmpty();
	}
	
	public void MostraErros(){
		for (String s : erros) {
			System.out.println(s);
		}
	}
	
	public void MostraArvore(){
		MostraNo(raiz, "   ");
	}
	
	private void MostraNo(No no, String esp) {
		System.out.println(esp + no);
		for(No filho: no.getFilhos()){
			MostraNo(filho, esp + "   ");
		}
	}

	public void Analisar() {
		if (listaToken != null && !listaToken.isEmpty()) {
			leToken();
			raiz = listaComando();
			if(!token.getImagem().equals("$")){
				erros.add("Esperado o marcado de final de arquivo!!!");
			}
		}
	}
	
	//<list_cmd> ::= <cmd> <list_cmd> |
	public No listaComando(){
		No no = new No("NO_LISTA_CMD");
		if(token.getImagem().equals("{")){
			no.AdicionarFilho(comando());
			no.AdicionarFilho(listaComando());
		}
		return no;
	}
	
	//<cmd> ::= '{' <cmd_inter> '}'
	private No comando() {
		No no = new No("NO_CMD");
		if(token.getImagem().equals("{")){
			no.AdicionarFilho(new No(token));
			leToken();
			no.AdicionarFilho(comandoInter());
			if(token.getImagem().equals("}")){
				no.AdicionarFilho(new No(token));
				leToken();
			}else{
				erros.add("Erro: Não encontramos o comando de fim } na linha "+token.getLinha()+" na coluna "+token.getColuna());
			}
		}else{
			erros.add("Erro: Não encontramos o comando de inicio { na linha "+token.getLinha()+" na coluna "+token.getColuna());
		}
		return no;
	}
	
	//<cmd_inter> ::= <decl> | <atrib> | <laco> | <cond> | <escrita> | <leitura>
	private No comandoInter(){
		No no = new No("NO_COMANDO_INTER");
		if(listaTipo.contains(token.getImagem())){
			no.AdicionarFilho(decl());
		}else if(token.getImagem().equals("=")){
			no.AdicionarFilho(atrib());
		}else if(token.getImagem().equals("enquanto")){
			no.AdicionarFilho(laco());
		}else if(token.getImagem().equals("se")){
			no.AdicionarFilho(cond());
		}else if(token.getImagem().equals("mostra")){
			no.AdicionarFilho(escrita());
		}else if(token.getImagem().equals("le")){
			no.AdicionarFilho(leitura());
		}else{
			erros.add("Erro: Erro de sintaxe na linha "+token.getLinha()+" na coluna "+token.getColuna());
		}
		return no;
	}

	//<leitura> ::= 'le' id
	private No leitura() {
		No no = new No("NO_LEITURA");
		// TODO Auto-generated method stub
		if(token.getImagem().equals("le")){
			no.AdicionarFilho(new No(token));
			leToken();
			if(token.getClasse().equals("ID")){
				no.AdicionarFilho(new No(token));
				leToken();
			}else{
				erros.add("Erro: Erro na declaracão ID na linha "+token.getLinha()+" na coluna "+token.getColuna());
			}
		}else{
			erros.add("Erro: Erro na leitura (le) na linha "+token.getLinha()+" na coluna "+token.getColuna());
		}
		return no;
	}

	//<escrita> ::= 'mostra' <operan>
	private No escrita() {
		No no = new No("NO_ESCRITA");
		// TODO Auto-generated method stub
		if(token.getImagem().equals("mostra")){
			no.AdicionarFilho(new No(token));
			leToken();
			no.AdicionarFilho(operan());
		}else{
			erros.add("Erro: Erro de sintaxe na linha "+token.getLinha()+" na coluna "+token.getColuna());
		}
		return no;
	}

	//<operan> ::= id | cli | clr | cls
	private No operan() {
		No no = new No("NO_OPERAN");
		// TODO Auto-generated method stub
		if(token.getClasse().equals("ID") || token.getClasse().equals("CLI") || token.getClasse().equals("CLR") || token.getClasse().equals("CLS")){
			no.AdicionarFilho(new No(token));
			leToken();
		}else{
			erros.add("Erro: Erro de sintaxe na linha "+token.getLinha()+" na coluna "+token.getColuna());
		}
		return no;
	}

	//<cond> ::= 'se' <exp_log> '{' <list_cmd> '}' <senao>
	private No cond() {
		No no = new No("NO_COND");
		// TODO Auto-generated method stub
		if(token.getImagem().equals("se")){
			no.AdicionarFilho(new No(token));
			leToken();
			no.AdicionarFilho(expLog());
			if(token.getImagem().equals("{")){
				no.AdicionarFilho(new No(token));
				leToken();
				no.AdicionarFilho(listaComando());
				if(token.getImagem().equals("}")){
					no.AdicionarFilho(new No(token));
					leToken();
					no.AdicionarFilho(senao());
				}else{
					erros.add("Erro: Erro no fim } na linha "+token.getLinha()+" na coluna "+token.getColuna());
				}
			}
		}
		return no;
	}

	//<senao> ::= '{' <list_cmd> '}' |
	private No senao() {
		No no = new No("NO_SENAO");
		// TODO Auto-generated method stub
		if(token.getImagem().equals("{")){
			no.AdicionarFilho(new No(token));
			leToken();
			no.AdicionarFilho(listaComando());
			if(token.getImagem().equals("}")){
				no.AdicionarFilho(new No(token));
				leToken();
			}else{
				erros.add("Erro: Erro no fim } na linha "+token.getLinha()+" na coluna "+token.getColuna());
			}
		}
		return no;
	}

	//<laco> ::= 'enquanto' <exp_log> <list_cmd>
	private No laco() {
		No no = new No("NO_LACO");
		// TODO Auto-generated method stub
		if(token.getImagem().equals("enquanto")){
			no.AdicionarFilho(new No(token));
			leToken();
			no.AdicionarFilho(expLog());
			no.AdicionarFilho(listaComando());
		}else{
			erros.add("Erro: Erro no enquanto na linha "+token.getLinha()+" na coluna "+token.getColuna());
		}
		return no;
	}

	//<atrib> ::= '=' id <exp_arit>
	private No atrib() {
		No no = new No("NO_ATRIB");
		// TODO Auto-generated method stub
		if(token.getImagem().equals("=")){
			no.AdicionarFilho(new No(token));
			leToken();
			if(token.getClasse().equals("ID")){
				no.AdicionarFilho(new No(token));
				leToken();
				no.AdicionarFilho(expArit());
			}else{
				erros.add("Erro: Erro de atribuicão (= x 5) na linha "+token.getLinha()+" na coluna "+token.getColuna());
			}
		}else{
			erros.add("Erro: Erro ao atribuir = na linha "+token.getLinha()+" na coluna "+token.getColuna());
		}
		return no;
	}
	
	//<decl> ::= <tipo> <list_id>
	private No decl() {
		No no = new No("NO_DECL");
		if(listaTipo.contains(token.getImagem())){
			no.AdicionarFilho(tipo());
			no.AdicionarFilho(listId());
		}else{
			erros.add("Erro: Erro de declaracão (int x) na linha "+token.getLinha()+" na coluna "+token.getColuna());
		}
		return no;
	}

	private No tipo() {
		No no = new No("NO_TIPO");
		if (listaTipo.contains(token.getImagem())) {
			no.AdicionarFilho(new No(token));
			leToken();
		} else {
			erros.add("Erro: Erro de declaracão (int x, float x ...) na linha "+token.getLinha()+" na coluna "+token.getColuna());
		}
		return no;
	}
	
	//<list_id> ::= id <list_id2>
	private No listId() {
		No no = new No("NO_LIST_ID");
		// TODO Auto-generated method stub
		if(token.getClasse().equals("ID")){
			no.AdicionarFilho(new No(token));
			leToken();
			no.AdicionarFilho(listId2());
		}else{
			erros.add("Erro: Erro na declaracão ID na linha "+token.getLinha()+" na coluna "+token.getColuna());
		}
		return no;
	}

	//<list_id2> ::= <list_id> |
	private No listId2() {
		No no = new No("NO_LIST_ID2");
		// TODO Auto-generated method stub
		if(token.getClasse().equals("ID")){
			no.AdicionarFilho(listId());
		}
		return no;
	}

	//<exp_arit> ::= <operan> | '{' <op_arit> <exp_arit> <exp_arit> '}'
	private No expArit() {
		No no = new No("NO_EXP_ARIT");
		// TODO Auto-generated method stub
		if(token.getClasse().equals("ID") || token.getClasse().equals("CLI") || token.getClasse().equals("CLR") || token.getClasse().equals("CLS")){
			no.AdicionarFilho(operan());
		}else if(token.getImagem().equals("{")){
			no.AdicionarFilho(new No(token));
			leToken();
			no.AdicionarFilho(opArit());
			no.AdicionarFilho(expArit());
			no.AdicionarFilho(expArit());
			if(token.getImagem().equals("}")){
				no.AdicionarFilho(new No(token));
				leToken();
			}else{
				erros.add("Erro: Erro no fim } na linha "+token.getLinha()+" na coluna "+token.getColuna());
			}
		}else{
			erros.add("Erro: Erro de sintaxe na linha "+token.getLinha()+" na coluna "+token.getColuna());
		}
		return no;
	}
	
	//<op_arit> ::= '+' | '-' | '*' | '/' | '.'
	private No opArit() {
		No no = new No("NO_OP_ARIT");
		// TODO Auto-generated method stub
		if(listaOpArit.contains(token.getImagem())){
			no.AdicionarFilho(new No(token));
			leToken();
		}else{
			erros.add("Erro: Erro na operacao aritmetica na linha "+token.getLinha()+" na coluna "+token.getColuna());
		}
		return no;
	}

	//<exp_log> ::= '{' <exp_rel> '}'
	private No expLog() {
		No no = new No("NO_EXP_LOG");
		// TODO Auto-generated method stub
		if(token.getImagem().equals("{")){
			no.AdicionarFilho(new No(token));
			leToken();
			no.AdicionarFilho(expRel());
			if(token.getImagem().equals("}")){
				no.AdicionarFilho(new No(token));
				leToken();
			}else{
				erros.add("Erro: Erro no fim } na linha "+token.getLinha()+" na coluna "+token.getColuna());
			}
		}
		return no;
	}

	//<op_rel> ::= '>' | '<' | '>=' | '<=' | '==' | '!='
	private No opRel(){
		No no = new No("NO_OP_REL");
		if(listaOperadores.contains(token.getImagem())){
			no.AdicionarFilho(new No(token));
			leToken();
		}else{
			erros.add("Erro: Erro no operador na linha "+token.getLinha()+" na coluna "+token.getColuna());
		}
		return no;
	}
	
	private No opLog() {
		No no = new No("NO_OP_LOG");
		if (token.getImagem().equals("&&") || token.getImagem().equals("||")) {
			no.AdicionarFilho(new No(token));
			leToken();
		} else {
			erros.add("Erro: Erro no operador logico && ou || na linha "+token.getLinha()+" na coluna "+token.getColuna());
		}
		return no;
	}
	
	//<exp_rel> ::= <op_rel> <operan> <operan> | <op_log> '{' <exp_rel> '}' '{' <exp_rel> '}'
	private No expRel() {
		No no = new No("NO_EXP_REL");
		// TODO Auto-generated method stub
		if(listaOperadores.contains(token.getImagem())){
			no.AdicionarFilho(opRel());
			no.AdicionarFilho(operan());
			no.AdicionarFilho(operan());
		}else if(token.getImagem().equals("&&") || token.getImagem().equals("||")){
			no.AdicionarFilho(opLog());
			if (token.getImagem().equals("{")) {
				no.AdicionarFilho(new No(token));
				leToken();
				no.AdicionarFilho(expRel());
				if (token.getImagem().equals("}")) { 
					no.AdicionarFilho(new No(token));
					leToken();
				} else {
					erros.add("Erro: Erro esperando } na linha "+token.getLinha()+" na coluna "+token.getColuna());
				}
			} else {
				erros.add("Erro: Erro esperando { na linha "+token.getLinha()+" na coluna "+token.getColuna());
			}
			if (token.getImagem().equals("{")) {
				no.AdicionarFilho(new No(token));
				leToken();
				no.AdicionarFilho(expRel());
				if (token.getImagem().equals("}")) { 
					no.AdicionarFilho(new No(token));
					leToken();
				} else {
					erros.add("Erro: Erro esperando } na linha "+token.getLinha()+" na coluna "+token.getColuna());
				}
			} else {
				erros.add("Erro: Erro esperando } na linha "+token.getLinha()+" na coluna "+token.getColuna());
			}
		}
		return no;
	}
}
