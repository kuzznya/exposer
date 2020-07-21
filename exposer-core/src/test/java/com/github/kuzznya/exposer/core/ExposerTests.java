package com.github.kuzznya.exposer.core;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
public class ExposerTests {
    @Autowired
    private WebApplicationContext ctx;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @Test
    public void ExposerMvcTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/test/v1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("test"))
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.post("/test/v1?value=testpost"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.log())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.get("/test/v1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("testpost"))
                .andDo(MockMvcResultHandlers.log())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.get("/test?val=xxxx"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("xxxxtst"))
                .andDo(MockMvcResultHandlers.log())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.get("/test/listsize?list=arg1&list=arg2&list=arg3"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{size: 3}"))
                .andDo(MockMvcResultHandlers.log())
                .andReturn();
    }
}
