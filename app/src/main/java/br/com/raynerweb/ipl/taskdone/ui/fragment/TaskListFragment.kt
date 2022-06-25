package br.com.raynerweb.ipl.taskdone.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.raynerweb.ipl.taskdone.R
import br.com.raynerweb.ipl.taskdone.databinding.FragmentTaskListBinding

class TaskListFragment : Fragment() {

    private lateinit var binding: FragmentTaskListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskListBinding.inflate(inflater, container, false)
        binding.fragment = this
        binding.lifecycleOwner = this
        return binding.root
    }

    fun addTask(view: View) {
        findNavController().navigate(R.id.action_taskListFragment_to_taskFormFragment)
    }

}