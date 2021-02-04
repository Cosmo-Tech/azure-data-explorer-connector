package com.cosmotech.connector.adx.extensions

import com.cosmotech.connector.adx.constants.SCHEMA_AS_JSON_SUFFIX
import com.cosmotech.connector.adx.constants.SHOW_TABLE_PREFIX

/**
 * @return return the .show table query
 */
fun String.toShowTableQuery(): String {
    return SHOW_TABLE_PREFIX.plus(this).plus(SCHEMA_AS_JSON_SUFFIX)
}

fun String.toADXType():String {
    val typeLowerCased = this.toLowerCase()
    if (typeLowerCased == "integer") return "int"
    if (typeLowerCased == "compositetype") return "string"
    return typeLowerCased
}
