package com.zerodev.deliverytracker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.zerodev.deliverytracker.data.model.LogLocation
import com.zerodev.deliverytracker.domain.usecases.GetLocationUseCase
import com.zerodev.deliverytracker.presentation.viewmodel.LogLocationViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class LogLocationViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: LogLocationViewModel
    private val getLogLocationUseCase: GetLocationUseCase = mockk()

    @Before
    fun setup() {
        viewModel = LogLocationViewModel(getLogLocationUseCase)
    }

    @Test
    fun `test getAllLogLocations emits correct data`() = runTest {
        val logData = listOf(
            LogLocation(id = 1, latitude = 1.0, longitude = 1.0, timestamp = 1L, isOnline = true),
            LogLocation(id = 2, latitude = 2.0, longitude = 2.0, timestamp = 2L, isOnline = false)
        )

        coEvery { getLogLocationUseCase.invoke() } returns flowOf(PagingData.from(logData))

        val differ = createPagingDataDiffer()

        viewModel.getAllLogLocations().collect { pagingData ->
            differ.submitData(pagingData)
        }

        advanceUntilIdle()

        assertEquals(logData.size, differ.snapshot().size)
        assertEquals(logData[0], differ.snapshot()[0])
        assertEquals(logData[1], differ.snapshot()[1])
    }

    private fun createPagingDataDiffer(): AsyncPagingDataDiffer<LogLocation> {
        return AsyncPagingDataDiffer(
            diffCallback = object :
                DiffUtil.ItemCallback<LogLocation>() {
                override fun areItemsTheSame(oldItem: LogLocation, newItem: LogLocation): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: LogLocation,
                    newItem: LogLocation
                ): Boolean {
                    return oldItem == newItem
                }
            },
            updateCallback = NoopListCallback(),
            workerDispatcher = Dispatchers.Main
        )
    }

    class NoopListCallback : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}