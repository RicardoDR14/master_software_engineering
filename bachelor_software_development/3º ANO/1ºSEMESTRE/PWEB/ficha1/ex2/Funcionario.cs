

namespace ex2
{
    public class Funcionario
    {
        private string nome { get; set; }
        private string apelido { get; set; }
        private int NIF { get; set; }
        private double comissao;
        private int n_vendas;

        public Funcionario(string nome, string apelido, int nif, double comissao, int n_vendas)
        {
            this.nome = nome;
            this.apelido = apelido;
            this.NIF = nif;
            this.comissao = comissao;
            this.n_vendas = n_vendas;
        }

        public override string ToString() 
        {
            return "\nFuncionario: "+ nome+" "+apelido+ "\nNIF: "+ NIF + "\nVendas Totais: "+ n_vendas + "\nComissão de vendas: "+ comissao;
        }

        public virtual double calcularVencTotal()
        {
            return comissao * n_vendas; 
        }

        public virtual void mostrarVencTotal()
        {
            Console.WriteLine("Vencimento Total: " + calcularVencTotal() + "\n");
        } 
    }
}
