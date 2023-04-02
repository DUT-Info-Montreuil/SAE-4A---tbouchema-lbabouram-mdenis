using System;
using Microsoft.AspNetCore.Mvc;
using QuizAPI.Services;
using QuizAPI.Models;

namespace QuizAPI.Controllers
{
    [Controller]
    [Route("api/[controller]")]
    public class QuizController : Controller
    {
        private readonly MongoDBQuizService _mongoDBQuizService;
        
        public QuizController(MongoDBQuizService mongoDBQuizService)
        {
            _mongoDBQuizService = mongoDBQuizService;
        }

        [HttpGet]
        public async Task<List<QuizInfo>> GetAll() {
            return await _mongoDBQuizService.GetAsync();
        }

        [HttpGet("{id}")]
        public async Task<QuizInfo> GetQuizInfo(string id) {
            return await _mongoDBQuizService.GetQuizInfoAsync(id);
        }

        [HttpGet("questionnary/{id}")]
        public async Task<QuizQuestionnary> GetQuizQuestionnary(string id) {
            return await _mongoDBQuizService.GetQuizQuestionnaryAsync(id);
        }

        [HttpPost]
        public async Task<IActionResult> PostQuiz([FromBody] QuizInfo quizInfo) {
            await _mongoDBQuizService.CreateQuizAsync(quizInfo);
            return CreatedAtAction(nameof(GetAll), new { id = quizInfo.Id }, quizInfo); // Information about the created resource
        }

        [HttpPut("name/{id}")]
        public async Task<IActionResult> PutQuizName(string id, string quizName) {
            await _mongoDBQuizService.UpdateQuizNameAsync(id, quizName);
            return NoContent();
        }

        [HttpPut("description/{id}")]
        public async Task<IActionResult> PutQuizDescription(string id, string quizDescription) {
            await _mongoDBQuizService.UpdateQuizDescriptionAsync(id, quizDescription);
            return NoContent();
        }

        [HttpPatch("questionnary/{id}")]
        public async Task<IActionResult> PatchQuizQuestionnary(string id, [FromBody] QuizQuestionnary quizQuestionnary) {
            await _mongoDBQuizService.UpdateQuizQuestionnaryAsync(id, quizQuestionnary);
            return NoContent();
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(string id) {
            await _mongoDBQuizService.DeleteAsync(id);
            return NoContent();
        }
    }
}
