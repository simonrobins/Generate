package misc;

import play.Play;

public class Settings
{
	public static final String MASTER_PASSWORD = getString("sds.master.password");
	public static final String SECURE_PASSWORD = getString("sds.secure.password");

	private static String getString(final String property)
	{
		return Play.application().configuration().getString(property);
	}
}
