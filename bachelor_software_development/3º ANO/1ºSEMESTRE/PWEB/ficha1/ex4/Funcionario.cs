

namespace ex4
{
    public abstract class Funcionario
    {
        public string Nomes {  get; }
        public string Apelidos {  get; }
        public string NIF { get; }

        public Funcionario(string nomes, string apelidos, string nIF)
        {
            Nomes = nomes;
            Apelidos = apelidos;
            NIF = nIF;
        }

        public abstract double Vencimento();

        public override string ToString()
        {
            return $"Nome = {Nomes} {Apelidos} \n NIF = {NIF}\n";
        }
    }
}
