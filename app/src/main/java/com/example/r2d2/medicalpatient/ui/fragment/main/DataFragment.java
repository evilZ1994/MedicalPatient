package com.example.r2d2.medicalpatient.ui.fragment.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.r2d2.medicalpatient.R;
import com.example.r2d2.medicalpatient.mvp.presenter.DataPresenter;
import com.example.r2d2.medicalpatient.mvp.view.DataView;
import com.example.r2d2.medicalpatient.ui.activity.DataDetailActivity;
import com.example.r2d2.medicalpatient.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataFragment extends BaseFragment implements DataView{
    private LineChartView chartView;
    private LineChartData lineData;
    private List<Integer> datas = new ArrayList<>();
    public final static String[] hours = new String[]{"0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55",};

    @Inject
    DataPresenter dataPresenter;

    @BindView(R.id.pressure_layout)
    LinearLayout pressureLayout;
    @BindView(R.id.temperature_layout)
    LinearLayout temperatureLayout;
    @BindView(R.id.angle_layout)
    LinearLayout angleLayout;
    @BindView(R.id.pulse_layout)
    LinearLayout pulseLayout;
    @BindView(R.id.data_pressure)
    TextView dataPressure;
    @BindView(R.id.data_angle)
    TextView dataAngle;
    @BindView(R.id.data_temperature)
    TextView dataTemperature;
    @BindView(R.id.data_pulse)
    TextView dataPulse;

    public DataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data, container, false);
        chartView = (LineChartView)view.findViewById(R.id.chart);
        getFragmentComponent().inject(this);
        ButterKnife.bind(this, view);
        dataPresenter.attachView(this);
        //绑定监听器
        OnDataClickListener listener = new OnDataClickListener();
        pressureLayout.setOnClickListener(listener);
        angleLayout.setOnClickListener(listener);
        temperatureLayout.setOnClickListener(listener);
        pulseLayout.setOnClickListener(listener);
        //初始化图表
        generate(datas);
        //刷新图表
        dataPresenter.updateData();
        return view;
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError(String msg) {

    }

    @Override
    public void showDialog() {

    }

    @Override
    public void hideDialog() {

    }

    @Override
    public void refreshChart(List<Integer> list) {
        //datas保存当前状态
        datas = list;
        generate(datas);
    }

    @Override
    public void refreshData(Map<String, Object> map) {
        dataPressure.setText(map.get("pressure")+"Pa");
        dataAngle.setText(map.get("angle")+"度");
        dataTemperature.setText(map.get("temperature")+"℃");
        dataPulse.setText(map.get("pulse")+"次/分");
    }

    /**
     * 图表生成
     * @param datas 数据
     */
    private void generate(List<Integer> datas) {
        int numValues = 12;

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<PointValue> values = new ArrayList<PointValue>();
        for (int i=0; i<numValues; i++){
            axisValues.add(new AxisValue(i).setLabel(hours[i]));
        }
        for (int i = 0; i < datas.size(); ++i) {
            values.add(new PointValue(i, datas.get(i)));
        }

        Line line = new Line(values);
        line.setColor(ChartUtils.COLOR_BLUE).setCubic(true);
        line.setPointRadius(3);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        lineData = new LineChartData(lines);
        Axis axis = new Axis(axisValues);
        axis.setHasLines(true);
        axis.setHasTiltedLabels(true);
        lineData.setAxisXBottom(axis);
        lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));

        chartView.setLineChartData(lineData);

        // For build-up animation you have to disable viewport recalculation.
        chartView.setViewportCalculationEnabled(false);

        // And set initial max viewport and current viewport- remember to set viewports after data.
        Viewport v = new Viewport(0, 1000, 12, 0);
        chartView.setMaximumViewport(v);
        chartView.setCurrentViewport(v);

        chartView.setZoomType(ZoomType.HORIZONTAL);
    }

    private class OnDataClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            int id = view.getId();
            Intent intent = new Intent(getContext(), DataDetailActivity.class);
            switch (id){
                case R.id.pressure_layout:
                    intent.putExtra("tag", "pressure");
                    intent.putExtra("title", "压强");
                    break;
                case R.id.temperature_layout:
                    intent.putExtra("tag", "temperature");
                    intent.putExtra("title", "温度");
                    break;
                case R.id.angle_layout:
                    intent.putExtra("tag", "angle");
                    intent.putExtra("title", "角度");
                    break;
                case R.id.pulse_layout:
                    intent.putExtra("tag", "pulse");
                    intent.putExtra("title", "脉搏");
                    break;
            }
            startActivity(intent);
        }
    }
}
