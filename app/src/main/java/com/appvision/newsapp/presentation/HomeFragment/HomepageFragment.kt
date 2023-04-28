package com.appvision.newsapp.presentation.HomeFragment


import android.content.Context
import android.os.Bundle
import android.util.Log.e
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.appvision.newsapp.adapter.AllArticleAdapter
import com.appvision.newsapp.adapter.TopHeadlinesAdapter
import com.appvision.newsapp.data.model.ArticleModel
import com.appvision.newsapp.databinding.FragmentHomepageBinding
import com.appvision.newsapp.presentation.BookmarksFragment.CategoryListAdapter
import com.appvision.newsapp.utils.OnClickListener
import kotlinx.coroutines.launch


class HomepageFragment : Fragment(), OnClickListener {
    lateinit var binding: FragmentHomepageBinding
    private lateinit var viewModel: HomepageViewModel
    var headLinesAdapter = TopHeadlinesAdapter(this)
    private val allArticleAdapter = AllArticleAdapter(this)
    val category = arrayOf<String>("Apple","Samsung","USA","Mobile","Europe","Android")
    private val categoryListAdapter = CategoryListAdapter(category,this)
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
                val searchQuery = binding.querySearch.text.toString()
                search(searchQuery)
                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0)
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
    override fun onCategoryClick(title: String) {
       search(title)
    }

    private fun search(title: String){
        viewModel.deleteForSearch()
        lifecycleScope.launch {
            viewModel.loadAndSave(title)
            viewModel.loadBookmark(title)
            viewModel.bookmark?.observe(viewLifecycleOwner, Observer {
                allArticleAdapter.setList(it)
                binding.rvAllArticles.adapter = allArticleAdapter
                return@Observer
            })
        }

    }

}


