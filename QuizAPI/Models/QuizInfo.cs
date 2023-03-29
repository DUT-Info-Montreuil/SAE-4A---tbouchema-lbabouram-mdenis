using System.Text.Json.Serialization;
using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace QuizAPI.Models
{
    public class QuizInfo
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string? Id { get; set; }
        public string QuizName { get; set; } = null!;
        public string QuizDescription { get; set; } = null!;
        public string QuizCreator { get; set; } = null!;
        [JsonIgnore]
        public DateOnly QuizCreationDate { get; set; }
        public QuizQuestionnary QuizQuestionnary { get; set; } = null!;
    }
}
