package com.example.businesscardapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.businesscardapp.common.adapters.CustomBusinessCardAdapter
import com.example.businesscardapp.room.database.DatabaseConfig
import com.example.businesscardapp.common.utils.Utils
import com.example.businesscardapp.databinding.FragmentHomeBinding
import com.example.businesscardapp.room.models.BusinessCard
import com.example.businesscardapp.room.repositories.BusinessCardRepository
import com.example.businesscardapp.ui.businessCardManager.BusinessCardManagerActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var customBusinessCardAdapter: CustomBusinessCardAdapter
    private var businessCardList = listOf<BusinessCard>()

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
        init()
        registerEvents()
        listenEvents()
    }

    private fun init(){
        homeViewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory())[HomeViewModel::class.java]
        val businessCardDao = DatabaseConfig.getDatabase(requireContext()).businessCardDao()
        homeViewModel.repository = BusinessCardRepository(businessCardDao)
        customBusinessCardAdapter = CustomBusinessCardAdapter(requireContext())
        getAll()
    }

    private fun registerEvents(){
        binding.businessCardsListView.adapter = customBusinessCardAdapter
        Utils.editTextChangeListener(binding.etSearch) { afterTextChanged(binding.etSearch.text) }
        binding.businessCardsListView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(requireContext(),BusinessCardManagerActivity::class.java)
            intent.putExtra("id", businessCardList[position].id)
            startActivity(intent)
        }
    }

    private fun afterTextChanged(s: Editable?) {
        if(s != null && s.isNotEmpty()){
            Thread{
                homeViewModel.searchByFirstName(s.toString().trim())
            }.start()
        }else{
            getAll()
        }
    }

    private fun getAll(){
        Thread{
            homeViewModel.getAll()
        }.start()
    }

    private fun listenEvents(){
        homeViewModel.businessCardList.observe(viewLifecycleOwner){ result->
            businessCardList = result
            if(result.isEmpty()){
                binding.txtNothingToShowText.visibility = View.VISIBLE
            }else{
                binding.txtNothingToShowText.visibility = View.GONE
            }
            customBusinessCardAdapter.submitList(result)
        }
    }

    override fun onStart() {
        super.onStart()
        getAll()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}