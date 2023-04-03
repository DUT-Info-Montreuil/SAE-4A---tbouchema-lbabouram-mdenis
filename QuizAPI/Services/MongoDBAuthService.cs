using System;
using QuizAPI.Models;
using MongoDB.Driver;
using Microsoft.Extensions.Options;
using MongoDB.Bson;

namespace QuizAPI.Services
{
    public class MongoDBAuthService
    {
        private readonly IMongoCollection<QuizInfo> _quizCollection;
        private readonly IMongoCollection<UserProfile> _userCollection;

        public MongoDBAuthService(IOptions<MongoDBSettings> mongoDBSettings)
        {
            MongoClient client = new MongoClient(mongoDBSettings.Value.ConnectionURL);
            IMongoDatabase database = client.GetDatabase(mongoDBSettings.Value.DatabaseName);
            _quizCollection = database.GetCollection<QuizInfo>(mongoDBSettings.Value.QuizCollectionName);
            _userCollection = database.GetCollection<UserProfile>(mongoDBSettings.Value.UserCollectionName);
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
    }
}
