import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;




class Login extends JFrame implements ActionListener {
    private JLabel email;
    static JTextField typedemail;
    private JLabel password;
    static JPasswordField typedpassword;
    private JButton login;
    public static String s;
    static boolean success;
    public String getEmail() {
        return typedemail.getText();
    }
    public static String getPassword() {
        return String.valueOf(typedpassword.getPassword());
    }
    // constructor
    public Login(){
    	getContentPane().setBackground(Color.WHITE);
        setPreferredSize(new Dimension(428, 370));
        setResizable(false);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        setForeground(Color.red);

        success = false;
        email = new JLabel("Email");
        email.setForeground(Color.LIGHT_GRAY);
        email.setSize(110, 45);
        email.setFont(new Font("Verdana", Font.PLAIN, 31));
        email.setLocation(20, 15);
        getContentPane().add(email);

        typedemail = new JTextField();
        typedemail.setSize(365, 35);
        typedemail.setLocation(29, 62);
        typedemail.setBorder(new MatteBorder(0, 0, 2, 0, Color.gray));
        getContentPane().add(typedemail);

        password = new JLabel("Password");
        password.setForeground(Color.LIGHT_GRAY);
        password.setFont(new Font("Verdana", Font.PLAIN, 30));
        password.setSize(200, 45);
        password.setLocation(20, 120);
        getContentPane().add(password);

        typedpassword = new JPasswordField();
        typedpassword.setSize(365, 35);
        typedpassword.setLocation(29, 168);
        typedpassword.setBorder(new MatteBorder(0, 0, 2, 0, Color.gray));
        getContentPane().add(typedpassword);

        // creating the login button which will login the user to the system once he puts his details and presses the button
        login = new JButton("Login");
        login.setFont(new Font("Verdana", Font.PLAIN, 30));
        login.setSize(365, 50);
        login.setLocation(26, 229);
        login.addActionListener(this);
        getContentPane().add(login);
        
        JPanel panel = new JPanel();
        panel.setBounds(26, 301, 365, 35);
        panel.setOpaque(false);
        getContentPane().add(panel);
        
        JLabel lblNewLabel = new JLabel("Have Not Signed Up Yet?");
        panel.add(lblNewLabel);
        
        JLabel lblClickHere = new JLabel("Click Here");
        lblClickHere.setForeground(Color.BLUE);
        lblClickHere.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblClickHere.addMouseListener(new MouseAdapter() {
        	 
            @Override
            public void mouseClicked(MouseEvent e) {
                // Open Registration
            	new Registration();
            }
        });
        
        panel.add(lblClickHere);
        
        pack();
        
        //Window initializes in the center of screen regardless of size
        setLocationRelativeTo(null);
        
        //Only set this after all items are added to the this
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        s = "";
        if (e.getSource() == login) {
            if (getEmail().isEmpty() || getPassword().isEmpty()) {
                s = "Fill in all fields!";
            }else {
                try {
                    if (checkDB(getEmail()) == null) {
                        s = "Wrong email";
                    } else if (checkDB(getPassword()) == null) {
                        s = "Wrong password";
                    }else {
                    	new SMATD();
                    	dispose();
                    	return;
                    }

                }catch (IOException e1) {
                    System.out.println("User Not found");
                }
            }
        }
        JOptionPane.showMessageDialog(null, s);
    }

    public static String checkDB(String s) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("db.txt"));
        String line;
        
        while ((line = br.readLine()) != null){  
            if(line.contains(hashPassword(s))){
            	return s;
            }
        }
        
        br.close();
        
        return null;

    }
    public static String hashPassword(String cs) {
        String passwordToHash = cs;
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(passwordToHash.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }
    
    public static void main(String[] args){
        new Login();
    }
}