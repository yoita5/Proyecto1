package grafos;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.Random;
import javax.swing.JFrame;
import grafos.Csv;


public class CreateUserPanel extends javax.swing.JFrame {
    Csv UsersCsv;
    Boolean successValidations = false;
    javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField jTextField2;
    
    public CreateUserPanel(Csv usersCsv) {
        this.UsersCsv = usersCsv;
        initComponents();
    }

    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel1.setText("Crear Usuario");

        jLabel3.setText("Nombre de Usuario");

        jTextField2.setText("Username");

        jButton1.setText("Crear");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(156, 156, 156)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(150, 150, 150)
                        .addComponent(jButton1)))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }                    

    public void jButton1ActionPerformed(java.awt.event.ActionEvent evt) { 
        if(!successValidations) {
            int id = 0;
            Random random = new Random();
            Boolean isRepeated = true;
            while (isRepeated){
                id = random.nextInt(10000000);
                Integer repeatedTimes = 0;
                for(int i = 0; i < UsersCsv.usersArr.length; i++) {
                    if(UsersCsv.usersArr[i].ID.equals(id)) {
                        repeatedTimes += 1;
                    }
                }
                if(repeatedTimes > 0) {
                    isRepeated = true;
                } else {
                    isRepeated = false;
                }
            }

            String username = jTextField2.getText();
            userValidations(username);

            if(successValidations == true) {
                User newUser = new User();
                newUser.ID = id;
                newUser.Username = username;
                User[] newUsersArr = new User[UsersCsv.usersArr.length+1];
                for(int i = 0; i < UsersCsv.usersArr.length; i++) {
                    newUsersArr[i] = UsersCsv.usersArr[i];
                }
                newUsersArr[UsersCsv.usersArr.length] = newUser;
                UsersCsv.usersArr = newUsersArr;
                dispose();
            }
        }
    }                                        
    
    private void userValidations(String username) {
        final JPanel panel = new JPanel();
        if(username.length() == 0) {
            JOptionPane.showMessageDialog(panel, "El nombre de usuario no puede estar vacío.", "Warning",JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(username.length() > 30) {
            JOptionPane.showMessageDialog(panel, "El nombre de usuario no puede contener mas de 30 caracteres", "Warning",JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(username.lastIndexOf('@') != 0) {
            JOptionPane.showMessageDialog(panel, "El nombre de usuario debe tener un @ al inicio.", "Warning",JOptionPane.WARNING_MESSAGE);
            return;
        }
        successValidations = true;
    }
}
