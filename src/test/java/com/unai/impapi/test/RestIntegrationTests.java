package com.unai.impapi.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.unai.impapi.RestMain;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes=RestMain.class)
public class RestIntegrationTests {
	
	@Autowired
	private WebApplicationContext context;
	
	private MockMvc mvc;
	
	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}
	
	@Test
	public void personControllerPingTest() throws Exception {
		mvc.perform(get("/people"))
				.andExpect(status().isOk())
				.andExpect(content().string("OK"));
	}
	
	@Test
	public void movieControllerPingTest() throws Exception {
		mvc.perform(get("/movies"))
				.andExpect(status().isOk())
				.andExpect(content().string("OK"));
	}
	
	@Test
	public void personControllerTest() throws Exception {
		mvc.perform(get("/people/nm0000129"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("Tom Cruise")))
				.andExpect(jsonPath("$.birthday", is("1962-7-3")))
				.andExpect(jsonPath("$.birthplace", is("Syracuse, New York, USA")))
				.andExpect(jsonPath("$.knownFor[0].title", is("Top Gun")))
				.andExpect(jsonPath("$.knownFor[0].roleName", is("Maverick")));
	}
	
	@Test
	public void movieControllerTest() throws Exception {
		mvc.perform(get("/movies/tt0339291")
				.header("Accept-Language", "en-US"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title", is("A Series of Unfortunate Events")))
			.andExpect(jsonPath("$.rating", greaterThanOrEqualTo(6.5)))
			.andExpect(jsonPath("$.rating", lessThanOrEqualTo(7.5)))
			.andExpect(jsonPath("$.releaseYear", is(2004)))
			.andExpect(jsonPath("$.directedBy[0].name", is("Brad Silberling")))
			.andExpect(jsonPath("$.writtenBy[0].name", is("Robert Gordon")))
			.andExpect(jsonPath("$.writtenBy[0].details[0]", is("screenplay")))
			.andExpect(jsonPath("$.writtenBy[0].as").doesNotExist())
			.andExpect(jsonPath("$.writtenBy[1].name", is("Daniel Handler")))
			.andExpect(jsonPath("$.writtenBy[1].details[0]", is("books")))
			.andExpect(jsonPath("$.writtenBy[1].as", is("Lemony Snicket")));
	}
	
}
