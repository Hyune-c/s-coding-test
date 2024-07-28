package com.hyunec.common.util.id

import com.hyunec.common.BaseSupport
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.RepeatedTest

@DisplayName("IdGenerator 구현체 테스트 - UlidGenerator")
class UlidGeneratorTest : BaseSupport() {

    @RepeatedTest(10)
    fun `ulid 는 26자리 문자열이다`() {
        UlidGenerator.get().length shouldBe 26
    }

    @RepeatedTest(10)
    fun `ulid 는 생성된 시간에 따라 정렬할 수 있다`() = runBlocking {
        val ulid1 = UlidGenerator.get()
        delay(100)
        val ulid2 = UlidGenerator.get()

        ulid2 shouldNotBe ulid1
        ulid2 shouldBeGreaterThan ulid1
    }
}
