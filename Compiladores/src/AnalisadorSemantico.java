import java.util.ArrayList;
import java.util.List;

public class AnalisadorSemantico {

	private List<Simbolo> listaSimbolos;
	private No raiz;
	private List<String> erros = new ArrayList<>();
	
	AnalisadorSemantico(List<Simbolo> lista){
		this.listaSimbolos = lista;
	}
	
	private String getTipo(Token token){
		String tipo = token.getClasse();
		switch(tipo){
			case "CLI":
				return "int";
			case "CLR":
				return "real";
			case "CLS":
				return "texto";
			case "CLL":
				return "logico";
			case "ID":
				return listaSimbolos.get(token.getIndice()).getTipo();
			default:
				throw new RuntimeException();
		}		
	}
	
	private void setTipo(Token tk, String tipo){
		listaSimbolos.get(tk.getIndice()).setTipo(tipo);
	}
	
	public void MostraErros(){
		for (String s : erros) {
			System.out.println(s);
		}
	}
	
	public boolean TemErros(){
		return !erros.isEmpty();
	}
	
	public void Analisar(No raiz) {
		this.raiz = raiz;
		analisa(this.raiz);
	}
	
	public Object analisa(No no){
		
		System.out.println(no.getTipo());
		
		if(no.getTipo().equals("NO_LISTA_CMD")) {
			return listaComando(no);
		} else if(no.getTipo().equals("NO_LIST_ID")) {
			return listId(no);
		} else if(no.getTipo().equals("NO_LIST_ID2")) {
			return listId2(no);
		} else if (no.getTipo().equals("NO_DECL")) {
			return decl(no);
		} else if (no.getTipo().equals("NO_ATRIB")) {
			return atrib(no);
		} else if (no.getTipo().equals("NO_LACO")) {
			return laco(no);
		} else if (no.getTipo().equals("NO_COND")) {
			return cond(no);
		} else if (no.getTipo().equals("NO_ESCRITA")) {
			return escrita(no);
		} else if (no.getTipo().equals("NO_EXP_LOG")) {
			return expLog(no);
		} else if(no.getTipo().equals("NO_CMD")){
			return comando(no);
		} else if(no.getTipo().equals("NO_COMANDO_INTER")){
			return comandoInterno(no);
		} else if(no.getTipo().equals("NO_OPERAN")){
			return operan(no);
		} else if(no.getTipo().equals("NO_EXP_REL")){
			return expRel(no);
		} else if(no.getTipo().equals("NO_SENAO")){
			return senao(no);
		} else if(no.getTipo().equals("NO_OP_ARIT")){
			return opArit(no);
		} else if(no.getTipo().equals("NO_EXP_ARIT")){
			return opArit(no);
		} else if(no.getTipo().equals("NO_OP_LOG")){
			return opLog(no);
		}
	
		return no;
	}
	
//	<list_id> ::= id <list_id2>
	private Object listId(No no) {
		Token id = no.getFilho(0).getToken();
		List<Token> ids = (List<Token>) analisa(no.getFilho(1)); 
		ids.add(0,id);
		return ids;
	}
 
	private Object listId2(No no) {
		if (no.getFilhos().isEmpty()) {
			return new ArrayList<>();
		} else {
			return analisa(no.getFilho(0));
		}
	}

//	<list_cmd> ::= <cmd> <list_cmd> |
	private Object listaComando(No no) {
		if (!no.getFilhos().isEmpty()) {
			analisa(no.getFilho(0));
			analisa(no.getFilhos().get(1));
		}
		return null;
	}

//	<cmd> ::= '{' <cmd_inter> '}'
	private Object comando(No no) {
		analisa(no.getFilhos().get(1));
		return null;
	}

//	<cmd_inter> ::= <decl> | <atrib> | <laco> | <cond> | <escrita> | <leitura>
	private Object comandoInterno(No no) {
		analisa(no.getFilhos().get(0));
		return null;
	}
//	<leitura> ::= 'le' id
	private void leitura(No no) {
		// TODO Auto-generated method stub
		
	}

//	<escrita> ::= 'mostra' <operan>
	private Object escrita(No no) {
		analisa(no.getFilho(1));
		return null;
	}

//	<cond> ::= 'se' <exp_log> '{' <list_cmd> '}' <senao>
	private Object cond(No no) {
		analisa(no.getFilho(1));
		analisa(no.getFilho(3));
		analisa(no.getFilho(5));
		return null;
		
	}

//	<senao> ::= '{' <list_cmd> '}' |
	private Object senao(No no) {
		listaComando(no.getFilho(1));
		return null;
	}

//	<laco> ::= 'enquanto' <exp_log> <list_cmd>
	private Object laco(No no) {
		analisa(no.getFilho(1));
		analisa(no.getFilho(2));
		return null;
	}
//	<exp_log> ::= '{' <exp_rel> '}'
	private Object expLog(No no) {
		return analisa(no.getFilho(1));
	}
	
//	<exp_rel> ::= <op_rel> <operan> <operan> | <op_log> '{' <exp_rel> '}' '{' <exp_rel> '}'
	private Object expRel(No no) {
		Token op1 = (Token) analisa(no.getFilho(1));
		Token op2 = (Token) analisa(no.getFilho(2));
		String tipo1 = getTipo(op1);
		String tipo2 = getTipo(op2);
		if (tipo1 == null) {
			erros.add("Erro: Erro tipo1 nulo expRel na linha "+op1.getLinha()+" na coluna "+op1.getColuna());
		} else if (tipo2 == null) {
			erros.add("Erro: Erro tipo2 nulo expRel na linha "+op2.getLinha()+" na coluna "+op2.getColuna());
		} else if (!tipo1.equals(tipo2)) {
			erros.add("Erro: Erro tipos diferentes expRel na linha "+op2.getLinha()+" na coluna "+op2.getColuna());
		}
		return null;
	}

//	<op_log> ::= '&&' | '||'
	private Object opLog(No no) {
		return null;
	}
	
//	<atrib> ::= '=' id <exp_arit>
	private Object atrib(No no) {
		Token id = no.getFilho(1).getToken();
		List<Token> operans = (List<Token>) expArit(no.getFilho(2));
		String tipoId = getTipo(id);
		if (tipoId == null) { 
			erros.add("Erro: Erro variavel não declarada em atrib na linha "+id.getLinha()+" na coluna "+id.getColuna());
			return null;
		}
		for (Token t: operans) {
			String tipo = getTipo(t);
			if (tipo == null) {
				erros.add("Erro: Erro variável não declarada em atrib na linha "+t.getLinha()+" na coluna "+t.getColuna());
			}else if (!tipo.equals(tipoId)) {
				erros.add("Erro: Erro tipos diferentes em atrib na linha "+t.getLinha()+" na coluna "+t.getColuna());
			}
		}
		return null;
	}

//	<exp_arit> ::= <operan> | '{' <op_arit> <exp_arit> <exp_arit> '}'
	private Object expArit(No no) {
		if (no.getFilhos().size() == 1) {
			List<Token> tokens = new ArrayList<>();
			Token token = (Token) analisa(no.getFilho(0));
			tokens.add(token);
			return tokens;
		} else if (no.getFilhos().size() == 5) {
			List<Token> tokens1 = (List<Token>) expArit(no.getFilho(2));
			List<Token> tokens2 = (List<Token>) expArit(no.getFilho(3));
			tokens2.addAll(tokens1);
			return tokens2;
		} else {
			throw new RuntimeException("ERRO: NO inválido expArit");
		}
	}
	
//	<op_arit> ::= '+' | '-' | '*' | '/' | '.'	
	private Object opArit(No no) {
		return no.getFilho(0).getToken();
	}

//	<operan> ::= id | cli | clr | cls
	private Object operan(No no) {
		return no.getFilho(0).getToken();
	}

	//	<decl> ::= <tipo> <list_id>
	private Object decl(No no) {
		String tipo = (String) tipo(no.getFilho(0));
		List<Token> ids = (List<Token>) listId(no.getFilho(1));
		for(Token id: ids) {
			if(getTipo(id) != null) {
				erros.add("Erro: Variável não declarada em decl na linha "+no.getToken().getLinha()+" na coluna "+no.getToken().getColuna());
			} else {
				setTipo(id, tipo);
			}
		}
		return null;
	}

	private String tipo(No no) {
		return no.getFilho(0).getToken().getImagem();
	}
}
