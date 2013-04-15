package app;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.HTMLUNIT;
import static play.test.Helpers.callAction;
import static play.test.Helpers.contentType;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.running;
import static play.test.Helpers.status;
import static play.test.Helpers.testServer;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import play.libs.F.Callback;
import play.mvc.Result;
import play.test.TestBrowser;

public class TestApplication extends BaseModelTest
{
	@Test
	public void testIndex()
	{
		Result result = callAction(controllers.routes.ref.Application.index());

		assertEquals(OK, status(result));
		assertEquals("text/html", contentType(result));
	}

	@Test
	public void testGenerated()
	{
		Result result = callAction(controllers.routes.ref.Application.display("999999", "41724", "0"));

		assertEquals(OK, status(result));
		assertEquals("text/html", contentType(result));
	}

	@Test
	public void testGenerate()
	{
		Map<String, String> data = new HashMap<String, String>();
		data.put("company", "999999");
		data.put("user", "41724");
		data.put("period", "1");
		data.put("access", "0");

		Result result = callAction(controllers.routes.ref.Application.generate(), fakeRequest().withFormUrlEncodedBody(data));

		assertEquals(SEE_OTHER, status(result));
		assertEquals(null, contentType(result));
	}

	@Test
	public void testUrlGenerated()
	{
		running(testServer(3333), HTMLUNIT, new Callback<TestBrowser>()
			{
				@Override
				public void invoke(TestBrowser browser)
					throws Throwable
				{
					browser.goTo("http://localhost:3333/");
					assertEquals("input", browser.$("#generate", 0).getTagName());
					browser.$("#generate", 0).submit();
					assertEquals("", browser.$("#href"));
				}
			});
	}
}
