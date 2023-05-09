package com.appvision.newsapp.presentation.bookmarks_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.appvision.newsapp.databinding.FragmentBookmarksBinding

class BookmarksFragment : Fragment() {
    private var _binding: FragmentBookmarksBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: BookmarksViewModel
    private val adapter = BookmarksAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[BookmarksViewModel::class.java]
        _binding = FragmentBookmarksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.bookmarksList.observe(viewLifecycleOwner) {
            adapter.setList(it)
            if (it.isNotEmpty()) {
                binding.rLayoutMsg.visibility = View.GONE
                binding.rvBookmarks.visibility = View.VISIBLE
                binding.rvBookmarks.adapter = adapter
            } else {
                binding.rLayoutMsg.visibility = View.VISIBLE
                binding.rvBookmarks.visibility = View.GONE
            }
        }
    }
}