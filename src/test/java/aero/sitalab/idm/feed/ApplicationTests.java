package aero.sitalab.idm.feed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.junit.jupiter.api.Test;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationTests {

	@MockBean 
	private CommandLineRunner commandLineRunner;
	
	@Test
	public void contextLoads() {
	}

}
