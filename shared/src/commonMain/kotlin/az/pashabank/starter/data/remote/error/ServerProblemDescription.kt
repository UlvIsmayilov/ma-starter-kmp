package az.pashabank.starter.data.remote.error

import kotlinx.serialization.Serializable

@Serializable
class ServerProblemDescription(val code: String = "", val message: String = "")
