package com.example.ucryton.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.ucryton.adapter.MarketAdapter
import com.example.ucryton.apis.ApiInterface
import com.example.ucryton.apis.ApiUtilities
import com.example.ucryton.databinding.FragmentTopLossGainBinding
import com.example.ucryton.models.CryptoCurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class TopLossGainFragment : Fragment() {
        lateinit var binding : FragmentTopLossGainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
            binding = FragmentTopLossGainBinding.inflate(layoutInflater)

        getMarketData()

        return binding.root
    }

    private fun getMarketData() {
        val position = requireArguments().getInt("position")
        lifecycleScope.launch(Dispatchers.IO){

            val res  = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()

            if(res.body() != null){

                withContext(Dispatchers.Main){
                    val dataItem = res.body()!!.data.cryptoCurrencyList

                    Collections.sort(dataItem){
                        o1,o2 -> (o2.quotes[0].percentChange24h.toInt())
                        .compareTo(o1.quotes[0].percentChange24h.toInt())
                    }

                    binding.spinKitView.visibility = GONE
                    val list = ArrayList<CryptoCurrency>()

                    if (position == 0) {
                        list.clear()
                        for (i in 0..9){
                            list.add(dataItem[i])
                        }

                        binding.topGainLoseRecyclerView.adapter = MarketAdapter(
                            requireContext(),
                            list,
                            "home"
                        )
                    }
                    else {
                        list.clear()
                        for (i in 0..9){
                            list.add(dataItem[dataItem.size-1-i])
                        }

                        binding.topGainLoseRecyclerView.adapter = MarketAdapter(
                            requireContext(),
                            list,
                            "home"
                        )
                    }
                }
            }
        }
    }
}