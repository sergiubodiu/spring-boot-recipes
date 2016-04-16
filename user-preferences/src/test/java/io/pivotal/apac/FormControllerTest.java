package io.pivotal.apac;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = UserPreferencesApplication.class)
public class FormControllerTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    /**
     * All documentation production happens here.
     *
     * @throws Exception
     */
    @Test
    public void testFormBean() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();

        this.mockMvc.perform(post("/form")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("age", "20"))
                .andExpect(status().is4xxClientError());

        this.mockMvc.perform(post("/form")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("name", "Sample")
                    .param("age", "21"))
                .andExpect(status().isCreated());
    }

}