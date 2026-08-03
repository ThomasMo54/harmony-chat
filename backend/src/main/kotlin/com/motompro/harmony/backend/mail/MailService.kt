package com.motompro.harmony.backend.mail

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class MailService(
    private val brevoWebClient: WebClient,
) {

    @Value("\${brevo.api-key}")
    private lateinit var apiKey: String

    @Value("\${brevo.sender-email}")
    private lateinit var senderEmail: String

    @Value("\${brevo.sender-name}")
    private lateinit var senderName: String

    fun sendHtmlEmail(to: String, subject: String, htmlContent: String) {
        val body = mapOf(
            "sender" to mapOf("name" to senderName, "email" to senderEmail),
            "to" to listOf(mapOf("email" to to)),
            "subject" to subject,
            "htmlContent" to htmlContent
        )
        brevoWebClient.post()
            .uri("/smtp/email")
            .header("api-key", apiKey)
            .bodyValue(body)
            .retrieve()
            .toBodilessEntity()
            .block()
    }
}