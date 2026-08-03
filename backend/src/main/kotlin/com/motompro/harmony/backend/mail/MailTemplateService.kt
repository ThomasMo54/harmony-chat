package com.motompro.harmony.backend.mail

import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Service
class MailTemplateService(
    private val templateEngine: TemplateEngine
) {

    fun renderWelcomeEmail(username: String, activationCode: String, validityMinutes: Int): String {
        val context = Context().apply {
            setVariable("username", username)
            setVariable("activationCode", activationCode)
            setVariable("validityMinutes", validityMinutes)
        }
        return templateEngine.process("emails/welcome", context)
    }
}