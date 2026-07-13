package com.signal.lost.domain.usecase

import com.signal.lost.domain.model.CaseSolution
import com.signal.lost.domain.model.PlayerHypothesis

class CheckHypothesisUseCase {
    fun check(
        hypothesis: PlayerHypothesis,
        solution: CaseSolution
    ): HypothesisCheckResult {
        val missingEvidenceIds = solution.requiredEvidenceIds
            .filterNot { requiredId -> requiredId in hypothesis.selectedEvidenceIds }

        val checks = listOf(
            HypothesisCheckItem(
                title = "Жертва",
                isCorrect = hypothesis.victimId == solution.victimId
            ),
            HypothesisCheckItem(
                title = "Причастный",
                isCorrect = hypothesis.suspectId == solution.suspectId
            ),
            HypothesisCheckItem(
                title = "Место",
                isCorrect = hypothesis.locationId == solution.locationId
            ),
            HypothesisCheckItem(
                title = "Время",
                isCorrect = hypothesis.timeRange == solution.timeRange
            ),
            HypothesisCheckItem(
                title = "Причина",
                isCorrect = hypothesis.cause == solution.cause
            ),
            HypothesisCheckItem(
                title = "Способ",
                isCorrect = hypothesis.method == solution.method
            ),
            HypothesisCheckItem(
                title = "Обязательные улики",
                isCorrect = missingEvidenceIds.isEmpty()
            )
        )

        return HypothesisCheckResult(
            isSolved = checks.all { item -> item.isCorrect },
            checks = checks,
            missingEvidenceIds = missingEvidenceIds
        )
    }
}

data class HypothesisCheckResult(
    val isSolved: Boolean,
    val checks: List<HypothesisCheckItem>,
    val missingEvidenceIds: List<String>
)

data class HypothesisCheckItem(
    val title: String,
    val isCorrect: Boolean
)
