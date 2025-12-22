from classes import edge
#import graphviz
#import pydotplus

class Node:

    def __init__(self):
        self.idNode = None # identificação do node
        self.nomeNode = None # nome do centro de serviço
        self.tipoNode = None  # tipo do node 1, 2 ou 3 - fonte, centro ou sorvedouro  
        self.distribuicaoChegada = None # tipo da distribuição - esponencial, normal, triangular e etc.
        self.distribuicaoServico = None # tipo da distribuição - esponencial, normal, triangular e etc.
    #    self.numFilas = numFilas # se o centro de servço possuir mais de um servidor tem mais de uma fila
    #    self.numServidores = numServidores # numeros de servidores
        self.mediaChegadas = None # média das chegadas - um cliente por 1 min
        self.mediaServico = None # média do serviço - o atendimento dura 5 min em média
        self.primeiroRecurso = False # para identificar onde começa nosso sistema - (ver se vai poder ter mais de um)
        self.proximoProb = False # se o node tiver mais de uma saida e a direção for definida por proba.
        self.proximoCiclo = None # se o node tiver mais de uma saida e a direção for definida por ciclo.
        self.edges = []
    
    def construtor(self, idNode, nomeNode, atributos):
        self.idNode = idNode # identificação do node
        self.nomeNode = nomeNode # nome do centro de serviço
        self.tipoNode = atributos[1]  # tipo do node 1, 2 ou 3 - fonte, centro ou sorvedouro  
        self.distribuicaoChegada = atributos[2] # tipo da distribuição -  1esponencial, 0normal e 2uniforme.
        self.distribuicaoServico = atributos[3] # tipo da distribuição - 1esponencial, 0normal e 2uniforme.
    #    self.numFilas = numFilas # se o centro de servço possuir mais de um servidor tem mais de uma fila
    #    self.numServidores = numServidores # numeros de servidores
        self.mediaChegadas = atributos[4] # média das chegadas - um cliente por 1 min
        self.mediaServico = atributos[5] # média do serviço - o atendimento dura 5 min em média - uniforme (1, 2)
        self.primeiroRecurso = False # para identificar onde começa nosso sistema - (ver se vai poder ter mais de um)
        self.proximoProb = False # se o node tiver mais de uma saida e a direção for definida por proba.
        self.proximoCiclo = False # se o node tiver mais de uma saida e a direção for definida por ciclo.
        self.edges = []
    

# ------------------ GETS ----------------------------------------------------

    def getIdNode(self):
        return self.idNode
    
    def getNomeNode(self):
        return self.nomeNode

    def getTipoNode(self):
        return self.tipoNode

    def getDistribuicaoChegada(self):
        return self.distribuicaoChegada
    
    def getDistribuicaoServico(self):
        return self.distribuicaoServico

    def getNumFilas(self):
        return self.numFilas

    def getNumServidores(self):
        return self.numServidores

    def getMediaChegadas(self):
        return self.mediaChegadas

    def getMediaServico(self):
        return self.mediaServico

    def getPrimeiroRecurso(self):
        return self.primeiroRecurso

    def getProximoProb(self):
        return self.proximoProb

    def getProximoCiclo(self):
        return self.proximoCiclo

    
  #  def getEdges(self):
   #     return self.edges

# -------------------- SETS -----------------------------------------------------

    def setIdNode(self, idNode):
        self.idNode = idNode
    
    def setNomeNode(self, nomeNode):
        self.nomeNode =nomeNode

    def setTipoNode(self, tipoNode):
        self.tipoNode = tipoNode

    def setDistribuicaoChegada(self, distribuicaoChegada):
        self.distribuicaoChegada = distribuicaoChegada
    
    def setDistribuicaoServico(self, distribuicaoServico):
        self.distribuicaoServico = distribuicaoServico

    def setNumFilas(self, numFilas):
        self.numFilas = numFilas

    def setNumServidores(self, numServidores):
        self.numServidores = numServidores

    def setMediaChegadas(self, mediaChegadas):
        self.mediaChegadas = mediaChegadas

    def setMediaServico(self, mediaServico):
        self.mediaServico = mediaServico

    def setPrimeiroRecurso(self, primeiroRecurso):
        self.primeiroRecurso = primeiroRecurso

    def setProximoProb(self, proximoProb):
        self.proximoProb = proximoProb

    def setProximoCiclo(self, proximoCiclo):
        self.proximoCiclo = proximoCiclo

   

# ------------------- METODOS PARA ADD EDGES ----------------------------------
    
    # função responsável por inserir um edge na lista do node origem 
    def addEdge(self, nodeB, probabilidade):

        ed = edge.Edge(self, nodeB, probabilidade)

        # verifica se a ligação é válida 
        adiciona = ed.verifica(self.getTipoNode(), nodeB.getTipoNode())

        if adiciona:
            self.edges.append(ed)
           # print(edge.origem.getTipoNode())
            # se o node for do tipo "fonte" só vai estar ligado a um node
            # e esse é o primeiro recurso - para saber onde começa o grafo 
            if ed.origem.getTipoNode() == '1':	
               # print('entrei')			
                nodeB.setPrimeiroRecurso(True) 
			
        else:
            print("Ligação inválida")


    # Função que preenche as informações do node recebido por parametro e retorna o node 
    def criarNode(self, no):

        atributos = no.get_comment().split()
        n = Node()
        # verifica se o tipo do centro de serviço para preencher
        if len(atributos) > 1:    
            n.construtor(no.get_name(), no.get_label(), atributos)
        else:
            n.idNode = no.get_name()
            n.nomeNode = no.get_label()
            n.tipoNode = atributos[0]

        return n

        
			
			
			

	
    
    





























