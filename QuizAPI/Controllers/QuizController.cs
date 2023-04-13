using System;
using Microsoft.AspNetCore.Mvc;
using QuizAPI.Services;
using QuizAPI.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Authentication.Cookies;

namespace QuizAPI.Controllers
{
    [Controller]
    [Route("api/[controller]")]
    [Authorize(AuthenticationSchemes = CookieAuthenticationDefaults.AuthenticationScheme)]
    public class QuizController : Controller
    {
        private readonly MongoDBQuizService _mongoDBQuizService;
        private readonly MongoDBAuthService _mongoDBAuthService;

        public QuizController(MongoDBQuizService mongoDBQuizService, MongoDBAuthService mongoDBAuthService)
        {
            _mongoDBQuizService = mongoDBQuizService;
            _mongoDBAuthService = mongoDBAuthService;
        }

        [HttpGet]
        [AllowAnonymous]
        public async Task<List<QuizInfo>> GetAll()
        {
            return await _mongoDBQuizService.GetAsync();
        }

        [HttpGet("byid/{elementId}")]
        [AllowAnonymous]
        public async Task<QuizInfo> GetQuizInfo([FromRoute] string elementId)
        {
            return await _mongoDBQuizService.GetQuizInfoAsync(elementId);
        }

        [HttpGet("questionnary/{elementId}")]
        [AllowAnonymous]
        public async Task<QuizQuestionnary> GetQuizQuestionnary([FromRoute] string elementId)
        {
            return await _mongoDBQuizService.GetQuizQuestionnaryAsync(elementId);
        }

        [HttpGet("random/")]
        [AllowAnonymous]
        public async Task<List<QuizInfo>> GetRandomQuizzes()
        {
            return await _mongoDBQuizService.GetRandomQuizzesAsync();
        }

        [HttpPost]
        [Authorize]
        public async Task<IActionResult> PostQuiz([FromBody] QuizInfo quizInfo)
        {
            await _mongoDBQuizService.CreateQuizAsync(quizInfo);
            return CreatedAtAction(nameof(GetAll), new { id = quizInfo.Id }, quizInfo); // Information about the created resource
        }

        [HttpPut("name/{elementId}")]
        [Authorize(Policy = "RequireOwnershipOrAdmin")]
        public async Task<IActionResult> PutQuizName([FromRoute] string elementId, string quizName)
        {
            await _mongoDBQuizService.UpdateQuizNameAsync(elementId, quizName);
            return NoContent();
        }

        [HttpPut("description/{elementId}")]
        [Authorize(Policy = "RequireOwnershipOrAdmin")]
        public async Task<IActionResult> PutQuizDescription([FromRoute] string elementId, string quizDescription)
        {
            await _mongoDBQuizService.UpdateQuizDescriptionAsync(elementId, quizDescription);
            return NoContent();
        }

        [HttpPut("questionnary/{elementId}")]
        [Authorize(Policy = "RequireOwnershipOrAdmin")]
        public async Task<IActionResult> PatchQuizQuestionnary([FromRoute] string elementId, [FromBody] QuizQuestionnary quizQuestionnary)
        {
            await _mongoDBQuizService.UpdateQuizQuestionnaryAsync(elementId, quizQuestionnary);
            return NoContent();
        }

        [HttpDelete("byelementId/{elementId}")]
        [Authorize(Policy = "RequireOwnershipOrAdmin")]
        public async Task<IActionResult> Delete([FromRoute] string elementId)
        {
            await _mongoDBQuizService.DeleteAsync(elementId);
            return NoContent();
        }
    }
}
