package br.com.raynerweb.ipl.taskdone.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.raynerweb.ipl.taskdone.R
import br.com.raynerweb.ipl.taskdone.databinding.FragmentTaskFormBinding
import br.com.raynerweb.ipl.taskdone.ext.toDate
import br.com.raynerweb.ipl.taskdone.ui.model.Status
import br.com.raynerweb.ipl.taskdone.ui.viewmodel.TaskFormViewModel
import br.com.raynerweb.ipl.taskdone.util.Mask
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskFormFragment : Fragment() {

    private lateinit var binding: FragmentTaskFormBinding
    private val viewModel: TaskFormViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskFormBinding.inflate(inflater, container, false)
        binding.fragment = this
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        subscribe()
    }

    private fun setupViews() {
        binding.etDeadline.addTextChangedListener(Mask.insert("##/##/####"))
    }

    private fun subscribe() {
        viewModel.showRequiredDescriptionMessage.observe(viewLifecycleOwner) {
            if (it) {
                binding.tilDescription.error = getString(R.string.required_field)
            } else {
                binding.tilDescription.error = null
            }
        }

        viewModel.showRequiredDateMessage.observe(viewLifecycleOwner) {
            if (it) {
                binding.tilDeadline.error = getString(R.string.required_field)
                binding.ivCalendar.visibility = View.GONE
            } else {
                binding.tilDeadline.error = null
                binding.ivCalendar.visibility = View.VISIBLE
            }
        }

        viewModel.showInvalidDateMessage.observe(viewLifecycleOwner) {
            if (it) {
                binding.tilDeadline.error = getString(R.string.invalid_date)
                binding.ivCalendar.visibility = View.GONE
            } else {
                binding.tilDeadline.error = null
                binding.ivCalendar.visibility = View.VISIBLE
            }
        }

        viewModel.taskSaved.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
    }

    fun showCalendar(view: View) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.select_date))
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.addOnPositiveButtonClickListener {
            binding.etDeadline.setText(it.toDate())
        }

        datePicker.show(parentFragmentManager, TAG)
    }

    fun statusSelected(radioGroup: RadioGroup, id: Int) {
        when (id) {
            binding.rbBacklog.id -> viewModel.setStatus(Status.BACKLOG)
            binding.rbProgress.id -> viewModel.setStatus(Status.IN_PROGRESS)
            binding.rbCompleted.id -> viewModel.setStatus(Status.COMPLETED)
        }
    }

    fun save(view: View) {
        viewModel.save()
    }

    companion object {
        const val TAG = "TASK_FORM"
    }

}