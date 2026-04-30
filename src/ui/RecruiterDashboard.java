package ui;

import service.ApiService;
import util.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RecruiterDashboard extends JFrame {

    public RecruiterDashboard() {

        setTitle("Recruiter Dashboard");
        setSize(560,520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new RecruiterBackground());
        setLayout(null);

        JLabel label = new JLabel("Welcome " + Session.username);
        label.setBounds(120,25,320,40);
        label.setFont(new Font("Segoe UI", Font.BOLD, 30));
        label.setForeground(Color.WHITE);
        add(label);

        JLabel sub = new JLabel("Manage Jobs & Applicants");
        sub.setBounds(165,62,220,20);
        sub.setForeground(new Color(220,220,220));
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(sub);

        JButton btn1 = new JButton("Post Job");
        btn1.setBounds(165,115,220,42);

        JButton btn2 = new JButton("My Posted Jobs");
        btn2.setBounds(165,175,220,42);

        JButton btn3 = new JButton("View Applicants");
        btn3.setBounds(165,235,220,42);

        JButton btn4 = new JButton("Logout");
        btn4.setBounds(165,350,220,42);

        add(btn1);
        add(btn2);
        add(btn3);
        add(btn4);

        styleButton(btn1);
        styleButton(btn2);
        styleButton(btn3);
        styleButton(btn4);

        btn1.addActionListener(e -> openPostJobForm());

        btn2.addActionListener(e -> {
            String data = ApiService.myPostedJobs();
            showJobsTable(data);
        });

        btn3.addActionListener(e -> {
            String data = ApiService.myApplicants();
            showApplicantsTable(data);
        });

        btn4.addActionListener(e -> {

            Session.token = "";
            Session.username = "";
            Session.role = "";
            Session.tenantId = "";

            dispose();
            new LoginFrame();
        });

        setVisible(true);
    }

    // =====================================================
    // POST JOB FORM
    // =====================================================
    private void openPostJobForm() {

        JFrame form = new JFrame("Create New Job");
        form.setSize(600,760);
        form.setLocationRelativeTo(this);
        form.setContentPane(new FormBackground());
        form.setLayout(null);

        JLabel head = new JLabel("Create New Job");
        head.setBounds(175,18,260,40);
        head.setFont(new Font("Segoe UI", Font.BOLD, 30));
        head.setForeground(Color.WHITE);
        form.add(head);

        JTextField jobId   = createField(form,"Job ID",80);
        JTextField title   = createField(form,"Job Title",145);
        JTextField company = createField(form,"Company Name",210);
        JTextField city    = createField(form,"City",275);
        JTextField type    = createField(form,"Job Type",340);
        JTextField mode    = createField(form,"Work Mode",405);
        JTextField salary  = createField(form,"Salary",470);
        JTextField email   = createField(form,"Email",535);

        JButton submit = new JButton("SUBMIT JOB");
        submit.setBounds(170,630,240,48);
        stylePinkButton(submit);
        form.add(submit);

        submit.addActionListener(e -> {

            String msg = ApiService.postJob(
                    jobId.getText(),
                    title.getText(),
                    company.getText(),
                    city.getText(),
                    type.getText(),
                    mode.getText(),
                    salary.getText(),
                    email.getText()
            );

            JOptionPane.showMessageDialog(form,msg);

            if(msg.toLowerCase().contains("success")){
                form.dispose();
            }
        });

        form.setVisible(true);
    }

    // =====================================================
    // FIELD DESIGN
    // =====================================================
    private JTextField createField(JFrame form,String text,int y){

        JLabel lbl = new JLabel(text);
        lbl.setBounds(70,y,200,18);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI",Font.BOLD,14));
        form.add(lbl);

        JTextField field = new JTextField();
        field.setBounds(70,y+22,450,36);
        field.setFont(new Font("Segoe UI",Font.PLAIN,15));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setOpaque(false);

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255,255,255,120),1),
                BorderFactory.createEmptyBorder(5,12,5,12)
        ));

        form.add(field);

        return field;
    }

    // =====================================================
    // BUTTON STYLE
    // =====================================================
    private void styleButton(JButton btn){

        btn.setFocusPainted(false);
        btn.setBorder(null);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI",Font.BOLD,16));
        btn.setBackground(new Color(0,145,255));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e){
                btn.setBackground(new Color(255,70,170));
            }
            public void mouseExited(MouseEvent e){
                btn.setBackground(new Color(0,145,255));
            }
        });
    }

    private void stylePinkButton(JButton btn){

        btn.setFocusPainted(false);
        btn.setBorder(null);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI",Font.BOLD,18));
        btn.setBackground(new Color(255,70,170));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e){
                btn.setBackground(new Color(0,170,255));
            }
            public void mouseExited(MouseEvent e){
                btn.setBackground(new Color(255,70,170));
            }
        });
    }

    // =====================================================
    // JOBS TABLE
    // =====================================================
    private void showJobsTable(String data){

        String[] cols = {
                "Job ID","Title","Company","City",
                "Type","Mode","Salary"
        };

        DefaultTableModel model =
                new DefaultTableModel(cols,0);

        String[] rows = splitRows(data);

        for(String row : rows){

            if(row.trim().isEmpty()) continue;

            model.addRow(new Object[]{
                    getValue(row,"jobId"),
                    getValue(row,"jobTitle"),
                    getValue(row,"companyName"),
                    getValue(row,"city"),
                    getValue(row,"jobType"),
                    getValue(row,"workMode"),
                    getValue(row,"salary")
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(28);

        JFrame frame = new JFrame("My Posted Jobs");
        frame.setSize(950,420);
        frame.add(new JScrollPane(table));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // =====================================================
    // APPLICANTS TABLE
    // =====================================================
    private void showApplicantsTable(String data){

        String[] cols = {
                "Username","Name","Email",
                "Job ID","Job Title","Status"
        };

        DefaultTableModel model =
                new DefaultTableModel(cols,0);

        String[] rows = splitRows(data);

        for(String row : rows){

            if(row.trim().isEmpty()) continue;

            model.addRow(new Object[]{
                    getValue(row,"studentUsername"),
                    getValue(row,"studentName"),
                    getValue(row,"email"),
                    getValue(row,"jobId"),
                    getValue(row,"jobTitle"),
                    getValue(row,"status")
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(28);

        JFrame frame = new JFrame("Applicants");
        frame.setSize(1000,430);
        frame.add(new JScrollPane(table));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // =====================================================
    // JSON SPLIT
    // =====================================================
    private String[] splitRows(String data){

        data = data.replace("[","");
        data = data.replace("]","");
        data = data.replace("},{","}SPLIT{");

        return data.split("SPLIT");
    }

    // =====================================================
    // JSON VALUE
    // =====================================================
    private String getValue(String row,String key){

        try{
            String find = "\"" + key + "\":\"";
            int start = row.indexOf(find);

            if(start != -1){
                start += find.length();
                int end = row.indexOf("\"",start);
                return row.substring(start,end);
            }

            find = "\"" + key + "\":";
            start = row.indexOf(find);

            if(start != -1){
                start += find.length();
                int end = row.indexOf(",",start);

                if(end == -1)
                    end = row.indexOf("}",start);

                return row.substring(start,end);
            }

        }catch(Exception e){}

        return "";
    }
}

// =====================================================
// DASHBOARD BACKGROUND
// =====================================================
class RecruiterBackground extends JPanel {

    protected void paintComponent(Graphics g){

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        GradientPaint gp = new GradientPaint(
                0,0,new Color(8,20,55),
                getWidth(),getHeight(),
                new Color(20,60,110));

        g2.setPaint(gp);
        g2.fillRect(0,0,getWidth(),getHeight());

        g2.setColor(new Color(255,255,255,25));
        g2.fillOval(35,70,95,95);
        g2.fillOval(410,115,95,95);
        g2.fillOval(105,360,110,110);
        g2.fillOval(405,345,75,75);
    }
}

// =====================================================
// FORM BACKGROUND
// =====================================================
class FormBackground extends JPanel {

    protected void paintComponent(Graphics g){

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        GradientPaint gp = new GradientPaint(
                0,0,new Color(8,15,45),
                getWidth(),getHeight(),
                new Color(35,10,65));

        g2.setPaint(gp);
        g2.fillRect(0,0,getWidth(),getHeight());

        g2.setColor(new Color(255,255,255,20));
        g2.fillOval(420,60,120,120);
        g2.fillOval(40,520,140,140);
        g2.fillOval(430,540,80,80);
    }
}