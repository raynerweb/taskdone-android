package br.com.raynerweb.ipl.taskdone.ui.fragment

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.RadioGroup
import androidx.appcompat.widget.SearchView
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.raynerweb.ipl.taskdone.BuildConfig
import br.com.raynerweb.ipl.taskdone.R
import br.com.raynerweb.ipl.taskdone.databinding.FragmentTaskListBinding
import br.com.raynerweb.ipl.taskdone.databinding.ViewStatusFilterBinding
import br.com.raynerweb.ipl.taskdone.ext.toDate
import br.com.raynerweb.ipl.taskdone.ui.adapter.TaskAdapter
import br.com.raynerweb.ipl.taskdone.ui.model.Status
import br.com.raynerweb.ipl.taskdone.ui.viewmodel.TaskListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TaskListFragment : Fragment() {

    private lateinit var binding: FragmentTaskListBinding
    private val viewModel: TaskListViewModel by viewModels()

    private lateinit var viewStatusFilterBinding: ViewStatusFilterBinding
    private lateinit var statusFilterDialog: BottomSheetDialog

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
                    viewModel.setDescriptionFilter(newText)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_filter_by_status) {
            viewModel.showStatusFilter()
        }
        if (item.itemId == R.id.action_filter_by_date) {
            viewModel.showDateFilter()
        }
        if (item.itemId == R.id.action_clear_filter_by_date) {
            viewModel.setDateFilter(null, null)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupToolbar() {
        setHasOptionsMenu(true)
    }

    private fun setupStatusFilterDialog() {
        val inflater = LayoutInflater.from(requireContext())
        viewStatusFilterBinding = ViewStatusFilterBinding.inflate(inflater)
        viewStatusFilterBinding.fragment = this
        viewStatusFilterBinding.viewModel = viewModel
        viewStatusFilterBinding.lifecycleOwner = this

        statusFilterDialog = BottomSheetDialog(requireContext())
        statusFilterDialog.setContentView(viewStatusFilterBinding.root)
    }

    private fun setupViews() {
        setupToolbar()
        setupStatusFilterDialog()
        taskAdapter = TaskAdapter(mutableListOf(), { task, position ->
            viewModel.deleteTask(task, position)
        }, {
            viewModel.shareTask(it)
        })

        binding.rvUserWithTasks.adapter = taskAdapter
    }

    private fun subscribe() {
        viewModel.dateFilterActive.observe(viewLifecycleOwner) {
            filteringByDate = it
        }
        viewModel.toggleDateFilter.observe(viewLifecycleOwner) {
            val datePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText(getString(R.string.select_date))
                .setSelection(
                    Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                )
                .build()

            datePicker.addOnPositiveButtonClickListener { interval ->
                viewModel.setDateFilter(interval.first.toDate(), interval.second.toDate())
            }

            datePicker.show(parentFragmentManager, TAG)
        }

        viewModel.toggleStatusFilter.observe(viewLifecycleOwner) {
            statusFilterDialog.show()
        }

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
            taskAdapter.notifyItemRemoved(it.second)
            taskAdapter.tasks.remove(it.first)
            if (taskAdapter.tasks.isEmpty()) {
                viewModel.findAll()
            }
        }

        viewModel.taskList.observe(viewLifecycleOwner) { tasks ->
            if (tasks.isEmpty()) {
                binding.tvEmptyListMessage.visibility = View.VISIBLE
            } else {
                binding.tvEmptyListMessage.visibility = View.GONE
            }
            taskAdapter.updateTasks(tasks.toMutableList())

        }

    }

    fun statusSelected(radioGroup: RadioGroup, id: Int) {
        when (id) {
            viewStatusFilterBinding.rbBacklog.id -> viewModel.setStatusFilter(Status.TODO)
            viewStatusFilterBinding.rbProgress.id -> viewModel.setStatusFilter(Status.IN_PROGRESS)
            viewStatusFilterBinding.rbCompleted.id -> viewModel.setStatusFilter(Status.DONE)
            else -> viewModel.setStatusFilter(null)
        }
        statusFilterDialog.dismiss()
    }

    fun addTask(view: View) {
        findNavController().navigate(R.id.action_taskListFragment_to_taskFormFragment)
    }

    companion object {
        const val TAG = "TASK_LIST"
    }

}