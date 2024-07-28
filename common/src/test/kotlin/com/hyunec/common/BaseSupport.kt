package com.hyunec.common

import com.hyunec.common.util.KLogging
import net.datafaker.Faker
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles(value = ["test"])
abstract class BaseSupport {

    companion object : KLogging() {
        @JvmStatic
        protected val faker = Faker()
    }
}
