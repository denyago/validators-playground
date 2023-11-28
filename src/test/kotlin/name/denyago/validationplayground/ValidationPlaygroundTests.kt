package name.denyago.validationplayground

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import name.denyago.validationplayground.common.LineItem
import name.denyago.validationplayground.common.Order
import name.denyago.validationplayground.common.OrderRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDate

@SpringBootTest
@AutoConfigureMockMvc
class ValidationPlaygroundTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @BeforeEach
    fun setUp() {
        orderRepository.deleteAll()
    }

    @Test
    fun `Spring Application can load`() {
    }

    @ParameterizedTest
    @ValueSource(strings = ["/orders-jakarta", "/orders-tribune"])
    fun `valid order created when the payload is valid`(endpoint: String) {
        val order =
            Order(
                customerName = "test",
                total = 1.0,
                dueDate = LocalDate.now(),
                lineItems =
                    listOf(
                        LineItem(
                            name = "test",
                            price = 1.0,
                            quantity = 1,
                        ),
                        LineItem(
                            name = "test",
                            price = 1.0,
                            quantity = 1,
                        ),
                    ),
            )
        mockMvc
            .perform(
                MockMvcRequestBuilders.post(endpoint)
                    .content(objectMapper.writeValueAsString(order))
                    .contentType(MediaType.APPLICATION_JSON),
            )
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(objectMapper.writeValueAsString(order)),
            )
            .andExpect(
                MockMvcResultMatchers.status()
                    .isOk,
            )

        assertGetOrders(size = 1)
    }

    @ParameterizedTest
    @ValueSource(strings = ["/orders-jakarta", "/orders-tribune"])
    fun `no Orders created when the payload is not valid`(endpoint: String) {
        val order =
            mapOf(
                "customerName" to "",
                "total" to 0.0,
                "dueDate" to "the end is near",
                // But you still can game the system
                // "dueDate" to "7777-77-77",
                "lineItems" to
                    listOf(
                        mapOf(
                            "name" to "",
                            "price" to 0.0,
                            "quantity" to 0,
                        ),
                        emptyMap(),
                    ),
            )
        mockMvc
            .perform(
                MockMvcRequestBuilders.post(endpoint)
                    .content(objectMapper.writeValueAsString(order))
                    .contentType(MediaType.APPLICATION_JSON),
            )
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(
                        objectMapper.writeValueAsString(
                            mapOf(
                                "customerName" to listOf("must not be blank"),
                                "total" to listOf("must be greater than 0"),
                                "dueDate" to listOf("must match \"\\d{4}-\\d{2}-\\d{2}\""),
                                "lineItems[0].name" to listOf("must not be blank"),
                                "lineItems[0].price" to listOf("must be greater than 0"),
                                "lineItems[0].quantity" to listOf("must be greater than 0"),
                                "lineItems[1].name" to listOf("must not be blank"),
                                "lineItems[1].quantity" to listOf("must not be null"),
                                "lineItems[1].price" to listOf("must not be null"),
                            ),
                        ),
                    ),
            )
            .andExpect(
                MockMvcResultMatchers.status()
                    .isBadRequest,
            )

        assertGetOrders(size = 0)
    }

    @ParameterizedTest
    @ValueSource(strings = ["/orders-jakarta", "/orders-tribune"])
    fun `no Orders created when the payload is empty`(endpoint: String) {
        val order = emptyMap<String, Any>()
        mockMvc
            .perform(
                MockMvcRequestBuilders.post(endpoint)
                    .content(objectMapper.writeValueAsString(order))
                    .contentType(MediaType.APPLICATION_JSON),
            )
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(
                        objectMapper.writeValueAsString(
                            mapOf(
                                "total" to listOf("must not be null"),
                                "lineItems" to listOf("must not be null"),
                                "dueDate" to listOf("must not be null"),
                                "customerName" to listOf("must not be blank"),
                            ),
                        ),
                    ),
            )
            .andExpect(
                MockMvcResultMatchers.status()
                    .isBadRequest,
            )

        assertGetOrders(size = 0)
    }

    private fun assertGetOrders(size: Int) {
        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/orders")
                    .contentType(MediaType.APPLICATION_JSON),
            )
            .andExpect(
                MockMvcResultMatchers.status().isOk,
            )
            .andExpect {
                val orders = objectMapper.readValue<List<Order>>(it.response.contentAsString)
                assertThat(orders).hasSize(size)
            }
    }
}
