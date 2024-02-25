package zuper.dev.android.dashboard.ui.jobs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import zuper.dev.android.dashboard.data.model.JobApiModel
import zuper.dev.android.dashboard.data.model.JobStatus
import zuper.dev.android.dashboard.utils.Utils

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JobsTabLayout(listItems: List<JobApiModel>) {
    val titles = listOf("Yet To Start", "In-Progress", "Cancelled", "In-Complete", "Completed")
    val pagerState = rememberPagerState(pageCount = {
        5
    })
    val coroutineScope = rememberCoroutineScope()

    val jobMap = getStats(listItems)

    Column(
        modifier = Modifier
            .padding(top = 20.dp)
    ) {
        ScrollableTabRow(
            edgePadding = 0.dp,
            selectedTabIndex = pagerState.currentPage,
            tabs = {
                titles.forEachIndexed { index, title ->
                    Tab(
                        text = {
                            Text(
                                "$title (${jobMap.get(title)})",
                                color = Color.Black
                            )
                        },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )
                }
            }
        )

        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
        ) { page ->
            val list = when (page) {
                0 -> {
                    listItems.filter {
                        it.status == JobStatus.YetToStart
                    }
                }

                1 -> {
                    listItems.filter {
                        it.status == JobStatus.InProgress
                    }
                }

                2 -> {
                    listItems.filter {
                        it.status == JobStatus.Canceled
                    }
                }

                3 -> {
                    listItems.filter {
                        it.status == JobStatus.Incomplete
                    }
                }

                4 -> {
                    listItems.filter {
                        it.status == JobStatus.Completed
                    }
                }

                else -> emptyList()
            }
            JobCardItem(listItems = list)
        }
    }
}

@Composable
fun JobCardItem(listItems: List<JobApiModel>) {
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        state = listState,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items = listItems) { item ->
            OutlinedCard(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                ),
                border = BorderStroke(1.dp, Color.LightGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                shape = RoundedCornerShape(5.dp)

            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .padding(15.dp)
                ) {
                    Text(
                        text = item.jobNumber.toString(),
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = item.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 10.dp)
                    )
                    Text(
                        text = "${Utils.convertUtcToCurrentTimeZone(item.startTime)} -> ${
                            Utils.convertUtcToCurrentTimeZone(
                                item.endTime
                            )
                        }",
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(top = 5.dp)
                    )
                }

            }
        }
    }
}

fun getStats(list: List<JobApiModel>): HashMap<String, Int> {
    var jobMap = hashMapOf<String, Int>()
    var completeCount = 0
    var incompleteCount = 0
    var inProgressCount = 0
    var yetToStartCount = 0
    var cancelledCount = 0
    list.forEach {
        when (it.status) {
            JobStatus.YetToStart -> yetToStartCount++
            JobStatus.Completed -> completeCount++
            JobStatus.Canceled -> cancelledCount++
            JobStatus.InProgress -> inProgressCount++
            JobStatus.Incomplete -> incompleteCount++
        }
    }
    jobMap.put("In-Progress", inProgressCount)
    jobMap.put("In-Complete", incompleteCount)
    jobMap.put("Cancelled", cancelledCount)
    jobMap.put("Completed", completeCount)
    jobMap.put("Yet To Start", yetToStartCount)
    return jobMap
}


@Preview
@Composable
fun PreviewTab() {
    JobsTabLayout(
        listOf(
            JobApiModel(
                jobNumber = 121,
                title = "Interior Design",
                startTime = "Start Time",
                endTime = "End Time",
                status = JobStatus.Completed

            )
        )
    )
}

@Preview
@Composable
fun PreviewJobCardItem() {
    JobCardItem(listItems = emptyList())
}