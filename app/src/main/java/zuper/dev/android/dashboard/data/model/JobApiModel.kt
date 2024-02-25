package zuper.dev.android.dashboard.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * A simple API model representing a Job
 *
 * Start and end date time is in ISO 8601 format - Date and time are stored in UTC timezone and
 * expected to be shown on the UI in the local timezone
 */
@Parcelize
data class JobApiModel(
    val jobNumber: Int,
    val title: String,
    val startTime: String,
    val endTime: String,
    val status: JobStatus
):Parcelable

enum class JobStatus {
    YetToStart,
    InProgress,
    Canceled,
    Completed,
    Incomplete
}
