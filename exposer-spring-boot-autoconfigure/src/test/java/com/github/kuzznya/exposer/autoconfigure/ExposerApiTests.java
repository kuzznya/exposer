package com.github.kuzznya.exposer.autoconfigure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class ExposerApiTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRequests() throws Exception {
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
