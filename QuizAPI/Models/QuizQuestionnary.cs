using System;
using System.Text.Json.Serialization;
using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace QuizAPI.Models
{
    public class QuizQuestionnary
    {
        public List<QuizQuestion> Questions { get; set; } = null!;
    }
}
