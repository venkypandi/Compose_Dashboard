package zuper.dev.android.dashboard.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import zuper.dev.android.dashboard.data.DataRepository
import zuper.dev.android.dashboard.data.model.InvoiceApiModel
import zuper.dev.android.dashboard.data.model.JobApiModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val repository: DataRepository) : ViewModel() {

    var _jobList = MutableStateFlow<List<JobApiModel>>(emptyList())
    val jobList: StateFlow<List<JobApiModel>> = _jobList

    var _invoiceList = MutableStateFlow<List<InvoiceApiModel>>(emptyList())
    val invoiceList: StateFlow<List<InvoiceApiModel>> = _invoiceList

    init {
        viewModelScope.launch {
            repository.observeJobs().collect {
                _jobList.value = it
            }
        }
        viewModelScope.launch {
            repository.observeInvoices().collect {
                _invoiceList.value = it
            }
        }
    }
}