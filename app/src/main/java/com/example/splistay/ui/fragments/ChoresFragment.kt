package com.example.splistay.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.splistay.databinding.FragmentChoresBinding

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.splistay.ServiceLocator
import com.example.splistay.ui.adapters.ChoreAdapter
import com.example.splistay.viewmodel.ChoresViewModel
import kotlinx.coroutines.launch

class ChoresFragment : Fragment() {
    private var _binding: FragmentChoresBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChoresViewModel by viewModels {
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return ChoresViewModel(ServiceLocator.provideRepository(requireContext())) as T
            }
        }
    }

    private lateinit var pendingAdapter: ChoreAdapter
    private lateinit var todayAdapter: ChoreAdapter
    private lateinit var completedAdapter: ChoreAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChoresBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerViews()
        observeViewModel()
        
        viewModel.loadChores("default_room")
    }

    private fun setupRecyclerViews() {
        pendingAdapter = ChoreAdapter { /* Handle proof */ }
        binding.rvPending.apply {
            adapter = pendingAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        todayAdapter = ChoreAdapter { chore ->
            viewModel.verifyChore(chore, "mock_uri", 12.9716, 77.5946)
        }
        binding.rvToday.apply {
            adapter = todayAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        completedAdapter = ChoreAdapter { /* No-op */ }
        binding.rvCompleted.apply {
            adapter = completedAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    pendingAdapter.submitList(uiState.pendingChores)
                    todayAdapter.submitList(uiState.todayChores)
                    completedAdapter.submitList(uiState.completedChores)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
