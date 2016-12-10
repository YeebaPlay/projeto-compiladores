# projeto-compiladores
Desenvolvimento de uma linguagem de programação nova, sem funções e classes, apenas para nível de conhecimento, a linguagem será de código aberto, qualquer um poderá utilizar.

Linguagem desenvolvida na faculdade, com sintaxe totalmente diferente, tudo dentro de blocos { }, cada bloco de ação deve estar dentro das chaves, por exemplo: 

#Para mostrar uma mensagem no terminal digite <br />
{ mostra "Texto que quiser" } <br />
{ mostra variavel } <br />

#Declarar variáveis  <br />
{ int x } <br />
{ real y } <br />
{ texto z } <br />
{ logico q } <br />

#Criar um bloco de condição "if" <br />
{ se { CONDICAO }  <br />
  { BLOCOS DE COMANDO } <br />
} <br />

ex:  <br />

{ se { == x 10} <br />
  { = x 20 } <br />
} <br />

O bloco acima diz: Se x igual a 10, então x igual a 20 <br />

#Atribuíção de varáveis <br />
{ = x 20 } <br />

#Laço (while) <br />

{ enquanto { < x 10 } <br />
  { mostra x } <br />
} <br />

# SCP Fatorial

{ int i n fat } <br />
{ mostra "Entre n:" } <br />
{ le n } <br />
{ se { < n 0 }<br />
  { <br />
    { mostra " Entre valor valido " }<br />
  }<br />
  {<br />
    { = i 1 }<br />
    { = fat 1 }<br />
    { enquanto { <= i n }<br />
      { = fat { * fat i } }<br />
      { = i { + i 1 } }<br />
    }<br />
    { mostra "fat: " }<br />
    { mostra fat }<br />
  }<br />
}<br />
{ real r1 }<br />
{ = r1 2.34 }<br />
$

#Passos para testar a linguagem

1 - Em uma mesma pasta coloque o arquivo jar e o .scp <br />
2 - Abre o terminal <br />
3 - Vá até a pasta com os dois arquivos utilizando o CD (dentro do terminal) <br />
4 - Digite: java -jar compilador.jar linguagem.scp <br />
5 - Pronto!! O código que tiver dentro do linguagem.scp será executado <br />

Obs: Você poderá abrir o arquivo .scp com qualquer editor de texto

#Quem tiver novos pequenos algotimos utilizando essa linguagem pode compartilhar comigo pelo twitter @yeebaplay
