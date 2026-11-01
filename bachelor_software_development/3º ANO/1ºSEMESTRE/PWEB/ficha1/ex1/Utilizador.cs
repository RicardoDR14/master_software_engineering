using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ex1
{
    internal class Utilizador
    {
        public string Nome { get; }
        public List<Tarefa> Tarefas { get; }

        public Utilizador(string nome, List<Tarefa> tarefas)
        {
            this.Nome = nome;
            this.Tarefas = tarefas;
        }

        public void AdicionarTarefa(string nome = null,
            TipoCategoria categoria = TipoCategoria.trabalho,
            TipoPrioridade prioridade = TipoPrioridade.alta,
            TipoEstado estado = TipoEstado.executar,
            string datalimite = null)
        {
            DateTime datareg = DateTime.Now;

            if (string.IsNullOrEmpty(nome) || string.IsNullOrEmpty(nome)) nome = "Tarefa";

            if (!DateTime.TryParse(datalimite, out DateTime datalim)) datalim = datareg.AddHours(24);

            Tarefas.Add(new Tarefa(nome, categoria, prioridade, estado, datareg, datalim));
        }

        public List<Tarefa> TarefasAtrasadas(DateTime data)
        {
            List<Tarefa> Atrasadas = new List<Tarefa>();

            foreach (Tarefa t in Tarefas)
                if (t.DataLimite < data && t.Estado != TipoEstado.concluida)
                    Atrasadas.Add(t);

            return Atrasadas;
        }

        public List<Tarefa> ListaTarefas(TipoPrioridade tipo)
        {
            List<Tarefa> tarefas_prioridade = new List<Tarefa>();

            foreach (Tarefa t in Tarefas)
                if (t.Prioridade == tipo)
                    tarefas_prioridade.Add(t);

            return tarefas_prioridade;
        }


        public List<Tarefa> ListaTarefas(TipoCategoria tipo)
        {
            List<Tarefa> tarefas_Categoria = new List<Tarefa>();

            foreach (Tarefa t in Tarefas)
                if (t.Categoria == tipo)
                    tarefas_Categoria.Add(t);

            return tarefas_Categoria;
        }
        public List<Tarefa> ListaTarefas(TipoEstado tipo)
        {
            List<Tarefa> tarefas_estado = new List<Tarefa>();

            foreach (Tarefa t in Tarefas)
                if (t.Estado == tipo)
                    tarefas_estado.Add(t);

            return tarefas_estado;
        }

        public void RetirarTarefa(TipoEstado tipo)
        {
            foreach (Tarefa t in ListaTarefas(tipo))
                Tarefas.Remove(t);
        }

        public void RetirarTarefa(TipoCategoria tipo)
        {
            foreach (Tarefa t in ListaTarefas(tipo))
                    Tarefas.Remove(t);
        }

        public void RetirarTarefa(TipoPrioridade tipo) 
        {
            foreach (Tarefa t in ListaTarefas(tipo))
                Tarefas.Remove(t);
        }

        public void MostrarTarefas(List<Tarefa> tarefas, string titulo = null)
        {
            if (titulo != null) Console.WriteLine(titulo);

            foreach (Tarefa t in tarefas)
                t.MostrarTarefa();
        }
        
    }
}
