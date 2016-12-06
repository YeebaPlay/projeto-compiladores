# projeto-compiladores
Desenvolvimento de uma linguagem de programação nova, sem funções e classes

Linguagem desenvolvida na faculdade, com sintaxe totalmente diferente, tudo dentro de { }, cada bloco de ação deve estar dentro das chaves, por exemplo: 

# prog01 - fatorial em SCP
# SCP (Script de chaves em portugues)

{ int i n fat }
{ mostra "Entre n:" }
{ le n }
{ se { < n 0 }
  { 
    { mostra " Entre valor valido " }
  }
  {
    { = i 1 }
    { = fat 1 }
    { enquanto { <= i n }
      { = fat { * fat i } }
      { = i { + i 1 } }
    }
    { mostra "fat: " }
    { mostra fat }
  }
}
{ real r1 }
{ = r1 2.34 }
$
