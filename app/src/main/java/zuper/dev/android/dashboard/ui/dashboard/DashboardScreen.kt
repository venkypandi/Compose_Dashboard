package zuper.dev.android.dashboard.ui.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import zuper.dev.android.dashboard.R
import zuper.dev.android.dashboard.data.model.InvoiceApiModel
import zuper.dev.android.dashboard.data.model.InvoiceStatus
import zuper.dev.android.dashboard.data.model.JobApiModel
import zuper.dev.android.dashboard.data.model.JobStatus
import zuper.dev.android.dashboard.ui.theme.BlueProgress80
import zuper.dev.android.dashboard.ui.theme.GreenCompleted80
import zuper.dev.android.dashboard.ui.theme.PurpleStart80
import zuper.dev.android.dashboard.ui.theme.RedInComplete80
import zuper.dev.android.dashboard.ui.theme.YellowCancelled80
import zuper.dev.android.dashboard.utils.Utils.getInVoiceTotal
import zuper.dev.android.dashboard.utils.Utils.getInvoiceStats
import zuper.dev.android.dashboard.utils.Utils.getJobStats
import zuper.dev.android.dashboard.utils.Utils.getTodayDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(onClick: (jobList:List<JobApiModel>) -> Unit) {

    val dashboardViewModel: DashboardViewModel = hiltViewModel()
    val jobList = dashboardViewModel.jobList.collectAsState()
    val invoiceList = dashboardViewModel.invoiceList.collectAsState()

    Scaffold(
        topBar = {
            Surface(shadowElevation = 3.dp) {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                    ),
                    title = {
                        Text("Dashboard",
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }
        }
    ){
       Column(
           modifier = Modifier
               .padding(it)
               .fillMaxWidth()
               .fillMaxHeight(),
           verticalArrangement = Arrangement.Top,
           horizontalAlignment = Alignment.CenterHorizontally

       ) {
           ProfileCard()
           JobStatsCard(onClick,jobList.value)
           InvoiceStatsCard(invoiceList =invoiceList.value )
       }
    }
}

@Composable
fun ProfileCard(){
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
        ),
        border = BorderStroke(1.dp, Color.LightGray),
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        shape = RoundedCornerShape(5.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .weight(0.8f)
            ) {
                Text(
                    text = "Hello, Venkatesh Pandian!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = getTodayDate(),
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                )
            }
            Column(
                modifier = Modifier
                    .weight(0.2f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = stringResource(id = R.string.profile_desc),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(7.dp))
                )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobStatsCard(onClick: (jobList:List<JobApiModel>)->Unit, jobList:List<JobApiModel>){
    val jobMap = getJobStats(jobList)
    if(jobList.isEmpty()){
        Box(
            modifier = Modifier.fillMaxSize(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No Jobs Found...")
        }
    }else {
        OutlinedCard(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
            ),
            border = BorderStroke(1.dp, Color.LightGray),
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .clickable {
                    onClick(jobList)
                },
            shape = RoundedCornerShape(5.dp)
        ) {

            Text(
                text = "Job Stats",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(10.dp)
            )
            Divider(
                color = Color.LightGray, modifier = Modifier
                    .fillMaxWidth()
                    .width(1.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = "${jobList.size} Jobs",
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "${jobMap.get(JobStatus.Completed)} of ${jobList.size} Completed",
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
                    count = jobList.size
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, start = 15.dp, end = 15.dp)
            ) {
                StatColorView(text = "Yet To Start", count = jobMap.get(JobStatus.YetToStart) ?: 0, PurpleStart80)
                StatColorView(text = "In-Progress", count = jobMap.get(JobStatus.InProgress) ?: 0, BlueProgress80)
            }

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, start = 15.dp, end = 15.dp)
            ) {
                StatColorView(text = "Cancelled", count = jobMap.get(JobStatus.Canceled) ?: 0, YellowCancelled80)
                StatColorView(text = "Completed", count = jobMap.get(JobStatus.Completed) ?: 0, GreenCompleted80)
            }

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, start = 15.dp, end = 15.dp, bottom = 15.dp)
            ) {
                StatColorView(text = "In-Complete", count = jobMap.get(JobStatus.Incomplete) ?: 0, RedInComplete80)
            }


        }
    }
}

@Composable
fun InvoiceStatsCard(invoiceList: List<InvoiceApiModel>) {
    val invoiceMap = getInvoiceStats(invoiceList)
    if (invoiceList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No Invoice Found...")
        }
    } else {
        OutlinedCard(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
            ),
            border = BorderStroke(1.dp, Color.LightGray),
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(5.dp)
        ) {

            Text(
                text = "Invoice Stats",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(10.dp)
            )
            Divider(
                color = Color.LightGray, modifier = Modifier
                    .fillMaxWidth()
                    .width(1.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {

                Text(
                    text = "Total Value ($${getInVoiceTotal(invoiceMap)})",
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "$${invoiceMap.get(InvoiceStatus.Paid)} collected",
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
                InvoiceStatsProgressIndicator(
                    invoiceMap = invoiceMap,
                    count = invoiceList.size
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, start = 15.dp, end = 15.dp)
            ) {
                InvoiceStatColorView(
                    text = "Draft",
                    count = invoiceMap.get(InvoiceStatus.Draft) ?: 0,
                    YellowCancelled80
                )
                InvoiceStatColorView(
                    text = "Pending",
                    count = invoiceMap.get(InvoiceStatus.Pending) ?: 0,
                    BlueProgress80
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, start = 15.dp, end = 15.dp, bottom = 15.dp)
            ) {
                InvoiceStatColorView(
                    text = "Paid",
                    count = invoiceMap.get(InvoiceStatus.Paid) ?: 0,
                    GreenCompleted80
                )
                InvoiceStatColorView(
                    text = "Bad Debt",
                    count = invoiceMap.get(InvoiceStatus.BadDebt) ?: 0,
                    RedInComplete80
                )
            }
        }
    }
}


@Composable
fun StatColorView(text:String,count:Int,color: Color){
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp, 10.dp)
                .background(color)
        )
        Text(
            text = "$text ($count)",
            modifier = Modifier
                .padding(start = 5.dp),
            textAlign = TextAlign.Center
        )

    }
}

@Composable
fun InvoiceStatColorView(text:String,count:Int,color: Color){
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp, 10.dp)
                .background(color)
        )
        Text(
            text = "$text ($$count)",
            modifier = Modifier
                .padding(start = 5.dp),
            textAlign = TextAlign.Center
        )

    }
}

@Preview
@Composable
fun PreviewProfileCard(){
    ProfileCard()
}

@Preview
@Composable
fun PreviewDashboardScreen(){
    DashboardScreen({})
}


