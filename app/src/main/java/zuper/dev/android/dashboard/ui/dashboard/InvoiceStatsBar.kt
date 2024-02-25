package zuper.dev.android.dashboard.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import zuper.dev.android.dashboard.data.model.InvoiceStatus
import zuper.dev.android.dashboard.ui.theme.BlueProgress80
import zuper.dev.android.dashboard.ui.theme.GreenCompleted80
import zuper.dev.android.dashboard.ui.theme.RedInComplete80
import zuper.dev.android.dashboard.ui.theme.YellowCancelled80

@Composable
fun InvoiceStatsProgressIndicator(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.LightGray,
    clipShape: Shape = RoundedCornerShape(3.dp),
    invoiceMap: HashMap<InvoiceStatus, Int>,
    count: Int
) {
    val colorMap = hashMapOf<InvoiceStatus, Color>()
    colorMap[InvoiceStatus.BadDebt] = RedInComplete80
    colorMap[InvoiceStatus.Pending] = BlueProgress80
    colorMap[InvoiceStatus.Paid] = GreenCompleted80
    colorMap[InvoiceStatus.Draft] = YellowCancelled80

    Box(
        modifier = modifier
            .clip(clipShape)
            .background(backgroundColor)
            .height(15.dp)
    ) {
        val sortedMap = invoiceMap.entries.sortedBy { it.value }.associate { it.toPair() }
        val reverseIterator = sortedMap.entries.reversed().iterator()
        val map = HashMap<InvoiceStatus, Double>()
        var temp = 0.0
        while (reverseIterator.hasNext()) {
            val entry = reverseIterator.next()
            temp = (temp + (entry.value).toDouble() / count)
            map.put(entry.key, temp)
        }
        sortedMap.entries.forEach {
            Box(
                modifier = Modifier
                    .background(colorMap[it.key]!!)
                    .fillMaxHeight()
                    .fillMaxWidth(map[it.key]!!.toFloat())
            )
        }
    }
}

@Preview
@Composable
fun PreviewInvoiceStatsProgressIndicator() {
    InvoiceStatsProgressIndicator(invoiceMap = hashMapOf(), count = 0)
}