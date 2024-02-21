package lotto.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import java.lang.IllegalArgumentException

class LottoTest {
    @ParameterizedTest
    @ValueSource(strings = ["1, 2, 3, 4, 5, 6, 7, 8", "1, 1, 1, 2, 3, 4", "0, 2, 3, 4, 5, 46"])
    fun `로또 번호는 1~45 사이의 6개의 서로 다른 자연수를 갖지 않으면 오류를 발생시킨다`(input: String) {
        val numbers = input.split(", ").map { it.toInt() }
        assertThrows<IllegalArgumentException> { Lotto(numbers) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["1, 2, 3, 4, 5, 6", "40, 41, 42, 43, 44, 45", "7, 2, 3, 4, 5, 35"])
    fun `로또 번호는 1~45 사이의 6개의 서로 다른 자연수를 갖는다`(input: String) {
        val numbers = input.split(", ").map { it.toInt() }
        assertDoesNotThrow { Lotto(numbers) }
    }

    @ParameterizedTest
    @CsvSource("1 2 3 4 5 6,6", "40 41 42 43 44 45,0", "7 2 3 4 5 35,4")
    fun `당첨 번호를 비교하여 동일한 번호의 개수를 반환한다`(winning: String, expected: Int) {
        val lotto = Lotto(listOf(1, 2, 3, 4, 5, 6))
        val result = lotto.checkWinningNumbers(winning.split(" ").map { it.toInt() })
        assertThat(result).isEqualTo(expected)
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 5, 6])
    fun `로또 번호에 보너스 번호가 포함되어 있으면 True를 반환한다`(bonusNumber: Int) {
        val lotto = Lotto(listOf(1, 2, 3, 4, 5, 6))
        val result = lotto.checkBonusNumbers(bonusNumber)
        assertTrue(result)
    }

    @ParameterizedTest
    @ValueSource(ints = [7, 10, 45])
    fun `로또 번호에 보너스 번호가 포함되어 있지 않으면 False을 반환한다`(bonusNumber: Int) {
        val lotto = Lotto(listOf(1, 2, 3, 4, 5, 6))
        val result = lotto.checkBonusNumbers(bonusNumber)
        assertFalse(result)
    }
}