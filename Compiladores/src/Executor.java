import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Executor {

	private List<Simbolo> listaSimbolos;
	private No raiz;
	private List<String> erros = new ArrayList<>();
	
	Executor(List<Simbolo> lista){
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
		
		//System.out.println(no.getTipo());
		
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
			return expArit(no);
		} else if(no.getTipo().equals("NO_OP_LOG")){
			return opLog(no);
		}	else if(no.getTipo().equals("NO_LEITURA")){
			return leitura(no);
		} else if(no.getTipo().equals("NO_OP_REL")){
			return opRel(no);
		}
	
		return no;
	}
	
	private Object getValorDeclaracao(String tipo) {
		if (tipo.equals("int")) {
			return 0;
		} else if (tipo.equals("real")) {
			return 0.0;
		} else if (tipo.equals("texto")) {
			return "";
		} else {
			return null;
		}
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
	private Object leitura(No no) {
		String s = "";
		Scanner ler = new Scanner(System.in);
		s = ler.nextLine();
	
		setValor(no.getFilho(1).getToken(), s);
		return null;
	}

//	<escrita> ::= 'mostra' <operan>
	private Object escrita(No no) {
		String tipo = getTipo(no.getFilho(1).getFilho(0).getToken());
		if(tipo.equals("texto")){
			System.out.println(no.getFilho(1).getFilho(0));
		}else if(tipo.equals("int")){
			System.out.println(getValorSimbolo(no.getFilho(1).getFilho(0).getToken()));
		}else if(tipo.equals("real")){
			System.out.println(getValorSimbolo(no.getFilho(1).getFilho(0).getToken()));
		}
		
		return null;
	}

//	<cond> ::= 'se' <exp_log> '{' <list_cmd> '}' <senao>
	private Object cond(No no) {
		boolean cond = (boolean) analisa(no.getFilho(1));
		if(cond){
			analisa(no.getFilho(3));
		}else{
			analisa(no.getFilho(5));
		}
		return null;
		
	}

//	<senao> ::= '{' <list_cmd> '}' |
	private Object senao(No no) {
		listaComando(no.getFilho(1));
		return null;
	}

//	<laco> ::= 'enquanto' <exp_log> <list_cmd>
	private Object laco(No no) {
		boolean cond = (boolean) analisa(no.getFilho(1));
		while(cond){
			analisa(no.getFilho(2));
			cond = (boolean) analisa(no.getFilho(1));
		}
		return null;
	}
//	<exp_log> ::= '{' <exp_rel> '}'
	private boolean expLog(No no) {
		return (boolean) analisa(no.getFilho(1));
	}
	
	//<op_rel> ::= '>' | '<' | '>=' | '<=' | '==' | '!='
	private Object opRel (No no){
		return no.getFilho(0);
	}
	
//	<exp_rel> ::= <op_rel> <operan> <operan> | <op_log> '{' <exp_rel> '}' '{' <exp_rel> '}'
	private boolean expRel(No no) {
		if (no.getFilho(0).getTipo().equals("NO_OP_REL")) {

			No sinal = (No) analisa(no.getFilho(0));
			Object operan1 = (Object) analisa(no.getFilho(1));
			Object operan2 = (Object) analisa(no.getFilho(2));
			
			if (sinal.getToken().getImagem().equals(">")) {
				if (operan1 instanceof Integer && operan2 instanceof Integer) {
					if (((Integer) operan1).intValue() > ((Integer) operan2).intValue()) {
						return true;
					} else {
						return false;
					}
				} else if (operan1 instanceof Double && operan2 instanceof Double) {
					if (((Double) operan1).doubleValue() > ((Double) operan2).doubleValue()) {
						return true;
					} else {
						return false;
					}
				}
				
			} else if (sinal.getToken().getImagem().equals("<")) {
				if (operan1 instanceof Integer && operan2 instanceof Integer) {
					if (((Integer) operan1).intValue() < ((Integer) operan2).intValue()) {
						return true;
					} else {
						return false;
					}
				}else if (operan1 instanceof Double && operan2 instanceof Double) {
					if (((Double) operan1).doubleValue() < ((Double) operan2).doubleValue()) {
						return true;
					} else {
						return false;
					}
				}
			} else if (sinal.getToken().getImagem().equals(">=")) {
				if (operan1 instanceof Integer && operan2 instanceof Integer) {
					if (((Integer) operan1).intValue() >= ((Integer) operan2).intValue()) {
						return true;
					} else {
						return false;
					}
				}else if (operan1 instanceof Double && operan2 instanceof Double) {
					if (((Double) operan1).doubleValue() >= ((Double) operan2).doubleValue()) {
						return true;
					} else {
						return false;
					}
				}
			} else if (sinal.getToken().getImagem().equals("<=")) {
				if (operan1 instanceof Integer && operan2 instanceof Integer) {
					if (((Integer) operan1).intValue() <= ((Integer) operan2).intValue()) {
						return true;
					} else {
						return false;
					}
				}else if (operan1 instanceof Double && operan2 instanceof Double) {
					if (((Double) operan1).doubleValue() <= ((Double) operan2).doubleValue()) {
						return true;
					} else {
						return false;
					}
				}
			} else if (sinal.getToken().getImagem().equals("==")) {
				if (operan1 instanceof Integer && operan2 instanceof Integer) {
					if (((Integer) operan1).intValue() == ((Integer) operan2).intValue()) {
						return true;
					} else {
						return false;
					}
				}else if (operan1 instanceof Double && operan2 instanceof Double) {
					if (((Double) operan1).doubleValue() == ((Double) operan2).doubleValue()) {
						return true;
					} else {
						return false;
					}
				}
			} else if (sinal.getToken().getImagem().equals("!=")) {
				if (operan1 instanceof Integer && operan2 instanceof Integer) {
					if (((Integer) operan1).intValue() != ((Integer) operan2).intValue()) {
						return true;
					} else {
						return false;
					}
				}else if (operan1 instanceof Double && operan2 instanceof Double) {
					if (((Double) operan1).doubleValue() != ((Double) operan2).doubleValue()) {
						return true;
					} else {
						return false;
					}
				}
			}

		} else if (no.getFilho(0).getTipo().equals("NO_OP_LOG")) {
			analisa(no.getFilhos().get(0));
			analisa(no.getFilhos().get(2));
			analisa(no.getFilhos().get(5));
		}
		return false;
	}

//	<op_log> ::= '&&' | '||'
	private Object opLog(No no) {
		return null;
	}
	
//	<atrib> ::= '=' id <exp_arit>
	private Object atrib(No no) {
		Token id = no.getFilho(1).getToken();
		Object valor = analisa(no.getFilho(2));
		setValor(id, valor);
		return null;
	}

//	<exp_arit> ::= <operan> | '{' <op_arit> <exp_arit> <exp_arit> '}'
	private Object expArit(No no) {
		if (no.getFilhos().size() == 1) {
			Object Tokenvalor = analisa(no.getFilho(0));
			return Tokenvalor;
		} else if (no.getFilhos().size() == 5) {
			Object valor1 =  expArit(no.getFilho(2));
			Object valor2 =  expArit(no.getFilho(3));
			Token op = (Token) opArit(no.getFilho(1));
			
			if(op.getImagem().equals("+")){
				if (valor1 instanceof Integer && valor2 instanceof Integer) {
					return (int) valor1 + (int) valor2;
				} else if(valor1 instanceof Double && valor2 instanceof Double){
					return (Double) valor1 + (Double) valor2;
				}
			}else if(op.getImagem().equals("-")){
				if (valor1 instanceof Integer && valor2 instanceof Integer) {
					return (int) valor1 - (int) valor2;
				} else if(valor1 instanceof Double && valor2 instanceof Double){
					return (Double) valor1 - (Double) valor2;
				}
			}else if(op.getImagem().equals("*")){
				if (valor1 instanceof Integer && valor2 instanceof Integer) {
					return (int) valor1 * (int) valor2;
				} else if(valor1 instanceof Double && valor2 instanceof Double){
					return (Double) valor1 * (Double) valor2;
				}
			}else if(op.getImagem().equals("/")){
				if (valor1 instanceof Integer && valor2 instanceof Integer) {
					return (int) valor1 / (int) valor2;
				} else if(valor1 instanceof Double && valor2 instanceof Double){
					return (Double) valor1 / (Double) valor2;
				}
			}else if(op.getImagem().equals(".")){
				if (valor1 instanceof String && valor2 instanceof String) {
					return ""+ valor1.toString() + valor2.toString();
				}
			}else{
				return null;
			}
		} else {
			throw new RuntimeException("ERRO: NO inválido expArit");
		}
		return null;
	}
	
//	<op_arit> ::= '+' | '-' | '*' | '/' | '.'	
	private Object opArit(No no) {
		return no.getFilho(0).getToken();
	}

//	<operan> ::= id | cli | clr | cls
	private Object operan(No no) {
		//return no.getFilho(0).getToken();
		if(no.getFilho(0).getToken().getClasse().equals("ID")){
			return getValorSimbolo(no.getFilho(0).getToken());
		}else if(no.getFilho(0).getToken().getClasse().equals("CLI")){
			return Integer.parseInt(no.getFilho(0).getToken().getImagem());
		}else if(no.getFilho(0).getToken().getClasse().equals("CLR")){
			return Double.parseDouble(no.getFilho(0).getToken().getImagem());
		}else if(no.getFilho(0).getToken().getClasse().equals("CLS")){
			return no.getFilho(0).getToken().getImagem().toString();
		}else {
			return null; //ERRO
		}
	}

	//	<decl> ::= <tipo> <list_id>
	private Object decl(No no) {
		String tipo = (String) tipo(no.getFilho(0));
		List<Token> ids = (List<Token>) listId(no.getFilho(1));
		for(Token id: ids) {
			setValor(id, getValorDeclaracao(tipo));
		}
		return null;
	}

	private String tipo(No no) {
		return no.getFilho(0).getToken().getImagem();
	}
	
	private void setValor (Token id, Object valor){
		for(Simbolo sim : listaSimbolos){
			if(sim.getIndice() == id.getIndice()){
				sim.setValor(valor);
			}
		}
	}
	
	private Object getValorSimbolo(Token token) {
		Object valor = null;
		for(Simbolo sim : listaSimbolos){
			if(sim.getIndice() == token.getIndice()){
				if(sim.getTipo().equals("int")){
					valor = sim.getValor();
					valor = Integer.parseInt(valor.toString());
				}else if(sim.getTipo().equals("real")){
					valor = sim.getValor();
					valor = Double.parseDouble(valor.toString());
				}
			}
		}
		return valor;
	}
}
