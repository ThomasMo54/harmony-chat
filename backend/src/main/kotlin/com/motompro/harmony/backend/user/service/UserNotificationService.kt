package com.motompro.harmony.backend.user.service

import com.motompro.harmony.backend.mail.MailService
import com.motompro.harmony.backend.mail.MailTemplateService
import com.motompro.harmony.backend.user.entity.User
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class UserNotificationService(
    private val mailService: MailService,
    private val mailTemplateService: MailTemplateService
) {

    @Async
    fun sendWelcomeEmail(user: User, code: String, validityMinutes: Int) {
        val htmlContent = mailTemplateService.renderWelcomeEmail(
            username = user.name,
            activationCode = code,
            validityMinutes = validityMinutes
        )
        mailService.sendHtmlEmail(
            to = user.email,
            subject = "Welcome to Harmony!",
            htmlContent = htmlContent
        )
    }
}