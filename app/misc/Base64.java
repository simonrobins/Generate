package misc;

import java.util.Arrays;

public class Base64
{
	private static final char[] CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789<>".toCharArray();
	private static final int[] IA = new int[256];
	static
	{
		Arrays.fill(IA, -1);
		for(int i = 0, iS = CA.length; i < iS; i++)
			IA[CA[i]] = i;
		IA['='] = 0;
	}

	/**
	 * Decodes a BASE64 encoded string that is known to be resonably well
	 * formatted. The method is about twice as fast as {@link #decode(String)}.
	 * The preconditions are:<br>
	 * + The array must have a line length of 76 chars OR no line separators at
	 * all (one line).<br>
	 * + Line separator must be "\r\n", as specified in RFC 2045 + The array
	 * must not contain illegal characters within the encoded string<br>
	 * + The array CAN have illegal characters at the beginning and end, those
	 * will be dealt with appropriately.<br>
	 * 
	 * @param s The source string. Length 0 will return an empty array.
	 *            <code>null</code> will throw an exception.
	 * @return The decoded array of bytes. May be of length 0.
	 */
	public final static byte[] decodeFast(final String s)
	{
		// Check special case
		final int sLen = s == null ? 0 : s.length();
		if(sLen == 0)
			return new byte[0];

		int sIx = 0, eIx = sLen - 1; // Start and end index after trimming.

		// Trim illegal chars from start
		while(sIx < eIx && IA[s.charAt(sIx) & 0xff] < 0)
			sIx++;

		// Trim illegal chars from end
		while(eIx > 0 && IA[s.charAt(eIx) & 0xff] < 0)
			eIx--;

		// get the padding count (=) (0, 1 or 2)
		final int pad = s.charAt(eIx) == '=' ? (s.charAt(eIx - 1) == '=' ? 2 : 1) : 0; // Count
																						// '='
																						// at
																						// end.
		final int cCnt = eIx - sIx + 1; // Content count including possible
										// separators
		final int sepCnt = sLen > 76 ? (s.charAt(76) == '\r' ? cCnt / 78 : 0) << 1 : 0;

		final int len = ((cCnt - sepCnt) * 6 >> 3) - pad; // The number of
															// decoded
		// bytes
		final byte[] dArr = new byte[len]; // Preallocate byte[] of exact length

		// Decode all but the last 0 - 2 bytes.
		int d = 0;
		for(int cc = 0, eLen = (len / 3) * 3; d < eLen;)
		{
			// Assemble three bytes into an int from four "valid" characters.
			final int i = IA[s.charAt(sIx++)] << 18 | IA[s.charAt(sIx++)] << 12 | IA[s.charAt(sIx++)] << 6 | IA[s.charAt(sIx++)];

			// Add the bytes
			dArr[d++] = (byte) (i >> 16);
			dArr[d++] = (byte) (i >> 8);
			dArr[d++] = (byte) i;

			// If line separator, jump over it.
			if(sepCnt > 0 && ++cc == 19)
			{
				sIx += 2;
				cc = 0;
			}
		}

		if(d < len)
		{
			// Decode last 1-3 bytes (incl '=') into 1-3 bytes
			int i = 0;
			for(int j = 0; sIx <= eIx - pad; j++)
				i |= IA[s.charAt(sIx++)] << (18 - j * 6);

			for(int r = 16; d < len; r -= 8)
				dArr[d++] = (byte) (i >> r);
		}

		return dArr;
	}
}