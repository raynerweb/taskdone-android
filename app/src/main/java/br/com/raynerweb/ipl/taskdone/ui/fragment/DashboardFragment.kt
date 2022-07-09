package br.com.raynerweb.ipl.taskdone.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.raynerweb.ipl.taskdone.R
import br.com.raynerweb.ipl.taskdone.databinding.FragmentDashboardBinding
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        binding.fragment = this
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        setupChart()

        val entries = mutableListOf(
            PieEntry(
                51.toFloat(),
                "Bolsonaro",
            ),
            PieEntry(
                30.toFloat(),
                "Lula",
            ),
            PieEntry(
                19.toFloat(),
                "Moro",
            )
        )
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
        binding.chart.setNoDataText("nenhum dado para apresentar")

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
                findNavController().navigate(R.id.action_dashboardFragment_to_taskListFragment)
            }

            override fun onNothingSelected() {
                findNavController().navigate(R.id.action_dashboardFragment_to_taskListFragment)
            }

        })
    }

}