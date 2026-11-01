namespace ex2;
class Program
{
    private static void Main(string[] args)
    {
        string[] ListaUm = { "C#", "Aprender C#","ASP.NET MVC com C#",
                            "Entity Framework","Bootstrap","Identity",
                            "Lambda","Delegates","Linq","POO com C#"
                            };

        string[] ListaDois = { "C#", "ASP.NET MVC", "Linq", "Lambda e C#" };

        int[] Numeros = { 10, 23, 54, 77, 85, 12, 1, 4, 53 };

        ///////////////////////////
        ////a)
        ////com extension methods
        //string[] ListaUmOrdenadaAlfabeticamente1 = ListaUm.OrderBy(x => x).ToArray();
        ////com query syntax
        //string[] ListaUmOrdenadaAlfabeticamente2 = (from x in ListaUm
        //                                            orderby x
        //                                            select x).ToArray();
        //Console.WriteLine("ListaUm:\n");
        //foreach (var item in ListaUm)
        //{
        //    Console.WriteLine(item);
        //}
        //Console.WriteLine("\n\nListaUmOrdenadaAlfabeticamente1:\n");
        //foreach (var item in ListaUmOrdenadaAlfabeticamente1)
        //{
        //    Console.WriteLine(item);
        //}
        //Console.WriteLine("\nListaUmOrdenadaAlfabeticamente2:\n");
        //foreach (var item in ListaUmOrdenadaAlfabeticamente2)
        //{
        //    Console.WriteLine(item);
        //}


        ////////////////////////
        ////b)
        ////com extended methods
        //string[] ListaUmSeisCaracteres1 = ListaUm.Where(x => x.Length<6).ToArray();

        //// com query syntax
        //string[] ListaUmSeisCaracteres2 = (from x in ListaUm
        //                                   where x.Length<6
        //                                   select x).ToArray();

        //Console.WriteLine("ListaUm:\n");
        //foreach (var item in ListaUm)
        //{
        //    Console.WriteLine(item);
        //}
        //Console.WriteLine("\n\nListaUmSeisCaracteres1:\n");
        //foreach (var item in ListaUmSeisCaracteres1)
        //{
        //    Console.WriteLine(item);
        //}
        //Console.WriteLine("\nListaUmSeisCaracteres2:\n");
        //foreach (var item in ListaUmSeisCaracteres2)
        //{
        //    Console.WriteLine(item);
        //}



        ////////////////////////
        ////c)
        ////com extended methods
        //string[] ListaUmComCSharp1 = ListaUm.Where(x => x.Contains("C#")==true).ToArray();

        ////com query syntax
        //string[] ListaUmComCSharp2 = (from x in ListaUm
        //                              where x.Contains("C#") == true
        //                              select x).ToArray();

        //Console.WriteLine("ListaUm:\n");
        //foreach (var item in ListaUm)
        //{
        //    Console.WriteLine(item);
        //}
        //Console.WriteLine("\n\nListaUmComCSharp1:\n");
        //foreach (var item in ListaUmComCSharp1)
        //{
        //    Console.WriteLine(item);
        //}
        //Console.WriteLine("\nListaUmComCSharp2:\n");
        //foreach (var item in ListaUmComCSharp2)
        //{
        //    Console.WriteLine(item);
        //}



        ////////////////////////
        ////d)
        ////com extended methods
        //string[] ListaUmNPalavrasElementos1 = ListaUm.Select(x => x.Split(' ').Length).Select(n => n.ToString()).ToArray();

        ////com query syntax
        //string[] ListaUmNPalavrasElementos2 = (from x in ListaUm
        //                                       select x.Split(" ").Length.ToString()).ToArray();


        //Console.WriteLine("ListaUm:\n");
        //foreach (var item in ListaUm)
        //{
        //    Console.WriteLine(item);
        //}
        //Console.WriteLine("\n\nListaUmNPalavrasElementos1:\n");
        //foreach (var item in ListaUmNPalavrasElementos1)
        //{
        //    Console.WriteLine(item);
        //}
        //Console.WriteLine("\nListaUmNPalavrasElementos2:\n");
        //foreach (var item in ListaUmNPalavrasElementos2)
        //{
        //    Console.WriteLine(item);
        //}

        ////////////////////////
        ////e)
        ////com extended methods
        //double mediaNumeros1 = Numeros.Average();
        ////com query syntax
        //double mediaNumeros2 = (from x in Numeros
        //                                       select x).Average();


        //Console.WriteLine(mediaNumeros1);
        //Console.WriteLine(mediaNumeros2);

        ////////////////////////
        ////f)
        ////com extended methods
        //double MaxNumeros1 = Numeros.Max();
        ////com query syntax
        //double MaxNumeros2 = (from x in Numeros
        //                        select x).Max();


        //Console.WriteLine(MaxNumeros1);
        //Console.WriteLine(MaxNumeros2);

        ////////////////////////
        ////g)
        ////com extended methods
        //int[] NumerosBetween1and25_1 = Numeros.Where(x=>x>=1&&x<=25).OrderByDescending(x=>x).ToArray();
        ////com query syntax
        //int[] NumerosBetween1and25_2 = (from x in Numeros
        //                                where x >= 1 && x <= 25
        //                                orderby x descending
        //                                select x).ToArray();

        //foreach (var item in NumerosBetween1and25_1)
        //{
        //    Console.WriteLine(item);
        //}
        //Console.WriteLine();
        //foreach (var item in NumerosBetween1and25_2)
        //{
        //    Console.WriteLine(item);
        //}

        ////////////////////////
        ////h)
        ////com extended methods
        //string[] elementosComunsListas_1 = ListaUm.Intersect(ListaDois).ToArray();
        ////com query syntax
        //string[] elementosComunsListas_2 = (from x in ListaUm
        //                                    where ListaDois.Contains(x)
        //                                   select x).ToArray();

        //foreach (var item in elementosComunsListas_1)
        //{
        //    Console.WriteLine(item);
        //}
        //Console.WriteLine();
        //foreach (var item in elementosComunsListas_2)
        //{
        //    Console.WriteLine(item);
        //}

        ////////////////////////
        ////i)
        ////com extended methods
        //string[] elementosReuniaoListas_1 = ListaUm.Union(ListaDois).OrderBy(x=>x).ToArray();
        ////com query syntax
        //string[] elementosReuniaoListas_2 = (from x in ListaDois.Union(ListaUm)
        //                                     orderby x
        //                                    select x).ToArray();

        //foreach (var item in elementosReuniaoListas_1)
        //{
        //    Console.WriteLine(item);
        //}
        //Console.WriteLine();
        //foreach (var item in elementosReuniaoListas_2)
        //{
        //    Console.WriteLine(item);
        //}

        ////////////////////////
        ////j)
        ////com extended methods
        //var solucao1 = Numeros.GroupBy(x=>x%2);
        ////com query syntax
        //var solucao2 = (from x in Numeros
        //                group x by x%2 into grupo
        //                select grupo);

        //foreach (var x in solucao1)
        //{
        //    Console.WriteLine();
        //    Console.Write(x.Key == 0 ? "Numeros Pares: " : "Numeros Impares: ");
        //    foreach (var y in x) Console.Write(y + " ");
        //}
        //Console.WriteLine();
        //foreach (var x in solucao2)
        //{
        //    Console.WriteLine();
        //    Console.Write(x.Key == 0 ? "Numeros Pares: " : "Numeros Impares: ");
        //    foreach (var y in x) Console.Write(y + " ");
        //}

        ////////////////////////
        ////k)
        ////com extended methods
        //var produto1 = Numeros
        //               .Where(numero => numero < 30)
        //               .Aggregate(1, (acc, numero) => acc * numero);

        ////com query syntax
        //var produto2 = (from numero in Numeros
        //                where numero < 30
        //                select numero)
        //                .Aggregate(1, (acc, numero) => acc * numero);

        //Console.WriteLine(produto1);
        //Console.WriteLine(produto2);

        //////////////////////
        //l)
        //com extended methods
        var solucao1 = ListaUm
            .Where(str => str.Contains("C#"))
            .Select(str => new
            {
                str,
                sInicial = str.Split(' ')[0],
                sFinal = str.Split(' ').Length > 1 ? str.Split(' ').Last() : "NÃO TEM"
            });

        //com query syntax
        var solucao2 = from str in ListaUm
                       where str.Contains("C#")
                       select new
                       {
                           str,
                           sInicial = str.Split(' ')[0],
                           sFinal = str.Split(' ').Length > 1 ? str.Split(' ').Last() : "NÃO TEM"
                       }; ;

        foreach (var s in solucao1)
        {
            Console.WriteLine("String: " + s.str +
                              "\n\t Primeira Palavra: " + s.sInicial +
                              "\n\t Ultima Palavra: " + s.sFinal);
        }
        Console.WriteLine();
        foreach (var s in solucao2)
        {
            Console.WriteLine("String: " + s.str +
                              "\n\t Primeira Palavra: " + s.sInicial +
                              "\n\t Ultima Palavra: " + s.sFinal);
        }
    }
}