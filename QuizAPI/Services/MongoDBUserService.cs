using System;
using QuizAPI.Models;
using MongoDB.Driver;
using Microsoft.Extensions.Options;
using MongoDB.Bson;

namespace QuizAPI.Services
{
    public class MongoDBUserService
    {
        private readonly IMongoCollection<UserProfile> _userCollection;

        public MongoDBUserService(IOptions<MongoDBSettings> mongoDBSettings)
        {
            MongoClient client = new MongoClient(mongoDBSettings.Value.ConnectionURL);
            IMongoDatabase database = client.GetDatabase(mongoDBSettings.Value.DatabaseName);
            _userCollection = database.GetCollection<UserProfile>(mongoDBSettings.Value.UserCollectionName);
        }

        public async Task UpdateUserScoreAsync(string id, int score)
            => await _userCollection.UpdateOneAsync(user => user.Id == id, Builders<UserProfile>.Update.Set("Score", score));

        public async Task UpdateAddUserScoreAsync(string id, int score)
            => await _userCollection.UpdateOneAsync(user => user.Id == id, Builders<UserProfile>.Update.Inc("Score", score));

        public async Task UpdateSubtractUserScoreAsync(string id, int score)
            => await _userCollection.UpdateOneAsync(user => user.Id == id, Builders<UserProfile>.Update.Inc("Score", -score));

        public async Task UpdateAddUserFavQuizAsync(string id, string favQuiz)
            => await _userCollection.UpdateOneAsync(user => user.Id == id, Builders<UserProfile>.Update.AddToSet("FavQuizzes", favQuiz));
        public async Task UpdateRemoveUserFavQuizAsync(string id, string favQuiz)
            => await _userCollection.UpdateOneAsync(user => user.Id == id, Builders<UserProfile>.Update.Pull("FavQuizzes", favQuiz));

        public async Task UpdateUserProfilePictureAsync(string id, string profilePicture)
            => await _userCollection.UpdateOneAsync(user => user.Id == id, Builders<UserProfile>.Update.Set("ProfilePicture", profilePicture));

        public async Task UpdateUserCreatedQuizzesAsync(string id, string createdQuiz)
            => await _userCollection.UpdateOneAsync(user => user.Id == id, Builders<UserProfile>.Update.AddToSet("CreatedQuizzes", createdQuiz));

        public async Task UpdateUserPlayedQuizzesAsync(string id, string playedQuiz)
            => await _userCollection.UpdateOneAsync(user => user.Id == id, Builders<UserProfile>.Update.AddToSet("PlayedQuizzes", playedQuiz));

        public async Task UpdateUserPseudoAsync(string id, string pseudo)
            => await _userCollection.UpdateOneAsync(user => user.Id == id, Builders<UserProfile>.Update.Set("Pseudo", pseudo));

        public async Task UpdateUserEmailAsync(string id, string email)
            => await _userCollection.UpdateOneAsync(user => user.Id == id, Builders<UserProfile>.Update.Set("Email", email));

        public async Task<List<UserProfile>> GetAllUsersAsync()
            => await _userCollection.Find(new BsonDocument()).ToListAsync();

        public async Task<UserProfile> GetUserByIdAsync(string id)
            => await _userCollection.Find(user => user.Id == id).FirstOrDefaultAsync();

        public async Task<UserProfile> GetUserByLoginAsync(string login)
            => await _userCollection.Find(user => user.Pseudo == login || user.Email == login).FirstOrDefaultAsync();

        public async Task DeleteUserAsync(string id)
            => await _userCollection.DeleteOneAsync(user => user.Id == id);
    }
}
