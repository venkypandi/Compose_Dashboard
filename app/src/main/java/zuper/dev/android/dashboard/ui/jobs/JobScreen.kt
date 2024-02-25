package zuper.dev.android.dashboard.ui.jobs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import zuper.dev.android.dashboard.data.model.JobStatus
import zuper.dev.android.dashboard.ui.dashboard.JobStatsProgressIndicator
import zuper.dev.android.dashboard.ui.theme.BlueProgress80
import zuper.dev.android.dashboard.ui.theme.GreenCompleted80
import zuper.dev.android.dashboard.ui.theme.PurpleStart80
import zuper.dev.android.dashboard.ui.theme.RedInComplete80
import zuper.dev.android.dashboard.ui.theme.YellowCancelled80
import zuper.dev.android.dashboard.utils.Utils.getJobStats


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun JobScreen(navController: NavHostController) {
    val jobViewModel = hiltViewModel<JobViewModel>()
    val jobList = jobViewModel.jobList.collectAsState()
    val isRefreshing = jobViewModel.isRefreshing.collectAsState().value
    val pullRefreshState = rememberPullRefreshState(isRefreshing, { jobViewModel.getJobs() })

    val colorMap = hashMapOf<JobStatus, Color>()
    colorMap[JobStatus.Incomplete] = RedInComplete80
    colorMap[JobStatus.InProgress] = BlueProgress80
    colorMap[JobStatus.YetToStart] = PurpleStart80
    colorMap[JobStatus.Completed] = GreenCompleted80
    colorMap[JobStatus.Canceled] = YellowCancelled80


    Scaffold(
        topBar = {
            Surface(shadowElevation = 3.dp) {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                    ),
                    title = {
                        Text(
                            "Jobs (${jobList.value.size})",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    }
                )
            }
        }
    ) {
        val jobMap = getJobStats(jobList.value)
        Column(
            modifier = Modifier
                .padding(it)
        )
        {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = "${jobList.value.size} Jobs",
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "${jobMap.get(JobStatus.Completed)} of ${jobList.value.size} Completed",
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, start = 15.dp, end = 15.dp)
            ) {
                JobStatsProgressIndicator(
                    jobMap = jobMap,
                    count = jobList.value.size
                )
            }

            Box(
                modifier = Modifier
                    .pullRefresh(pullRefreshState)
                    .fillMaxSize()
            ) {
                JobsTabLayout(jobList.value)
                PullRefreshIndicator(
                    refreshing = isRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }


    }
}

@Preview
@Composable
fun PreviewJobPage() {
    JobScreen(rememberNavController())
}