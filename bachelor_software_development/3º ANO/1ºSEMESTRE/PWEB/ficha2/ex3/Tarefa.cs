using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ex1
{

    enum TipoPrioridade { baixa, normal, alta }
    enum TipoCategoria { pessoal, trabalho }
    enum TipoEstado { executar, execucao, concluida }

    class Tarefa
    {
        public TipoPrioridade Prioridade { get; set; }
        public TipoCategoria Categoria { get; set; }
        public TipoEstado Estado { get; set; }
        public string Nome { get; set; }
        public DateTime DataRegisto { get; set; }
        public DateTime DataLimite { get; set; }

        public Tarefa(string nome, TipoCategoria categoria, TipoPrioridade prioridade, TipoEstado estado, DateTime dataResgisto, DateTime dataLimite)
        {
            Prioridade = prioridade;
            Categoria = categoria;
            Estado = estado;
            Nome = nome;
            DataRegisto = dataResgisto;
            DataLimite = dataLimite;
        }

        public void MostrarTarefa()
        {
            Console.WriteLine("{0} | {1} |{2} | {3} |{4}| {5} ", Nome, Prioridade, Categoria, Estado, DataRegisto, DataLimite);
        }

    }
}

