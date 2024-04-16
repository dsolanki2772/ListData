package com.demo.listdata

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.demo.listdata.databinding.ActivityDetailsBinding
import com.google.gson.Gson

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setDetailsData()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Details"
    }

    private fun setDetailsData() {
        val details = intent.getStringExtra("details")
        val itemModel = Gson().fromJson(details, Item::class.java)
        binding.tvIdVal.text = itemModel.id.toString()
        binding.tvUIdVal.text = itemModel.userId.toString()
        binding.tvName.text = itemModel.title
        binding.tvSource.text = itemModel.body
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed() // Navigate back when the home button is pressed
            return true
        }
        return super.onOptionsItemSelected(item);
    }
}