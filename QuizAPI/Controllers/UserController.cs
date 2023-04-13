using System;
using Microsoft.AspNetCore.Mvc;
using QuizAPI.Services;
using QuizAPI.Models;
using ImageMagick;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Authorization;

namespace QuizAPI.Controllers
{
    [Controller]
    [Route("api/[controller]")]
    [Authorize(AuthenticationSchemes = CookieAuthenticationDefaults.AuthenticationScheme)]
    public class UserController : Controller
    {
        private readonly MongoDBUserService _mongoDBUserService;
        private readonly MongoDBAuthService _mongoDBAuthService;
        private readonly IWebHostEnvironment _env;

        public UserController(MongoDBUserService mongoDBUserService, MongoDBAuthService mongoDBAuthService, IWebHostEnvironment env)
        {
            _mongoDBUserService = mongoDBUserService;
            _mongoDBAuthService = mongoDBAuthService;
            _env = env;
        }

        [HttpGet]
        public async Task<List<UserProfile>> GetAllUsers()
        {
            return await _mongoDBUserService.GetAllUsersAsync();
        }

        [HttpGet("byid/{elementId}")]
        [Authorize]
        public async Task<UserProfile> GetUserById([FromRoute] string elementId)
        {
            return await _mongoDBUserService.GetUserByIdAsync(elementId);
        }

        [HttpGet("bylogin/{Login}")]
        [Authorize]
        public async Task<UserProfile> GetUserByPseudo([FromRoute] string Login)
        {
            return await _mongoDBUserService.GetUserByLoginAsync(Login);
        }

        [HttpGet("userProfilePicture/{elementId}")]
        [Authorize]
        public async Task<IActionResult> GetUserProfilePicture([FromRoute] string elementId)
        {
            var user = await _mongoDBUserService.GetUserByIdAsync(elementId);

            if (user.ProfilePicture == null)
            {
                return NotFound();
            }

            return PhysicalFile(user.ProfilePicture, "image/jpeg");
        }

        [HttpPut("updateProfilePicture/{elementId}")]
        [Authorize(Policy = "RequireOwnershipOnly")]
        public async Task<IActionResult> PutUserUpdateProfilePicture([FromRoute] string elementId)
        {
            try
            {
                IFormFile file = Request.Form.Files[0];
                if (Request.Form.Files.Count == 0)
                {
                    return BadRequest("No file was uploaded");
                }

                if (!Request.Form.ContainsKey("userid"))
                {
                    return BadRequest("The id of the requesting user was not provided");
                }

                if (!Request.Form.ContainsKey("elementId"))
                {
                    return BadRequest("The id of the user to update was not provided");
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

                var user = await _mongoDBUserService.GetUserByIdAsync(elementId);

                if (user.ProfilePicture != null && System.IO.File.Exists(user.ProfilePicture))
                {
                    System.IO.File.Delete(user.ProfilePicture);
                }

                image.Write(imagePath);

                await _mongoDBUserService.UpdateUserProfilePictureAsync(elementId, imagePath);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
            return Ok("Image uploaded successfully");
        }

        [HttpPut("score/{elementId}")]
        [Authorize(Policy = "RequireOwnershipOrAdmin")]
        public async Task<IActionResult> PutUserScore([FromRoute] string elementId, int score)
        {
            await _mongoDBUserService.UpdateUserScoreAsync(elementId, score);
            return NoContent();
        }

        [HttpPut("addScore/{elementId}")]
        [Authorize(Policy = "RequireOwnershipOrAdmin")]
        public async Task<IActionResult> PutUserAddScore([FromRoute] string elementId, int score)
        {
            await _mongoDBUserService.UpdateAddUserScoreAsync(elementId, score);
            return NoContent();
        }

        [HttpPut("subtractScore/{elementId}")]
        [Authorize(Policy = "RequireOwnershipOrAdmin")]
        public async Task<IActionResult> PutUserSubtractScore([FromRoute] string elementId, int score)
        {
            await _mongoDBUserService.UpdateSubtractUserScoreAsync(elementId, score);
            return NoContent();
        }

        [HttpPut("addFavQuiz/{elementId}")]
        [Authorize(Policy = "RequireOwnershipOnly")]
        public async Task<IActionResult> PutUserFavQuiz([FromRoute] string elementId, string favQuiz)
        {
            await _mongoDBUserService.UpdateAddUserFavQuizAsync(elementId, favQuiz);
            return NoContent();
        }

        [HttpPut("removeFavQuiz/{elementId}")]
        [Authorize(Policy = "RequireOwnershipOnly")]
        public async Task<IActionResult> PutUserRemoveFavQuiz([FromRoute] string elementId, string favQuiz)
        {
            await _mongoDBUserService.UpdateRemoveUserFavQuizAsync(elementId, favQuiz);
            return NoContent();
        }

        [HttpPut("addCreatedQuiz/{elementId}")]
        [Authorize(Policy = "RequireOwnershipOnly")]
        public async Task<IActionResult> PutUserCreatedQuiz([FromRoute] string elementId, string createdQuiz)
        {
            await _mongoDBUserService.UpdateUserCreatedQuizzesAsync(elementId, createdQuiz);
            return NoContent();
        }

        [HttpPut("addPlayedQuiz/")]
        [Authorize(Policy = "RequireOwnershipOnly")]
        public async Task<IActionResult> PutUserPlayedQuiz([FromRoute] string elementId, string playedQuiz)
        {
            await _mongoDBUserService.UpdateUserPlayedQuizzesAsync(elementId, playedQuiz);
            return NoContent();
        }

        [HttpPut("updateUsername/{elementId}")]
        [Authorize(Policy = "RequireOwnershipOnly")]
        public async Task<IActionResult> PutUserUpdatePseudo([FromRoute] string elementId, string username)
        {
            await _mongoDBUserService.UpdateUserPseudoAsync(elementId, username);
            return NoContent();
        }

        [HttpPut("updateEmail/{elementId}")]
        [Authorize(Policy = "RequireOwnershipOnly")]
        public async Task<IActionResult> PutUserUpdateEmail([FromRoute] string elementId, string email)
        {
            await _mongoDBUserService.UpdateUserEmailAsync(elementId, email);
            return NoContent();
        }

        [HttpDelete("delete/{elementId}")]
        [Authorize(Policy = "RequireOwnershipOrAdmin")]
        public async Task<IActionResult> DeleteUser([FromRoute] string elementId)
        {
            await _mongoDBUserService.DeleteUserAsync(elementId);
            return NoContent();
        }
    }
}
