/* ----------------------------------------------------------------------- 
C�digo gerado com o ASDA - Ambiente de Simula��o Distribu�da Autom�tico
--------------------------------------------------------------------------*/
#include "smpl.h"

main()
{
 /* definicoes */
 float Te = 12;
 int Event = 1, Customer = 1, Aleatorio;
 float Ta0 = 15, Ta1 = 17, Ts0 = 16, Ts1 = 18;
 int i = 0;

 char flag_reset = 0;
 float timeWarmUp = 45;
 int CS0, CS1;
 unsigned int Max1 = 0, Min1 = 1000;
 unsigned int Total_Clientes = 0; 
 unsigned int Tot0 = 0, Vaz0 = 0;
 unsigned int Maximo_Entidades = 0, Num_Max_Entidades = 7;
 float TBatch = 0;
 int r = 0;
 FILE *p, *saida;
 saida = fopen("untitled.out","w");

 if ((p = sendto(saida)) == NULL)
    printf("Erro na saida \n");


 /* prepara o sistema de simulacao e da nome ao modelo */
 smpl(0," untitled");


 /* cria e da nome as facilidades */
 CS0 = facility("CS0",1);
 CS1 = facility("CS1",5);

 /* escalona a chegada do primeiro cliente */



 while( (time() < Te) && (Maximo_Entidades < Num_Max_Entidades)&& (r== 0) )
{
   if ( (!flag_reset) && (time() > timeWarmUp) )
   {
      reset();
      flag_reset = 1;
      Tot0 = 0; Vaz0 = 0;
      Max1 = 0; Min1 = 1000; 
      Total_Clientes = 0;
      Maximo_Entidades = 0;
   }
    cause(&Event,&Customer);
    switch(Event)
    {
        case 1 : 
          schedule(3,0.0, Customer);
          stream(34);
          break;
        case 2 : 
          schedule(5,0.0, Customer);
          stream(43);
          break;

        /*  centro de servi   o = CS0 */
        case 3 : 
          Total_Clientes = inq(CS0);
          if (Total_Clientes == 0)
             Vaz0++;
          Tot0++;
          stream(24);
          if (request(CS0, Customer,0) == 0)
          {
             TBatch = normal(Ts0, 32);
              r = obs(TBatch);
             schedule(4,TBatch, Customer);
          }
          break;
        case 4 : 
          release(CS0, Customer);
          Maximo_Entidades++;
          break;

        /*  centro de servi   o = CS1 */
        case 5 : 
          Total_Clientes = inq(CS1);
          if (Total_Clientes > Max1)
             Max1 = Total_Clientes;
         else
          if (Total_Clientes < Min1)
             Min1 = Total_Clientes;
          stream(34);
          if (request(CS1, Customer,0) == 0)
          {
             TBatch = normal(Ts1, 65);
              r = obs(TBatch);
             schedule(6,TBatch, Customer);
          }
          break;
        case 6 : 
          release(CS1, Customer);
          Maximo_Entidades++;
          break;
 }
}


/* gera o relatorio da simulacao */
   report();
   fprintf(saida,"\n\nRelat   rio - M   ximo e M   nimo das Filas \n "); 
   fprintf(saida,"\n Maximo clientes recurso CS1 : %i ", Max1);
   fprintf(saida,"\n M   nimo clientes recurso CS1 : %i ", Min1);
   fprintf(saida,"\n\nRelat   rio - Total de Vezes - Fila Vazia \n "); 
   fprintf(saida,"\n Total de Clientes do recurso CS0 : %i ", Tot0);
   fprintf(saida,"\n Total clientes que encontraram fila vazia no recurso CS0 : %i ", Vaz0);
   fclose(saida);
}
/* ----------------------------------------------------------------------- */
