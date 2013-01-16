package app;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import play.test.FakeApplication;
import play.test.Helpers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.config.GlobalProperties;
import com.avaje.ebeaninternal.server.core.DefaultServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;

public class BaseModelTest
{
	public static FakeApplication app;

	@BeforeClass
	public static void startApp()
	{
		GlobalProperties.put("ebean.ddl.run", "true");

		app = Helpers.fakeApplication(Helpers.inMemoryDatabase());
		Helpers.start(app);
	}

	@Before
	public void start()
	{
		EbeanServer server = Ebean.getServer(null);
		if(server instanceof DefaultServer)
		{
			DdlGenerator ddl = ((DefaultServer) server).getDdlGenerator();

			String dropDdl = ddl.generateDropDdl();
			ddl.runScript(true, dropDdl);

			String createDdl = ddl.generateCreateDdl();
			ddl.runScript(true, createDdl);
		}
	}

	@AfterClass
	public static void stopApp()
	{
		Helpers.stop(app);
	}
}
