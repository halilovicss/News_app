package com.appvision.newsapp.presentation.home_fragment


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.appvision.newsapp.databinding.FragmentHomepageBinding
import com.appvision.newsapp.presentation.TopHeadlinesAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomepageFragment : Fragment(), HomeCallback {

    lateinit var binding: FragmentHomepageBinding
    private lateinit var viewModel: HomepageViewModel
    private val headLinesAdapter = TopHeadlinesAdapter(this)
    private val allArticleAdapter = AllArticleAdapter(this)
    private val category = arrayOf("Apple", "Samsung", "USA", "Mobile", "Europe", "Android")
    private val categoryListAdapter = CategoryListAdapter(category, this)
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
        binding.rvCategoryList.adapter = categoryListAdapter

        viewModel.topHeadlineList?.observe(viewLifecycleOwner) {
            headLinesAdapter.setList(it)
            binding.rvTopHeadlines.adapter = headLinesAdapter
        }

        viewModel.allArticleList?.observe(viewLifecycleOwner) { articleList ->
            allArticleAdapter.setList(articleList)
            binding.rvAllArticles.adapter = allArticleAdapter
        }

        binding.querySearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (binding.querySearch.text.toString().trim().isNotEmpty()) {
                    val searchQuery = binding.querySearch.text.toString()
                    search(searchQuery)
                    val imm =
                        activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(requireView().windowToken, 0)
                    return@setOnEditorActionListener true
                }
            }
            false
        }
    }

    override fun onClick(id: String) {
        val directions = HomepageFragmentDirections.actionHomepageFragmentToArticleFragment(id)
        findNavController().navigate(directions)
    }

    override fun onImageClick(id: String, status: Int?, position: Int) {
        when (status) {
            1 -> viewModel.saveArticle(id)
            0 -> viewModel.deleteArticle(id)
        }
    }

    override fun onCategoryClick(title: String) {
        search(title)
    }

    private fun search(title: String) {
        viewModel.deleteForSearch()
        lifecycleScope.launch {
            viewModel.loadAndSave(title)
            delay(1000)
            return@launch
        }
        viewModel.loadAllArticle(title)
        viewModel.allArticleList?.observe(viewLifecycleOwner) {
            allArticleAdapter.setList(it)
        }
    }
}


