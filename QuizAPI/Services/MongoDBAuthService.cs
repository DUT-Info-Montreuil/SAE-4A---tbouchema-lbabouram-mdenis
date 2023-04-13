using System.Security.Claims;
using System.Collections.Generic;
using System.Threading.Tasks;
using QuizAPI.Models;
using MongoDB.Driver;
using Microsoft.Extensions.Options;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Authentication;

namespace QuizAPI.Services
{
    public class MongoDBAuthService
    {
        private readonly IMongoCollection<QuizInfo> _quizCollection;
        private readonly IMongoCollection<UserProfile> _userCollection;
        private readonly IHttpContextAccessor _httpContextAccessor;

        public MongoDBAuthService(IOptions<MongoDBSettings> mongoDBSettings, IHttpContextAccessor httpContextAccessor)
        {
            MongoClient client = new MongoClient(mongoDBSettings.Value.ConnectionURL);
            IMongoDatabase database = client.GetDatabase(mongoDBSettings.Value.DatabaseName);
            _quizCollection = database.GetCollection<QuizInfo>(mongoDBSettings.Value.QuizCollectionName);
            _userCollection = database.GetCollection<UserProfile>(mongoDBSettings.Value.UserCollectionName);
            _httpContextAccessor = httpContextAccessor;
        }

        public async Task<bool> VerifyPrivilegesQuizAsync(string userid, string id)
        {
            var user = await _userCollection.Find(user => user.Id == userid).FirstOrDefaultAsync();
            if (user == null)
            {
                throw new Exception("User not found");
            }
            if (user.IsAdmin)
            {
                return true;
            }
            var quiz = await _quizCollection.Find(quiz => quiz.Id == id).FirstOrDefaultAsync();
            if (quiz == null)
            {
                throw new Exception("Quiz not found");
            }
            if (quiz.QuizCreatorId == userid)
            {
                return true;
            }
            return false;
        }

        public async Task<bool> VerifyPrivilegesUserAsync(string userid, string id)
        {
            var user = await _userCollection.Find(user => user.Id == userid).FirstOrDefaultAsync();
            if (user == null)
            {
                throw new Exception("User not found");
            }
            if (user.IsAdmin)
            {
                return true;
            }
            if (user.Id == id)
            {
                return true;
            }
            return false;
        }

        public async Task<bool> VerifyPrivilegesPersonnalInfoAsync(string userid, string id)
        {
            var user = await _userCollection.Find(user => user.Id == userid).FirstOrDefaultAsync();
            if (user == null)
            {
                throw new Exception("User not found");
            }
            if (user.Id == id)
            {
                return true;
            }
            return false;
        }

        public async Task<UserProfile> LoginAsync(string login, string password)
        {
            var user = await _userCollection.Find(user => user.Email == login || user.Pseudo == login).FirstOrDefaultAsync();
            if (user == null || !VerifyPassword(password, user.Password!))
                return null!;

            var claims = new List<Claim>
            {
                new Claim(ClaimTypes.NameIdentifier, user.Id!),
                new Claim(ClaimTypes.Role, user.IsAdmin ? "Admin" : "User"),
                new Claim(ClaimTypes.Email, user.Email!),
                new Claim(ClaimTypes.Name, user.Pseudo!)
            };

            var claimsIdentity = new ClaimsIdentity(
                claims, CookieAuthenticationDefaults.AuthenticationScheme
            );

            var authProperties = new AuthenticationProperties
            {
                AllowRefresh = true,
                IsPersistent = true,
                ExpiresUtc = DateTimeOffset.UtcNow.AddMinutes(15)
            };

            await _httpContextAccessor.HttpContext!.SignInAsync(
                CookieAuthenticationDefaults.AuthenticationScheme,
                new ClaimsPrincipal(claimsIdentity),
                authProperties
            );

            return user!;
        }

        public async Task LogoutAsync()
        {
            await _httpContextAccessor.HttpContext!.SignOutAsync(
                CookieAuthenticationDefaults.AuthenticationScheme
            );
        }

        public async Task RegisterAsync(UserProfile user)
        {
            var userExists = await _userCollection.Find(user => user.Email == user.Email).FirstOrDefaultAsync();
            if (userExists != null)
            {
                user.Password = BCrypt.Net.BCrypt.HashPassword(user.Password, BCrypt.Net.BCrypt.GenerateSalt());
                await _userCollection.InsertOneAsync(user);
            }
        }
        private bool VerifyPassword(string enteredPassword, string hashedPassword) 
            => BCrypt.Net.BCrypt.Verify(enteredPassword, hashedPassword);
    }
}
