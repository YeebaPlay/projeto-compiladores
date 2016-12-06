import java.util.ArrayList;
import java.util.List;

public class No {
	
	private No pai;
	private List<No> filhos = new ArrayList<No>();
	private String tipo;
	private Token token;
	
	public No(String tipo){
		this.tipo = tipo;
	}
	
	public No(Token token){
		this.token = token;
		this.tipo = "NoToken";
	}
	
	public void AdicionarFilho(No filho){
		filho.setPai(this);
		filhos.add(filho);
	}

	public No getPai() {
		return pai;
	}

	public void setPai(No pai) {
		this.pai = pai;
	}

	public No getFilho(int index) {
		return filhos.get(index);
	}
	
	public List<No> getFilhos() {
		return filhos;
	}

	public void setFilhos(List<No> filhos) {
		this.filhos = filhos;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	@Override
	public String toString() {
		if(this.token != null){
			return token.getImagem();
		}else{
			return this.tipo;
		}
	}
	
	

}
