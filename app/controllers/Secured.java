package controllers;

import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

public class Secured extends Security.Authenticator
{
	@Override
	public String getUsername(Context ctx)
	{
		return Login.authorize(ctx.request());
	}

	@Override
	public Result onUnauthorized(Context ctx)
	{
		return redirect(routes.Login.index());
	}
}
