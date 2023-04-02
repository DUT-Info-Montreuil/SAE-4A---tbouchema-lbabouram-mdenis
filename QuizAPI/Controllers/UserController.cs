using System;
using Microsoft.AspNetCore.Mvc;
using QuizAPI.Services;
using QuizAPI.Models;
using ImageMagick;

namespace QuizAPI.Controllers
{
    [Controller]
    [Route("api/[controller]")]
    public class UserController : Controller
    {
       private readonly MongoDBUserService _mongoDBUserService;
       private readonly IWebHostEnvironment _env;

       public UserController(MongoDBUserService mongoDBUserService, IWebHostEnvironment env)
       {
            _mongoDBUserService = mongoDBUserService;
            _env = env;
       }

       [HttpGet]
       public async Task<List<UserProfile>> GetAllUsers() {
           return await _mongoDBUserService.GetAllUsersAsync();
       }

       [HttpGet("{id}")]
       public async Task<UserProfile> GetUserById(string id) {
           return await _mongoDBUserService.GetUserByIdAsync(id);
       }

       [HttpGet("bypseudo/{Pseudo}")]
       public async Task<UserProfile> GetUserByPseudo(string Pseudo) {
           return await _mongoDBUserService.GetUserByPseudoAsync(Pseudo);
       }

       [HttpPost]
       public async Task<IActionResult> PostUser([FromBody] UserProfile userProfile) {
           await _mongoDBUserService.CreateUserAsync(userProfile);
           return CreatedAtAction(nameof(GetAllUsers), new { id = userProfile.Id }, userProfile); // Information about the created resource
       }

       [HttpPost]
       [Route("uploadProfilePicture")]
       public async Task<IActionResult> uploadProfilePicture() {
            try
            {
                IFormFile file = Request.Form.Files[0];
                if (Request.Form.Files.Count == 0)
                {
                    return BadRequest("No file was uploaded");
                }

                if (!Request.Form.ContainsKey("id"))
                {
                    return BadRequest("No id was provided");
                }
                
                string fileExtension = Path.GetExtension(file.FileName);

                if (fileExtension != ".jpg" && fileExtension != ".png" && fileExtension != ".jpeg")
                {
                    return BadRequest("Only .jpg, .jpeg and .png files are allowed");
                }

                if (file.Length > 1000000)
                {
                    return BadRequest("File size must be less than 1MB");
                }

                MagickImage image = new MagickImage(file.OpenReadStream());
                image.Strip();
                
                if (image.Width > 64 || image.Height > 64 || image.Width < 64 || image.Height < 64)
                {
                    //return BadRequest("Image must be 64x64 pixels");
                }

                string imagePath = Path.Combine(_env.ContentRootPath, "Ressources\\profilePictures\\", Guid.NewGuid().ToString() + fileExtension);

                if (System.IO.File.Exists(imagePath))
                {
                    System.IO.File.Delete(imagePath);
                }

                var user = await _mongoDBUserService.GetUserByIdAsync(Request.Form["id"]);

                if (user.ProfilePicture != null && System.IO.File.Exists(user.ProfilePicture))
                {
                    System.IO.File.Delete(user.ProfilePicture);
                }

                image.Write(imagePath);

                await _mongoDBUserService.UpdateUserProfilePictureAsync(Request.Form["id"], imagePath);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
            return Ok("Image uploaded successfully");
       }

       [HttpPut("score/{id}")]
       public async Task<IActionResult> PutUserScore(string id, int score) {
           await _mongoDBUserService.UpdateUserScoreAsync(id, score);
           return NoContent();
       }

       [HttpPut("addScore/{id}")]
       public async Task<IActionResult> PutUserAddScore(string id, int score) {
           await _mongoDBUserService.UpdateAddUserScoreAsync(id, score);
           return NoContent();
       }

       [HttpPut("subtractScore/{id}")]
       public async Task<IActionResult> PutUserSubtractScore(string id, int score) {
           await _mongoDBUserService.UpdateSubtractUserScoreAsync(id, score);
           return NoContent();
       }

       [HttpPut("addFavQuiz/{id}")]
       public async Task<IActionResult> PutUserFavQuiz(string id, string favQuiz) {
           await _mongoDBUserService.UpdateAddUserFavQuizAsync(id, favQuiz);
           return NoContent();
       }

       [HttpPut("removeFavQuiz/{id}")]
       public async Task<IActionResult> PutUserRemoveFavQuiz(string id, string favQuiz) {
           await _mongoDBUserService.UpdateRemoveUserFavQuizAsync(id, favQuiz);
           return NoContent();
       }

       [HttpPut("addCreatedQuiz/{id}")]
       public async Task<IActionResult> PutUserCreatedQuiz(string id, string createdQuiz) {
           await _mongoDBUserService.UpdateUserCreatedQuizzesAsync(id, createdQuiz);
           return NoContent();
       }

       [HttpPut("addPlayedQuiz/{id}")]
       public async Task<IActionResult> PutUserPlayedQuiz(string id, string playedQuiz) {
           await _mongoDBUserService.UpdateUserPlayedQuizzesAsync(id, playedQuiz);
           return NoContent();
       }

       [HttpPut("updateProfilePicture/{id}")]
       public async Task<IActionResult> PutUserUpdateProfilePicture(string id, string profilePicture) {
           await _mongoDBUserService.UpdateUserProfilePictureAsync(id, profilePicture);
           return NoContent();
       }

       [HttpPut("updatePseudo/{id}")]
       public async Task<IActionResult> PutUserUpdatePseudo(string id, string pseudo) {
           await _mongoDBUserService.UpdateUserPseudoAsync(id, pseudo);
           return NoContent();
       }

       [HttpPut("updateEmail/{id}")]
       public async Task<IActionResult> PutUserUpdateEmail(string id, string email) {
           await _mongoDBUserService.UpdateUserEmailAsync(id, email);
           return NoContent();
       }

       [HttpPut("updatePassword/{id}")]
       public async Task<IActionResult> PutUserUpdatePassword(string id, string password) {
           await _mongoDBUserService.UpdateUserPasswordAsync(id, password);
           return NoContent();
       }

       [HttpDelete("{id}")]
       public async Task<IActionResult> DeleteUser(string id) {
           await _mongoDBUserService.DeleteUserAsync(id);
           return NoContent();
       }
    }
}
