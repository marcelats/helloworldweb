from classes import node

#import graphviz
import pydotplus
import logging
class Graph:

    def __init__(self):
        self.nomeModelo = None
        self.tempoExecucao = None
        self.numCiclos = None
        self.tamBatch = None # chegadas simultaneas
        self.numMaxEntidades = None
        self.tipoModelo = None # true - aberto, false - fechado
        self.warmUp = None # true - definido pelo usuario, false - automatico
        self.tamWarmUp = None # periodo de aquecimento q n representa fielmente o estado do sistema
        self.semente = None
        self.graphNodes = []

    def construtor(self, nomeModelo, atributos):
        self.nomeModelo = nomeModelo
        self.tempoExecucao = atributos[1]
        self.numCiclos = atributos[2]
        self.tamBatch = atributos[3]
        self.numMaxEntidades = atributos[4]
        self.tipoModelo = atributos[5] # true - aberto, false - fechado
        self.warmUp = atributos[6] # true - definido pelo usuario, false - automatico
        self.tamWarmUp = atributos[7] # tamanho 
        self.semente = atributos[8]
    
# ------------------ GETS ----------------------------------------------------

    def getNomeModelo(self):
        return self.nomeModelo    
    
    def getTempoExecucao(self):
        return self.tempoExecucao  
    
    def getNumCiclos(self):
        return self.numCiclos  
    
    def getTamBatch(self):
        return self.tamBatch  
    
    def getNumMaxEntidades(self):
        return self.numMaxEntidades  
    
    def getTipoModelo(self):
        return self.tipoModelo  

    def getWarmUp(self):
        return self.warmUp

    def getTamWarmUp(self):
        return self.tamWarmUp

    def getSemente(self):
        return self.semente

    def getNodePrimeiroRecurso(self):
        n = node.Node()
        for n in self.graphNodes:
            if n.getPrimeiroRecurso():
                return n
    
    def getUltimoNode(self):
        n = node.Node()
        for n in self.graphNodes:
            index = len(self.graphNodes)-1
            n = self.graphNodes[index]
            return n


    def getListaNode(self):
        lista = list()

        for n in self.graphNodes:
            if n.getTipoNode() == '2':
                lista.append(n)       
        return lista 

    def getListaNodeSimples(self):
        lista = list()

        for n in self.graphNodes:
            if n.getTipoNode() != '3':
                lista.append(n)       
        return lista 

    def getListaNodeTra(self):
        lista = list()

        for n in self.graphNodes:
            if n.getTipoNode() != '1':
                lista.append(n)      
        print(lista)
        return lista 
#---------------------- SETS --------------------------------

    def setNomeModelo(self, nomeModelo):
        self.nomeModelo = nomeModelo
    
    def setTempoExecucao(self, tempoExecucao):
        self.tempoExecucao = tempoExecucao
    
    def setNumCiclos(self, numCiclos):
        self.numCiclos = numCiclos
    
    def setTamBatch(self, tamBatch):
        self.tamBatch = tamBatch  
    
    def setNumMaxEntidades(self, numMaxEntidades):
        self.numMaxEntidades = numMaxEntidades
    
    def setTipoModelo(self, tipoModelo):
        self.tipoModelo = tipoModelo

    def setWarmUp(self, warmUp):
        self.warmUp = warmUp

    def setTamWarmUp(self, tamWarmUp):
        self.tamWarmUp = tamWarmUp

    def setSemente(self, semente):
        self.semente = semente

# ------------------- METODOS PARA BUSCAR AS INFORMAÇÕES DO .DOT ----------------------------------
    
    # função responsável por pegar todas as informações do grafo no arquivo .dot e preencher o
    # o objeto Graph - vai ser chamada pelo gerador
    def buscarInformacoes(self, nomeArquivo):
        # abre o arquivo modelo1.gv/dot e pega todas as informações em uma lista
        graph=pydotplus.graphviz.graph_from_dot_file(nomeArquivo) 
        logging.info(graph)
        nodesDot=graph.get_nodes() # retira do modelo uma lista de nodes
        edgesDot=graph.get_edges() # retira do modelo uma lista de edges
        
        # a variável recebe as informações do comentário do grafo e a função split()
        # separa as informações por espaço e cria uma lista 
        atributos = graph.get_comment().split() 
        self.construtor(graph.get_name(), atributos) # preenche as informações do objeto
        n = []
        no = node.Node() # cria um objeto do tipo node

        # for roda a lista de nodes do modelo e manda as informações node por node 
        for n in nodesDot:
            no = no.criarNode(n) # chama a função que preenche as informações do node
            self.graphNodes.append(no) # add o node na lista
        
        e = []
        # for percorre a lista de node do objeto graph
        for n in self.graphNodes: 
            # for percorre a lista de edges do modelo
            for e in edgesDot:
                # verifica se o id do node na lista de edge é igual ao do node na lista do objeto
                if e.get_source() == n.idNode: 
                    d = int(e.get_destination())
                    # chama a função para add o edge a lista de edges daquele node
                    n.addEdge(self.graphNodes[d], e.get_comment())

#g = Graph()
#g.buscarInformacoes('modelo1.gv')
#print(g.graphNodes[1].getNomeNode())
   

