package com.finalgrade;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MainPage {
    public static void main(String[] args) {
        // Frame setup
        JFrame frame = new JFrame("Final Grade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setLayout(null);

        // Panel setup
        JPanel panel = new GradientPanel();
        panel.setBounds(0, 0, 400, 600);
        panel.setLayout(null);
        frame.add(panel);

        // Title Label
        JLabel titleLabel = new JLabel("Final Grade", SwingConstants.CENTER);
        titleLabel.setBounds(50, 50, 300, 40);
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 40));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Enter your personal data to create your account.", SwingConstants.CENTER);
        subtitleLabel.setBounds(50, 100, 300, 20);
        subtitleLabel.setFont(new Font("Montserrat", Font.PLAIN, 12));
        subtitleLabel.setForeground(Color.WHITE);
        panel.add(subtitleLabel);

        // Email Field
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setBounds(50, 150, 300, 20);
        emailLabel.setFont(new Font("Montserrat", Font.PLAIN, 14));
        emailLabel.setForeground(Color.WHITE);
        panel.add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(50, 180, 300, 30);
        panel.add(emailField);

        // Password Field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(50, 220, 300, 20);
        passwordLabel.setFont(new Font("Montserrat", Font.PLAIN, 14));
        passwordLabel.setForeground(Color.WHITE);
        panel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(50, 250, 300, 30);
        panel.add(passwordField);

        JLabel passwordHint = new JLabel("Must be at least 8 characters.");
        passwordHint.setBounds(50, 290, 300, 20);
        passwordHint.setFont(new Font("Montserrat", Font.PLAIN, 10));
        passwordHint.setForeground(Color.WHITE);
        panel.add(passwordHint);

        // Button setup
        JButton loginButton = new JButton("Masuk");
        loginButton.setBounds(150, 350, 100, 40);
        loginButton.setBackground(Color.BLACK);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Montserrat", Font.BOLD, 14));

        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            // Validate email contains @gmail.com
            if (!email.endsWith("@gmail.com")) {
                JOptionPane.showMessageDialog(frame, "Email harus diakhiri dengan @gmail.com.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (password.length() < 8) {
                JOptionPane.showMessageDialog(frame, "Password harus minimal 8 karakter.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(frame, "Login berhasil dengan email: " + email);

            frame.dispose(); // Close current frame
            InputPage inputPage = new InputPage();
            inputPage.showInputPage();
        });

        panel.add(loginButton);

        // Show frame
        frame.setVisible(true);
    }
}

class GradientPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        Color[] colors = {
                new Color(0x3F0B93), new Color(0x8646EE), new Color(0x9909DD)
        };
        float[] fractions = {0.0f, 0.5f, 1.0f};
        LinearGradientPaint gradientPaint = new LinearGradientPaint(
                0, 0, getWidth(), getHeight(), fractions, colors);
        g2d.setPaint(gradientPaint);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}

class InputPage {
    private final Map<String, Integer> userData = new HashMap<>();
    private final DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Nama", "Nilai", "Grade"}, 0);

    public void showInputPage() {
        // Frame setup
        JFrame frame = new JFrame("Input Nilai");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setLayout(null);

        // Panel setup
        JPanel panel = new GradientPanel();
        panel.setBounds(0, 0, 400, 600);
        panel.setLayout(null);
        frame.add(panel);

        // Title Label
        JLabel titleLabel = new JLabel("Masukkan Nama dan Nilai", SwingConstants.CENTER);
        titleLabel.setBounds(50, 50, 300, 40);
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel);

        // Input Fields
        JLabel nameLabel = new JLabel("Nama");
        nameLabel.setBounds(50, 150, 300, 20);
        nameLabel.setFont(new Font("Montserrat", Font.PLAIN, 14));
        nameLabel.setForeground(Color.WHITE);
        panel.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(50, 180, 300, 30);
        panel.add(nameField);

        JLabel scoreLabel = new JLabel("Nilai");
        scoreLabel.setBounds(50, 220, 300, 20);
        scoreLabel.setFont(new Font("Montserrat", Font.PLAIN, 14));
        scoreLabel.setForeground(Color.WHITE);
        panel.add(scoreLabel);

        JTextField scoreField = new JTextField();
        scoreField.setBounds(50, 250, 300, 30);
        panel.add(scoreField);

        // Table setup
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 360, 300, 150);
        panel.add(scrollPane);

        // Buttons
        JButton submitButton = new JButton("Hasil");
        submitButton.setBounds(50, 300, 100, 40);
        submitButton.setBackground(Color.WHITE);
        submitButton.setForeground(Color.BLACK);
        submitButton.setFocusPainted(false);
        submitButton.setFont(new Font("Montserrat", Font.PLAIN, 14));

        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            String scoreText = scoreField.getText();

            try {
                validateName(name);
                int score = Integer.parseInt(scoreText);
                validateScore(score);

                userData.put(name, score);

                String grade = calculateGrade(score);
                tableModel.addRow(new Object[]{name, score, grade});

                String response = ApiService.sendUserData(name, score);
                JOptionPane.showMessageDialog(frame, "Nama: " + name + "\nNilai: " + score + "\nGrade: " + grade + "\n\n" + response);

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Input tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(submitButton);

        JButton updateButton = new JButton("Update");
        updateButton.setBounds(170, 520, 100, 40);
        updateButton.setBackground(Color.BLUE);
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.setFont(new Font("Montserrat", Font.PLAIN, 14));

        updateButton.addActionListener(e -> {
            String name = nameField.getText();
            String scoreText = scoreField.getText();

            try {
                if (!userData.containsKey(name)) {
                    throw new IllegalArgumentException("Nama tidak ditemukan. Tidak dapat mengupdate nilai.");
                }

                int newScore = Integer.parseInt(scoreText);
                validateScore(newScore);

                userData.put(name, newScore);

                String newGrade = calculateGrade(newScore);

                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    if (tableModel.getValueAt(i, 0).equals(name)) {
                        tableModel.setValueAt(newScore, i, 1);
                        tableModel.setValueAt(newGrade, i, 2);
                        break;
                    }
                }

                JOptionPane.showMessageDialog(frame, "Nilai untuk nama " + name + " telah diperbarui menjadi: " + newScore);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Input tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(updateButton);

        JButton clearButton = new JButton("Clear");
        clearButton.setBounds(290, 520, 100, 40);
        clearButton.setBackground(Color.RED);
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.setFont(new Font("Montserrat", Font.PLAIN, 14));

        clearButton.addActionListener(e -> {
            nameField.setText("");
            scoreField.setText("");

            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                tableModel.removeRow(selectedRow);
            }
        });

        panel.add(clearButton);

        // Show frame
        frame.setVisible(true);
    }

    public String calculateGrade(int score) {
        if (score >= 90) return "A";
        else if (score >= 80) return "B";
        else if (score >= 70) return "C";
        else if (score >= 60) return "D";
        else return "E";
    }

    public void validateName(String name) {
        if (!name.matches("[a-zA-Z ]+")) {
            throw new IllegalArgumentException("Nama hanya boleh berupa huruf.");
        }
    }

    public void validateScore(int score) {
        if (score > 100) {
            throw new IllegalArgumentException("Nilai yang diinput maksimal 100.");
        }
    }
}

class ApiService {
    public static String sendUserData(String name, int score) {
        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/posts"); // Dummy API
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            // Create JSON payload
            String jsonInput = "{\"name\":\"" + name + "\",\"score\":" + score + "}";

            // Write JSON to request body
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Get response code
            int code = conn.getResponseCode();
            return (code == 201) ? "Data berhasil dikirim ke server!" : "Gagal mengirim data. Kode: " + code;

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
