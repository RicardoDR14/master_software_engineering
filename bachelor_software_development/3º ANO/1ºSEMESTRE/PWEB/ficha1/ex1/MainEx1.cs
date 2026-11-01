namespace ex1;

class MainEx1
{
    static void Main(string[] args)
    {
        Utilizador u = new Utilizador("ricardo", new List<Tarefa>());

        u.AdicionarTarefa();
        u.AdicionarTarefa("compras", categoria: TipoCategoria.pessoal);
        u.AdicionarTarefa("exame",prioridade:TipoPrioridade.normal, datalimite: "30/09/2024 09:30");
        u.AdicionarTarefa("rescurso", prioridade:TipoPrioridade.normal);

        u.MostrarTarefas(u.Tarefas, "Todas as Tarefas\n");
        u.MostrarTarefas(u.TarefasAtrasadas(DateTime.Now), "\nTarefas atrasadas\n");

    }

}
