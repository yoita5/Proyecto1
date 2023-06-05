
package grafos;
import java.util.*; 
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Component;
import java.awt.Color;
import javax.swing.*;
import java.awt.event.*;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import grafos.User;
import grafos.UsersRelation;
import grafos.Csv;
import grafos.UserRelationsPanel;
import grafos.AddUserRelationPanel;

public class UsersPanel extends JFrame {
    Csv UsersCsv;
    Csv usersRelationsCsv;
    private JList<String> jList;
    private Integer selectedListElementIndex = 0;
    private static Object lock = new Object();
    private static UserRelationsPanel userRelationsWindow;
    private static AddUserRelationPanel AddUserRelationsWindow;
    
    
    //when a Jlist element is right-clicklied (gray background)
    public class SelectedListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (isSelected) {
                c.setBackground(Color.GRAY);
            }
            return c;
        }
    }
    
    public UsersPanel(Csv usersCsv, Csv UsersRelationsCsv) {
        this.UsersCsv = usersCsv;
        this.usersRelationsCsv = UsersRelationsCsv;
        JPopupMenu popup = new JPopupMenu();
        JMenuItem PopUpshowRelationsMenuItem = popup.add(new JMenuItem("Ver Usuarios Relacionados"));
        JMenuItem PopUpdeleteUserMenuItem = popup.add(new JMenuItem("Eliminar Usuario"));
        JMenuItem PopUpAddUserRelationMenuItem = popup.add(new JMenuItem("Agregar Relación"));
        this.initComponents();
        //adding action listen to the pop up menuitems
        PopUpshowRelationsMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
               userRelationsWindow = new UserRelationsPanel(usersRelationsCsv, UsersCsv.usersArr, UsersCsv.usersArr[selectedListElementIndex]);
               OpenUserRelationsWindow();
            }
        });
        
        PopUpdeleteUserMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
               createDeleteUserDecisionModal(UsersCsv.usersArr[selectedListElementIndex].Username, selectedListElementIndex);
            }
        });
        
        PopUpAddUserRelationMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
               AddUserRelationsWindow = new AddUserRelationPanel(UsersCsv.usersArr[selectedListElementIndex],UsersCsv.usersArr, usersRelationsCsv.usersRelationsArr);
               OpenAddUserRelationWindow();
            }
        });

        //adds event listener to the right click of the mouse
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
              JList  theList = (JList) 
              mouseEvent.getSource();
              if (mouseEvent.getButton() == mouseEvent.BUTTON3 && mouseEvent.getClickCount() == 1) {
                int index = theList.locationToIndex(mouseEvent.getPoint());
                if (index >= 0) {
                  Object o = theList.getModel().getElementAt(index);
                  //theList.setCellRenderer( new SelectedListCellRenderer() );
                  popup.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
                  selectedListElementIndex = index;
                }
              }
            }
        };
        jList.addMouseListener(mouseListener);
    }
    
    private void OpenAddUserRelationWindow() {
        setVisible(false);
        AddUserRelationsWindow.setVisible(true);

        Thread t = new Thread() {
            public void run() {
                synchronized(lock) {
                    while (AddUserRelationsWindow.isVisible())
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                }
            }
        };
        t.start();

        AddUserRelationsWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                synchronized (lock) {
                    AddUserRelationsWindow.setVisible(false);
                    lock.notify();
                    setVisible(true);
                }
            }

        });
        
        AddUserRelationsWindow.jButton1.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddUserRelationsWindow.jButton1ActionPerformed(e);
                synchronized (lock) {
                    lock.notify();
                    usersRelationsCsv.usersRelationsArr = AddUserRelationsWindow.UsersRelationsArr;
                    setVisible(true);
                }
            }
        });
    }
    
    private void createDeleteUserDecisionModal(String username, Integer selectedListElementIndex){  
        LayoutManager layout = new FlowLayout();
        int result = JOptionPane.showConfirmDialog(this, "¿Desea Eliminar el usuario "+username+"?",
           "Eliminar",
           JOptionPane.YES_NO_OPTION
        );

        if(result == JOptionPane.YES_OPTION){
            this.deleteUser(selectedListElementIndex);
        } 
    }
    
    private void deleteUser(Integer userIndex){  
        User[] newUsersArr = new User[UsersCsv.usersArr.length-1];
        Integer numOfRelations = 0;
        for(int i = 0; i < usersRelationsCsv.usersRelationsArr.length; i++) {
            if(!usersRelationsCsv.usersRelationsArr[i].User1ID.equals(UsersCsv.usersArr[userIndex].ID) && !usersRelationsCsv.usersRelationsArr[i].User2ID.equals(UsersCsv.usersArr[userIndex].ID)) {
                numOfRelations += 1;
            }
        }
        UsersRelation[] newUsersRelationsArr = new UsersRelation[numOfRelations];
        for(int i = 0; i < UsersCsv.usersArr.length; i++) {
            if(i >= userIndex) {
                if(i == userIndex) {
                    Integer counter = 0;
                    for(int a = 0; a < usersRelationsCsv.usersRelationsArr.length; a++) {
                      if(!usersRelationsCsv.usersRelationsArr[a].User1ID.equals(UsersCsv.usersArr[userIndex].ID) && !usersRelationsCsv.usersRelationsArr[a].User2ID.equals(UsersCsv.usersArr[userIndex].ID)) {
                          newUsersRelationsArr[counter] = usersRelationsCsv.usersRelationsArr[a];
                          counter += 1;
                      }
                      if(counter == 0) {
                          counter = 1;
                      }
                    }
                }
                if(!(userIndex == UsersCsv.usersArr.length-1)) {
                    if(i < UsersCsv.usersArr.length-1) {
                        newUsersArr[i] = UsersCsv.usersArr[i+1];
                    }
                    
                }
            } else if(i < userIndex){
                newUsersArr[i] = UsersCsv.usersArr[i];
            }
        }
        
        usersRelationsCsv.usersRelationsArr = newUsersRelationsArr;
        UsersCsv.usersArr = newUsersArr;
       
        String[] JListItems = new String[newUsersArr.length];
        
        for(int i = 0;i < newUsersArr.length; i++) {
            JListItems[i] = newUsersArr[i].Username;
        }
        
        //adding list items
        jList.setModel(new javax.swing.AbstractListModel<String>() {
            public int getSize() { return JListItems.length; }
            public String getElementAt(int i) { return JListItems[i]; }
        });
    }
    
    private void OpenUserRelationsWindow() {
        setVisible(false);
        userRelationsWindow.setSize(200, 300);
        userRelationsWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        userRelationsWindow.setVisible(true);

        Thread t = new Thread() {
            public void run() {
                synchronized(lock) {
                    while (userRelationsWindow.isVisible())
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                }
            }
        };
        t.start();

        userRelationsWindow.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent arg0) {
                synchronized (lock) {
                    userRelationsWindow.setVisible(false);
                    lock.notify();
                    usersRelationsCsv = userRelationsWindow.usersRelationsCsv;
                    setVisible(true);
                }
            }

        });
    }
    
    
    private void initComponents() {
        JScrollPane jScrollPane1 = new JScrollPane();
        jList = new javax.swing.JList<>();
        String[] JListItems;
        JListItems = new String[UsersCsv.usersArr.length];
        
        for(int i = 0;i < this.UsersCsv.usersArr.length; i++) {
            JListItems[i] = this.UsersCsv.usersArr[i].Username;
        }
        
        //adding list items
        jList.setModel(new javax.swing.AbstractListModel<String>() {
            public int getSize() { return JListItems.length; }
            public String getElementAt(int i) { return JListItems[i]; }
        });
        jScrollPane1.setViewportView(jList);
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup( GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 218, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        
        pack();
    }
          
}
