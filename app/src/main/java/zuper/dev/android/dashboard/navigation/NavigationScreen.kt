package zuper.dev.android.dashboard.navigation

sealed class Screen(
    val route: String,
    val title: String,
){
    object Dashboard:Screen("dashboard","Dashboard")
    object Jobs:Screen("jobs","Jobs")
}
