package com.iyakovlev.task2.presentation.utils.extensions

fun String.capitalizeFirstChar(): String = this.replaceFirstChar { it.uppercaseChar() }