package controllers;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import misc.Base64;
import play.cache.Cache;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;

public class Login extends Controller
{
	public static Result index()
	{
		String username = authorize(request());
		if(username == null)
		{
			Status status = unauthorized();
			response().setHeader("WWW-Authenticate", "Basic realm=\"SDS Server Realm\"");
			return status;
		}
		else
		{
			return redirect(routes.Application.index());
		}
	}

	public static String authorize(Request request)
	{
		String encoded = request.getHeader("Authorization");
		if(encoded != null && encoded.startsWith("Basic "))
		{
			try
			{
				byte[] decoded = Base64.decodeFast(encoded.substring(6));
				String userPass = new String(decoded);
				String[] split = userPass.split(":");
				String username = split[0];
				String password = split[1];

				Properties usernames = (Properties) Cache.get("passwords");
				if(usernames == null)
				{
					usernames = new Properties();
					InputStream is = new FileInputStream("passwords.txt");
					usernames.load(is);
					Cache.set("passwords", usernames, 60);
				}
				if(password.equals(usernames.getProperty(username)))
					return username;
			}
			catch(Exception e)
			{
				// Just fall through on any exception
			}
		}

		return null;
	}
}
