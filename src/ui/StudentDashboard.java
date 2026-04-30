// ===============================
// StudentDashboard.java
// Ultra Stylish Premium Version
// ===============================
package ui;

import service.ApiService;
import util.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StudentDashboard extends JFrame {

    public StudentDashboard() {

        setTitle("Student Dashboard");
        setSize(560,560);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new BackgroundPanel());
        setLayout(null);

        JLabel label = new JLabel("Welcome " + Session.username);
        label.setBounds(130,25,320,40);
        label.setFont(new Font("Segoe UI", Font.BOLD, 28));
        label.setForeground(Color.WHITE);
        add(label);

        JLabel sub = new JLabel("Find Your Dream Job Today");
        sub.setBounds(165,60,250,20);
        sub.setForeground(new Color(220,220,220));
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        add(sub);

        JButton btn1 = new JButton("View Recruiter Jobs");
        btn1.setBounds(160,105,230,42);

        JButton btn2 = new JButton("All Jobs");
        btn2.setBounds(160,155,230,42);

        JButton btn3 = new JButton("Search Job By ID");
        btn3.setBounds(160,205,230,42);

        JButton btn4 = new JButton("Apply Job");
        btn4.setBounds(160,255,230,42);

        JButton btn6 = new JButton("My Applied Jobs");
        btn6.setBounds(160,305,230,42);

        JButton btn5 = new JButton("Logout");
        btn5.setBounds(160,390,230,42);

        add(btn1); add(btn2); add(btn3);
        add(btn4); add(btn6); add(btn5);

        styleButton(btn1);
        styleButton(btn2);
        styleButton(btn3);
        styleButton(btn4);
        styleButton(btn5);
        styleButton(btn6);

        // Recruiter Jobs
        btn1.addActionListener(e -> {

            String tenantId =
                    JOptionPane.showInputDialog("Enter Recruiter TenantId");

            if(tenantId == null || tenantId.trim().isEmpty()) return;

            String data = ApiService.getJobsByTenant(tenantId);

            if(data.contains("ERROR")) {
                JOptionPane.showMessageDialog(null,"Error");
                return;
            }

            showJobsTable(data);
        });

        // All Jobs
        btn2.addActionListener(e -> {

            String data = ApiService.getAllJobs();

            if(data.contains("ERROR")) {
                JOptionPane.showMessageDialog(null,"Error");
                return;
            }

            showJobsTable(data);
        });

        // Search Job
        btn3.addActionListener(e -> {

            String jobId =
                    JOptionPane.showInputDialog("Enter Job ID");

            if(jobId == null || jobId.trim().isEmpty()) return;

            String data = ApiService.getJobById(jobId);

            if(data.contains("ERROR")) {
                JOptionPane.showMessageDialog(null,"Job Not Found");
                return;
            }

            showJobsTable("[" + data + "]");
        });

        // Apply Job
        btn4.addActionListener(e -> {

            String jobId =
                    JOptionPane.showInputDialog("Enter Job ID");

            if(jobId == null || jobId.trim().isEmpty()) return;

            String msg = ApiService.applyJob(jobId);

            JOptionPane.showMessageDialog(null,msg);
        });

        // Applied Jobs
        btn6.addActionListener(e -> {

            String data = ApiService.myAppliedJobs();

            if(data.contains("ERROR")) {
                JOptionPane.showMessageDialog(null,"Unable to fetch");
                return;
            }

            showAppliedJobsTable(data);
        });

        // Logout
        btn5.addActionListener(e -> {

            Session.token="";
            Session.role="";
            Session.username="";
            Session.tenantId="";

            dispose();
            new LoginFrame();
        });

        setVisible(true);
    }

    // ===============================
    // Stylish Button
    // ===============================
    private void styleButton(JButton btn){

        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(0,153,255));
        btn.setBorder(BorderFactory.createLineBorder(
                new Color(255,255,255,60),1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(255,102,0));
                btn.setBounds(
                        btn.getX()-5,
                        btn.getY(),
                        btn.getWidth()+10,
                        btn.getHeight()
                );
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(0,153,255));
                btn.setBounds(
                        btn.getX()+5,
                        btn.getY(),
                        btn.getWidth()-10,
                        btn.getHeight()
                );
            }
        });
    }

    // ===============================
    // JOB TABLE
    // ===============================
    private void showJobsTable(String data){

        String[] columns = {
                "Job ID","Title","Company","City","Salary"
        };

        DefaultTableModel model =
                new DefaultTableModel(columns,0);

        data = data.replace("[","")
                .replace("]","")
                .replace("},{","}split{");

        String[] jobs = data.split("split");

        for(String job : jobs){

            if(job.trim().isEmpty()) continue;

            model.addRow(new Object[]{

                    getValue(job,"jobId"),
                    getValue(job,"jobTitle"),
                    getValue(job,"companyName"),
                    getValue(job,"city"),
                    getValue(job,"salary")
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI",Font.PLAIN,13));
        table.getTableHeader().setFont(
                new Font("Segoe UI",Font.BOLD,14));

        JFrame frame = new JFrame("Jobs");
        frame.setSize(850,400);
        frame.add(new JScrollPane(table));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ===============================
    // Applied Jobs
    // ===============================
    private void showAppliedJobsTable(String data){

        String[] columns = {
                "Job ID","Title","Company","Status"
        };

        DefaultTableModel model =
                new DefaultTableModel(columns,0);

        data = data.replace("[","")
                .replace("]","")
                .replace("},{","}split{");

        String[] jobs = data.split("split");

        for(String job : jobs){

            if(job.trim().isEmpty()) continue;

            model.addRow(new Object[]{

                    getValue(job,"jobId"),
                    getValue(job,"jobTitle"),
                    getValue(job,"companyName"),
                    getValue(job,"status")
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(28);

        JFrame frame = new JFrame("My Applied Jobs");
        frame.setSize(700,380);
        frame.add(new JScrollPane(table));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ===============================
    // JSON Value Extractor
    // ===============================
    private String getValue(String text,String key){

        try{

            int start =
                    text.indexOf("\""+key+"\":\"");

            if(start!=-1){

                start += key.length()+4;

                int end =
                        text.indexOf("\"",start);

                return text.substring(start,end);
            }

            start =
                    text.indexOf("\""+key+"\":" );

            if(start!=-1){

                start += key.length()+3;

                int end =
                        text.indexOf(",",start);

                if(end==-1)
                    end=text.indexOf("}",start);

                return text.substring(start,end);
            }

        }catch(Exception e){}

        return "";
    }
}

// ===============================
// Premium Gradient Background
// ===============================
class BackgroundPanel extends JPanel {

    protected void paintComponent(Graphics g){

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        GradientPaint gp = new GradientPaint(
                0,0,new Color(15,32,39),
                getWidth(),getHeight(),
                new Color(32,58,67)
        );

        g2d.setPaint(gp);
        g2d.fillRect(0,0,getWidth(),getHeight());

        g2d.setColor(new Color(255,255,255,15));

        for(int i=0;i<15;i++){
            g2d.fillOval(
                    (int)(Math.random()*getWidth()),
                    (int)(Math.random()*getHeight()),
                    80,80
            );
        }
    }
}