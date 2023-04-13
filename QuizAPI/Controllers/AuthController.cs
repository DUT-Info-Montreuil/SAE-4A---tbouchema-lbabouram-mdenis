using System;
using Microsoft.AspNetCore.Mvc;
using QuizAPI.Services;
using QuizAPI.Models;
using Microsoft.AspNetCore.Authorization;

namespace QuizAPI.Controllers
{
    [Controller]
    [Route("api/[controller]")]
    [AllowAnonymous]
    public class AuthController : Controller
    {
        private readonly MongoDBAuthService _mongoDBAuthService;
        private readonly MongoDBUserService _mongoDBUserService;

        public AuthController(MongoDBAuthService mongoDBAuthService, MongoDBUserService mongoDBUserService)
        {
            _mongoDBAuthService = mongoDBAuthService;
            _mongoDBUserService = mongoDBUserService;
        }

        [HttpGet("ping")]
        [AllowAnonymous]
        public String ping()
        {
            return "pong";
        }

        [HttpGet("denied")]
        [AllowAnonymous]
        public IActionResult AccessDenied()
        {
            return Forbid();
        }

        [HttpPost("login")]
        [AllowAnonymous]
        public async Task<ActionResult<UserProfile>> Login(string login, string password)
        {
            var user = await _mongoDBAuthService.LoginAsync(login, password);
            if (user == null)
            {
                return Unauthorized();
            }
            return user;
        }

        [HttpPost("logout")]
        [AllowAnonymous]
        public async Task<IActionResult> Logout()
        {
            await _mongoDBAuthService.LogoutAsync();
            return Ok();
        }

        [HttpPost("register")]
        [AllowAnonymous]
        public async Task<ActionResult> Register([FromBody] UserProfile user)
        {
            UserProfile isExisting = await _mongoDBUserService.GetUserByLoginAsync(user.Email!);
            if (isExisting != null)
            {
                return BadRequest("This email is already used");
            }

            await _mongoDBAuthService.RegisterAsync(user);

            var authResult = await _mongoDBAuthService.LoginAsync(user.Email!, user.Password!);
            return Ok(authResult);
        }

        [HttpPut("updatePassword/{elementid}")]
        [Authorize(Policy = "RequireOwnershipOnly")]
        public async Task<IActionResult> PutUserUpdatePassword([FromRoute] string elementid, string password)
        {
            await _mongoDBAuthService.UpdateUserPasswordAsync(elementid, password);
            return Ok();
        }
    }
}
