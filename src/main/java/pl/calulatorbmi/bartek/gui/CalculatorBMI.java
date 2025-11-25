package pl.calulatorbmi.bartek.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CalculatorBMI  extends JFrame {

    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel weightLabel;
    private JLabel heightLabel;
    private JTextField weightTextField;
    private JTextField heightTextField;
    private JButton calculateButton;

    public CalculatorBMI() throws HeadlessException {
        setTitle("Calculator BMI");
        setSize(350, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);


        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateBmi();
            }

        });

        heightTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyPressed = e.getKeyCode();
                if (KeyEvent.VK_ENTER == keyPressed) {
                    calculateBmi();
                }
            }
        });

        weightTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyPressed = e.getKeyCode();
                if (KeyEvent.VK_ENTER == keyPressed) {
                    calculateBmi();
                }
            }
        });

    }

    private void calculateBmi() {
        double height = 0;
        double weight = 0;

        try {
            height = Double.valueOf(heightTextField.getText()) / 100;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainPanel, "Wpisano błędny wzrost!", "Błąd!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            weight = Double.valueOf(weightTextField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainPanel, "Wpisano błędną wagę!", "Błąd!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double bmi = weight / (height * height);

        if (bmi < 18.5) {
            JOptionPane.showMessageDialog(CalculatorBMI.this, "Masz niedowagę!", "Wynik BMI", JOptionPane.INFORMATION_MESSAGE);

        } else if (bmi < 25) {
            JOptionPane.showMessageDialog(CalculatorBMI.this, "Waga prawidłowa.", "Wynik BMI", JOptionPane.INFORMATION_MESSAGE);

        } else if (bmi < 30) {
            JOptionPane.showMessageDialog(CalculatorBMI.this, "Masz nadwagę!", "Wynik BMI", JOptionPane.INFORMATION_MESSAGE);

        } else {
            JOptionPane.showMessageDialog(CalculatorBMI.this, "Masz otyłość!", "Wynik BMI", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalculatorBMI().setVisible(true));
    }
}
