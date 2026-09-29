package com.example.splistay.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.splistay.ServiceLocator
import com.example.splistay.data.model.Expense
import com.example.splistay.databinding.FragmentAddExpenseBinding
import com.example.splistay.viewmodel.ExpensesViewModel
import java.util.*

class AddExpenseFragment : Fragment() {
    private var _binding: FragmentAddExpenseBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExpensesViewModel by viewModels {
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return ExpensesViewModel(ServiceLocator.provideRepository(requireContext())) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.btnSaveExpense.setOnClickListener {
            val amount = binding.etAmount.text.toString().toDoubleOrNull() ?: 0.0
            val description = binding.etDescription.text.toString()
            
            if (description.isNotEmpty() && amount > 0) {
                val expense = Expense(
                    id = UUID.randomUUID().toString(),
                    roomId = "default_room",
                    description = description,
                    amount = amount,
                    category = "General",
                    paidByRoommateId = "user_1",
                    splitBetween = emptyMap(),
                    splitType = "equal",
                    notes = null,
                    receiptPhotoUri = null,
                    date = System.currentTimeMillis()
                )
                viewModel.addExpense(expense)
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
