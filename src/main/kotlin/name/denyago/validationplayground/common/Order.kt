package name.denyago.validationplayground.common

import java.time.LocalDate

data class Order(val customerName: String, val total: Double, val dueDate: LocalDate, val lineItems: List<LineItem>)

data class LineItem(val name: String, val price: Double, val quantity: Int)
