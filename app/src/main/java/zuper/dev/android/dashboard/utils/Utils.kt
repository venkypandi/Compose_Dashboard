package zuper.dev.android.dashboard.utils

import zuper.dev.android.dashboard.data.model.InvoiceApiModel
import zuper.dev.android.dashboard.data.model.InvoiceStatus
import zuper.dev.android.dashboard.data.model.JobApiModel
import zuper.dev.android.dashboard.data.model.JobStatus
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object Utils {

    fun getJobStats(list:List<JobApiModel>):HashMap<JobStatus,Int>{
        var jobMap = hashMapOf<JobStatus,Int>()
        var completeCount = 0
        var incompleteCount = 0
        var inProgressCount = 0
        var yetToStartCount = 0
        var cancelledCount = 0
        list.forEach {
            when(it.status){
                JobStatus.YetToStart->yetToStartCount++
                JobStatus.Completed->completeCount++
                JobStatus.Canceled->cancelledCount++
                JobStatus.InProgress->inProgressCount++
                JobStatus.Incomplete->incompleteCount++
            }
        }
        jobMap.put(JobStatus.InProgress,inProgressCount)
        jobMap.put(JobStatus.Incomplete,incompleteCount)
        jobMap.put(JobStatus.Canceled,cancelledCount)
        jobMap.put(JobStatus.Completed,completeCount)
        jobMap.put(JobStatus.YetToStart,yetToStartCount)

        return jobMap
    }

    fun getInvoiceStats(list:List<InvoiceApiModel>):HashMap<InvoiceStatus,Int>{
        val invoiceMap = hashMapOf<InvoiceStatus,Int>()
        var draftCount = 0
        var pendingCount = 0
        var paidCount = 0
        var debtCount = 0
        list.forEach {
            when(it.status){
                InvoiceStatus.Draft->draftCount++
                InvoiceStatus.BadDebt->debtCount++
                InvoiceStatus.Paid->paidCount++
                InvoiceStatus.Pending->pendingCount++
                else->0
            }
        }
        invoiceMap.put(InvoiceStatus.Draft,draftCount)
        invoiceMap.put(InvoiceStatus.BadDebt,debtCount)
        invoiceMap.put(InvoiceStatus.Pending,pendingCount)
        invoiceMap.put(InvoiceStatus.Paid,paidCount)

        return invoiceMap
    }

    fun getInVoiceTotal(invoiceMap:HashMap<InvoiceStatus,Int>):Int{
        var total = 0
        invoiceMap.values.forEach {
            total += it
        }
        return total
    }

    fun getTodayDate():String{
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy")
        return today.format(formatter)
    }

    fun convertUtcToCurrentTimeZone(utcDateTimeString: String): String {
        // Parse the UTC datetime string
        val utcDateTime = ZonedDateTime.parse(utcDateTimeString, DateTimeFormatter.ISO_DATE_TIME)

        // Get the current time zone
        val currentTimeZone = ZoneId.systemDefault()

        // Convert the datetime to the current time zone
        val currentDateTime = utcDateTime.withZoneSameInstant(currentTimeZone)

        // Format the datetime as a string
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return currentDateTime.format(formatter)
    }

}