package com.example.healthtracker.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.healthtracker.R
import com.example.healthtracker.domain.model.Activity

/** "MET 9.8" — bỏ ".0" thừa khi met là số nguyên. */
@Composable
fun formatActivityMetInfo(activity: Activity): String {
    val met = activity.met
    val metText = if (met == met.toInt().toDouble()) met.toInt().toString() else met.toString()
    return stringResource(R.string.label_met_value, metText)
}
