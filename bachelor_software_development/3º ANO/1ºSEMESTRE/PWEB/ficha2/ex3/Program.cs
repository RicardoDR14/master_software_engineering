using ex1;

namespace ex3;
class Program
{
    private static void Main(string[] args)
    {
        Utilizador utilizador = new Utilizador("Nome", new List<Tarefa>());
        utilizador.ListarTarefasOrdenadas("nome", true); // Ordenar por nome de forma crescente
        utilizador.ListarTarefasOrdenadas("prioridade", false); // Ordenar por prioridade de forma decrescente

    }
}