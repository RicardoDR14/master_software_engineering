

namespace ex4
{
    public class FuncHora: Funcionario
    {

        private double vencimento_hora;
        private int horas_trabalho;
        private int dias_uteis_mes;

        public FuncHora(string nome, string apelido, string nif, double vencimento_hora, int horas_trabalho, int dias_uteis_mes):base(nome,apelido,nif)
        {
            this.vencimento_hora = vencimento_hora;
            this.horas_trabalho = horas_trabalho;
            this.dias_uteis_mes = dias_uteis_mes;
        }

        public double VencimentoHora
        {
            get { return vencimento_hora; }
            set
            {
                if (value < 0) throw new ArgumentOutOfRangeException(nameof(value), value, $"O valor {nameof(VencimentoHora)} tem que ser >= 0");
                vencimento_hora = value;
            }
        }

        public int HorasTrabalho
        {
            get { return horas_trabalho; }
            set
            {
                if (value < 0||value >230) throw new ArgumentOutOfRangeException(nameof(value), value, $"O valor {nameof(HorasTrabalho)} tem que ser >= 0 e <= 10 * Numero dias uteis");
                horas_trabalho = value;
            }
        }

        public int NumeroDiasUteis
        {
            get { return dias_uteis_mes; }
            set
            {
                if (value < 0) throw new ArgumentOutOfRangeException(nameof(value), value, $"O valor {nameof(DiasUteisMes)} tem que ser >= 0");
                dias_uteis_mes = value;
            }
        }

        public override double Vencimento()
        {
            int max_horas_normais = 8;

            if (HorasTrabalho <= max_horas_normais)
                return VencimentoHora*HorasTrabalho;
            else
                return max_horas_normais*HorasTrabalho+
                    ((HorasTrabalho-max_horas_normais)* VencimentoHora *1.5);
        }

        public override string ToString()
        {
            return $"Funcionatio (Vencimento Hora): {base.ToString()}"+
                $"Vencimento por Hora: {VencimentoHora:F2}\n"+
                $"Dias de trabalho: {NumeroDiasUteis}\n"+
                $"Horas de trabalho: {HorasTrabalho}";
        }
    }
}
