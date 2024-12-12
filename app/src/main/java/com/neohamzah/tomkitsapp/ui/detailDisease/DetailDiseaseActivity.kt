package com.neohamzah.tomkitsapp.ui.detailDisease

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.neohamzah.tomkitsapp.data.remote.response.ProductList
import com.neohamzah.tomkitsapp.databinding.ActivityDetailDiseaseBinding
import com.neohamzah.tomkitsapp.utils.filterString
import java.io.File

class DetailDiseaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailDiseaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDiseaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val confidence = intent.getStringExtra(EXTRA_CONFIDENCE)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION)
        val diseaseName = intent.getStringExtra(EXTRA_DISEASE_NAME)
        val productList = intent.getParcelableArrayListExtra<ProductList>(EXTRA_PRODUCT_LIST)
        val solution = intent.getStringExtra(EXTRA_SOLUTION)

        val imagePath = intent.getStringExtra(EXTRA_IMAGE)
        val imageFile = imagePath?.let { File(it) }

        Glide.with(this)
            .load(imageFile)
            .into(binding.ivDisease)

        binding.tvDetailDisease.text = filterString(diseaseName.toString())
        binding.tvConfidence.text = confidence
        binding.tvDetailDescription.text = description
        binding.tvDetailSolution.text = solution

        if (productList.isNullOrEmpty()) {
            binding.tvRecommendation.visibility = View.GONE
            binding.recyclerView.visibility = View.GONE
        } else {
            val productAdapter = ProductAdapter { product ->
                val productLink = product.productLink
                if (!productLink.isNullOrBlank()) {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(productLink))
                        startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@DetailDiseaseActivity,
                            "Cannot open product link",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@DetailDiseaseActivity,
                        "No product link available",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            binding.recyclerView.layoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )

            binding.recyclerView.isNestedScrollingEnabled = false
            binding.recyclerView.adapter = productAdapter

            productAdapter.submitList(productList.mapNotNull { it })
        }
    }

    companion object {
        const val EXTRA_CONFIDENCE = "extra_confidence"
        const val EXTRA_DESCRIPTION = "extra_description"
        const val EXTRA_DISEASE_NAME = "extra_disease_name"
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_PRODUCT_LIST = "extra_product"
        const val EXTRA_SOLUTION = "extra_solution"


    }
}