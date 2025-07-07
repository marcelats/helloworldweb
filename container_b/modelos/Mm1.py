import graphviz 
 

modelo = graphviz.Digraph(name='Mm1', directory= 'modelos/') # da pra usar o comment pra passar quant. de cliente ou tempo total

# Especifica caracteristicas para o grafo e passa as principais informaçoes do grafo
# (tempo total, numero de ciclos, tamanho do batch, numero maximo de entidades,
# tipo do modelo, warmup, tamanho warmup, semente)
modelo.attr(rankdir='LR', comment=' 2000 0 0 0 True False 0 10 ')

# Especifica as caracteristicas dos centros de serviço
# (tipo do node, tipo da distribuição de chegada, tipo da distribuição de serviço,
# média das chegadas, média de serviço)

modelo.node('0', 'Source', image='Rsource.gif', shape='plaintext', labelloc='t', comment='1')

modelo.node('1', 'CPU', image='R1x1.gif', shape='plaintext', labelloc='t', comment=' 2 1 1 1 0.1 ') 

modelo.node('2', 'Destination', image='REnd.gif', shape='plaintext', labelloc='t', comment='3')

# colocar todos os atributos necessários na classe da aresta e 
# passar os valores em sequencia no attrs na string 
# separando por espaços e usar o split pra colocar tudo no 
# seu devido lugar na classe Edge
modelo.edge('0', '1', comment='100')
modelo.edge('1', '2', comment='100') #comment = 'porcentagem de envio se o nó tiver mais de uma saída'
modelo.render(format='png')
