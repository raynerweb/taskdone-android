package br.com.raynerweb.ipl.taskdone.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.raynerweb.ipl.taskdone.R
import br.com.raynerweb.ipl.taskdone.databinding.FragmentTaskListBinding
import br.com.raynerweb.ipl.taskdone.ui.adapter.TaskAdapter
import br.com.raynerweb.ipl.taskdone.ui.viewmodel.TaskListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskListFragment : Fragment() {

    private lateinit var binding: FragmentTaskListBinding
    private val viewModel: TaskListViewModel by viewModels()

    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskListBinding.inflate(inflater, container, false)
        binding.fragment = this
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        subscribe()

        viewModel.findAll()
    }

    private fun setupViews() {
        taskAdapter = TaskAdapter(mutableListOf(), { task, position ->
            viewModel.deleteTask(task, position)
        }, {
            viewModel.shareTask(it)
        })

        binding.rvUserWithTasks.adapter = taskAdapter
    }

    private fun subscribe() {
        viewModel.taskDeleted.observe(viewLifecycleOwner) {
            taskAdapter.tasks.remove(it.first)
            taskAdapter.notifyItemRemoved(it.second)
        }

        viewModel.taskList.observe(viewLifecycleOwner) { tasks ->
            taskAdapter.updateTasks(tasks.toMutableList())
            binding.tvEmptyListMessage.visibility = View.GONE
        }

        viewModel.emptyTaskList.observe(viewLifecycleOwner) {
            taskAdapter.updateTasks(mutableListOf())
            binding.tvEmptyListMessage.visibility = View.VISIBLE
        }
    }

    fun addTask(view: View) {
        findNavController().navigate(R.id.action_taskListFragment_to_taskFormFragment)
    }

}