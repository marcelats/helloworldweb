import os
from classes import graph
from classes import node
# TUDO: criar as funções responsaveis por cada parte do código em python
# TUDO: Aqui precisa pegar todas as informações do modelo - nós, arestas, distribuições e tempo/clientes.

class GeradorJava:

	def __init__(self, modelo):
		
		# cria o objeto do tipo graph e preenche ele com as informações do modelo especificado
		self.graph = graph.Graph()
		#self.graph.buscarInformacoes('modelos/modelo2.gv')
		self.graph.buscarInformacoes(modelo)

		# abre o arquivo do gabarito para leitura para pegar a estrutura e chamar as funções
		self.gabarito = open("gabaritos/gabaritoJava.txt", "r")
		self.gabarito1 = open("gabaritos/gabaritoJava.txt", "r")
		self.gabaritoVet = self.gabarito1.readlines()
		self.inter = []

		#print(gabarito.readlines()[0:2])
		#print(len(gabaritoVet))

		#print(lista.index('@1classe_controle'))
		if os.path.isdir('codigo/'+ self.graph.getNomeModelo() + '-JavaSim'):
			self.nomePasta = self.graph.getNomeModelo() + '-JavaSim'
		else:	
			os.mkdir('codigo/'+ self.graph.getNomeModelo() + '-JavaSim')
			#pega o nome da pasta
			self.nomePasta = self.graph.getNomeModelo() + '-JavaSim'             #graph.getNomeModelo() + '-JavaSim'
	
	def nome(self):
		return self.nomePasta

	def criarCodigo(self, opcoes):
		# for le o arquivo do gabarito linha por linha
		self.intervalo()
		for linha in self.gabarito:
			#print('for')
			# verifica se o primeiro caracter é o % 
			if linha.startswith('@'):
			#	print(linha)
			#	print('teste')
				# pega o segundo caracter que indica o numero da função
				op = linha[1]
				# chama a função indicando o indice
				opcoes[int(op)]()	
		self.gabarito.close()
		self.gabarito1.close()

	
			

	def principal(self):
		opcoes = [
			(self.criaClasseMain),
			(self.criaClasseControle),
			(self.criaClasseChegadas),
			(self.criaClasseCliente),
			(self.criaClasseFila),
			(self.criaClasseRecursos)
		]
		return self.criarCodigo(opcoes)

	def funcoes(self):
		opcoes = [
			(self.declaraObjetos),
			(self.estanciaObjetos),
			(self.tempoTotal),
			(self.relatorioFinal),
			(self.encerraProcessos),
			(self.insereNaFila),
			(self.ativaPrimeiroRecurso),
			(self.nomeDoRecurso),
			(self.construtorDoRecurso),
			(self.retirarDaFila),
			(self.ativaProximoRecurso)
		]
		return opcoes

	#------------------------------ FUNCOES PRINCIPAIS @ ---------------------------------------

	def criaClasseMain(self):
	# função responsavel por criar o arquivo main	
		
		codigo = open('codigo/' + str(self.nomePasta) + '/Main.java', "w+")
		# pega o intervalo da classe main no gabarito

		intervalo = self.gabaritoVet[(self.inter[0]+1):self.inter[1]]
		#print(intervalo)
		for linha in intervalo:
			codigo.write(linha)
		
		codigo.close();		 

	def criaClasseControle(self):
	#função reponsavel por criar a classe controle que controla a simulacao
		
		codigo = open('codigo/' + str(self.nomePasta) + '/Controle.java', "w+")
		intervalo = self.gabaritoVet[(self.inter[1]+1):self.inter[2]]
		
		for linha in intervalo:
			ops = self.funcoes()
			if linha.startswith("%"):
				ops[int(linha[1])](codigo)
			else:
				codigo.write(linha)
		
		codigo.close()

	def criaClasseChegadas(self):
		codigo = open('codigo/' + str(self.nomePasta) + '/Chegadas.java', "w+")

		intervalo = self.gabaritoVet[(self.inter[2]+1):self.inter[3]]
		for linha in intervalo:
			codigo.write(linha)
		
		codigo.close()

	def criaClasseCliente(self):
		codigo = open('codigo/' + str(self.nomePasta) + '/Cliente.java', "w+")
		
		intervalo = self.gabaritoVet[(self.inter[3]+1):self.inter[4]]
		for linha in intervalo:
			ops = self.funcoes()
			if linha.startswith("%"):
				ops[int(linha[1])](codigo)
			else:
				codigo.write(linha)
		
		codigo.close()

	def criaClasseFila(self):
		codigo = open('codigo/' + str(self.nomePasta) + '/Fila.java', "w+")
		
		intervalo = self.gabaritoVet[(self.inter[4]+1):self.inter[5]]
		for linha in intervalo:
			codigo.write(linha)
		
		codigo.close()

	def criaClasseRecursos(self):

		intervalo = self.gabaritoVet[(self.inter[5]+1):(len(self.gabaritoVet))]
		listaNodes = self.graph.getListaNode()
		#print(intervalo)
		for n in listaNodes:

			codigo = open('codigo/' + str(self.nomePasta) +'/'+ n.getNomeNode() +'.java', "w+")

			for linha in intervalo:
				ops = self.funcoes()
				if linha.startswith("%"):
					if linha[2] == '0':
						ops[10](codigo, n)
					else:
						ops[int(linha[1])](codigo, n)
				else:
					codigo.write(linha)
			codigo.close()
		



	#------------------------------- FUNCOES SECUNDARIAS % -------------------------------
	def declaraObjetos(self, controle):
		
		listaNodes = self.graph.getListaNode()
		
		for n in listaNodes:
			linha1 = []
			variavel = n.getNomeNode().lower()
			linha1.append('	public static ' + n.getNomeNode() +' '+ variavel + ' = null;\n')
			linha1.append('	public static Fila filaDo' + n.getNomeNode() + ' = new Fila();\n\n' )
			
			controle.writelines(linha1)

	def estanciaObjetos(self, controle):
		
		no = self.graph.getNodePrimeiroRecurso()
		linha = '			Chegadas chegadas = new Chegadas('+ no.getMediaChegadas() + ');\n'
		controle.write(linha)

		listaNodes = self.graph.getListaNode()

		for n in listaNodes:
			variavel = n.getNomeNode().lower()
			linha = '			Controle.'+ variavel+ ' = new ' + n.getNomeNode() + '('+ n.getMediaServico() +');\n'
			controle.write(linha)


	def tempoTotal(self, controle):
		
		linha = '			hold('+self.graph.getTempoExecucao()+');\n'
		controle.write(linha)

	def relatorioFinal(self, controle):
		listaNodes = self.graph.getListaNode()
		
		for n in listaNodes:
			linha1 = []
			variavel = n.getNomeNode().lower()
			linha1.append('			System.out.println("Utilização do '+ n.getNomeNode()+' = " + Controle.'+ variavel +'.tempoDeServico);\n')
			linha1.append('			System.out.println("Comprimento médio de fila' + n.getNomeNode()+ ' = "' +
						'+ (Controle.filaDo'+ n.getNomeNode()+'.clientesEmFila / Controle.filaDo'+ n.getNomeNode()+'.checkFila));\n')
			
			controle.writelines(linha1)

	def encerraProcessos(self, controle):
		listaNodes = self.graph.getListaNode()
		
		for n in listaNodes:
			variavel = n.getNomeNode().lower()
			linha = '			Controle.'+ variavel+'.terminate();\n'
			controle.write(linha)

	def insereNaFila(self, cliente):

		n = self.graph.getNodePrimeiroRecurso()
		linha1 = []
		linha1.append('		vazio = Controle.filaDo'+n.getNomeNode()+'.isEmpty();\n')
		linha1.append('		Controle.filaDo'+n.getNomeNode()+'.enqueue(this);\n' )
			
		cliente.writelines(linha1)

	def ativaPrimeiroRecurso(self, cliente):

		n = self.graph.getNodePrimeiroRecurso()
		variavel = n.getNomeNode().lower()
		linha = '				Controle.'+variavel+'.activate();\n'
		cliente.write(linha)

	def nomeDoRecurso(self, recurso, n):

		linha = 'public class '+n.getNomeNode()+' extends SimulationProcess\n'
		recurso.write(linha)

	def construtorDoRecurso(self, recurso, n):

		linha = '    public '+n.getNomeNode()+'(double media)\n'
		recurso.write(linha)
		

	def retirarDaFila(self, recurso, n):

		linha1 = []
		linha1.append('			      while (!Controle.filaDo'+n.getNomeNode()+'.isEmpty())\n')
		linha1.append('			      {\n')
		linha1.append('				        inicioAtividade = currentTime();\n\n')
		linha1.append('				        Controle.filaDo'+n.getNomeNode()+'.checkFila++;\n')
		linha1.append('				        Controle.filaDo'+n.getNomeNode()+'.clientesEmFila += Controle.filaDo'+n.getNomeNode()+'.queueSize();\n')
		linha1.append('				        cliente = Controle.filaDo'+n.getNomeNode()+'.dequeue();\n\n')
		
		recurso.writelines(linha1)

	def ativaProximoRecurso(self, recurso, no):
		#verificar
		
		if len(no.edges) > 1:
			no.setProximoProb(True)
			linha1 = []
			linha1.append('                Random x = new Random();\n')
			linha1.append('                int aleatorio;\n')
			linha1.append('                aleatorio = x.nextInt(1000);\n\n')
			
			recurso.writelines(linha1)

			inicio = 0
			# pegar a probabilidade de cada edge
			for e in no.edges:
				fim = int(e.getProbabilidade())*100 + inicio
				self.ifAleatorio(inicio, fim, recurso) 
				inicio = fim
				variavel = e.destino.getNomeNode().lower()
				linha = []
				linha.append('                vazio = Controle.filaDo' + e.destino.getNomeNode() + '.isEmpty();\n')
				linha.append('                Controle.filaDo' + e.destino.getNomeNode() + '.enqueue(cliente);\n\n')
				linha.append('                if (vazio)\n')
				linha.append('                {\n')
				linha.append('                    try\n')
				linha.append('                    {\n')
				linha.append('                        Controle.' + variavel + '.activate();\n')
				linha.append('                    }\n')
				linha.append('                    catch (SimulationException e)\n')
				linha.append('                    {\n')
				linha.append('                    }\n')
				linha.append('                    catch (RestartException e)\n')
				linha.append('                    {\n')
				linha.append('                    }\n')
				linha.append('                }\n')
				linha.append('            }\n')
				
				recurso.writelines(linha)

		else:
			if no.edges[0].destino.getTipoNode() == '3':
				linha = []
				linha.append('\n                Controle.clientesProcessados++;\n')
				linha.append('                cliente.finished();\n\n')
				
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


	def ifAleatorio (self, inicio, fim, recurso):
		# funcao que faz o if quando a chamada do proximo node
		# é por probabilidade
		linha = []
		linha.append('				if ('+ str(inicio) + ' < aleatorio && aleatorio < ' + str((fim))+ ')\n')
		linha.append('				{\n')
		recurso.writelines(linha)

	def intervalo(self):
		
		for i, linha in enumerate(self.gabaritoVet):
			if linha.startswith('@'):
				self.inter.append(i)


	#--------------------------------------------main------------------------


	#principal()

	
