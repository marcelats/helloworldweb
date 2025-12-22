from classes import graph
from classes import node

class GeradorPython:

	def __init__(self, modelo):
		
		# cria o objeto do tipo graph e preenche ele com as informações do modelo especificado
		self.graph = graph.Graph()
		#graph.buscarInformacoes('modelos/modelo2.gv')
		self.graph.buscarInformacoes(modelo)

		# cria o arquivo que vai receber todas as informações
		self.nomeCodigo = self.graph.getNomeModelo() + 'Simpy.py'
		self.codigo = open('codigo/' + str(self.nomeCodigo), "w+")

		# abre o arquivo do gabarito para leitura para pegar a estrutura e chamar as funções
		self.gabarito = open("gabaritos/gabaritoPython.txt", "r")

		#print(len(gabarito.readlines()))

	def nome(self):
		return self.nomeCodigo

	def criarCodigo(self, opcoes):
		# for le o arquivo do gabarito linha por linha
		for linha in self.gabarito:
			# verifica se o primeiro caracter é o % 
			if linha.startswith("%"):
				# pega o segundo caracter que indica o numero da função
				op = linha[1]
				# chama a função indicando o indice
				opcoes[int(op)]()	
			# copia a linha toda para o arquivo do código
			else:
				self.codigo.write(linha)	
		self.gabarito.close()
		self.codigo.close()		
				

	def principal(self):
		opcoes = [
			(self.variaveis),
			(self.defineDistribuicoes),
			(self.primeiroRec),
			(self.geraProcessamentoPorNode),
			(self.defineSemente),
			(self.criaRecursos),
			(self.defineTempoTotal),
			(self.relatorioFinal)
		]
		return self.criarCodigo(opcoes)

	#------------------------------- FUNCOES PRINCIPAIS DE 0 A 9 -------------------------------
	def variaveis(self):

		listaNodes = self.graph.getListaNode()
		
		linha = []
		linha.append('tempoServico = [0]*'+ str(len(listaNodes) )+"\n")
		linha.append('tempoResposta = [0]*'+ str(len(listaNodes) )+"\n")

		self.codigo.writelines(linha)
		
		pass
	def defineDistribuicoes (self):
		# eu tenho q olhar todos os nodes e pegar as distribuições de
		# chegada e serviço - verificar o primeiro recurso

		no = node.Node()
		for no in self.graph.graphNodes:
			#print(no.getTipoNode())
		#	print(no.getPrimeiroRecurso())
			if no.getTipoNode() == '2':
				#print("sou tipo 2")
				dis = [
					no.getMediaServico(),
					'random.expovariate(1.0/' + no.getMediaServico() + ')',
					'random.uniform(' + no.getMediaServico() + ')',
				]		
				
				if no.getPrimeiroRecurso():
					linha = "		'chegadas': random.expovariate(1.0/" + no.getMediaChegadas() + '), \n'
					self.codigo.write(linha)

				linha = "		'" + no.getNomeNode() + "': " + dis[int(no.getDistribuicaoServico())] + ', \n'
				linha = linha.lower()
				#print(linha)
				self.codigo.write(linha)
					

	def primeiroRec (self):
		# funcao que ocupa o primeiro recurso

		no = node.Node()
		no = self.graph.getNodePrimeiroRecurso()
		
		linha = '		env.process(processo' + no.getNomeNode() + '(env, recursos))\n\n'
		self.codigo.write(linha)
		

	def geraProcessamentoPorNode (self):
		# faz a função para cada node
		listaNodes = self.graph.getListaNode()
		
		for n in listaNodes:
			linha = '\ndef processo' + n.getNomeNode() + '(env, recursos):\n\n'
			self.codigo.write(linha)
			
			linha1 = []
			nome = n.getNomeNode().lower()
			linha1.append('	global contaTerminos, tempoServico, tempoResposta\n\n')
			linha1.append('	chegada = env.now\n')
			linha1.append('	req = recursos[recursos.index(' + nome + ')].request()\n')
			linha1.append('	yield req\n')
			linha1.append('	tempoFila = env.now - chegada\n\n')
			linha1.append('	inicio = env.now\n')
			linha1.append('	yield env.timeout(distributions(' + "'" + nome + "'))\n")
			linha1.append('	tempoServico['+ str(listaNodes.index(n)) +'] = tempoServico['+ str(listaNodes.index(n)) +'] + (env.now - inicio)\n')
			linha1.append('	tempoResposta['+ str(listaNodes.index(n)) +'] = tempoResposta['+ str(listaNodes.index(n)) +'] + tempoFila + tempoServico['+ str(listaNodes.index(n)) +']\n\n')
			linha1.append('	recursos[recursos.index(' + nome + ')].release(req)\n\n')
			self.codigo.writelines(linha1)

			self.chamadaProximoProcesso(n)
			
		

	def defineSemente (self):
		# funcao que defime a semente da funcao random
		linha = 'random.seed(' + self.graph.getSemente() + ')\n'
		self.codigo.write(linha)


	def criaRecursos (self):
		# funcao que cria os recursos do modelo 
		# TUDO: verificar se vai usar mais de um servidor no centro de serviço
		listaNodes = self.graph.getListaNode()

		for n in listaNodes:
			nome = n.getNomeNode().lower()
			linha = nome + ' = simpy.Resource(env, capacity = 1)\n'
			self.codigo.write(linha)

		linha = '\nrecursos = [\n'
		self.codigo.write(linha)
		# for para gerar a lista de recursos
		for n in listaNodes:
			nome = n.getNomeNode().lower()
			linha = '	' + nome + ',\n'
			self.codigo.write(linha)

		linha = ']\n'
		self.codigo.write(linha)		


	def defineTempoTotal (self):

		linha = 'env.run(until=' + self.graph.getTempoExecucao() + ')'
		self.codigo.write(linha) 
		

	def relatorioFinal (self):
		# escrever com .write mesmo os resultados ou print('jfjfjf', file=saida)
		# linha = 'saida = open("' + self.graph.getNomeModelo() +'.txt","w+")\n\n'
		# linha = linha.lower()
		# self.codigo.write(linha)

		linha1 = []
		linha1.append("print('Total de Clientes processados = ', contaTerminos)\n")
		linha1.append("print('Throughput = ', contaTerminos/"+ self.graph.getTempoExecucao() + ")\n\n")
		self.codigo.writelines(linha1)

		listaNodes = self.graph.getListaNode()
		
		for n in listaNodes:
			linha1 = []
			
			linha1.append("print('Tempo de Serviço "+ n.getNomeNode()+" = ', tempoServico["+ str(listaNodes.index(n)) +"])\n")
			linha1.append("print('Tempo Médio de Serviço "+ n.getNomeNode()+" = ', tempoServico["+ str(listaNodes.index(n)) +"]/contaTerminos)\n")
			linha1.append("print('Utilização "+ n.getNomeNode()+" = ', tempoServico["+ str(listaNodes.index(n)) +"]/"+ self.graph.getTempoExecucao() + ")\n")
			linha1.append("print('Tempo de resposta "+ n.getNomeNode()+" = ', tempoResposta["+ str(listaNodes.index(n)) +"])\n")
			linha1.append("print('Tempo Médio de resposta "+ n.getNomeNode()+" = ', tempoResposta["+ str(listaNodes.index(n)) +"]/contaTerminos)\n")
			linha1.append("print('Tempo Médio em Fila "+ n.getNomeNode()+" = ',(tempoResposta["+ str(listaNodes.index(n)) +"]/contaTerminos)-(tempoServico["+ str(listaNodes.index(n)) +"]/contaTerminos))\n")
			linha1.append("print('----------------------------------------------------')\n\n")
			self.codigo.writelines(linha1)

		
		# print dos relatorios 
		
		
		#linha1.append("print('Numero Médio no Sistema = ',tempoResposta/"+ graph.getTempoExecucao() + ")\n")
		#linha1.append("print('Numero médio na fila = ', (tempoResposta/"+ graph.getTempoExecucao() + ")-(tempoServico/"+ graph.getTempoExecucao() + "))\n")
		
		# TUDO: procurar os relatorios no simpy
		# linha = '\nsaida.close()\n'
		# self.codigo.write(linha)

	#-------------------- FUNCOES DE APOIO ----------------------------------------


	def ifAleatorio (self, inicio, fim):
		# funcao que faz o if quando a chamada do proximo node
		# é por probabilidade

		linha = '	if '+ str(inicio) + ' < x and x < ' + str((fim+1))+ ':\n'
		self.codigo.write(linha)


	def chamadaProximoProcesso(self, no):
		# funcao pra verificar qual o proximo node e se é por probabilidade
		# print('entrou')
		if len(no.edges) > 1:
			no.setProximoProb(True)
			linha = '	x = random.randint(1,10000)\n\n'
			self.codigo.write(linha)

			inicio = 0
			# pegar a probabilidade de cada edge
			for e in no.edges:
				if e.destino.getTipoNode() == '3':
					linha ='	contaTerminos+=1\n'
					self.codigo.write(linha)
				fim = int(e.getProbabilidade())*100 + inicio
				self.ifAleatorio(inicio, fim)
				inicio = fim
				linha = '		env.process(processo' + e.destino.getNomeNode() + '(env, recursos))\n\n'
				self.codigo.write(linha)

		else:
			if no.edges[0].destino.getTipoNode() == '2':
				linha = '	env.process(processo' + no.edges[0].destino.getNomeNode() + '(env, recursos))\n\n'
				self.codigo.write(linha)
				
			else:
				linha ='	contaTerminos+=1\n'
				self.codigo.write(linha) 



	#principal()



	