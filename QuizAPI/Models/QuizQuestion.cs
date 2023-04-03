using System;

namespace QuizAPI.Models
{
    public class QuizQuestion
    {
        public string Question { get; set; } = null!;
        public List<QuizAnswer> Answers { get; set; } = null!;
    }
}
