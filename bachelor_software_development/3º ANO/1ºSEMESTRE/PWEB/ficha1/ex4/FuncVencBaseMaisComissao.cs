

namespace ex4
{
    public class FuncVencBaseMaisComissao : FuncComissao
    {
        private double vencimento_base;

        public FuncVencBaseMaisComissao(string nome, string apelido, string nif, double comissao_vendas, double vendas_totais, double vencimento_base) : base(nome, apelido, nif, comissao_vendas, vendas_totais)
        {
            this.vencimento_base = vencimento_base;
        }

        public double VencimentoBase
        {
            get { return vencimento_base; }
            set
            {
                if (value < 0) throw new ArgumentOutOfRangeException(nameof(value), value, $"O valor {nameof(VencimentoBase)} tem que ser >= 0");
                vencimento_base = value;
            }
        }

        public override double Vencimento()
        {
            return base.Vencimento() + VencimentoBase;
        }

        public override string ToString()
        {
            return $"Funcinario Base e {base.ToString()}"+
                $"\nVencimento Base: {VencimentoBase:F2}";
        }
    }
}
