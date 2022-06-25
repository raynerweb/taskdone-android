package br.com.raynerweb.ipl.taskdone.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.raynerweb.ipl.taskdone.databinding.ViewTasksBinding
import br.com.raynerweb.ipl.taskdone.ui.model.Task

class TaskAdapter(
    var tasks: MutableList<Task>,
    val deleteListener: (Task, Int) -> Unit,
    val shareListener: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    fun updateTasks(tasks: MutableList<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ViewTasksBinding.inflate(LayoutInflater.from(parent.context))
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position], position)
    }

    override fun getItemCount(): Int = tasks.size

    inner class TaskViewHolder(private val binding: ViewTasksBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task, position: Int) {
            binding.task = task
            binding.delete.setOnClickListener {
                deleteListener.invoke(task, position)
            }
            binding.shared.setOnClickListener {
                shareListener.invoke(task)
            }
            binding.executePendingBindings()
        }

    }


}