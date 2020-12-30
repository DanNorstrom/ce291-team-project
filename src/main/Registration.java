package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

class Registration extends JDialog implements ActionListener {

    // components of the Student Registration frame
    private JLabel heading;
    private JLabel name;
    private JTextField typedname;
    private JLabel email;
    public JTextField typedemail;
    private JLabel address;
    private JLabel password;
    public JPasswordField typedpassword;
    private JTextArea typedaddress;
    private JCheckBox term;
    private JButton register;
    String emailpart = "";
    public boolean success = false;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    
    public Registration(){
    	
    	setModal(true);
        setTitle("Student Registration Form"); // Title of the Registration Form frame
        setPreferredSize(new Dimension(495, 360)); // Setting the position and size of the frame
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //Hide the frame, but keep the application running
        
        getContentPane().setLayout(null);

        // Title inside the frame and the main heading
        heading = new JLabel("Teacher Registration Form");
        heading.setFont(new Font("Courier", Font.BOLD, 30));
        heading.setSize(461, 33);
        heading.setLocation(17, 6);
        getContentPane().add(heading);

        // Fields that student needs to enter
        // Fields with the red star are mandatory
        name = new JLabel("Name");
        name.setFont(new Font("Arial", Font.BOLD, 15));
        name.setSize(100, 30);
        name.setLocation(83, 51);
        getContentPane().add(name);

        // Text field where user will type his Name
        typedname = new JTextField();
        typedname.setSize(214, 30);
        typedname.setLocation(183, 51);
        getContentPane().add(typedname);
        setBackground(Color.lightGray);

        // Email is a mandatory field
        email = new JLabel("Email");
        email.setFont(new Font("Arial", Font.BOLD, 15));
        email.setSize(100, 30);
        email.setLocation(82, 87);
        getContentPane().add(email);

        // Text field where user will type his Email
        typedemail = new JTextField();
        typedemail.setSize(214, 30);
        typedemail.setLocation(182, 87);
        getContentPane().add(typedemail);

        // Password is a mandatory field
        password = new JLabel("Password");
        password.setFont(new Font("Arial", Font.BOLD, 15));
        password.setSize(101, 27);
        password.setLocation(83, 131);
        getContentPane().add(password);

        // Text field where user will type his Password
        typedpassword = new JPasswordField();
        typedpassword.setSize(214, 30);
        typedpassword.setLocation(183, 129);
        getContentPane().add(typedpassword);

        address = new JLabel("Address");
        address.setFont(new Font("Arial", Font.BOLD, 15));
        address.setSize(100, 20);
        address.setLocation(83, 175);
        getContentPane().add(address);

        // Text field where user will type his Address
        typedaddress = new JTextArea();
        typedaddress.setSize(206, 75);
        typedaddress.setLocation(186, 175);
        typedaddress.setLineWrap(true);
        getContentPane().add(typedaddress);

        // Student must accept the terms and conditions
        // It is a mandatory field
        term = new JCheckBox("<html><font> I agree to the </font><font color=blue>Terms of Service</font><font> and</font> <font color=blue>Privacy Policy</font></html>");
        term.setVerticalAlignment(SwingConstants.TOP);
        term.setSize(197, 47);
        term.setLocation(201, 273);
        getContentPane().add(term);

        // Button that will Register the student
        register = new JButton("Register");
        register.setSize(100, 37);
        register.setLocation(84, 273);
        register.addActionListener(this);
        getContentPane().add(register);

        pack();
        
        //Window initializes in the center of screen regardless of size
        setLocationRelativeTo(null);
        
        //Only set this after all items are added to the this
        setVisible(true);
    }

    // To get the action performed by the student
    public void actionPerformed(ActionEvent e){
        // If student has chosen to register and has pressed the Register button
        if (e.getSource() == register) {
            // If student has accepted the T&C
            // and the password field is not empty
            // and wrote an email that matches university' email pattern
            // display the data (except the password) the user has typed
            // in a string format in the text area on the right

        	String name = typedname.getText();
        	String email = typedemail.getText();
        	String pass = String.valueOf(typedpassword.getPassword());
        	String add = typedaddress.getText();
        	
            if (!term.isSelected()){JOptionPane.showMessageDialog(null, "You must accept the University T&C's."); return;}
            else if(name.equals("")) {JOptionPane.showMessageDialog(null, "Name field cannot be empty."); return;}
            else if(!email.matches(EMAIL_PATTERN)) {JOptionPane.showMessageDialog(null, "Invalid Email."); return;}
            else if(pass.equals("")) {JOptionPane.showMessageDialog(null, "Password field cannot be empty."); return;}
            else if(add.equals("")) {JOptionPane.showMessageDialog(null, "Address field cannot be empty."); return;}
            
            try {
                saveData2Flie(email, name, pass, add);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            // The registration is then successful
            dispose();
            JOptionPane.showMessageDialog(null, "Your registration was successful!");
        }
    }

    // Java code for writing something to a file. Every time after it runs, a new file is
    // created, but the previous one is gone.
    public void saveData2Flie(String... data) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("db.txt", true));
        for (String s : data) {
        	s = Login.hashPassword(s); // hashes the data before written on text file
        	if(s.contains("@")){
        		if(Login.checkDB(s) == null){return;}
        	}
            writer.append(s + " ");
        }
        writer.newLine();
        writer.close();
    }
}