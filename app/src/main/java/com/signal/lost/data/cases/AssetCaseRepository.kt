package com.signal.lost.data.cases

import android.content.Context
import com.signal.lost.domain.model.InvestigationCase
import kotlinx.serialization.json.Json

class AssetCaseRepository(
    private val context: Context
) {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    fun loadCases(): List<InvestigationCase> {
        return listOf(loadCase("cases/case_001_missing_engineer.json"))
    }

    private fun loadCase(path: String): InvestigationCase {
        val caseJson = context.assets.open(path).bufferedReader().use { reader ->
            reader.readText()
        }

        return json.decodeFromString<InvestigationCase>(caseJson)
    }
}
