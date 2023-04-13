using System;
using System.Security.Claims;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Authorization;

namespace QuizAPI
{
    public class Policies
    {
        public class ResourceOwnershipRequirement : IAuthorizationRequirement { }

        public class ResourceOwnershipHandler : AuthorizationHandler<ResourceOwnershipRequirement, string>
        {
            protected override Task HandleRequirementAsync(AuthorizationHandlerContext context, ResourceOwnershipRequirement requirement, string resource)
            {
                if (context.User.HasClaim(c => c.Type == ClaimTypes.NameIdentifier))
                {
                    string userId = context.User.FindFirst(c => c.Type == ClaimTypes.NameIdentifier)!.Value;
                    if (userId == resource)
                    {
                        context.Succeed(requirement);
                    }
                }
                return Task.CompletedTask;
            }
        }
    }
}
