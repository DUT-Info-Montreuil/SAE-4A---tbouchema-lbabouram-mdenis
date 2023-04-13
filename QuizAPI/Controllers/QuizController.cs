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

        [Authorize]
        [HttpGet("ping")]
        public String ping()
        {
            return "pong";
        }

        [HttpGet]
        public async Task<List<QuizInfo>> GetAll()
        {
            return await _mongoDBQuizService.GetAsync();
        }

        [HttpGet("byid/{elementid}")]
        public async Task<QuizInfo> GetQuizInfo([FromRoute] string elementId)
        {
            return await _mongoDBQuizService.GetQuizInfoAsync(elementId);
        }

        [HttpGet("questionnary/{elementid}")]
        public async Task<QuizQuestionnary> GetQuizQuestionnary([FromRoute] string elementId)
        {
            return await _mongoDBQuizService.GetQuizQuestionnaryAsync(elementId);
        }

        [HttpGet("random/")]
        public async Task<List<QuizInfo>> GetRandomQuizzes()
        {
            return await _mongoDBQuizService.GetRandomQuizzesAsync();
        }

        [HttpPost]
        public async Task<IActionResult> PostQuiz([FromBody] QuizInfo quizInfo)
        {
            await _mongoDBQuizService.CreateQuizAsync(quizInfo);
            return CreatedAtAction(nameof(GetAll), new { id = quizInfo.Id }, quizInfo); // Information about the created resource
        }

        // [HttpPut("name")]
        // public async Task<IActionResult> PutQuizName(string userid, string elementId, string quizName)
        // {
        //     if (!await _mongoDBAuthService.VerifyPrivilegesQuizAsync(userid, elementId))
        //         return Unauthorized();

        //     await _mongoDBQuizService.UpdateQuizNameAsync(elementId, quizName);
        //     return NoContent();
        // }

        [HttpPut("name")]
        [Authorize(Policy = "ModifyElement")]
        public async Task<IActionResult> PutQuizName(string elementId, string quizName)
        {
            await _mongoDBQuizService.UpdateQuizNameAsync(elementId, quizName);
            return NoContent();
        }

        [HttpPut("description")]
        public async Task<IActionResult> PutQuizDescription(string userid, string elementId, string quizDescription)
        {
            if (!await _mongoDBAuthService.VerifyPrivilegesQuizAsync(userid, elementId))
                return Unauthorized();

            await _mongoDBQuizService.UpdateQuizDescriptionAsync(elementId, quizDescription);
            return NoContent();
        }

        [HttpPut("questionnary")]
        public async Task<IActionResult> PatchQuizQuestionnary(string userid, string elementId, [FromBody] QuizQuestionnary quizQuestionnary)
        {
            if (!await _mongoDBAuthService.VerifyPrivilegesQuizAsync(userid, elementId))
                return Unauthorized();

            await _mongoDBQuizService.UpdateQuizQuestionnaryAsync(elementId, quizQuestionnary);
            return NoContent();
        }

        [HttpDelete("byelementid")]
        public async Task<IActionResult> Delete(string userid, string elementId)
        {
            if (!await _mongoDBAuthService.VerifyPrivilegesQuizAsync(userid, elementId))
                return Unauthorized();

            await _mongoDBQuizService.DeleteAsync(elementId);
            return NoContent();
        }
    }
}
