import os
from classes import graph
from classes import node
# TUDO: criar as funções responsaveis por cada parte do código em python
# TUDO: Aqui precisa pegar todas as informações do modelo - nós, arestas, distribuições e tempo/clientes.

# cria o objeto do tipo graph e preenche ele com as informações do modelo especificado
graph = graph.Graph()
graph.buscarInformacoes('modelos/modelo2.gv')

# abre o arquivo do gabarito para leitura para pegar a estrutura e chamar as funções
gabarito = open("gabaritos/gabaritoJava.txt", "r")
gabarito1 = open("gabaritos/gabaritoJava.txt", "r")
gabaritoVet = gabarito1.readlines()
inter = []

#print(gabarito.readlines()[0:2])
#print(len(gabaritoVet))

#print(lista.index('@1classe_controle'))
os.mkdir('/home/nathalialp/arquivo/gerador/codigo/'+ graph.getNomeModelo() + '-JavaSim')
#pega o nome da pasta
nomePasta = graph.getNomeModelo() + '-JavaSim'             #graph.getNomeModelo() + '-JavaSim'

def criarCodigo(opcoes):
	# for le o arquivo do gabarito linha por linha
	intervalo()
	for linha in gabarito:
		#print('for')
		# verifica se o primeiro caracter é o % 
		if linha.startswith('@'):
		#	print(linha)
		#	print('teste')
			# pega o segundo caracter que indica o numero da função
			op = linha[1]
			# chama a função indicando o indice
			opcoes[int(op)]()	

			

def principal():
	opcoes = [
		(criaClasseMain),
		(criaClasseControle),
		(criaClasseChegadas),
		(criaClasseCliente),
		(criaClasseFila),
		(criaClasseRecursos)
	]
	return criarCodigo(opcoes)

def funcoes():
	opcoes = [
		(declaraObjetos),
		(estanciaObjetos),
		(tempoTotal),
		(relatorioFinal),
		(encerraProcessos),
		(insereNaFila),
		(ativaPrimeiroRecurso),
		(nomeDoRecurso),
		(construtorDoRecurso),
		(retirarDaFila),
		(ativaProximoRecurso)
	]
	return opcoes

#------------------------------ FUNCOES PRINCIPAIS @ ---------------------------------------

def criaClasseMain():
# função responsavel por criar o arquivo main	
	
	codigo = open('codigo/' + str(nomePasta) + '/Main.java', "w+")
	# pega o intervalo da classe main no gabarito

	intervalo = gabaritoVet[(inter[0]+1):inter[1]]
	#print(intervalo)
	for linha in intervalo:
		codigo.write(linha)
	
	codigo.close();		 

def criaClasseControle():
#função reponsavel por criar a classe controle que controla a simulacao
	
	codigo = open('codigo/' + str(nomePasta) + '/Controle.java', "w+")
	intervalo = gabaritoVet[(inter[1]+1):inter[2]]
	
	for linha in intervalo:
		ops = funcoes()
		if linha.startswith("%"):
			ops[int(linha[1])](codigo)
		else:
			codigo.write(linha)
	
	codigo.close()

def criaClasseChegadas():
	codigo = open('codigo/' + str(nomePasta) + '/Chegadas.java', "w+")

	intervalo = gabaritoVet[(inter[2]+1):inter[3]]
	for linha in intervalo:
		codigo.write(linha)
	
	codigo.close()

def criaClasseCliente():
	codigo = open('codigo/' + str(nomePasta) + '/Cliente.java', "w+")
	
	intervalo = gabaritoVet[(inter[3]+1):inter[4]]
	for linha in intervalo:
		ops = funcoes()
		if linha.startswith("%"):
			ops[int(linha[1])](codigo)
		else:
			codigo.write(linha)
	
	codigo.close()

def criaClasseFila():
	codigo = open('codigo/' + str(nomePasta) + '/Fila.java', "w+")
	
	intervalo = gabaritoVet[(inter[4]+1):inter[5]]
	for linha in intervalo:
		codigo.write(linha)
	
	codigo.close()

def criaClasseRecursos():

	intervalo = gabaritoVet[(inter[5]+1):(len(gabaritoVet))]
	listaNodes = graph.getListaNode()
	#print(intervalo)
	for n in listaNodes:

		codigo = open('codigo/' + str(nomePasta) +'/'+ n.getNomeNode() +'.java', "w+")

		for linha in intervalo:
			ops = funcoes()
			if linha.startswith("%"):
				if linha[2] == '0':
					ops[10](codigo, n)
				else:
					ops[int(linha[1])](codigo, n)
			else:
				codigo.write(linha)
		codigo.close()
	



#------------------------------- FUNCOES SECUNDARIAS % -------------------------------
def declaraObjetos(controle):
	
	listaNodes = graph.getListaNode()
	
	for n in listaNodes:
		linha1 = []
		variavel = n.getNomeNode().lower()
		linha1.append('	public static ' + n.getNomeNode() +' '+ variavel + ' = null;\n')
		linha1.append('	public static Fila filaDo' + n.getNomeNode() + ' = new Fila();\n\n' )
		
		controle.writelines(linha1)

def estanciaObjetos(controle):
	
	no = graph.getNodePrimeiroRecurso()
	linha = '			Chegadas chegadas = new Chegadas('+ no.getMediaChegadas() + ');\n'
	controle.write(linha)

	listaNodes = graph.getListaNode()

	for n in listaNodes:
		variavel = n.getNomeNode().lower()
		linha = '			Controle.'+ variavel+ ' = new ' + n.getNomeNode() + '('+ no.getMediaServico() +');\n'
		controle.write(linha)


def tempoTotal(controle):
	
	linha = '			hold('+graph.getTempoExecucao()+');\n'
	controle.write(linha)

def relatorioFinal(controle):
	listaNodes = graph.getListaNode()
	
	for n in listaNodes:
		linha1 = []
		variavel = n.getNomeNode().lower()
		linha1.append('			System.out.println("Utilização do '+ n.getNomeNode()+' = " + Controle.'+ variavel +'.tempoDeServico);\n')
		linha1.append('			System.out.println("Comprimento médio de fila' + n.getNomeNode()+ ' = "' +
                    '+ (Controle.filaDo'+ n.getNomeNode()+'.clientesEmFila / Controle.filaDo'+ n.getNomeNode()+'.checkFila));\n')
		
		controle.writelines(linha1)

def encerraProcessos(controle):
	listaNodes = graph.getListaNode()
	
	for n in listaNodes:
		variavel = n.getNomeNode().lower()
		linha = '			Controle.'+ variavel+'.terminate();\n'
		controle.write(linha)

def insereNaFila(cliente):

	n = graph.getNodePrimeiroRecurso()
	linha1 = []
	linha1.append('		vazio = Controle.filaDo'+n.getNomeNode()+'.isEmpty();\n')
	linha1.append('		Controle.filaDo'+n.getNomeNode()+'.enqueue(this);\n' )
		
	cliente.writelines(linha1)

def ativaPrimeiroRecurso(cliente):

	n = graph.getNodePrimeiroRecurso()
	variavel = n.getNomeNode().lower()
	linha = '				Controle.'+variavel+'.activate();\n'
	cliente.write(linha)

def nomeDoRecurso(recurso, n):

	linha = 'public class '+n.getNomeNode()+' extends SimulationProcess\n'
	recurso.write(linha)

def construtorDoRecurso(recurso, n):

	linha = '	public '+n.getNomeNode()+'(double media)\n'
	recurso.write(linha)
	

def retirarDaFila(recurso, n):

	linha1 = []
	linha1.append('			while (!Controle.filaDo'+n.getNomeNode()+'.isEmpty())\n')
	linha1.append('			{\n')
	linha1.append('				inicioAtividade = currentTime();\n\n')
	linha1.append('				Controle.filaDo'+n.getNomeNode()+'.checkFila++;\n')
	linha1.append('				Controle.filaDo'+n.getNomeNode()+'.clientesEmFila += Controle.filaDo'+n.getNomeNode()+'.queueSize();\n')
	linha1.append('				cliente = Controle.filaDo'+n.getNomeNode()+'.dequeue();\n\n')
	
	recurso.writelines(linha1)

def ativaProximoRecurso(recurso, no):
	#verificar
	 
	if len(no.edges) > 1:
		no.setProximoProb(True)
		linha1 = []
		linha1.append('				Random x = new Random();\n')
		linha1.append('				int aleatorio;\n')
		linha1.append('				aleatorio = x.nextInt(1000);\n\n')
		
		recurso.writelines(linha1)

		inicio = 0
		# pegar a probabilidade de cada edge
		for e in no.edges:
			fim = int(e.getProbabilidade())*100 + inicio
			ifAleatorio(inicio, fim, recurso) 
			inicio = fim
			variavel = e.destino.getNomeNode().lower()
			linha = []
			linha.append('					vazio = Controle.filaDo' + e.destino.getNomeNode() + '.isEmpty();\n')
			linha.append('					Controle.filaDo' + e.destino.getNomeNode() + '.enqueue(cliente);\n\n')
			linha.append('					if (vazio)\n')
			linha.append('					{\n')
			linha.append('						try\n')
			linha.append('						{\n')
			linha.append('							Controle.' + variavel + '.activate();\n')
			linha.append('						}\n')
			linha.append('						catch (SimulationException e)\n')
			linha.append('						{\n')
			linha.append('						}\n')
			linha.append('						catch (RestartException e)\n')
			linha.append('						{\n')
			linha.append('						}\n')
			linha.append('				}\n')
			
			recurso.writelines(linha)

	else:
		if no.edges[0].destino.getTipoNode() == '3':
			linha = []
			linha.append('\n				Controle.clientesProcessados++;\n')
			linha.append('				cliente.finished();\n\n')
			
			recurso.writelines(linha)
			
		else:
			linha = []
			variavel = no.edges[0].destino.getNomeNode().lower()
			linha.append('\n				vazio = Controle.filaDo' + no.edges[0].destino.getNomeNode() + '.isEmpty();\n')
			linha.append('				Controle.filaDo' + no.edges[0].destino.getNomeNode() + '.enqueue(cliente);\n\n')
			linha.append('				if (vazio)\n')
			linha.append('				{\n')
			linha.append('					try\n')
			linha.append('					{\n')
			linha.append('						Controle.' + variavel + '.activate();\n')
			linha.append('					}\n')
			linha.append('					catch (SimulationException e)\n')
			linha.append('					{\n')
			linha.append('					}\n')
			linha.append('					catch (RestartException e)\n')
			linha.append('					{\n')
			linha.append('					}\n')
			linha.append('				}\n')
			
			recurso.writelines(linha) 


#-------------------- FUNCOES DE APOIO ----------------------------------------


def ifAleatorio (inicio, fim, recurso):
	# funcao que faz o if quando a chamada do proximo node
	# é por probabilidade
	linha = []
	linha.append('				if ('+ str(inicio) + ' < aleatorio && aleatorio < ' + str((fim))+ ')\n')
	linha.append('				{\n')
	recurso.writelines(linha)

def intervalo():
	
	for i, linha in enumerate(gabaritoVet):
		if linha.startswith('@'):
			inter.append(i)


#--------------------------------------------main------------------------


principal()

gabarito.close()
gabarito1.close()