package app.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import app.SliderListener;

/**
 * SliderPanel class that creates a panel with labeled sliders for different parameters.
 * Each slider's value can be adjusted and is monitored by a SliderListener.
 *
 * @author Yayun Tan
 * @author Zexu Wei
 * @version 1.0
 */

public class SliderPanel extends JPanel {
    private List<JSlider> sliders;
    private SliderListener sliderListener;

    public SliderPanel(SliderListener sliderListener) {
        this.sliderListener = sliderListener;
        List<String> names = Arrays.asList("Focus", "Stress", "Engagement", "Excitement", "Interest");

        setLayout(new GridLayout(names.size(), 1, 10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        sliders = names.stream()
                .map(this::createSliderWithLabel)  
                .collect(Collectors.toList());
    }

    private JSlider createSliderWithLabel(String name) {
        JSlider slider = new JSlider(0, 100, 50);
        setupSlider(slider, name);

        JLabel label = new JLabel(name, JLabel.LEFT);
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.add(label, BorderLayout.WEST);
        rowPanel.add(slider, BorderLayout.CENTER);
        add(rowPanel);

        return slider;
    }

    private void setupSlider(JSlider slider, String name) {
        slider.setMajorTickSpacing(25);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        slider.addChangeListener(e -> {
            if (!slider.getValueIsAdjusting()) { 
                sliderListener.setSliderValue(name, slider.getValue());
            }
        });
    }

    public List<JSlider> getSliders() {
        return sliders;
    }
}
