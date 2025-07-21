/* ----------------------------------------------------------------------- 
C�digo gerado com o ASDA - Ambiente de Simula��o Distribu�da Autom�tico
--------------------------------------------------------------------------*/
#include <stdio.h>
#include "smpl.h"

main()
{
 /* definicoes */
 float Te = 100000;
 int Event = 1, Customer = 1, Aleatorio;
 float Ta0 = 10, Ts0 = 10;
 int i = 0;

 FILE *p, *saida;
 saida = fopen("TESTE.out","w");

 if ((p = sendto(saida)) == NULL)
    printf("Erro na saida \n");


 /* prepara o sistema de simulacao e da nome ao modelo */
 smpl(0," TESTE");


 /* cria e da nome as facilidades */
 facility("CS_0",1);

 /* escalona a chegada do primeiro cliente */
   stream(1);

   schedule(1,0.0, Customer);



 while ( (time() < Te) )
{
    cause(&Event,&Customer);
    switch(Event)
    {
        case 1 : 
          schedule(2,0.0, Customer);
          schedule(1,expntl(Ta0), Customer);
          break;

        /*  centro de serviço = CS_0 */
        case 2 : 
          if (request("CS_0", Event, Customer, 0) == 0)
             schedule(3,expntl(Ts0), Customer);
          break;
        case 3 : 
          release("CS_0", Customer);
          break;
 }
}


/* gera o relatorio da simulacao */
  fprintf(saida,"TempoSimulado: %f\n", time() );

  fprintf(saida,"Utilizacao (\"CS_0\") = %g\n", utilizacao_recurso("CS_0"));
  fprintf(saida,"Comprimento medio fila (\"CS_0\") = %g\n", comprimento_medio_fila("CS_0"));
  fprintf(saida,"Periodo medio ocupado (\"CS_0\") = %g\n", periodo_medio_ocupado("CS_0"));

   fclose(saida);
}
/* ----------------------------------------------------------------------- */
