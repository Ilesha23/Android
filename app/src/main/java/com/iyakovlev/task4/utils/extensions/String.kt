package com.iyakovlev.task4.utils.extensions

fun String.capitalizeFirstChar(): String = this.replaceFirstChar { it.uppercaseChar() }