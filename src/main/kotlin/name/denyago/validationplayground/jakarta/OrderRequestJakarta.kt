package name.denyago.validationplayground.jakarta

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import name.denyago.validationplayground.common.LineItem
import name.denyago.validationplayground.common.Order
import org.springframework.validation.annotation.Validated
import java.time.LocalDate

@Validated
data class OrderRequestJakarta(
    @field:NotBlank
    val customerName: String?,
    @field:NotNull
    @field:Positive
    val total: Double?,
    @field:NotNull
    @field:Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}")
    val dueDate: String?,
    @field:NotNull
    @field:Valid
    val lineItems: List<LineItemRequestJakarta>?,
)

@Validated
data class LineItemRequestJakarta(
    @field:NotBlank
    val name: String?,
    @field:NotNull
    @field:Positive
    val price: Double?,
    @field:NotNull
    @field:Positive
    val quantity: Int?,
)

fun OrderRequestJakarta.toDomain(): Order {
    return Order(
        customerName = this.customerName!!,
        total = this.total!!,
        dueDate = LocalDate.parse(this.dueDate!!),
        lineItems =
            this.lineItems!!.map {
                LineItem(
                    name = it.name!!,
                    price = it.price!!,
                    quantity = it.quantity!!,
                )
            },
    )
}
