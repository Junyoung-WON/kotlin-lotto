package lotto.controller

import lotto.model.LotteryResult
import lotto.model.Lotto
import lotto.model.LottoMachine
import lotto.model.LottoNumber
import lotto.model.WinningRank
import lotto.util.NumberGenerate
import org.assertj.core.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class LottoGameControllerTest {
    private val customNumberGenerate =
        NumberGenerate { idx: Int ->
            LottoNumber.NUMBER_RANGE.toList().subList(idx, idx + Lotto.NUMBER_COUNT)
        }

    @ParameterizedTest
    @CsvSource(
        "1 2 3 4 5 6,7,FIRST SECOND FOURTH",
        "2 3 4 5 6 7,45,THIRD FIRST THIRD",
        "39 40 41 42 43 44,45,MISS MISS MISS",
    )
    fun `발행한 로또와 당첨 번호를 비교하여 각 로또의 등수를 매긴다`(
        winningInput: String,
        bonusNumber: Int,
        resultInput: String,
    ) {
        val lottoTickets = LottoMachine.issueTickets(3, customNumberGenerate)
        val lotteryResult =
            LotteryResult(
                Lotto(winningInput.split(" ").map { LottoNumber(it.toInt()) }),
                LottoNumber(bonusNumber),
            )
        val expectedResult = resultInput.split(" ")
        val actualResult =
            lottoTickets.map {
                WinningRank.convert(
                    it.checkWinningNumbers(lotteryResult.winning),
                    it.checkBonusNumbers(lotteryResult.bonusNumber),
                )
            }

        for (idx in actualResult.indices)
            Assertions.assertThat(actualResult[idx].name).isEqualTo(expectedResult[idx])
    }
}
