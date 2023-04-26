package com.appvision.newsapp.presentation.HomeFragment


import android.os.Bundle
import android.util.Log.e
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.appvision.newsapp.adapter.AllArticleAdapter
import com.appvision.newsapp.adapter.TopHeadlinesAdapter
import com.appvision.newsapp.data.model.ArticleModel
import com.appvision.newsapp.databinding.FragmentHomepageBinding
import com.appvision.newsapp.utils.OnClickListener
import kotlinx.coroutines.launch


class HomepageFragment : Fragment(), OnClickListener {
    lateinit var binding: FragmentHomepageBinding
    private lateinit var viewModel: HomepageViewModel
    var headLinesAdapter = TopHeadlinesAdapter(this)
    private val allArticleAdapter = AllArticleAdapter(this)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomepageBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(
                requireActivity().application
            )
        )[HomepageViewModel::class.java]
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.bookmark?.observe(viewLifecycleOwner, Observer {
            try {
                if (it.isNotEmpty()) {
                    allArticleAdapter.setList(it)
                    binding.rvAllArticles.adapter = allArticleAdapter
                    return@Observer
                }
            } catch (e: Exception) {
                e("HomeFragment ", "${e.message}")
            }

        })

        viewModel.headLineList?.observe(viewLifecycleOwner) {
                headLinesAdapter.setList(it)
                binding.rvTopHeadlines.adapter = headLinesAdapter


        }

        binding.querySearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.deleteForSearch()
                val searchQuery = binding.querySearch.text.toString()
                lifecycleScope.launch {
                    viewModel.loadAndSave(searchQuery)
                    viewModel.loadBookmark(searchQuery)
                    viewModel.bookmark?.observe(viewLifecycleOwner, Observer {
                        allArticleAdapter.setList(it)
                        binding.rvAllArticles.adapter = allArticleAdapter

                        return@Observer
                    })
                }
                return@setOnEditorActionListener true
            }
            false
        }
    }

    override fun onClick(position: Int, model: List<ArticleModel>) {

        val directions =
            HomepageFragmentDirections.actionHomepageFragmentToArticleFragment(model[position].id_key.toString())
        findNavController().navigate(directions)
    }

    override fun onImageClick(position: Int, model: List<ArticleModel>) {

    }


}


