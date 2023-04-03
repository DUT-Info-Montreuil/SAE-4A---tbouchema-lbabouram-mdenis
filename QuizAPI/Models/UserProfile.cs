using System;
using System.Text.Json.Serialization;
using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes; 

namespace QuizAPI.Models
{
    public class UserProfile
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string? Id { get; set; } = null!;
        public string? Pseudo { get; set; } = null!;
        public string? Email { get; set; } = null!;
        public string? Password { get; set; } = null!;
        public string? ProfilePicture { get; set; } = null!;
        public List<string> FavQuizzes { get; set; } = null!;
        public List<string> CreatedQuiz { get; set; } = null!;
        public List<string> PlayedQuiz { get; set; } = null!;
        public int Score { get; set; } = 0;
        public bool IsAdmin { get; set; } = false;
    }
}
