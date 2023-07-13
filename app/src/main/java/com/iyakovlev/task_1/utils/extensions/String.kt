package com.iyakovlev.task_1.utils.extensions

fun String.capitalizeFirstChar(): String = this.replaceFirstChar { it.uppercaseChar() }