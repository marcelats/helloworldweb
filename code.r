# -----------------------------------------------------------------------------
# Código gerado com o ASDA - Ambiente de Simulação Distribuída Automático
# -----------------------------------------------------------------------------

library(simmer)

set.seed(1)

env <- simmer("untitled")
env

# Configurar trajetória 

cliente <- trajectory() %>%
	seize("server", 1) %>%
	timeout(function() rexp(1, 2)) %>%
	release("server", 1) %>%
	set_attribute("queue_server", function() get_queue_count(env, "server"))

# criando os recursos 

env %>%
add_resource("server", 1) %>%
add_generator("cliente", cliente, function() rexp(1, 10), mon=2)

# tempo total de execução
env %>% 
	run(100000) %>%
 	now()
# dados da simulação

chegadas <- get_mon_arrivals(env, TRUE) 
recursos <- get_mon_resources(env) 
fila <- get_mon_attributes(env)

sprintf("Total de Clientes Processados = %d", nrow(get_mon_arrivals(env)))
sprintf("Thoughput = %f", (nrow(get_mon_arrivals(env))/100000))

sprintf("Tempo de Serviço server =  %f", sum(chegadas[chegadas$resource == "server", c("activity_time")]))
sprintf("Tempo Médio de Serviço server =  %f", sum(chegadas[chegadas$resource == "server", c("activity_time")])/nrow(chegadas[chegadas$resource == "server", c("resource", "name")]))
sprintf("Utilização CPU server = %f", sum(chegadas[chegadas$resource == "server", c("activity_time")])/100000)
sprintf("Tempo de resposta server = %f", sum(chegadas[chegadas$resource == "server", c("end_time")])-sum(chegadas[chegadas$resource == "server", c("start_time")]))
sprintf("Tempo Médio de resposta server = %f", (sum(chegadas[chegadas$resource == "server", c("end_time")])-sum(chegadas[chegadas$resource == "server", c("start_time")]))/nrow(chegadas[chegadas$resource == "server", c("resource", "name")]) )
sprintf("Tempo Médio em Fila server = %f ", ((sum(chegadas[chegadas$resource == "server", c("end_time")])-sum(chegadas[chegadas$resource == "server", c("start_time")]))/nrow(chegadas[chegadas$resource == "server", c("resource", "name")]))-(sum(chegadas[chegadas$resource == "server", c("activity_time")])/nrow(chegadas[chegadas$resource == "server", c("resource", "name")])))
sprintf("Comprimento Médio de Fila server =  %f", sum(fila[fila$key == "queue_server",c("value")])/nrow(fila[fila$key == "queue_server",c("value", "key")]))

