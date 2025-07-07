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
		'chegadas': random.expovariate(1.0/1), 
		'cpu': random.expovariate(1.0/0.1), 
	}.get(tipo, 0.0)

# função de chegada de clientes de acordo com os lugares que os clientes chegam
def chegadaClientes(env, recursos):
	global contaChegada
	while True:
		contaChegada+=1
		yield env.timeout(distributions('chegadas'))	

		env.process(processoCPU(env, recursos))


# funções que realizam o processamento dos demais nodes

def processoCPU(env, recursos):

	global contaTerminos, tempoServico, tempoResposta

	chegada = env.now
	req = recursos[recursos.index(cpu)].request()
	yield req
	tempoFila = env.now - chegada

	inicio = env.now
	yield env.timeout(distributions('cpu'))
	tempoServico[0] = tempoServico[0] + (env.now - inicio)
	tempoResposta[0] = tempoResposta[0] + tempoFila + tempoServico[0]

	recursos[recursos.index(cpu)].release(req)

	contaTerminos+=1

# define a semente ultizada para a geração aleatoria de numeros
random.seed(10)

# cria o ambiente de simulação
env = simpy.Environment()

# cria todos os recursos = facility
cpu = simpy.Resource(env, capacity = 1)

recursos = [
	cpu,
]

# iniciar os processos de chegada 
env.process(chegadaClientes(env, recursos))

# define o tempo total de execução da simulação
env.run(until=2000)

# gera os relatorios finais
print('Total de Clientes processados = ', contaTerminos)
print('Throughput = ', contaTerminos/2000)

print('Tempo de Serviço CPU = ', tempoServico[0])
print('Tempo Médio de Serviço CPU = ', tempoServico[0]/contaTerminos)
print('Utilização CPU = ', tempoServico[0]/2000)
print('Tempo de resposta CPU = ', tempoResposta[0])
print('Tempo Médio de resposta CPU = ', tempoResposta[0]/contaTerminos)
print('Tempo Médio em Fila CPU = ',(tempoResposta[0]/contaTerminos)-(tempoServico[0]/contaTerminos))
print('----------------------------------------------------')

