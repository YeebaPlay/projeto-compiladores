import java.util.ArrayList;
import java.util.List;

public class Token {

	public static List<Token> lista =  new ArrayList<>();
	
	private String imagem;
	private String classe;
	private int indice;
	private int linha;
	private int coluna;
	
	public String getImagem() {
		return imagem;
	}
	public void setImagem(String imagem) {
		this.imagem = imagem;
	}
	public String getClasse() {
		return classe;
	}
	public void setClasse(String classe) {
		this.classe = classe;
	}
	public int getIndice() {
		return indice;
	}
	public void setIndice(int indice) {
		this.indice = indice;
	}
	public int getLinha() {
		return linha;
	}
	public void setLinha(int linha) {
		this.linha = linha;
	}
	public int getColuna() {
		return coluna;
	}
	public void setColuna(int coluna) {
		this.coluna = coluna;
	}
	@Override
	public String toString() {
		return "Token [imagem=" + imagem + ", classe=" + classe + ", indice=" + indice + ", linha=" + linha
				+ ", coluna=" + coluna + "]";
	}
	
	
	
}
