

namespace ex4
{
    public class FuncComissao : Funcionario
    {
        private double vendas_totais;
        private double comissao_vendas;

        public FuncComissao(string nome, string apelido, string nif, double comissao_vendas, double vendas_totais) : base(nome, apelido, nif)
        {
            ComissaoVendas = comissao_vendas;
            VendasTotais = vendas_totais;
        }

        public double VendasTotais
        {
            get { return vendas_totais; }
            set
            {
                if (value < 0)
                {
                    throw new ArgumentOutOfRangeException(nameof(value),
                        value, $"{nameof(vendas_totais)} tem que ser >= 0");
                }
                vendas_totais = value;
            }
        }

        public double ComissaoVendas
        {
            get { return comissao_vendas; }
            set
            {
                if (value <= 0 || value >=1)
                {
                    throw new ArgumentOutOfRangeException(nameof(value),
                        value, $"{nameof(ComissaoVendas)} tem que ser > 0 e < 1");
                }
                comissao_vendas = value;
            }
        }

        public override double Vencimento()
        {
            return comissao_vendas * vendas_totais;
        }

        public override string ToString()
        {
            return $"Funcionario (por comissão): {base.ToString()}"+
                $"Vendas Totais: {vendas_totais:F2}\n"+
                $"Comissão de Vendas: {comissao_vendas:F2}\n"+
                $"Componente do Vencimento por Comissões: {comissao_vendas*vendas_totais}";
        }
    }
}
