using System;
using QuizAPI.Models;
using MongoDB.Driver;
using Microsoft.Extensions.Options;
using MongoDB.Bson;

namespace QuizAPI.Services
{
    public class MongoDBQuizService
    {
        private readonly IMongoCollection<QuizInfo> _quizCollection;
        private List<String> _quizIds = new List<String>();

        public MongoDBQuizService(IOptions<MongoDBSettings> mongoDBSettings)
        {
            MongoClient client = new MongoClient(mongoDBSettings.Value.ConnectionURL);
            IMongoDatabase database = client.GetDatabase(mongoDBSettings.Value.DatabaseName);
            _quizCollection = database.GetCollection<QuizInfo>(mongoDBSettings.Value.QuizCollectionName);
        }

        public async Task CreateQuizAsync(QuizInfo quiz)
        {
            quiz.QuizCreationDate = DateOnly.FromDateTime(DateTime.Now);
            await _quizCollection.InsertOneAsync(quiz);
            return;
        }

        public async Task UpdateQuizNameAsync(string id, string name)
            => await _quizCollection.UpdateOneAsync(quiz => quiz.Id == id, Builders<QuizInfo>.Update.Set("QuizName", name));

        public async Task UpdateQuizDescriptionAsync(string id, string description)
            => await _quizCollection.UpdateOneAsync(quiz => quiz.Id == id, Builders<QuizInfo>.Update.Set("QuizDescription", description));

        public async Task UpdateQuizQuestionnaryAsync(string id, QuizQuestionnary questionnary)
            => await _quizCollection.UpdateOneAsync(quiz => quiz.Id == id, Builders<QuizInfo>.Update.Set("QuizQuestionnary", questionnary));

        public async Task<List<QuizInfo>> GetAsync()
            => await _quizCollection.Find(new BsonDocument()).ToListAsync();

        public async Task<QuizInfo> GetQuizInfoAsync(string id)
            => await _quizCollection.Find(quiz => quiz.Id == id).FirstOrDefaultAsync();

        public async Task<QuizQuestionnary> GetQuizQuestionnaryAsync(string id)
            => await _quizCollection.Find(quiz => quiz.Id == id).Project(quiz => quiz.QuizQuestionnary).FirstOrDefaultAsync();

        public async Task<List<QuizInfo>> GetRandomQuizzesAsync()
        {
            if (_quizIds.Count == 0)
                _quizIds = await _quizCollection.Find(new BsonDocument()).Project(quiz => quiz.Id).ToListAsync();

            List<QuizInfo> randomQuizzes = new List<QuizInfo>();
            Random random = new Random();
            int nbQuiz = _quizIds.Count <= 3 ? _quizIds.Count : 3; // Get 3 random quizzes or less if there are less than 3 quizzes
            for (int i = 0; i < nbQuiz; i++)
            {
                int randomIndex = random.Next(0, _quizIds.Count);
                randomQuizzes.Add(await _quizCollection.Find(quiz => quiz.Id == _quizIds[randomIndex]).FirstOrDefaultAsync());
                _quizIds.RemoveAt(randomIndex);
            }
            return randomQuizzes;
        }

        public async Task DeleteAsync(string id)
            => await _quizCollection.DeleteOneAsync(quiz => quiz.Id == id);
    }
}
