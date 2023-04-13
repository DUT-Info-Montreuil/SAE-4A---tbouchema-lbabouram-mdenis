using System.Security.Claims;
using Microsoft.AspNetCore.Authentication.Cookies;
using QuizAPI.Models;
using QuizAPI.Services;
using static QuizAPI.Policies;

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
    options.AddPolicy("RequireOwnership", policy =>
    policy.Requirements.Add(new ResourceOwnershipRequirement()));

    options.AddPolicy("ModifyElement", policy => {
        policy.RequireAssertion(context =>
        {
            if (context.User.HasClaim(c => c.Type == ClaimTypes.Role && c.Value == "Admin"))
            {
                return true;
            }

            if (context.User.HasClaim(c => c.Type == ClaimTypes.NameIdentifier))
            {
                string userId = context.User.FindFirst(c => c.Type == ClaimTypes.NameIdentifier)!.Value;
                if (userId == context.Resource!.ToString()) //TODO: find a way to get the element id
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