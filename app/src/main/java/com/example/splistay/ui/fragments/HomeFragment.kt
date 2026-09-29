package com.example.splistay.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.splistay.databinding.FragmentHomeBinding

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.example.splistay.R
import com.example.splistay.ServiceLocator
import com.example.splistay.ui.adapters.ActivityAdapter
import com.example.splistay.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(ServiceLocator.provideRepository(requireContext())) as T
            }
        }
    }

    private lateinit var activityAdapter: ActivityAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        observeViewModel()
        setupQuickActions()
        
        viewModel.loadData("default_room")
    }

    private fun setupQuickActions() {
        binding.btnAddExpense.setOnClickListener {
            findNavController().navigate(R.id.navigation_expenses)
        }
        binding.btnAddChore.setOnClickListener {
            findNavController().navigate(R.id.navigation_chores)
        }
        binding.btnSettleUp.setOnClickListener {
            findNavController().navigate(R.id.navigation_ai)
        }
    }

    private fun setupRecyclerView() {
        activityAdapter = ActivityAdapter()
        binding.rvActivity.apply {
            adapter = activityAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    activityAdapter.submitList(uiState.recentActivity)
                    // Update other UI elements from uiState
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
