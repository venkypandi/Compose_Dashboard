package zuper.dev.android.dashboard.ui.jobs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import zuper.dev.android.dashboard.data.DataRepository
import zuper.dev.android.dashboard.data.model.JobApiModel
import javax.inject.Inject

@HiltViewModel
class JobViewModel @Inject constructor(private val repository: DataRepository) : ViewModel() {

    var _jobList = MutableStateFlow<List<JobApiModel>>(emptyList())
    val jobList: StateFlow<List<JobApiModel>> = _jobList

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    init {
        getJobs()
    }

    fun getJobs() {
        viewModelScope.launch {
            repository.getJobs().collect {
                _jobList.value = it
                _isRefreshing.emit(false)
            }
        }
    }
}