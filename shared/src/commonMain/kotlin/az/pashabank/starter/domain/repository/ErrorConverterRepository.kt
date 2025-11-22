package az.pashabank.starter.domain.repository

interface ErrorConverterRepository {
    fun getError(code: Int, identifier: String)
}