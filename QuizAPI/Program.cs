using System.Security.Claims;
using Microsoft.AspNetCore.Authentication.Cookies;
using QuizAPI.Models;
using QuizAPI.Services;

var builder = WebApplication.CreateBuilder(args);

builder.Services.Configure<MongoDBSettings>(builder.Configuration.GetSection("MongoDBSettings"));
builder.Services.AddSingleton<MongoDBQuizService>();
builder.Services.AddSingleton<MongoDBUserService>();
builder.Services.AddSingleton<MongoDBAuthService>();

// Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

//call the startup class
builder.Services.AddAuthentication(CookieAuthenticationDefaults.AuthenticationScheme)
    .AddCookie(options =>
        {
            options.LoginPath = "/api/auth/login";
            options.LogoutPath = "/api/auth/logout";
            options.AccessDeniedPath = "/api/auth/denied";
            options.SlidingExpiration = true;
            options.ExpireTimeSpan = TimeSpan.FromMinutes(30);
        });

builder.Services.AddAuthorization(options =>
 {
    options.AddPolicy("RequireOwnershipOrAdmin", policy => {
        policy.RequireAssertion(async context =>
        {
            if (context.User.HasClaim(c => c.Type == ClaimTypes.Role && c.Value == "Admin"))
            {
                return true;
            }

            if (context.User.HasClaim(c => c.Type == ClaimTypes.NameIdentifier))
            {
                var httpcontext = context.Resource as Microsoft.AspNetCore.Http.HttpContext;
                string elementId = httpcontext!.Request.RouteValues["elementId"]!.ToString()!;
                string userId = context.User.FindFirst(c => c.Type == ClaimTypes.NameIdentifier)!.Value;
                
                var mongoDBQuizService = httpcontext.RequestServices.GetService<MongoDBQuizService>();
                var quizInfo = await mongoDBQuizService!.GetQuizInfoAsync(elementId);

                if (quizInfo != null && quizInfo.QuizCreatorId == userId)
                {
                    return true;
                }
            }

            return false;
        });
    });

    options.AddPolicy("RequireOwnershipOnly", policy => {
        policy.RequireAssertion(async context =>
        {
            if (context.User.HasClaim(c => c.Type == ClaimTypes.NameIdentifier))
            {
                var httpcontext = context.Resource as Microsoft.AspNetCore.Http.HttpContext;
                string elementId = httpcontext!.Request.RouteValues["elementId"]!.ToString()!;
                string userId = context.User.FindFirst(c => c.Type == ClaimTypes.NameIdentifier)!.Value;
                
                var mongoDBQuizService = httpcontext.RequestServices.GetService<MongoDBQuizService>();
                var quizInfo = await mongoDBQuizService!.GetQuizInfoAsync(elementId);

                if (quizInfo != null && quizInfo.QuizCreatorId == userId)
                {
                    return true;
                }
            }

            return false;
        });
    });
});

builder.Services.AddHttpContextAccessor();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

app.UseAuthorization();

app.UseAuthentication();

app.MapControllers();

app.Run();