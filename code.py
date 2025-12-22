# -----------------------------------------------------------------------------
# Código gerado com o ASDA - Ambiente de Simulação Distribuída Automático
# -----------------------------------------------------------------------------

import random
import simpy

contaChegada = 0
contaTerminos = 0
tempoServico = [0]*1
tempoResposta = [0]*1

# função que armazena as distribuições utilizadas no modelo
def distributions(tipo):
	return {
		'chegadas': random.expovariate(1.0/10), 
		'server': random.expovariate(1.0/2), 
	}.get(tipo, 0.0)

# função de chegada de clientes de acordo com os lugares que os clientes chegam
def chegadaClientes(env, recursos):
	global contaChegada
	while True:
		contaChegada+=1
		yield env.timeout(distributions('chegadas'))	

		env.process(processoserver(env, recursos))


# funções que realizam o processamento dos demais nodes

def processoserver(env, recursos):

	global contaTerminos, tempoServico, tempoResposta

	chegada = env.now
	req = recursos[recursos.index(server)].request()
	yield req
	tempoFila = env.now - chegada

	inicio = env.now
	yield env.timeout(distributions('server'))
	tempoServico[0] = tempoServico[0] + (env.now - inicio)
	tempoResposta[0] = tempoResposta[0] + tempoFila + tempoServico[0]

	recursos[recursos.index(server)].release(req)

	contaTerminos+=1

# define a semente ultizada para a geração aleatoria de numeros
random.seed(0)

# cria o ambiente de simulação
env = simpy.Environment()

# cria todos os recursos = facility
server = simpy.Resource(env, capacity = 1)

recursos = [
	server,
]

# iniciar os processos de chegada 
env.process(chegadaClientes(env, recursos))

# define o tempo total de execução da simulação
env.run(until=100000)

# gera os relatorios finais
print('Total de Clientes processados = ', contaTerminos)
print('Throughput = ', contaTerminos/100000)

print('Tempo de Serviço server = ', tempoServico[0])
print('Tempo Médio de Serviço server = ', tempoServico[0]/contaTerminos)
print('Utilização server = ', tempoServico[0]/100000)
print('Tempo de resposta server = ', tempoResposta[0])
print('Tempo Médio de resposta server = ', tempoResposta[0]/contaTerminos)
print('Tempo Médio em Fila server = ',(tempoResposta[0]/contaTerminos)-(tempoServico[0]/contaTerminos))
print('----------------------------------------------------')


