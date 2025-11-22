package az.pashabank.starter.domain.model.customer

import kotlinx.datetime.LocalDateTime

data class Transaction(
    val id: String,
    val cardId: String,
    val category: ETransactionCategory,
    val title: String,
    val amount: String,
    val currency: String,
    val createdAt: LocalDateTime
) {

    fun formattedAmount(): String {
        return "-$amount $currency"
    }
}
