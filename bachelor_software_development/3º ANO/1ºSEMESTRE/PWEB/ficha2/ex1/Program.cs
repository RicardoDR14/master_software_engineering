namespace ex1;
class Program
{
    private static void Main(string[] args)
    {
        List<string> ListaStrings = new List<string>() {"C#", "Entity Framework", "ASP.NET MVC com C#", "Linq", "Lambda", "Identity Framework"} ;

        var ListA = new List<string>();
        foreach (var x in ListaStrings) if (x.Contains("C#")) ListA.Add(x);
        var ListB = from s in ListaStrings where s.Contains("C#") select s;
        var ListC = ListaStrings.Where(l => l.Contains("C#"));

    }
}