package com.neohamzah.tomkitsapp.ui.history

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.neohamzah.tomkitsapp.R
import com.neohamzah.tomkitsapp.ViewModelFactory
import com.neohamzah.tomkitsapp.databinding.FragmentHistoryBinding
import com.neohamzah.tomkitsapp.utils.Result
import com.neohamzah.tomkitsapp.utils.isNetworkAvailable

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private var adapter = Adapter()

    private val viewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeData()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        refreshHistory()
    }

    private fun setupRecyclerView() {
        adapter = Adapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun observeData() {
        viewModel.getSession().observe(viewLifecycleOwner) { session ->
            if (session.token.isNotEmpty()) {
                refreshHistory()
            } else {
                Toast.makeText(context, getString(R.string.no_session), Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun refreshHistory() {

        if (!isNetworkAvailable(requireContext())) {
            Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.getSession().observe(viewLifecycleOwner) { session ->
                if (session.token.isNotEmpty()) {
                    viewModel.getHistory(session.token).observe(viewLifecycleOwner) { result ->
                        when (result) {
                            is Result.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }

                            is Result.Success -> {
                                binding.progressBar.visibility = View.GONE
                                adapter.submitList(result.data.histories)
                            }

                            is Result.Error -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}