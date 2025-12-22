#include <stdio.h>
#include "smplx.h"

main()
{
	printf("teste");
 /* definicoes */
 float Te = 12;
 int Event = 1, Customer = 1, Aleatorio;
 float Ta0 = 15, Ta1 = 17, Ts0 = 16, Ts1 = 18;
 int i = 0;

 unsigned int Maximo_Entidades = 0, Num_Max_Entidades = 7;
 FILE *p, *saida;
 saida = fopen("untitled.out","w");

 if ((p = sendto(saida)) == NULL)
    printf("Erro na saida \n");


 /* prepara o sistema de simulacao e da nome ao modelo */
 smpl(0," untitled");


 /* cria e da nome as facilidades */
 facility("CS0",1);
 facility("CS1",5);

 /* escalona a chegada do primeiro cliente */
   stream(3);




 while( (time() < Te) && (Maximo_Entidades < Num_Max_Entidades))
{
    cause(&Event,&Customer);
    switch(Event)
    {
        case 1 : 
          schedule(3,0.0, Customer);
          break;
        case 2 : 
          schedule(5,0.0, Customer);
          break;

        /*  centro de servi   o = CS0 */
        case 3 : 
          if (request("CS0", Event, Customer, 0) == 0)
             schedule(4,normal(Ts0, 3), Customer);
          break;
        case 4 : 
          release("CS0", Customer);
          Maximo_Entidades++;
          break;

        /*  centro de servi   o = CS1 */
        case 5 : 
          if (request("CS1", Event, Customer, 0) == 0)
             schedule(6,normal(Ts1, 6), Customer);
          break;
        case 6 : 
          release("CS1", Customer);
          Maximo_Entidades++;
          break;
 }
}


/* gera o relatorio da simulacao */
  fprintf(saida,"TempoSimulado: %f\n", time() );

  fprintf(saida,"Utilizacao (\"CS0\") = %g\n", utilizacao_recurso("CS0"));
  fprintf(saida,"Comprimento medio fila (\"CS0\") = %g\n", comprimento_medio_fila("CS0"));
  fprintf(saida,"Periodo medio ocupado (\"CS0\") = %g\n", periodo_medio_ocupado("CS0"));
  fprintf(saida,"Utilizacao (\"CS1\") = %g\n", utilizacao_recurso("CS1"));
  fprintf(saida,"Comprimento medio fila (\"CS1\") = %g\n", comprimento_medio_fila("CS1"));
  fprintf(saida,"Periodo medio ocupado (\"CS1\") = %g\n", periodo_medio_ocupado("CS1"));

   fclose(saida);
}

