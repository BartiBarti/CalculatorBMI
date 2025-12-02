package pl.calulatorbmi.bartek.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CalculatorBMI extends JFrame {

    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel weightLabel;
    private JLabel heightLabel;
    private JTextField weightTextField;
    private JTextField heightTextField;
    private JButton calculateButton;

    private BMIAxisChartPanel chartPanel;

    public CalculatorBMI() throws HeadlessException {
        setTitle("Calculator BMI");
        setSize(420, 670);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        setContentPane(mainPanel);

        titleLabel = new JLabel("KALKULATOR BMI");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        weightLabel = new JLabel("Waga (kg):");
        heightLabel = new JLabel("Wzrost (cm):");

        weightTextField = new JTextField();
        heightTextField = new JTextField();
        calculateButton = new JButton("Oblicz");

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(weightLabel);
        mainPanel.add(weightTextField);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(heightLabel);
        mainPanel.add(heightTextField);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(calculateButton);
        mainPanel.add(Box.createVerticalStrut(20));

        chartPanel = new BMIAxisChartPanel();
        chartPanel.setPreferredSize(new Dimension(350, 350));
        mainPanel.add(chartPanel);

        calculateButton.addActionListener(e -> calculateBmi());

        KeyAdapter enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) calculateBmi();
            }
        };
        weightTextField.addKeyListener(enterListener);
        heightTextField.addKeyListener(enterListener);
    }

    private void calculateBmi() {
        double height;
        double weight;

        try {
            height = Double.parseDouble(heightTextField.getText()) / 100; // m
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainPanel, "Wpisano błędny wzrost!", "Błąd!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            weight = Double.parseDouble(weightTextField.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainPanel, "Wpisano błędną wagę!", "Błąd!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double bmi = weight / (height * height);
        String formattedBmi = String.format("%.1f", bmi);

        if (bmi < 18.5) {
            JOptionPane.showMessageDialog(this,
                    "Masz niedowagę.\nTwoje BMI wynosi: " + formattedBmi,
                    "Wynik BMI", JOptionPane.INFORMATION_MESSAGE);
        } else if (bmi < 25) {
            JOptionPane.showMessageDialog(this,
                    "Waga prawidłowa.\nTwoje BMI wynosi: " + formattedBmi,
                    "Wynik BMI", JOptionPane.INFORMATION_MESSAGE);
        } else if (bmi < 30) {
            JOptionPane.showMessageDialog(this,
                    "Masz nadwagę.\nTwoje BMI wynosi: " + formattedBmi,
                    "Wynik BMI", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Masz otyłość!\nTwoje BMI wynosi: " + formattedBmi,
                    "Wynik BMI", JOptionPane.INFORMATION_MESSAGE);
        }

        chartPanel.setPoint(height, weight);
    }

    // ==========================================
    //   PANEL Z TŁAMI BMI + OSIE + PODZIAŁKI
    // ==========================================
    private static class BMIAxisChartPanel extends JPanel {

        private Double height = null; // m
        private Double weight = null; // kg

        public void setPoint(double h, double w) {
            this.height = h;
            this.weight = w;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int W = getWidth();
            int H = getHeight();
            int margin = 50;

            // Zakres osi
            int minH = 140, maxH = 200;
            int minW = 40, maxW = 140;

            // === 1. OSIE ===
            g2.setColor(Color.BLACK);
            g2.drawLine(margin, H - margin, W - margin, H - margin); // X
            g2.drawLine(margin, margin, margin, H - margin);         // Y

            g2.drawString("Wzrost (cm)", W / 2 - 30, H - 10);
//            g2.drawString("Waga (kg)", 5, H / 2);
           g2.drawString("Waga (kg)", 10, 30);
//           Graphics2D g2Rot = (Graphics2D) g2.create();
//            g2Rot.rotate(-Math.PI / 2);
//            g2Rot.drawString("Waga (kg)", (int) (-height / 2 - 20), 20);
//            g2Rot.dispose();

            // === 2. PODZIAŁKI ===
            g2.setFont(new Font("Arial", Font.PLAIN, 10));

            // Oś X (wzrost)
            for (int cm = minH; cm <= maxH; cm += 5) {
                int x = (int) map(cm, minH, maxH, margin, W - margin);
                g2.drawLine(x, H - margin - 5, x, H - margin + 5);
                if (cm % 10 == 0)
                    g2.drawString(cm + "", x - 10, H - margin + 20);
            }

            // Oś Y (waga)
            for (int kg = minW; kg <= maxW; kg += 5) {
                int y = (int) map(kg, minW, maxW, H - margin, margin);
                g2.drawLine(margin - 5, y, margin + 5, y);
                if (kg % 10 == 0)
                    g2.drawString(kg + "", margin - 35, y + 5);
            }

            // === 3. KOLOROWE TŁO BMI ===
            for (int px = margin; px < W - margin; px++) {
                for (int py = margin; py < H - margin; py++) {

                    double heightCm = map(px, margin, W - margin, minH, maxH);
                    double weightKg = map(py, H - margin, margin, minW, maxW);
                    double bmi = (weightKg) / Math.pow(heightCm / 100.0, 2);

                    if (bmi < 18.5)
                        g2.setColor(new Color(170, 190, 255));
                    else if (bmi < 25)
                        g2.setColor(new Color(140, 255, 140));
                    else if (bmi < 30)
                        g2.setColor(new Color(255, 250, 140));
                    else
                        g2.setColor(new Color(255, 150, 150));

                    g2.drawLine(px, py, px, py);
                }
            }

            // === 4. PUNKT UŻYTKOWNIKA ===
            if (height != null && weight != null) {
                int px = (int) map(height * 100, minH, maxH, margin, W - margin);
                int py = (int) map(weight, minW, maxW, H - margin, margin);

                g2.setColor(Color.BLACK);
                g2.fillOval(px - 5, py - 5, 10, 10);
//                g2.setColor(Color.BLACK);
//                String text = String.format("BMI = %.1f", bmiValue);
//                g2.drawString(text, pointX - 20, pointY + 20);
            }
        }

        private double map(double v, double a1, double a2, double b1, double b2) {
            return (v - a1) * (b2 - b1) / (a2 - a1) + b1;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalculatorBMI().setVisible(true));
    }
}
