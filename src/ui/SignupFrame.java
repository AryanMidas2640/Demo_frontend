package ui;

import service.ApiService;

import javax.swing.*;
import java.awt.*;

public class SignupFrame extends JFrame {

    JTextField txtUser;
    JPasswordField txtPass;
    JComboBox<String> roleBox;

    public SignupFrame() {

        setTitle("Signup");
        setSize(500,420);
        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().setBackground(new Color(20,20,45));

        JLabel head = new JLabel("Create Account");
        head.setBounds(130,20,250,35);
        head.setForeground(Color.WHITE);
        head.setFont(new Font("Segoe UI",Font.BOLD,28));
        add(head);

        JLabel l1 = new JLabel("Username");
        l1.setBounds(60,90,120,20);
        l1.setForeground(Color.WHITE);
        add(l1);

        txtUser = new JTextField();
        txtUser.setBounds(60,115,350,35);
        add(txtUser);

        JLabel l2 = new JLabel("Password");
        l2.setBounds(60,165,120,20);
        l2.setForeground(Color.WHITE);
        add(l2);

        txtPass = new JPasswordField();
        txtPass.setBounds(60,190,350,35);
        add(txtPass);

        JLabel l3 = new JLabel("Role");
        l3.setBounds(60,240,120,20);
        l3.setForeground(Color.WHITE);
        add(l3);

        roleBox = new JComboBox<>(new String[]{
                "STUDENT",
                "RECRUITER"
        });

        roleBox.setBounds(60,265,350,35);
        add(roleBox);

        JButton btn = new JButton("Signup");
        btn.setBounds(60,320,350,40);
        btn.setBackground(new Color(0,140,255));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        add(btn);

        btn.addActionListener(e -> doSignup());

        setVisible(true);
    }

    private void doSignup() {

        String username =
                txtUser.getText().trim();

        String password =
                String.valueOf(
                        txtPass.getPassword()
                ).trim();

        String role =
                roleBox.getSelectedItem()
                        .toString();

        String response =
                ApiService.signup(
                        username,
                        password,
                        role
                );

        // SHORT CLEAN POPUP
        if(response.contains("\"success\":true")
                || response.toLowerCase().contains("success")){

            JOptionPane.showMessageDialog(
                    this,
                    "Signup Successful ✅"
            );

            dispose();
            new LoginFrame();

        }else{

            JOptionPane.showMessageDialog(
                    this,
                    "Signup Failed ❌"
            );
        }
    }
}