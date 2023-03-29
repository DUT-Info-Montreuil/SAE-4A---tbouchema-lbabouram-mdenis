using System;

namespace QuizAPI.Models
{
    public class QuizQuestion
    {
        public string Question { get; set; } = null!;
        public List<string> Answers { get; set; } = null!;
    }
}
