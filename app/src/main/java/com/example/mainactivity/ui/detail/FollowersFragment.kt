package com.example.mainactivity.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mainactivity.data.Result
import com.example.mainactivity.data.remote.response.ResponseFollow
import com.example.mainactivity.databinding.FragmentFollowersBinding

class FollowersFragment : Fragment() {
    private lateinit var binding: FragmentFollowersBinding

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_SECTION_USERNAME = "section_username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(ARG_SECTION_USERNAME)
        val factory: ViewFactoryModel = ViewFactoryModel.getInstance(requireActivity() as DetailActivity)
        val viewFollowModel: ViewFollowModel by viewModels {
            factory
        }

        viewFollowModel.getFollowers(username.toString()).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is com.example.mainactivity.data.Result.Loading -> binding.progressBarr.visibility =
                        View.VISIBLE
                    is com.example.mainactivity.data.Result.Success -> {
                        binding.progressBarr.visibility = View.GONE
                        val userData = result.data
                        setUserData(userData)
                    }
                    is Result.Error -> {
                        binding.progressBarr.visibility = View.GONE
                        Toast.makeText(
                            context,
                            "Terjadi kesalahan: " + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        val dividerItemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollowers.layoutManager = layoutManager
        binding.rvFollowers.addItemDecoration(dividerItemDecoration)
    }

    private fun setUserData(followItem: List<ResponseFollow>) {
        val followAdapter = AdapterFollow(followItem)
        binding.rvFollowers.adapter = followAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarr.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}