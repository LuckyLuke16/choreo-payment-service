package com.example.paymentservice;

import com.example.paymentservice.model.ItemDetailDTO;
import com.example.paymentservice.repository.PaymentRepository;
import config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
public class PaymentServiceIT extends DatabaseInitializer {

    private MockMvc mockMvc;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setup() {
        this.paymentRepository.deleteAll();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        fillDatabaseWithTestData();
    }

    @Test
    public void Should_Get_Product_Beans() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        webApplicationContext.getServletContext();
        assertNotNull(webApplicationContext.getBean("paymentController"));
        assertNotNull(webApplicationContext.getBean("paymentService"));
    }

    @Test
    public void Should_Fetch_ALL_Items() throws Exception {
        long expectedPaymentAmount = 1;
        long actualPaymentAmount;
        List<ItemDetailDTO> payload = new ArrayList<>();
        payload.add(new ItemDetailDTO(1, "test", "desc", "SCI_FI", "max mustermann", 9.99f,2));
        payload.add(new ItemDetailDTO(2, "test", "desc", "SCI_FI", "max mustermann", 9.99f,2));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/payments?userId=123456-abcd&paymentMethod=PAYPAL")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(payload)))
                .andDo(print())
                .andExpect(status().isOk());

        actualPaymentAmount = this.paymentRepository.count();
        assertEquals(expectedPaymentAmount, actualPaymentAmount);
    }
}
