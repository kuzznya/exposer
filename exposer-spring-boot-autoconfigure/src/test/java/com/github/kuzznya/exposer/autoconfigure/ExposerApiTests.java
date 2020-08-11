package com.github.kuzznya.exposer.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;

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

        mockMvc.perform(MockMvcRequestBuilders.get("/join?val=xxxx"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("xxxxtst"))
                .andDo(MockMvcResultHandlers.log())
                .andReturn();

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/join")
                .content(new ObjectMapper().writeValueAsString(Map.of("arg1", "xxxx", "arg2", "tst")))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("xxxxtst"))
                .andDo(MockMvcResultHandlers.log())
                .andReturn();

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/joinSer")
                        .content(new ObjectMapper().writeValueAsString(Map.of("arg1", "xxxx", "arg2", "tst")))
                        .contentType(MediaType.APPLICATION_JSON)
        )
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
