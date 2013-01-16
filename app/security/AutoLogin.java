package security;

import java.security.MessageDigest;

public class AutoLogin
{
	private static char[] HEX =
		{
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
		};

	public static String generateMd5(final String secret, final String id, final Long expires, final String access)
	{
		final StringBuffer sb = new StringBuffer();
		final MessageDigest digest = digest();
		digest.update(secret.getBytes());
		digest.update(new Integer(id).toString().getBytes());
		digest.update(expires.toString().getBytes());
		if(access != null)
			digest.update(new Integer(access).toString().getBytes());
		for(final byte hash : digest.digest())
		{
			final int h = hash >> 4 & 0xf;
			final int l = hash & 0xf;
			sb.append(HEX[h]);
			sb.append(HEX[l]);
		}

		return sb.toString();
	}

	private static MessageDigest digest()
	{
		try
		{
			return MessageDigest.getInstance("MD5");
		}
		catch(final Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}
}
