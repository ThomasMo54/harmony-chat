package com.motompro.harmony.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HarmonyApplication

fun main(args: Array<String>) {
    runApplication<HarmonyApplication>(*args)
}
