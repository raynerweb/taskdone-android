package br.com.raynerweb.ipl.taskdone.ui.fragment

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.raynerweb.ipl.taskdone.BuildConfig
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
    private var filteringByDate = false

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_filter, menu)

        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = searchItem.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))

        searchView.apply {
            maxWidth = Integer.MAX_VALUE
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
//                    viewModel.filter(newText)
                    return true
                }
            })
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val clearFilterByDate = menu.findItem(R.id.action_clear_filter_by_date)
        clearFilterByDate.isVisible = filteringByDate

        val filterByDate = menu.findItem(R.id.action_filter_by_date)
        filterByDate.isVisible = !filteringByDate
    }

    private fun setupToolbar() {
        setHasOptionsMenu(true)
    }

    private fun setupViews() {
        setupToolbar()
        taskAdapter = TaskAdapter(mutableListOf(), { task, position ->
            viewModel.deleteTask(task, position)
        }, {
            viewModel.shareTask(it)
        })

        binding.rvUserWithTasks.adapter = taskAdapter
    }

    private fun subscribe() {
        viewModel.taskShared.observe(viewLifecycleOwner) { task ->
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                "${getString(R.string.app_name)} Task: #${task.taskId}"
            )
            val shareMessage =
                """
                Message: ${task.description}
                
                Date: ${task.date}
                
                Status: ${task.status.name}
                
                https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                """.trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, getString(R.string.choose_one)))
        }

        viewModel.taskDeleted.observe(viewLifecycleOwner) {
            taskAdapter.tasks.remove(it.first)
            taskAdapter.notifyItemRemoved(it.second)
            viewModel.checkEmptyList(taskAdapter.tasks)
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