package controllers;

import java.util.List;
import java.util.Map;

import misc.Settings;
import models.Account;
import models.AccountContact;
import play.Play;
import play.cache.Cache;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import security.AutoLogin;

@Security.Authenticated(Secured.class)
public class Application extends Controller
{
	private static final String SDS_URL = "sds.url";

	public static Result index()
	{
		final List<Account> accounts = getAllAccounts();
		final List<AccountContact> contacts = accounts.get(0).contacts;

		return ok(views.html.generate.render(accounts, null, null, contacts, null, null, null));
	}

	public static Result changeCompany(final String accountId, final String userId, final String period)
	{
		return render(accountId, userId, period, false);
	}

	public static Result display(final String accountId, final String userId, final String period)
	{
		return render(accountId, userId, period, true);
	}

	private static Result render(final String accountId, final String userId, final String period, boolean generateUrl)
	{
		final String access = "999999".equals(accountId) ? "3" : "0";
		final String url = Play.application().configuration().getString(SDS_URL);

		String userUrl = null;
		if(generateUrl)
		{
			final Long expires = (System.currentTimeMillis() + (Long.parseLong(period) * 86400000L)) / 1000;
			final String userMd5 = AutoLogin.generateMd5(Settings.SECURE_PASSWORD, userId, expires, access);

			userUrl = url + userId + "/" + expires + "/" + access + "/" + userMd5;
		}

		int account_id = Integer.parseInt(accountId);
		int user_id = Integer.parseInt(userId);

		final List<Account> accounts = getAllAccounts();
		final Account account = Account.find(account_id);

		final List<AccountContact> contacts = account.contacts;
		final AccountContact contact = AccountContact.find(user_id);

		return ok(views.html.generate.render(accounts, userUrl, account_id, contacts, contact.id, access, period));
	}

	public static Result generate()
	{
		final Map<String, String[]> keys = request().body().asFormUrlEncoded();
		final String companyId = keys.get("company")[0];
		final String userId = keys.get("user")[0];
		final String period = keys.get("period")[0];

		if(keys.get("generate") == null)
			return redirect(routes.Application.changeCompany(companyId, userId, period));
		else
			return redirect(routes.Application.display(companyId, userId, period));
	}

	private static List<Account> getAllAccounts()
	{
		@SuppressWarnings("unchecked")
		List<Account> accounts = (List<Account>) Cache.get("Account.findAll");
		if(accounts == null)
		{
			accounts = Account.findAll();
			Cache.set("Account.findAll", accounts, 60);
		}
		return accounts;
	}
}