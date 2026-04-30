// ===============================
// LoginFrame.java
// UPDATED (ROLE REMOVED)
// username + password only
// ===============================
package ui;

import service.ApiService;
import util.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginFrame extends JFrame {

    JTextField txtUser;
    JPasswordField txtPass;

    public LoginFrame() {

        setTitle("Login");
        setSize(950,520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new LoginBackground());
        setLayout(null);

        // LEFT SIDE TEXT
        JLabel title1 = new JLabel("Find Your Dream Job");
        title1.setBounds(55,360,340,35);
        title1.setFont(new Font("Segoe UI",Font.BOLD,30));
        title1.setForeground(new Color(0,170,255));
        add(title1);

        JLabel title2 = new JLabel("Your career journey starts here");
        title2.setBounds(60,405,320,25);
        title2.setFont(new Font("Segoe UI",Font.PLAIN,18));
        title2.setForeground(Color.WHITE);
        add(title2);

        // RIGHT PANEL
        JPanel panel = new JPanel();
        panel.setBounds(500,55,380,390);
        panel.setLayout(null);
        panel.setOpaque(false);
        add(panel);

        JLabel heading = new JLabel("Welcome Back");
        heading.setBounds(80,10,250,35);
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Segoe UI",Font.BOLD,28));
        panel.add(heading);

        JLabel sub = new JLabel("Login to your account");
        sub.setBounds(110,45,180,20);
        sub.setForeground(new Color(220,220,220));
        panel.add(sub);

        // USERNAME
        JLabel lbl1 = new JLabel("Username");
        lbl1.setBounds(40,95,120,20);
        lbl1.setForeground(Color.WHITE);
        lbl1.setFont(new Font("Segoe UI",Font.BOLD,16));
        panel.add(lbl1);

        txtUser = new JTextField();
        txtUser.setBounds(40,120,300,40);
        styleField(txtUser);
        panel.add(txtUser);

        // PASSWORD
        JLabel lbl2 = new JLabel("Password");
        lbl2.setBounds(40,185,120,20);
        lbl2.setForeground(Color.WHITE);
        lbl2.setFont(new Font("Segoe UI",Font.BOLD,16));
        panel.add(lbl2);

        txtPass = new JPasswordField();
        txtPass.setBounds(40,210,300,40);
        stylePasswordField(txtPass);
        panel.add(txtPass);

        // LOGIN BUTTON
        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(40,285,300,42);
        styleButton(loginBtn);
        panel.add(loginBtn);

        loginBtn.addActionListener(e -> doLogin());

        // SIGNUP
        JLabel signText =
                new JLabel("Don't have an account?");
        signText.setBounds(75,345,160,20);
        signText.setForeground(Color.WHITE);
        panel.add(signText);

        JButton signupBtn = new JButton("Sign Up");
        signupBtn.setBounds(230,340,110,28);
        styleSmallButton(signupBtn);
        panel.add(signupBtn);

        signupBtn.addActionListener(e -> {
            dispose();
            new SignupFrame();
        });

        setVisible(true);
    }

    // ===============================
    // LOGIN
    // ===============================
    private void doLogin(){

        String username =
                txtUser.getText().trim();

        String password =
                String.valueOf(
                        txtPass.getPassword()
                ).trim();

        String response =
                ApiService.login(
                        username,
                        password
                );

        if(response.contains("token")){

            Session.token =
                    extractValue(response,"token");

            Session.role =
                    extractValue(response,"role");

            Session.username =
                    extractValue(response,"username");

            Session.tenantId =
                    extractValue(response,"tenantId");

            dispose();

            if(Session.role.equalsIgnoreCase("RECRUITER")){
                new RecruiterDashboard();
            }else{
                new StudentDashboard();
            }

        }else{
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid Login"
            );
        }
    }

    // ===============================
    // JSON VALUE
    // ===============================
    private String extractValue(
            String text,
            String key){

        try{

            int start =
                    text.indexOf(
                            "\"" + key + "\":\""
                    );

            if(start != -1){

                start =
                        start +
                                key.length() + 4;

                int end =
                        text.indexOf(
                                "\"",
                                start
                        );

                return text.substring(
                        start,
                        end
                );
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return "";
    }

    // ===============================
    // BUTTON STYLE
    // ===============================
    private void styleButton(JButton btn){

        btn.setFocusPainted(false);
        btn.setBorder(null);
        btn.setForeground(Color.WHITE);
        btn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        btn.setBackground(
                new Color(0,140,255)
        );

        btn.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        btn.addMouseListener(
                new MouseAdapter() {

                    public void mouseEntered(
                            MouseEvent e){

                        btn.setBackground(
                                new Color(
                                        255,70,170
                                )
                        );
                    }

                    public void mouseExited(
                            MouseEvent e){

                        btn.setBackground(
                                new Color(
                                        0,140,255
                                )
                        );
                    }
                }
        );
    }

    private void styleSmallButton(JButton btn){

        btn.setFocusPainted(false);
        btn.setBorder(null);
        btn.setForeground(Color.WHITE);
        btn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        btn.setBackground(
                new Color(255,70,170)
        );

        btn.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }

    private void styleField(JTextField txt){

        txt.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        17
                )
        );

        txt.setForeground(Color.WHITE);
        txt.setCaretColor(Color.WHITE);

        txt.setBackground(
                new Color(
                        255,255,255,30
                )
        );

        txt.setBorder(
                BorderFactory
                        .createCompoundBorder(
                                BorderFactory
                                        .createLineBorder(
                                                Color.WHITE,1
                                        ),
                                BorderFactory
                                        .createEmptyBorder(
                                                5,12,5,12
                                        )
                        )
        );
    }

    private void stylePasswordField(
            JPasswordField txt){

        txt.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        17
                )
        );

        txt.setForeground(Color.WHITE);
        txt.setCaretColor(Color.WHITE);
        txt.setEchoChar('•');

        txt.setBackground(
                new Color(
                        255,255,255,30
                )
        );

        txt.setBorder(
                BorderFactory
                        .createCompoundBorder(
                                BorderFactory
                                        .createLineBorder(
                                                Color.WHITE,1
                                        ),
                                BorderFactory
                                        .createEmptyBorder(
                                                5,12,5,12
                                        )
                        )
        );
    }
}

// ===============================
// BACKGROUND
// ===============================
class LoginBackground extends JPanel {

    protected void paintComponent(Graphics g){

        super.paintComponent(g);

        Graphics2D g2 =
                (Graphics2D) g;

        GradientPaint gp =
                new GradientPaint(
                        0,0,
                        new Color(8,20,55),
                        getWidth(),
                        getHeight(),
                        new Color(28,12,70)
                );

        g2.setPaint(gp);
        g2.fillRect(
                0,0,
                getWidth(),
                getHeight()
        );

        g2.setColor(
                new Color(
                        0,170,255,90
                )
        );

        g2.fillOval(
                80,120,240,240
        );

        g2.setColor(
                new Color(
                        255,70,170,70
                )
        );

        g2.fillOval(
                220,70,170,170
        );

        g2.setColor(Color.WHITE);
        g2.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        120
                )
        );

        g2.drawString(
                "💼",
                135,
                255
        );
    }
}