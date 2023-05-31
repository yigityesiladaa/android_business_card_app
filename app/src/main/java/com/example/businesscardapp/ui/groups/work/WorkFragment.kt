package com.example.businesscardapp.ui.groups.work

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.businesscardapp.common.adapters.CustomBusinessCardAdapter
import com.example.businesscardapp.room.database.DatabaseConfig
import com.example.businesscardapp.databinding.FragmentWorkBinding
import com.example.businesscardapp.room.models.BusinessCard
import com.example.businesscardapp.room.repositories.BusinessCardRepository
import com.example.businesscardapp.ui.businessCardManager.BusinessCardManagerActivity
import com.example.businesscardapp.ui.groups.SharedViewModel

class WorkFragment : Fragment() {

    private var _binding: FragmentWorkBinding? = null
    private val binding get() = _binding!!
    private lateinit var customBusinessCardAdapter: CustomBusinessCardAdapter
    private lateinit var sharedViewModel: SharedViewModel
    private var group : String? = null
    private var businessCardList = listOf<BusinessCard>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            group = it.getString("groupId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        registerEvents()
        listenEvents()
    }

    private fun init(){
        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        val businessCardDao = DatabaseConfig.getDatabase(requireContext()).businessCardDao()
        sharedViewModel.businessCardRepository = BusinessCardRepository(businessCardDao)
        customBusinessCardAdapter = CustomBusinessCardAdapter(requireContext())
        Thread{
            group?.let {
                sharedViewModel.getByGroup(it)
            }
        }.start()
    }

    private fun registerEvents(){
        binding.workGroupListView.adapter = customBusinessCardAdapter
        binding.workGroupListView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(requireContext(), BusinessCardManagerActivity::class.java)
            intent.putExtra("id", businessCardList[position].id)
            startActivity(intent)
        }
    }

    private fun listenEvents(){
        sharedViewModel.businessCardList.observe(viewLifecycleOwner){ list->
            businessCardList = list
            if(list.isEmpty()){
                binding.txtNothingToShowText.visibility = View.VISIBLE
            }else{
                binding.txtNothingToShowText.visibility = View.GONE
            }
            customBusinessCardAdapter.submitList(list)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}