using System;

namespace QuizAPI.Models
{
    public class MongoDBSettings
    {
        public string ConnectionURL { get; set; } = "mongodb://localhost:27017";
        public string DatabaseName { get; set; } = "SAEQuizDatabase";
        public string QuizCollectionName { get; set; } = "Quiz";
        public string UserCollectionName { get; set; } = "User";

    }
}
