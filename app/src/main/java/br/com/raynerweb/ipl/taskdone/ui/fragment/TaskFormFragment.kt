package br.com.raynerweb.ipl.taskdone.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.raynerweb.ipl.taskdone.databinding.FragmentTaskFormBinding

class TaskFormFragment : Fragment() {

    private lateinit var binding: FragmentTaskFormBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskFormBinding.inflate(inflater, container, false)
        binding.fragment = this
        binding.lifecycleOwner = this
        return binding.root
    }

    fun statusSelected(radioGroup: RadioGroup, id: Int) {
        when (id) {
            binding.rbBacklog.id -> Log.d("TASK", "BACKLOG")
            binding.rbProgress.id -> Log.d("TASK", "IN PROGRESS")
            binding.rbCompleted.id -> Log.d("TASK", "COMPLETED")
        }
    }

    fun save(view: View) {
        findNavController().navigateUp()
    }

}