import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


public class Main {
	 public static void main(String[] args) {
		    Scanner ler = new Scanner(System.in);
		    ///Users/viniciusgabriel/Documents/texto.RTF
		    //System.out.println("Informe o nome de arquivo texto:");
		    //String nome = ler.nextLine();
		    
		    String nome = "/Users/viniciusgabriel/Documents/textoTeste.txt";
		 
		    System.out.println("\nConteúdo do arquivo texto:");
		    try {
		      FileReader arq = new FileReader(nome);
		      BufferedReader lerArq = new BufferedReader(arq);
		      AnalisadorLexico analise = new AnalisadorLexico();
		      int indexLinha = 0;
		      String linha = "";
		      while (linha != null) {
		        System.out.println(linha);
		        linha = lerArq.readLine();
		        if(linha != null){
		        	analise.ReconhecerLinha(linha, indexLinha);
			        indexLinha++;
		        }
		      }
		      
		      System.out.println("-------------------");
		      System.out.println("Simbolos");
		      
		      for(Simbolo s: AnalisadorLexico.listaSimbolos){
		    	System.out.println(s.getImagem());  
		      }
		      
		      System.out.println("-------------------");
		      System.out.println("Tokens");
		      
		      for(Token t: Token.lista){ System.out.println(t); }
		 
		      arq.close();
		      
		      //Analisador Sintatico
		      AnalisadorSintatico analisador = new AnalisadorSintatico(Token.lista);
		      analisador.Analisar();
		      if(analisador.TemErros()){
		    	  analisador.MostraErros();
		    	  System.exit(0);
		      }else{
		    	  System.out.println("SUCESSO!!");
		      }
		      
		      //analisador.MostraArvore();
		      
		      //Analisador Semantico
		      AnalisadorSemantico semantico = new AnalisadorSemantico(AnalisadorLexico.listaSimbolos);
		      semantico.Analisar(analisador.raiz);
		      if(semantico.TemErros()){
		    	  semantico.MostraErros();
		    	  System.exit(0);
		      }else{
		    	  System.out.println("SUCESSO!!!");
		      }
		      
		    } catch (IOException e) {
		        System.err.println("Erro na abertura do arquivo: %s.\n" +
		          e.getMessage());
		    }
		 
		    System.out.println();
		    ler.close();
		  }
}
