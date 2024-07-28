package com.hyunec.common.util.id

import com.github.f4b6a3.ulid.Ulid

object UlidGenerator : IdGenerator {
    override fun get(): String {
        return Ulid.fast().toString()
    }
}
