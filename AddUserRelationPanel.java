
package grafos;
import java.util.*; 
import grafos.User;
import grafos.UsersRelation;
import grafos.Assets;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class AddUserRelationPanel extends javax.swing.JFrame {
    javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jTextField1;
    User CurrentUser;
    User[] UsersArr;
    Integer[] UnavailableUsersIDS;
    User[] AvailableUsers;
    UsersRelation[] UsersRelationsArr;
    Assets assets = new Assets();
    Boolean isSuccess = false;
    
    public AddUserRelationPanel(User currentUser, User[] usersArr, UsersRelation[] usersRelationsArr) {
        CurrentUser = currentUser;
        UsersArr = usersArr;
        UsersRelationsArr = usersRelationsArr;
        initComponents();
    }
    
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Elija el usuario con el cual establecera una nueva relación");
        
        Integer UnavailableUsersNumber = 0;
        Integer AvailableUsersNumber = 0;
        UnavailableUsersIDS = new Integer[UsersArr.length];   
        
        for(int i = 0; i < UsersRelationsArr.length; i++) {
            if(UsersRelationsArr[i].User1ID.equals(CurrentUser.ID)) {
                UnavailableUsersIDS[UnavailableUsersNumber] = UsersRelationsArr[i].User2ID;
                UnavailableUsersNumber += 1;
            } else if(UsersRelationsArr[i].User2ID.equals(CurrentUser.ID)) {
                UnavailableUsersIDS[UnavailableUsersNumber] = UsersRelationsArr[i].User1ID;
                UnavailableUsersNumber += 1;
            } 
        }
        UnavailableUsersIDS[UnavailableUsersNumber+1] = CurrentUser.ID;
        
        String[] UsernamesArr = new String[UsersArr.length-UnavailableUsersNumber];
        AvailableUsers = new User[UsersArr.length-UnavailableUsersNumber];
        Integer counter = 0;
        
        for(int i = 0; i < UsersArr.length; i++) {
            if(!Arrays.asList(UnavailableUsersIDS).contains(UsersArr[i].ID)){
                UsernamesArr[counter] = UsersArr[i].Username;
                AvailableUsers[counter] = UsersArr[i];
                counter += 1;
            }
        }
        
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(UsernamesArr));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel2.setText("Dias de Relación");

        jTextField1.setText("0");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton1.setText("Crear Nueva Relación");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(86, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.LEADING, 0, 143, Short.MAX_VALUE)))
                .addGap(81, 81, 81))
            .addGroup(layout.createSequentialGroup()
                .addGap(150, 150, 150)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
    }                 

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {
    }                                          

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {                                            
    }                                           

    public void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        if(!isSuccess) {
            final JPanel panel = new JPanel();
            Integer comboBoxSelectedIndex = jComboBox1.getSelectedIndex();
            User selectedUser = AvailableUsers[comboBoxSelectedIndex];
            String relationDays = jTextField1.getText();
            if(assets.isInteger(relationDays)) {
                if(relationDays.length() > 5) {
                    JOptionPane.showMessageDialog(panel, "El número de digitos de los dias debe ser menor o igual a 5.", "Warning",JOptionPane.WARNING_MESSAGE);
                    return;
                }
                isSuccess = true;
                Integer days = Integer.parseInt(relationDays);
                createNewRelation(selectedUser, days);
            } else {
                JOptionPane.showMessageDialog(panel, "El número de dias debe ser un número entero.", "Warning",JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        
    }
    
    private void createNewRelation(User selectedUser, Integer days) {
        UsersRelation newRelation = new UsersRelation();
        newRelation.User1ID = CurrentUser.ID;
        newRelation.User1 = CurrentUser;
        newRelation.User2ID = selectedUser.ID;
        newRelation.User2 = selectedUser;
        newRelation.RelationTime = days;
        UsersRelation[] newUsersRelationsArr = new UsersRelation[UsersRelationsArr.length+1];
        for(int i = 0; i < UsersRelationsArr.length; i++) {
            newUsersRelationsArr[i] = UsersRelationsArr[i];
        }
        newUsersRelationsArr[UsersRelationsArr.length] = newRelation;
        UsersRelationsArr = newUsersRelationsArr;
        dispose();
    }

}
