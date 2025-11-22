package az.pashabank.starter.domain.exceptions

fun interface ErrorConverter {
    fun convert(t: Throwable): Throwable
}