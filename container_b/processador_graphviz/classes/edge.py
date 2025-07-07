
class Edge:

    def __init__(self, origem, destino, probabilidade):
        self.origem = origem
        self.destino = destino
        self.probabilidade = probabilidade

#------------ GETS -----------------------------------------------

    def getOrigem(self):
        return self.origem

    def getDestino(self): 
        return self.destino

    def getProbabilidade(self):
        return self.probabilidade   

#------------------ SETS ----------------------------------------

    def setOrigem(self, origem):
        self.origem = origem

    def setDestino(self, destino):
        self.destino = destino

    def setProbabilidade(self, probabilidade):
        self.probabilidade = probabilidade


# ------------------- METODOS ------------------------------------------
    # Função verifica se a ligação é válida 
    def verifica(self, origemTipo, destinoTipo):

        if ((origemTipo == 1 ) and (destinoTipo == 3)) or ((origemTipo == 1) and (destinoTipo == 1)) or ((origemTipo == 3) and (destinoTipo == 3)) or ((origemTipo == 3) and (destinoTipo == 1)) or ((origemTipo == 3) and (destinoTipo == 2)) or ((origemTipo == 2) and (destinoTipo == 1)):
            return False
        else:
            return True
            

