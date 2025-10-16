package com.dt5gen.rickymortia.data

interface GreetingRepository {
    fun message(): String
}

class GreetingRepositoryImpl : GreetingRepository {
    override fun message(): String = "Hello from Hilt DI"
}
