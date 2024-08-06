package com.sz.springbootsample.demo.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import okhttp3.Credentials;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Yanghj
 * @date 2024/8/1 10:08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DemoControllerTest {

    @Autowired private MockMvc mockMvc;

    @Test
    public void headers() throws Exception {
        mockMvc.perform(
                        get("/demo/headers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", Credentials.basic("admin", "admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap());
    }
}
