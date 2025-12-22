/* ----------------------------------------------------------------------- 
Código gerado com o ASDA - Ambiente de Simulação Distribuída Automático
--------------------------------------------------------------------------*/
#include "smpl.h"

main()
{
 /* definicoes */
 float Te = 10000;
 int Event = 1, Customer = 1, Aleatorio;
 float Ta0 = 10, Ts0 = 5;
 int i = 0;

 char flag_reset = 0;
 float timeWarmUp = 500.0;
 int CS_0;
 FILE *p, *saida;
 saida = fopen("EXEMPLO1.out","w");

 if ((p = sendto(saida)) == NULL)
    printf("Erro na saida \n");


 /* prepara o sistema de simulacao e da nome ao modelo */
 smpl(0," EXEMPLO1");


 /* cria e da nome as facilidades */
 CS_0 = facility("CS_0",1);

 /* escalona a chegada do primeiro cliente */
   schedule(1,0.0, Customer);



 while ( (time() < Te) )
{
   if ( (!flag_reset) && (time() > timeWarmUp) )
   {
      reset();
      flag_reset = 1;
   }
    cause(&Event,&Customer);
    switch(Event)
    {
        case 1 : 
          schedule(2,0.0, Customer);
          schedule(1,expntl(Ta0), Customer);
          break;

        /*  centro de serviço = CS_0 */
        case 2 : 
          if (request(CS_0, Customer,0) == 0)
             schedule(3,expntl(Ts0), Customer);
          break;
        case 3 : 
          release(CS_0, Customer);
          break;
 }
}


/* gera o relatorio da simulacao */
   report();
   fclose(saida);
}
/* ----------------------------------------------------------------------- */
