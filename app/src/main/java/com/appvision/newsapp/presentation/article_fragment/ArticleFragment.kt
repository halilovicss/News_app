package com.appvision.newsapp.presentation.article_fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs
import com.appvision.newsapp.R
import com.appvision.newsapp.databinding.FragmentArticleBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticleFragment : Fragment(), ArticleCallback {

    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ArticleViewModel
    private val args: ArticleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel = ArticleViewModel(this, requireActivity().application)
        _binding =
            FragmentArticleBinding.inflate(inflater, container, false).also { it.data = viewModel }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("TAG", "onViewCreated: Otvoren ${args.id}")
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            viewModel.loadArticle(args.id)
        }
    }

    override fun shareArticle(articleUrl: String) {
        val sendUrl: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, articleUrl)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendUrl, null)
        startActivity(shareIntent)
    }

    override fun openArticle(articleUrl: String) {
        val uri = Uri.parse(articleUrl)
        val browserIntent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(browserIntent)
    }

    override fun createToolbarMenu(
        status: Int?, title: String, shareUrl: String, toggleBookmark: (Boolean) -> Unit
    ) {
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar_menu)
        viewModel.viewModelScope.launch {
            viewModel.setFavourite.observe(viewLifecycleOwner) { status ->

                if (status == 1) {
                    toolbar?.menu?.let {
                        it.clear()
                        it.add(1, 1, 0, "Share").apply {
                            setIcon(R.drawable.ic_share)
                            setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
                            it.add(2, 2, 0, "Delete").apply {
                                setIcon(R.drawable.ic_bookmark_filled)
                                setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
                            }

                        }

                    }
                } else if (status == 0) {
                    toolbar?.menu?.let {
                        it.clear()
                        it.add(1, 1, 0, "Share").apply {
                            setIcon(R.drawable.ic_share)
                            setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
                        }
                        it.add(3, 3, 0, " Add").apply {
                            setIcon(R.drawable.ic_bookmark_outlined)
                            setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
                        }
                    }
                }
            }
        }
        toolbar?.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                1 -> shareArticle(shareUrl)
                2 -> toggleBookmark(false)
                3 -> toggleBookmark(true)
            }
            true
        }
    }
}







