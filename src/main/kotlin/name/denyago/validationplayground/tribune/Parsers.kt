package name.denyago.validationplayground.tribune

import arrow.core.invalidNel
import arrow.core.sequence
import com.sksamuel.tribune.core.Parser
import com.sksamuel.tribune.core.compose
import com.sksamuel.tribune.core.filter
import com.sksamuel.tribune.core.map
import com.sksamuel.tribune.core.notNull
import com.sksamuel.tribune.core.positive
import com.sksamuel.tribune.core.strings.notNullOrBlank
import name.denyago.validationplayground.common.LineItem
import name.denyago.validationplayground.common.Order
import java.time.LocalDate

fun nameParser(attr: String) =
    Parser.fromNullableString()
        .notNullOrBlank { attr to "must not be blank" }

fun priceParser(attr: String) =
    Parser.from<Double?>()
        .notNull { attr to "must not be null" }
        .positive { attr to "must be greater than 0" }

val quantityParser =
    Parser.from<Int?>()
        .notNull { "quantity" to "must not be null" }
        .positive { "quantity" to "must be greater than 0" }

val dueDateParser =
    Parser.from<String?>()
        .notNull { "dueDate" to "must not be null" }
        .filter({ it.matches(Regex("\\d{4}-\\d{2}-\\d{2}")) }) { "dueDate" to "must match \"\\d{4}-\\d{2}-\\d{2}\"" }
        .map { LocalDate.parse(it) }

val lineItemParser =
    Parser.compose(
        nameParser("name").contramap<LineItemRequestTribune> { it.name },
        priceParser("price").contramap { it.price },
        quantityParser.contramap { it.quantity },
    ) { name, price, quantity -> LineItem(name, price, quantity) }

val orderParser =
    Parser.compose(
        nameParser("customerName").contramap<OrderRequestTribune> { it.customerName },
        priceParser("total").contramap { it.total },
        dueDateParser.contramap { it.dueDate },
        lineItemParser
            .asIndexedNamedList(min = 1, parentName = "lineItems") { "lineItems" to "must not be null" }
            .contramap { it.lineItems ?: emptyList() },
    ) { name, price, dueDate, lineItems -> Order(name, price, dueDate, lineItems.toList()) }

private fun <I, A, E> Parser<I, A, E>.asIndexedNamedList(
    min: Int = 0,
    max: Int? = null,
    parentName: String,
    ifInvalidSize: (Int) -> E,
): Parser<List<I>, List<A>, E> {
    return Parser { input ->
        val validSize =
            if (max != null) {
                input.size in (min..max)
            } else {
                input.size >= min
            }
        if (validSize) {
            input.withIndex().map { (index, value) ->
                this@asIndexedNamedList.parse(value).mapLeft { errors ->
                    errors.map { error ->
                        if (error is Pair<*, *> && error.first is String) {
                            @Suppress("UNCHECKED_CAST")
                            Pair("$parentName[$index].${error.first}", error.second) as E
                        } else {
                            error
                        }
                    }
                }
            }.sequence()
        } else {
            ifInvalidSize(input.size).invalidNel()
        }
    }
}
