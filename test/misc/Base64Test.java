package misc;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class Base64Test
{
	@Test
	public void testDecodeFastWithNullParameter()
	{
		byte[] result = Base64.decodeFast(null);

		assertArrayEquals(new byte[0], result);
	}

	@Test
	public void testDecodeFastWithEmptyStringParameter()
	{
		byte[] result = Base64.decodeFast("");

		assertArrayEquals(new byte[0], result);
	}
}
