
from classes import graph
from classes import node

class GeradorR:

	def __init__(self, modelo):
		
		# cria o objeto do tipo graph e preenche ele com as informações do modelo especificado
		self.graph = graph.Graph()
		#graph.buscarInformacoes('modelos/modelo2.gv')
		self.graph.buscarInformacoes(modelo)

		# cria o arquivo que vai receber todas as informações
		self.nomeCodigo = self.graph.getNomeModelo() + 'Simmer.R'
		self.codigo = open('codigo/' + str(self.nomeCodigo), "w+")

		# abre o arquivo do gabarito para leitura para pegar a estrutura e chamar as funções
		self.gabarito = open("gabaritos/gabaritoR.txt", "r")

		#print(len(gabarito.readlines()))
	def nome(self):
		return self.nomeCodigo

	def criarCodigo(self, opcoes):
		# for le o arquivo do gabarito linha por linha
		#prob = []
	#	prob.append(30)
	#	prob.append(60)
	#	prob.append(10)
	#	print(prob)
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
			(self.defineSemente),
			(self.criarAmbiente),
			(self.criarTrajetoria),
			(self.adicionarRecursos),
			(self.criarGeradorChegadas),
			(self.defineTempoTotal),
			(self.relatorioFinal)
		]
		return self.criarCodigo(opcoes)

	#------------------------------- FUNCOES PRINCIPAIS DE 0 A 9 -------------------------------


	def defineSemente (self):
		# funcao que defime a semente da funcao random
		linha = 'set.seed(' + self.graph.getSemente() + ')\n'
		self.codigo.write(linha)

	def criarAmbiente(self):
		linha = 'env <- simmer("' + self.graph.getNomeModelo() + '")\n'
		self.codigo.write(linha)
		

	def criarTrajetoria(self):

		#verifica qual o tipo de trajetoria - ver se algum node tem mais de 1 edge  
		listaNodes = self.graph.getListaNodeSimples()
		sub = self.subTrajetorias()

		if sub > 0:
			self.criarTrajetoriaAleatoria()
		else: 
			self.criarTrajetoriaSimples(listaNodes)

		
	def criarTrajetoriaSimples(self, listaNodes):
		# trajetoria reta sem caminhos alternativos
		for n in listaNodes: 

			if n.edges[0].destino.getTipoNode() != '3':
				nome = n.edges[0].destino.getNomeNode().lower()
				if n.edges[0].destino == listaNodes[-1]:
					linha1 = []
					linha1.append('	seize("' + nome +'", 1) %>%\n')
					linha1.append('	timeout(function() rexp(1, '+ n.edges[0].destino.getMediaServico() +')) %>%\n')
					linha1.append('	release("'+ nome +'", 1) %>%\n')
					linha1.append('	set_attribute("queue_'+ nome +'", function() get_queue_count(env, "'+ nome +'"))\n\n')
					self.codigo.writelines(linha1)
				else: 	
					linha1 = []
					linha1.append('	seize("' + nome +'", 1) %>%\n')
					linha1.append('	timeout(function() rexp(1, '+ n.edges[0].destino.getMediaServico() +')) %>%\n')
					linha1.append('	release("'+ nome +'", 1) %>%\n')
					linha1.append('	set_attribute("queue_'+ nome +'", function() get_queue_count(env, "'+ nome +'"))\n')
					self.codigo.writelines(linha1)

	def criarTrajetoriaAleatoria(self):
		listaNodes = self.graph.getListaNodeTra()
		i = 0
		linha1 = []
		linha1.append('	seize("' + listaNodes[0].getNomeNode().lower()+'", 1) %>%\n')
		linha1.append('	timeout(function() rexp(1, '+ listaNodes[0].getMediaServico() +')) %>%\n')
		linha1.append('	release("'+ listaNodes[0].getNomeNode().lower() +'", 1) %>%\n')
		linha1.append('	set_attribute("queue_'+ listaNodes[0].getNomeNode().lower() +'", function() get_queue_count(env, "'+ listaNodes[0].getNomeNode().lower() +'"))\n')
		self.codigo.writelines(linha1) 

		while (i < len(listaNodes)-1):
			#print('tamanho lista = ', len(listaNodes)-1)
			if len(listaNodes[i].edges) > 1:
				prob = ''
				for e in listaNodes[i].edges:
					if e == listaNodes[i].edges[-1]:
						prob = prob + e.getProbabilidade()
					else:	
						prob = prob + e.getProbabilidade() + ', ' 
					#print(prob)
					# sorteia um numero de 1:len(no.edges) de acordo com a probabilidade de cada nó
				#print(prob)
				linha = '	set_attribute("x", function() sample(1:'+ str(len(listaNodes[i].edges)) +', 1, prob=c('+prob+'), replace=TRUE))\n'
				self.codigo.write(linha)

				#verifica os destinos dos nodes que vão estar na branch
				verifica = self.verificaDestinos(listaNodes[i].edges)
				if verifica == (len(listaNodes[i].edges)-1):
					
					linha = ('	branch(\n		function() get_attribute(env, "x"), continue=c(TRUE')
					self.codigo.write(linha)

					x=1
					while x < len(listaNodes[i].edges):
						linha = (', TRUE')
						self.codigo.write(linha)
						x=x+1

					linha = ('),\n')
					self.codigo.write(linha)

					for e in listaNodes[i].edges:
						linha1 = []
						if e == listaNodes[i].edges[-1]:
							linha1.append('		 trajectory() %>%\n')
							linha1.append('			seize("' + e.destino.getNomeNode().lower+'", 1) %>%\n')
							linha1.append('			timeout(function() rexp(1, '+ e.destino.getMediaServico() +')) %>%\n')
							linha1.append('			release("'+ e.destino.getNomeNode().lower +'", 1) %>%\n')
							linha1.append('			set_attribute("queue_'+ e.destino.getNomeNode().lower() +'", function() get_queue_count(env, "'+ e.destino.getNomeNode().lower() +'"))\n')
						else:
							linha1.append('		 trajectory() %>%\n')
							linha1.append('			seize("' + e.destino.getNomeNode().lower+'", 1) %>%\n')
							linha1.append('			timeout(function() rexp(1, '+ e.destino.getMediaServico() +')) %>%\n')
							linha1.append('			release("'+ e.destino.getNomeNode().lower +'", 1) %>%\n')
							linha1.append('			set_attribute("queue_'+ e.destino.getNomeNode().lower() +'", function() get_queue_count(env, "'+ e.destino.getNomeNode().lower() +'")),\n')

						self.codigo.writelines(linha1)
					
					linha = ('	)\n')
					self.codigo.write(linha)
					i = i + len(listaNodes[i].edges)
				
				else:
					# segundo caso continua a branch ate juntar denovo
					# index na lista de node
					idFim = self.finalBranch((i+1), (i+len(listaNodes[i].edges)))
					#print('valor id = ', idFim )
					linha = ('	branch(\n		function() get_attribute(env, "x"), continue=c(TRUE')
					self.codigo.write(linha)

					x=1
					while x < len(listaNodes[i].edges):
						linha = (', TRUE')
						self.codigo.write(linha)
						x=x+1
					linha = ('),\n')
					self.codigo.write(linha)

					for e in listaNodes[i].edges:
						linha1 = []
						if e == listaNodes[i].edges[-1]:
							linha1.append('		 trajectory() %>%\n')
							linha1.append('			seize("' + e.destino.getNomeNode().lower()+'", 1) %>%\n')
							linha1.append('			timeout(function() rexp(1, '+ e.destino.getMediaServico() +')) %>%\n')
							linha1.append('			release("'+ e.destino.getNomeNode().lower() +'", 1) %>%\n')
							linha1.append('			set_attribute("queue_'+ e.destino.getNomeNode().lower() +'", function() get_queue_count(env, "'+ e.destino.getNomeNode().lower() +'"))\n')
							self.codigo.writelines(linha1)
							self.criarSubTrajetorias(e.destino, idFim, True)
						else:
							linha1.append('		 trajectory() %>%\n')
							linha1.append('			seize("' + e.destino.getNomeNode().lower()+'", 1) %>%\n')
							linha1.append('			timeout(function() rexp(1, '+ e.destino.getMediaServico() +')) %>%\n')
							linha1.append('			release("'+ e.destino.getNomeNode().lower() +'", 1) %>%\n')
							linha1.append('			set_attribute("queue_'+ e.destino.getNomeNode().lower() +'", function() get_queue_count(env, "'+ e.destino.getNomeNode().lower() +'"))\n')
							self.codigo.writelines(linha1)
							self.criarSubTrajetorias(e.destino, idFim, False)

					i = idFim
					if idFim < len(listaNodes)-1:
						linha = "%>% \n\n"
						self.codigo.write(linha)
					else:
						linha = "\n\n"
						self.codigo.write(linha)	
					#print('valor do i', i)
						
			else:
				linha1 = []
				linha1.append('	seize("' + listaNodes[i].edges[0].destino.getNomeNode().lower()+'", 1) %>%\n')
				linha1.append('	timeout(function() rexp(1, '+ str(listaNodes[i].edges[0].destino.getMediaServico()) +')) %>%\n')
				linha1.append('	release("'+ listaNodes[i].edges[0].destino.getNomeNode().lower() +'", 1) %>%\n')
				linha1.append('	set_attribute("queue_'+ listaNodes[i].edges[0].destino.getNomeNode().lower() +'", function() get_queue_count(env, "'+ listaNodes[i].edges[0].destino.getNomeNode().lower() +'"))\n')
				self.codigo.writelines(linha1)

		

	def adicionarRecursos(self):
		# funcao que cria os recursos do modelo 
		listaNodes = self.graph.getListaNode()

		for n in listaNodes:
			nome = n.getNomeNode().lower()
			linha = 'add_resource("' + nome+ '", 1) %>%\n'
			self.codigo.write(linha)
			

	def criarGeradorChegadas(self):

		no = node.Node()
		no = self.graph.getNodePrimeiroRecurso()
		linha = 'add_generator("cliente", cliente, function() rexp(1, '+ no.getMediaChegadas()+ '), mon=2)\n'
		self.codigo.write(linha)


	def defineTempoTotal (self):

		linha = '	run('+ self.graph.getTempoExecucao()+ ') %>%\n 	now()'
		self.codigo.write(linha) 
		
	def relatorioFinal(self):

		linha1 = []
		linha1.append('sprintf("Total de Clientes Processados = %d", nrow(get_mon_arrivals(env)))\n')
		linha1.append('sprintf("Thoughput = %f", (nrow(get_mon_arrivals(env))/'+ self.graph.getTempoExecucao() +'))\n\n')
		self.codigo.writelines(linha1)
		
		listaNodes = self.graph.getListaNode()

		for n in listaNodes:
			nome = n.getNomeNode().lower()
			linha1 = []
			linha1.append('sprintf("Tempo de Serviço '+ n.getNomeNode()+' =  %f", sum(chegadas[chegadas$resource == "'+ nome +'", c("activity_time")]))\n')
			linha1.append('sprintf("Tempo Médio de Serviço '+ n.getNomeNode()+' =  %f", sum(chegadas[chegadas$resource == "'+ nome +'", c("activity_time")])/nrow(chegadas[chegadas$resource == "'+ nome +'", c("resource", "name")]))\n')
			linha1.append('sprintf("Utilização CPU '+ n.getNomeNode()+' = %f", sum(chegadas[chegadas$resource == "'+ nome+'", c("activity_time")])/'+self.graph.getTempoExecucao()+')\n')
			linha1.append('sprintf("Tempo de resposta '+ n.getNomeNode()+' = %f", sum(chegadas[chegadas$resource == "'+ nome +'", c("end_time")])-sum(chegadas[chegadas$resource == "'+ nome +'", c("start_time")]))\n')
			linha1.append('sprintf("Tempo Médio de resposta '+ n.getNomeNode()+' = %f", (sum(chegadas[chegadas$resource == "'+ nome +'", c("end_time")])-sum(chegadas[chegadas$resource == "'+ nome +'", c("start_time")]))/nrow(chegadas[chegadas$resource == "'+ nome +'", c("resource", "name")]) )\n')
			linha1.append('sprintf("Tempo Médio em Fila '+ n.getNomeNode()+' = %f ", ((sum(chegadas[chegadas$resource == "'+ nome +'", c("end_time")])-sum(chegadas[chegadas$resource == "'+ nome +'", c("start_time")]))/nrow(chegadas[chegadas$resource == "'+ nome +'", c("resource", "name")]))-(sum(chegadas[chegadas$resource == "'+ nome +'", c("activity_time")])/nrow(chegadas[chegadas$resource == "'+ nome +'", c("resource", "name")])))\n')
			linha1.append('sprintf("Comprimento Médio de Fila '+ n.getNomeNode()+' =  %f", sum(fila[fila$key == "queue_'+ nome +'",c("value")])/nrow(fila[fila$key == "queue_'+ nome +'",c("value", "key")]))\n\n')
			self.codigo.writelines(linha1)
		
	#-------------------- FUNCOES DE APOIO ----------------------------------------
	def subTrajetorias(self):
		i = 0
		for n in self.graph.graphNodes:
			if len(n.edges) > 1:
				i+= 1
		return i

	def verificaDestinos(self, listaEdges):
	# verifica se tem mais de destino
		e = 0
		i = 0
		while e < len(listaEdges)-1:
			#print(listaEdges[e].destino.edges[0])
			if set(listaEdges[e].destino.edges) == set(listaEdges[e+1].destino.edges):
					i+=1
					e+=1
			else:
				break

		return i		
				
	def finalBranch(self, inicio, fim):
		listaNodes = self.graph.getListaNodeTra()
		# recebe os indices da lista de edges na lista de nodes
		tam = len(listaNodes[inicio:fim])
		e = inicio
		i = 0
		vet = []
		vet1 = []
		while i != tam:
			while e < fim:
				vet = self.criarVetor(listaNodes[e].edges)
				vet1 = self.criarVetor(listaNodes[e+1].edges)
				if set(vet) == set(vet1):
					i+=1
				e+=1
			tam = len(listaNodes[inicio:fim])
			inicio = listaNodes.index(listaNodes[inicio].edges[0].destino)
			fim = listaNodes.index(listaNodes[fim].edges[-1].destino)
			e = inicio	
		return inicio

	def criarVetor(self, lista):
		vet = []
		for d in lista:
			vet.append(d.destino)
		return vet	

	def criarSubTrajetorias(self, no, idFim, ultimo):
		
		listaNodes = self.graph.getListaNodeTra()
		i = listaNodes.index(no)
		f = ''
		#print(idFim)
		
		while listaNodes[i].edges[0].destino != listaNodes[idFim]:

			if listaNodes[i].edges[0].destino.getTipoNode() != '3':

				aux = listaNodes.index(listaNodes[i].edges[0].destino) 

				if listaNodes[aux].edges[0].destino == listaNodes[idFim]:
					f = ')'if ultimo else ', \n'
					linha1 = []
					linha1.append('			seize("' + listaNodes[i].edges[0].destino.getNomeNode().lower()+'", 1) %>%\n')
					linha1.append('			timeout(function() rexp(1, '+ str(listaNodes[i].edges[0].destino.getMediaServico()) +')) %>%\n')
					linha1.append('			release("'+ listaNodes[i].edges[0].destino.getNomeNode().lower() +'", 1) %>%\n')
					linha1.append('			set_attribute("queue_'+ listaNodes[i].edges[0].destino.getNomeNode().lower() +'", function() get_queue_count(env, "'+ listaNodes[i].edges[0].destino.getNomeNode().lower() +'"))'+ f +'')
					
				else:	
					linha1 = []
					linha1.append('			seize("' + listaNodes[i].edges[0].destino.getNomeNode().lower()+'", 1) %>%\n')
					linha1.append('			timeout(function() rexp(1, '+ str(listaNodes[i].edges[0].destino.getMediaServico()) +')) %>%\n')
					linha1.append('			release("'+ listaNodes[i].edges[0].destino.getNomeNode().lower() +'", 1) %>%\n')
					linha1.append('			set_attribute("queue_'+ listaNodes[i].edges[0].destino.getNomeNode().lower() +'", function() get_queue_count(env, "'+ listaNodes[i].edges[0].destino.getNomeNode().lower() +'"))\n')
				
				self.codigo.writelines(linha1)
				
			i = listaNodes.index(listaNodes[i].edges[0].destino)


		#principal()



	
