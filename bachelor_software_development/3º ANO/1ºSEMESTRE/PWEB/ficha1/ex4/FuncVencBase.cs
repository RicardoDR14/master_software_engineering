

namespace ex4
{
    public class FuncVencBase : Funcionario
    {
        private double vencimento_base;

        public FuncVencBase(string nome, string apelido, string nif, double vencimentoBase):base(nome, apelido,nif)
        {
            VencimentoBase = vencimentoBase;
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
            return VencimentoBase;
        }

        public override string ToString()
        {
            return $"Funcionário Base: {base.ToString()}"+
                $"Vencimento Base: {Vencimento}";
        }
    }
}
