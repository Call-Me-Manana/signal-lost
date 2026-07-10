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
                title = "Victim",
                isCorrect = hypothesis.victimId == solution.victimId
            ),
            HypothesisCheckItem(
                title = "Suspect",
                isCorrect = hypothesis.suspectId == solution.suspectId
            ),
            HypothesisCheckItem(
                title = "Location",
                isCorrect = hypothesis.locationId == solution.locationId
            ),
            HypothesisCheckItem(
                title = "Time range",
                isCorrect = hypothesis.timeRange == solution.timeRange
            ),
            HypothesisCheckItem(
                title = "Cause",
                isCorrect = hypothesis.cause == solution.cause
            ),
            HypothesisCheckItem(
                title = "Method",
                isCorrect = hypothesis.method == solution.method
            ),
            HypothesisCheckItem(
                title = "Required evidence",
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
