package az.pashabank.starter.domain.repository

interface AuthRepository {

    suspend fun masterLogin(email: String, password: String):Boolean

}