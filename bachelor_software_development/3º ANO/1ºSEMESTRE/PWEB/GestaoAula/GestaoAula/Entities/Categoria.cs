using System.ComponentModel.DataAnnotations.Schema;

namespace GestaoAula.Entities
{
    public class Categoria
    {
        public int Id { get; set; }
        public string? Nome { get; set; }
        public int? Ordem { get; set; }
        public string? UrlImage { get; set; }
        public byte[] Imagem { get; set; }

        [NotMapped]
        public IFormFile? ImageFile { get; set; }
    }
}
