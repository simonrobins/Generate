package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import misc.Settings;
import models.Account;
import models.AccountContact;
import play.Play;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import security.AutoLogin;

@Security.Authenticated(Secured.class)
public class Application extends Controller
{
	public static Result index()
	{
		final List<Account> accounts = Account.findAll();

		return ok(views.html.generate.render(accounts, null, null, null, null, null));
	}

	public static Result generated(final String accountId, final String userId, final String period, final String access)
	{
		final String url = Play.application().configuration().getString("sds.url");

		final Long expires = (System.currentTimeMillis() + (Long.parseLong(period) * 86400000L)) / 1000;

		final String userMd5 = AutoLogin.generateMd5(Settings.SECURE_PASSWORD, userId, expires, access);
		final String userUrl = url + userId + "/" + expires + "/" + access + "/" + userMd5;

		final List<Account> accounts = Account.findAll();

		int account_id = Integer.parseInt(accountId);
		int user_id = Integer.parseInt(userId);
		return ok(views.html.generate.render(accounts, userUrl, account_id, user_id, access, period));
	}

	public static Result generate()
	{
		final Map<String, String[]> keys = request().body().asFormUrlEncoded();
		final String companyId = keys.get("company")[0];
		final String userId = keys.get("user")[0];
		final String period = keys.get("period")[0];
		final String access = keys.get("access")[0];

		return redirect(routes.Application.generated(companyId, userId, period, access));
	}

	public static Result ajaxUser()
	{
		final Map<String, String[]> keys = request().queryString();
		final int id = Integer.parseInt(keys.get("_value")[0]);

		final List<AccountContact> contacts = AccountContact.findUsersForCompany(id);

		Map<Integer, String> users = new HashMap<Integer, String>();

		for(AccountContact contact : contacts)
			users.put(contact.id, contact.username);

		List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();
		list.add(users);

		return ok(Json.toJson(list));
	}
}
