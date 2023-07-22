package com.iyakovlev.task2.model

import com.github.javafaker.Faker

class ContactsService {

    private var contacts = mutableListOf<Contact>()

    init {
        val faker = Faker.instance()

        contacts = (1..20).map { Contact(
            id = it.toLong(),
            name = faker.name().name(),
            career = faker.company().name(),
            photo = IMAGES[it % IMAGES.size]
        ) }.toMutableList()
    }

    companion object {

        private val IMAGES = mutableListOf(
            "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1480&q=80",
            "https://images.unsplash.com/photo-1527980965255-d3b416303d12?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1480&q=80",
            "https://images.unsplash.com/photo-1580489944761-15a19d654956?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1361&q=80",
            "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1470&q=80",
            "https://images.unsplash.com/photo-1645830166230-187caf791b90?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1470&q=80"
        )

        private var instance: ContactsService? = null

        fun getInstance(): ContactsService {
            if (instance == null) {
                instance = ContactsService()
            }
            return instance!!
        }
    }

    fun getContacts(): List<Contact> = contacts

}