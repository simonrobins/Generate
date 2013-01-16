package app;

import static play.test.Helpers.*;

import org.junit.Test;
import static org.junit.Assert.*;

import play.mvc.Result;

public class TestApplication
{
	@Test
	public void testIndex()
	{
		Result result = callAction(controllers.routes.ref.Application.index());

		assertEquals(status(result), OK);
		assertEquals(contentType(result), OK);
	}
}
