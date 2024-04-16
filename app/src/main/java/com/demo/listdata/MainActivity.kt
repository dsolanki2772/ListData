package com.demo.listdata

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.listdata.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val listAdapter = ListAdapter()
    lateinit var viewModel: MainViewModel
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Home"
        val retrofitService = RetrofitService.getInstance()
        val mainRepository = MainRepository(retrofitService)
        binding.rvHome.adapter = listAdapter
        viewModel = ViewModelProvider(
            this, MyViewModelFactory(mainRepository)
        )[MainViewModel::class.java]

        viewModel.list.observe(this) {
            if (it != null) {
                listAdapter.setItems(it)
            }
        }
        viewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        viewModel.loading.observe(this) {
            if (it) {
                if (currentPage == 1) {
                    binding.progressCircular.visibility = View.VISIBLE
                } else
                    binding.progressCircularBottom.visibility = View.VISIBLE
            } else {
                binding.progressCircular.visibility = View.GONE
                binding.progressCircularBottom.visibility = View.GONE
            }
        }
        viewModel.getList(currentPage, PAGE_SIZE)

        listAdapter.onItemClick = {
            val item = viewModel.list.value
            val itemData = item?.get(it)
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("details", Gson().toJson(itemData))
            startActivity(intent)
        }

        binding.rvHome.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (!isLoading && !isLastPage) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PAGE_SIZE
                    ) {
                        loadMoreItems()
                    }
                }

            }
        })
    }

    private fun loadMoreItems() {
        isLoading = true
        currentPage++
        viewModel.getList(currentPage, PAGE_SIZE)
        Handler(Looper.myLooper()!!).postDelayed({
            isLoading = false
        }, 500) // Simulated loading delay
    }

    companion object {
        private const val PAGE_SIZE = 10 // Number of items to load per page
    }

}