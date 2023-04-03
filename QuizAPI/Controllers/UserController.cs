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

        [HttpGet("byid/{elementid}")]
        public async Task<UserProfile> GetUserById([FromRoute] string elementid)
        {
            return await _mongoDBUserService.GetUserByIdAsync(elementid);
        }

        [HttpGet("bylogin/{Login}")]
        public async Task<UserProfile> GetUserByPseudo([FromRoute] string Login)
        {
            return await _mongoDBUserService.GetUserByLoginAsync(Login);
        }

        [HttpGet("userProfilePicture/{elementid}")]
        public async Task<IActionResult> GetUserProfilePicture([FromRoute] string elementid)
        {
            var user = await _mongoDBUserService.GetUserByIdAsync(elementid);

            if (user.ProfilePicture == null)
            {
                return NotFound();
            }

            return PhysicalFile(user.ProfilePicture, "image/jpeg");
        }

        [HttpPost]
        public async Task<IActionResult> PostUser([FromBody] UserProfile userProfile)
        {
            await _mongoDBUserService.CreateUserAsync(userProfile);
            return CreatedAtAction(nameof(GetAllUsers), new { id = userProfile.Id }, userProfile); // Information about the created resource
        }

        [HttpPut("updateProfilePicture")]
        public async Task<IActionResult> PutUserUpdateProfilePicture(string userid, string elementid)
        {
            if (!await _mongoDBAuthService.VerifyPrivilegesPersonnalInfoAsync(userid, elementid))
                return Unauthorized();

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

                if (!Request.Form.ContainsKey("elementid"))
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

                var user = await _mongoDBUserService.GetUserByIdAsync(elementid);

                if (user.ProfilePicture != null && System.IO.File.Exists(user.ProfilePicture))
                {
                    System.IO.File.Delete(user.ProfilePicture);
                }

                image.Write(imagePath);

                await _mongoDBUserService.UpdateUserProfilePictureAsync(elementid, imagePath);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
            return Ok("Image uploaded successfully");
        }

        [HttpPut("score/")]
        public async Task<IActionResult> PutUserScore(string userid, string elementid, int score)
        {
            if (!await _mongoDBAuthService.VerifyPrivilegesUserAsync(userid, elementid))
                return Unauthorized();

            await _mongoDBUserService.UpdateUserScoreAsync(elementid, score);
            return NoContent();
        }

        [HttpPut("addScore/")]
        public async Task<IActionResult> PutUserAddScore(string userid, string elementid, int score)
        {
            if (!await _mongoDBAuthService.VerifyPrivilegesUserAsync(userid, elementid))
                return Unauthorized();

            await _mongoDBUserService.UpdateAddUserScoreAsync(elementid, score);
            return NoContent();
        }

        [HttpPut("subtractScore/")]
        public async Task<IActionResult> PutUserSubtractScore(string userid, string elementid, int score)
        {
            if (!await _mongoDBAuthService.VerifyPrivilegesUserAsync(userid, elementid))
                return Unauthorized();

            await _mongoDBUserService.UpdateSubtractUserScoreAsync(elementid, score);
            return NoContent();
        }

        [HttpPut("addFavQuiz/")]
        public async Task<IActionResult> PutUserFavQuiz(string userid, string elementid, string favQuiz)
        {
            if (!await _mongoDBAuthService.VerifyPrivilegesUserAsync(userid, elementid))
                return Unauthorized();

            await _mongoDBUserService.UpdateAddUserFavQuizAsync(elementid, favQuiz);
            return NoContent();
        }

        [HttpPut("removeFavQuiz/")]
        public async Task<IActionResult> PutUserRemoveFavQuiz(string userid, string elementid, string favQuiz)
        {
            if (!await _mongoDBAuthService.VerifyPrivilegesUserAsync(userid, elementid))
                return Unauthorized();

            await _mongoDBUserService.UpdateRemoveUserFavQuizAsync(elementid, favQuiz);
            return NoContent();
        }

        [HttpPut("addCreatedQuiz/")]
        public async Task<IActionResult> PutUserCreatedQuiz(string userid, string elementid, string createdQuiz)
        {
            if (!await _mongoDBAuthService.VerifyPrivilegesUserAsync(userid, elementid))
                return Unauthorized();

            await _mongoDBUserService.UpdateUserCreatedQuizzesAsync(elementid, createdQuiz);
            return NoContent();
        }

        [HttpPut("addPlayedQuiz/")]
        public async Task<IActionResult> PutUserPlayedQuiz(string userid, string elementid, string playedQuiz)
        {
            if (!await _mongoDBAuthService.VerifyPrivilegesUserAsync(userid, elementid))
                return Unauthorized();
                
            await _mongoDBUserService.UpdateUserPlayedQuizzesAsync(elementid, playedQuiz);
            return NoContent();
        }

        [HttpPut("updateUsername/")]
        public async Task<IActionResult> PutUserUpdatePseudo(string userid, string elementid, string username)
        {
            if (!await _mongoDBAuthService.VerifyPrivilegesPersonnalInfoAsync(userid, elementid))
                return Unauthorized();

            await _mongoDBUserService.UpdateUserPseudoAsync(elementid, username);
            return NoContent();
        }

        [HttpPut("updateEmail/")]
        public async Task<IActionResult> PutUserUpdateEmail(string userid, string elementid, string email)
        {
            if (!await _mongoDBAuthService.VerifyPrivilegesPersonnalInfoAsync(userid, elementid))
                return Unauthorized();

            await _mongoDBUserService.UpdateUserEmailAsync(elementid, email);
            return NoContent();
        }

        [HttpPut("updatePassword/")]
        public async Task<IActionResult> PutUserUpdatePassword(string userid, string elementid, string password)
        {
            if (!await _mongoDBAuthService.VerifyPrivilegesPersonnalInfoAsync(userid, elementid))
                return Unauthorized();

            await _mongoDBUserService.UpdateUserPasswordAsync(elementid, password);
            return NoContent();
        }

        [HttpDelete("")]
        public async Task<IActionResult> DeleteUser(string userid, string elementid)
        {
            if (!await _mongoDBAuthService.VerifyPrivilegesUserAsync(userid, elementid))
                return Unauthorized();

            await _mongoDBUserService.DeleteUserAsync(elementid);
            return NoContent();
        }
    }
}
