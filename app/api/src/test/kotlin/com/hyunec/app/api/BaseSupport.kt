package com.hyunec.app.api

import com.hyunec.common.util.KLogging
import net.datafaker.Faker
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestConstructor.AutowireMode

@ActiveProfiles(value = ["test"])
@TestInstance(Lifecycle.PER_CLASS)
@TestConstructor(autowireMode = AutowireMode.ALL)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class BaseSupport {

    companion object : KLogging() {
        @JvmStatic
        protected val faker = Faker()
    }
}
