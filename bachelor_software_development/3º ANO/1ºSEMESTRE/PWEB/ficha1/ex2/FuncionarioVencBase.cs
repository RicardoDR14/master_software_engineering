
namespace ex2
{
    public class FuncionarioVencBase : Funcionario
    {
        private int vencimentoBase { get; set; }

        public FuncionarioVencBase(string nome, string apelido, int nif, double comissao, int n_vendas, int vencimentoBase): base(nome, apelido, nif,comissao,n_vendas)
        {
            this.vencimentoBase = vencimentoBase;
        }

        public override string ToString() 
        {
            return base.ToString() + "\nVencimento Base: "+vencimentoBase;
        }

        public override double calcularVencTotal()
        {
            return base.calcularVencTotal()+ vencimentoBase;
        }

        public override void mostrarVencTotal()
        {
            Console.WriteLine("\nVencimento Total: "+calcularVencTotal()+"\n");
        }
    }
}
