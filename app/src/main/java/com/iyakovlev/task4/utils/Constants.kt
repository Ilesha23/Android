package com.iyakovlev.task4.utils

object Constants {

    /* preferences, permissions etc. */
    const val PREFERENCES = "PREFERENCES"
    const val READ_CONTACTS_PERMISSION_KEY = "READ_CONTACTS_PERMISSION_KEY"
    const val IS_FIRST_LAUNCH = "IS_FIRST_LAUNCH"

    /* SafeArgs keys */
    const val CONTACT_ID = "contactId"
    const val CONTACT_PHOTO = "contactPhoto"
    const val CONTACT_NAME = "contactName"
    const val CONTACT_CAREER = "contactCareer"
    const val CONTACT_ADDRESS = "contactAddress"

    /* Transition name */
    const val TRANSITION_NAME = "contactImageTransition_"

    /* durations */
    const val SNACK_BAR_LENGTH = 5000

    /* Logs */
    const val LOG_TAG = "LOG_TAG"

    val IMAGES = mutableListOf(
        "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1480&q=80",
        "https://images.unsplash.com/photo-1527980965255-d3b416303d12?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1480&q=80",
        "https://images.unsplash.com/photo-1580489944761-15a19d654956?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1361&q=80",
        "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1470&q=80",
        "https://images.unsplash.com/photo-1645830166230-187caf791b90?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1470&q=80"
    )

    // TODO: one common preferences
    const val APP_PREFERENCES = "APP_PREFERENCES"
    const val EMAIL = "EMAIL"
    const val NAME = "NAME"
    const val SURNAME = "SURNAME"
    const val ISLOGINED = "ISLOGINED"

    const val PASSWORD_LENGTH = 8
    const val PASSWORD_LOWERCASE_LETTERS = "a-z"
    const val PASSWORD_UPPERCASE_LETTERS = "A-Z"
    const val PASSWORD_NUMBERS = "0-9"

    const val DELIMITER_DOT = "."
    const val DELIMITER_AT = "@"
}