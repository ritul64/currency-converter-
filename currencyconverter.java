package org2;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.ArrayList;

public class currencyConverter extends JFrame {
    String apiKey = "e42e6b38dbc00bbe28744b74";
    ExchangeRateApi exchangeRateApi;
    ArrayList<String> history;
    Image backgroundImage;

    public currencyConverter() {
        super("CURRENCY CONVERTER");
        setSize(550, 450);
        setLayout(new BorderLayout());
        setResizable(true);

        // Load the background image
        backgroundImage = new ImageIcon("C:\\Users\\ritul\\Downloads\\currency.jpg").getImage();

        // Create a custom panel with the background image
        JPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel centerPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        topPanel.setOpaque(false);
        centerPanel.setOpaque(false);
        bottomPanel.setOpaque(false);

        JLabel titleLabel = createStyledLabel("Currency Converter", 20);
        JLabel amountLabel = createStyledLabel("Input amount:", 12);
        JTextField amountField = new JTextField(10);
        amountField.setPreferredSize(new Dimension(50, 30));  // Adjust size here

        String[] currencies = {"USD", "AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG", "AZN", "BAM", "BBD", "BDT", "BGN", "BHD", "BIF", "BMD", "BND", "BOB", "BRL", "BSD", "BTN", "BWP", "BYN", "BZD", "CAD", "CDF", "CHF", "CLP", "CNY", "COP", "CRC", "CUP", "CVE", "CZK", "DJF", "DKK", "DOP", "DZD", "EGP", "ERN", "ETB", "EUR", "FJD", "FKP", "FOK", "GBP", "GEL", "GGP", "GHS", "GIP", "GMD", "GNF", "GTQ", "GYD", "HKD", "HNL", "HRK", "HTG", "HUF", "IDR", "ILS", "IMP", "INR", "IQD", "IRR", "ISK", "JEP", "JMD", "JOD", "JPY", "KES", "KGS", "KHR", "KID", "KMF", "KRW", "KWD", "KYD", "KZT", "LAK", "LBP", "LKR", "LRD", "LSL", "LYD", "MAD", "MDL", "MGA", "MKD", "MMK", "MNT", "MOP", "MRU", "MUR", "MVR", "MWK", "MXN", "MYR", "MZN", "NAD", "NGN", "NIO", "NOK", "NPR", "NZD", "OMR", "PAB", "PEN", "PGK", "PHP", "PKR", "PLN", "PYG", "QAR", "RON", "RSD", "RUB", "RWF", "SAR", "SBD", "SCR", "SDG", "SEK", "SGD", "SHP", "SLE", "SLL", "SOS", "SRD", "SSP", "STN", "SYP", "SZL", "THB", "TJS", "TMT", "TND", "TOP", "TRY", "TTD", "TVD", "TWD", "TZS", "UAH", "UGX", "UYU", "UZS", "VES", "VND", "VUV", "WST", "XAF", "XCD", "XDR", "XOF", "XPF", "YER", "ZAR", "ZMW", "ZWL"};
        JComboBox<String> currencyDropdown1 = new JComboBox<>(currencies);
        currencyDropdown1.setPreferredSize(new Dimension(100, 30));  // Adjust size here
        JComboBox<String> currencyDropdown2 = new JComboBox<>(currencies);
        currencyDropdown2.setPreferredSize(new Dimension(100, 30));  // Adjust size here

        JLabel resultLabel = createStyledLabel("Conversion result will appear here", 16);
        JButton convertButton = new JButton("Convert");
        JButton quitButton = new JButton("Quit");
        JButton historyButton = new JButton("Show History");

        exchangeRateApi = new ExchangeRateApi(apiKey);
        history = new ArrayList<>();

        convertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                convertCurrency(amountField, currencyDropdown1, currencyDropdown2, resultLabel);
            }
        });

        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        historyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showConversionHistory();
            }
        });

        topPanel.add(titleLabel);

        centerPanel.add(amountLabel);
        centerPanel.add(amountField);
        JLabel fromLabel = createStyledLabel("From:", 12);
        centerPanel.add(fromLabel);
        centerPanel.add(currencyDropdown1);
        JLabel toLabel = createStyledLabel("To:", 12);
        centerPanel.add(toLabel);
        centerPanel.add(currencyDropdown2);
        centerPanel.add(convertButton);

        bottomPanel.add(resultLabel);
        bottomPanel.add(quitButton);
        bottomPanel.add(historyButton);

        backgroundPanel.add(topPanel, BorderLayout.NORTH);
        backgroundPanel.add(centerPanel, BorderLayout.CENTER);
        backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(backgroundPanel);
        //set default to exit the application on close
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen
    }

    private JLabel createStyledLabel(String text, int fontSize) {    //creates jlabel with specified text and style
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, fontSize));
        label.setForeground(Color.BLACK);
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setBorder(new EmptyBorder(5, 5, 5, 5)); // creates space between text and border padding
        return label;
    }

    private void convertCurrency(JTextField amountField, JComboBox<String> currencyDropdown1, JComboBox<String> currencyDropdown2, JLabel resultLabel) {
        String inputCurrency = (String) currencyDropdown1.getSelectedItem();
        String outputCurrency = (String) currencyDropdown2.getSelectedItem();
        String amountText = amountField.getText();

        if (amountText.isEmpty()) {
            resultLabel.setText("Please enter an amount.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException ex) {
            resultLabel.setText("Invalid amount.");
            return;
        }

        ExchangeRateApi obj = new ExchangeRateApi(apiKey);
        HashMap<String, Double> map = exchangeRateApi.getExchangeRates();
        double result = converter(inputCurrency, outputCurrency, amount, map);

        if (result == -1) {
            resultLabel.setText("Invalid input.");
        } else {
            String conversionResult = String.format("%.2f %s = %.2f %s", amount, inputCurrency, result, outputCurrency);
            resultLabel.setText("<html><div style='text-align: center;'>" + conversionResult + "<br>Last Updated: " + obj.getdate() + "</div></html>");
            history.add(conversionResult);
        }
    }

    private double converter(String input, String output, double amount, HashMap<String, Double> map) {
        if (input.equals(output) || amount == 0)
            return amount;
        if (amount < 0)
            return -1;
        else {
            double inDollar = any_to_dollar(input, amount, map);
            return dollar_to_any(output, inDollar, map);
        }
    }

    private double any_to_dollar(String input, double amt, HashMap<String, Double> map) {
        Double val = map.get(input);
        return (1 / val) * amt;
    }

    private double dollar_to_any(String output, double amt, HashMap<String, Double> map) {
        Double val = map.get(output);
        return val * (amt);
    }

    private void showConversionHistory() {
        StringBuilder historyText = new StringBuilder("Conversion History:\n");
        for (String conversion : history) {
            historyText.append(conversion).append("\n");
        }
        JOptionPane.showMessageDialog(null, historyText.toString(), "Conversion History", JOptionPane.INFORMATION_MESSAGE);
    }

    // Custom JPanel class to draw the background image
    class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            currencyConverter gui = new currencyConverter();
            gui.setVisible(true);
        });
    }
}
