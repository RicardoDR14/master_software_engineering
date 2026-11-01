
namespace ex2;
class MainEx2
{
    private static void Main(string[] args)
    {
        Funcionario f1 = new Funcionario("funcionário", "1", 000000000, 0.6, 1000);
        Funcionario f2 = new FuncionarioVencBase("funcionário", "2", 111111111,0.2, 1000, 100);

        Console.WriteLine(f1.ToString());
        f1.mostrarVencTotal();

        Console.WriteLine(f2.ToString());
        f2.mostrarVencTotal();
    }
}