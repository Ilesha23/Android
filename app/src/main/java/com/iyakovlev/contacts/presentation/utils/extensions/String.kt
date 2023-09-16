package com.iyakovlev.contacts.presentation.utils.extensions

fun String.capitalizeFirstChar(): String = this.replaceFirstChar { it.uppercaseChar() }