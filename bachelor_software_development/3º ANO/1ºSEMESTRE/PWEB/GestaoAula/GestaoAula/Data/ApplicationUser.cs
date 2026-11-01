using Microsoft.AspNetCore.Identity;

namespace GestaoAula.Data
{
    // Add profile data for application users by adding properties to the ApplicationUser class
    public class ApplicationUser : IdentityUser
    {
        public string Nome { get; set; }
        public string Apelido { get; set; }
        public string NIF { get; set; }
        public string Rua { get; set; }
    }

}
