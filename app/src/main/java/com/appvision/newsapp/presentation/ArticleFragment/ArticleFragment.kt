package com.appvision.newsapp.presentation.ArticleFragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.appvision.newsapp.databinding.FragmentArticleBinding
import com.bumptech.glide.Glide

class ArticleFragment : Fragment() {
    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ArticleViewModel
    private val args: ArticleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[ArticleViewModel::class.java]
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadUI(args.id)

        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun loadUI(id: String) {

        viewModel.loadArticle(id)
        viewModel.articleItem?.observe(viewLifecycleOwner) { list ->
            list.forEach { model ->

                binding.textTitle.text = model.title
                binding.textDescription.text = model.description
                binding.textItemCategories.text = model.name
                binding.textAuthor.text = model.author

                Glide.with(requireActivity()).load(model.urlToImage).into(binding.imgArticleMain)

                when (model.isFavorite!!.toInt()) {
                    0 -> {
                        binding.imgSaved.visibility = View.GONE
                        binding.imgRemove.visibility = View.VISIBLE
                    }
                    1 -> {
                        binding.imgSaved.visibility = View.VISIBLE
                        binding.imgRemove.visibility = View.GONE

                    }
                }

                binding.imgShare.setOnClickListener {
                    val sendUrl: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, model.url)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendUrl, null)
                    startActivity(shareIntent)
                }

                binding.imgSaved.setOnClickListener {
                    saveDelete(0, args.id)
                }
                binding.imgRemove.setOnClickListener {
                    saveDelete(1, args.id)
                }

                binding.textDescription.setOnClickListener {
                    val uri = Uri.parse(model.url)
                    val browserIntent = Intent(Intent.ACTION_VIEW,uri)
                    startActivity(browserIntent)
                }
            }
        }
    }

    private fun saveDelete(status: Int, id: String) {
        when (status) {
            0 -> {
                viewModel.setFavourite(status, id)
                binding.imgSaved.visibility = View.GONE
                binding.imgRemove.visibility = View.VISIBLE

            }
            1 -> {
                viewModel.setFavourite(status, id)
                binding.imgSaved.visibility = View.VISIBLE
                binding.imgRemove.visibility = View.GONE
            }
        }
    }
}