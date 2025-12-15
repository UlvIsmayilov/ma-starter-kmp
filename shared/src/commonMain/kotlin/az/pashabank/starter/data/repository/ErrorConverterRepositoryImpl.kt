package az.pashabank.starter.data.repository

import az.pashabank.starter.data.remote.error.ServerProblemDescription
import az.pashabank.starter.domain.repository.ErrorConverterRepository
import kotlinx.serialization.json.Json

class ErrorConverterRepositoryImpl(
    val jsonSerializer: Json
) : ErrorConverterRepository {

    override fun getError(code: Int, identifier: String) {
        val errorBody = jsonSerializer.encodeToString(
            ServerProblemDescription.serializer(),
            ServerProblemDescription(identifier)
        )
        
        // Create a mock exception for testing purposes
        // This mimics Ktor's ClientRequestException structure
        throw KtorTestException(code, errorBody, identifier)
    }
}

// Custom exception for testing purposes that mimics Ktor's ClientRequestException structure
class KtorTestException(
    val statusCode: Int,
    val errorBody: String,
    val identifier: String
) : Exception("HTTP $statusCode: $errorBody")
