using System;
using QuizAPI.Models;
using MongoDB.Driver;
using Microsoft.Extensions.Options;
using MongoDB.Bson;
using BCrypt.Net;

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

        public async Task CreateUserAsync(UserProfile user)
        {
            string salt = BCrypt.Net.BCrypt.GenerateSalt();
            user.Password = BCrypt.Net.BCrypt.HashPassword(user.Password, salt);
            
            await _userCollection.InsertOneAsync(user);
            return;
        }

        public async Task UpdateUserScoreAsync(string id, int score)
        {
            await _userCollection.UpdateOneAsync(user => user.Id == id, Builders<UserProfile>.Update.Set("Score", score));
            return;
        }

        public async Task UpdateAddUserScoreAsync(string id, int score)
        {
            await _userCollection.UpdateOneAsync(user => user.Id == id, Builders<UserProfile>.Update.Inc("Score", score));
            return;
        }

        public async Task UpdateSubtractUserScoreAsync(string id, int score)
        {
            await _userCollection.UpdateOneAsync(user => user.Id == id, Builders<UserProfile>.Update.Inc("Score", -score));
            return;
        }

        public async Task UpdateAddUserFavQuizAsync(string id, string favQuiz)
        {
            await _userCollection.UpdateOneAsync(user => user.Id == id, Builders<UserProfile>.Update.AddToSet("FavQuizzes", favQuiz));
            return;
        }

        public async Task UpdateRemoveUserFavQuizAsync(string id, string favQuiz)
        {
            await _userCollection.UpdateOneAsync(user => user.Id == id, Builders<UserProfile>.Update.Pull("FavQuizzes", favQuiz));
            return;
        }

        public async Task UpdateUserProfilePictureAsync(string id, string profilePicture)
        {
            await _userCollection.UpdateOneAsync(user => user.Id == id, Builders<UserProfile>.Update.Set("ProfilePicture", profilePicture));
            return;
        }

        public async Task UpdateUserCreatedQuizzesAsync(string id, string createdQuiz)
        {
            await _userCollection.UpdateOneAsync(user => user.Id == id, Builders<UserProfile>.Update.AddToSet("CreatedQuizzes", createdQuiz));
            return;
        }

        public async Task UpdateUserPlayedQuizzesAsync(string id, string playedQuiz)
        {
            await _userCollection.UpdateOneAsync(user => user.Id == id, Builders<UserProfile>.Update.AddToSet("PlayedQuizzes", playedQuiz));
            return;
        }

        public async Task UpdateUserPseudoAsync(string id, string pseudo)
        {
            await _userCollection.UpdateOneAsync(user => user.Id == id, Builders<UserProfile>.Update.Set("Pseudo", pseudo));
            return;
        }

        public async Task UpdateUserPasswordAsync(string id, string password) {
            string salt = BCrypt.Net.BCrypt.GenerateSalt();
            string hashedPassword = BCrypt.Net.BCrypt.HashPassword(password, salt);
            await _userCollection.UpdateOneAsync(user => user.Id == id, Builders<UserProfile>.Update.Set("Password", hashedPassword));
            return;
        }

        public async Task UpdateUserEmailAsync(string id, string email)
        {
            await _userCollection.UpdateOneAsync(user => user.Id == id, Builders<UserProfile>.Update.Set("Email", email));
            return;
        }

        public async Task<List<UserProfile>> GetAllUsersAsync()
        {
            return await _userCollection.Find(new BsonDocument()).ToListAsync();
        }

        public async Task<UserProfile> GetUserByIdAsync(string id)
        {
            return await _userCollection.Find(user => user.Id == id).FirstOrDefaultAsync();
        }

        public async Task<UserProfile> GetUserByPseudoAsync(string pseudo)
        {
            return await _userCollection.Find(user => user.Pseudo == pseudo).FirstOrDefaultAsync();
        }

        public async Task DeleteUserAsync(string id)
        {
            await _userCollection.DeleteOneAsync(user => user.Id == id);
            return;
        }
    }
}
