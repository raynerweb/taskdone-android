package br.com.raynerweb.ipl.taskdone.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import br.com.raynerweb.ipl.taskdone.R
import br.com.raynerweb.ipl.taskdone.databinding.FragmentDashboardBinding
import br.com.raynerweb.ipl.taskdone.ui.model.Status
import br.com.raynerweb.ipl.taskdone.ui.viewmodel.DashboardViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        binding.fragment = this
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribe()
        setupViews()

        viewModel.getChartEntries()
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_dashboard, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.login -> {
                        viewModel.showLoginScreen()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupViews() {
        setupMenu()
        setupChart()
    }

    private fun subscribe() {
        viewModel.chartEntries.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.chart.visibility = View.GONE
                binding.btnCreateTasks.visibility = View.VISIBLE
                binding.tvEmptyData.visibility = View.VISIBLE
            } else {
                binding.chart.visibility = View.VISIBLE
                binding.btnCreateTasks.visibility = View.GONE
                binding.tvEmptyData.visibility = View.GONE
                drawChart(it)
            }
        }

        viewModel.isLogged.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(R.id.action_dashboardFragment_to_taskListFragment)
            } else {
                findNavController().navigate(R.id.action_dashboardFragment_to_loginFragment)
            }
        }

        viewModel.showLogin.observe(viewLifecycleOwner) {
                findNavController().navigate(R.id.action_dashboardFragment_to_loginFragment)
        }
    }

    fun createTask(view: View) {
        viewModel.createTask()
    }

    private fun drawChart(data: List<Pair<Float, Status>>) {
        val entries = data.map { pair ->
            PieEntry(
                pair.first,
                pair.second.name
            )
        }
        val dataSet = PieDataSet(entries, "")

        // Set color of slice
        dataSet.colors = ColorTemplate.PASTEL_COLORS.map { color -> color }

        val data = PieData(dataSet)

        // Percent color in Pie Chart
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)

        binding.chart.data = data
    }

    private fun setupChart() {

        binding.chart.setUsePercentValues(true)
        binding.chart.description.isEnabled = false
        binding.chart.setExtraOffsets(5f, 10f, 5f, 5f)
        binding.chart.dragDecelerationFrictionCoef = 0.95f
        binding.chart.isDrawHoleEnabled = true
        binding.chart.setHoleColor(Color.WHITE)
        binding.chart.setTransparentCircleColor(Color.WHITE)
        binding.chart.setTransparentCircleAlpha(110)
        binding.chart.holeRadius = 58f
        binding.chart.transparentCircleRadius = 61f
        binding.chart.rotationAngle = 0f
        binding.chart.isRotationEnabled = true
        binding.chart.isHighlightPerTapEnabled = true
        binding.chart.animateY(1400, Easing.EaseInOutQuad)

        val l = binding.chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f

        binding.chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                viewModel.createTask()
            }

            override fun onNothingSelected() {
                viewModel.createTask()
            }

        })
    }

}