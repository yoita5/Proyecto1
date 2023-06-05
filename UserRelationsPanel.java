
package grafos;
import java.util.*; 
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import grafos.User;
import grafos.UsersRelation;
import grafos.Csv;

public class UserRelationsPanel extends JFrame{
    Csv usersRelationsCsv;
    private User UserData;
    private JList<String> jList;
    private JScrollPane jScrollPane1;
    private JPopupMenu popup;
    private Integer selectedListElementIndex = 0;
    private JMenuItem PopUpdeleteItem;
    
    public UserRelationsPanel(Csv usersRelationsCsv, User[] users, User userData) {
        this.UserData = userData;
        this.usersRelationsCsv = usersRelationsCsv;
        this.popup = new JPopupMenu();
        this.PopUpdeleteItem = popup.add(new JMenuItem("Eliminar Relación"));
        this.initComponents();
        
        //adding action listen to the pop up menuitems
        PopUpdeleteItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                String username = "";
                if(UserData.ID.equals(usersRelationsCsv.usersRelationsArr[selectedListElementIndex].User1ID)) {
                    username = usersRelationsCsv.usersRelationsArr[selectedListElementIndex].User1.Username;
                }
                if(UserData.ID.equals(usersRelationsCsv.usersRelationsArr[selectedListElementIndex].User2ID)) {
                    username = usersRelationsCsv.usersRelationsArr[selectedListElementIndex].User2.Username;
                }
                createDecisionModal(username, selectedListElementIndex);
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
    
   private void createDecisionModal(String username, Integer itemIndex){  
    LayoutManager layout = new FlowLayout();
    int result = JOptionPane.showConfirmDialog(this, "¿Desea Eliminar la relación con "+username+"?",
       "Eliminar",
       JOptionPane.YES_NO_OPTION
    );
            
    if(result == JOptionPane.YES_OPTION){
        this.deleteUserRelationItem(itemIndex);
    } 
   } 
   
   private void deleteUserRelationItem(Integer itemIndex) {
       UsersRelation[] usersRelationArr = new UsersRelation[usersRelationsCsv.usersRelationsArr.length-1];
        for(int i = 0; i < usersRelationsCsv.usersRelationsArr.length-1; i++) {
            if(i >= itemIndex) {
                usersRelationArr[i] = usersRelationsCsv.usersRelationsArr[i+1];
            } else if(i < itemIndex){
                usersRelationArr[i] = usersRelationsCsv.usersRelationsArr[i];
            }
        }
        usersRelationsCsv.usersRelationsArr = usersRelationArr;
       
        String[] JListItems = new String[usersRelationArr.length];
        
        for(int i = 0;i < usersRelationArr.length; i++) {
            if(usersRelationArr[i].User1ID.equals(UserData.ID)) {
                JListItems[i] = usersRelationArr[i].User2.Username;
            }
            if(usersRelationArr[i].User2ID.equals(UserData.ID)) {
                JListItems[i] = usersRelationArr[i].User1.Username;
            }
        }
        jList.setModel(new javax.swing.AbstractListModel<String>() {
            public int getSize() { return JListItems.length; }
            public String getElementAt(int i) { return JListItems[i]; }
        });
   }
    
    private void initComponents() {
        jScrollPane1 = new JScrollPane();
        jList = new javax.swing.JList<>();
        String[] JListItems = new String[usersRelationsCsv.usersRelationsArr.length];
        
        for(int i = 0;i < this.usersRelationsCsv.usersRelationsArr.length; i++) {
            if(this.usersRelationsCsv.usersRelationsArr[i].User1ID.equals(UserData.ID)) {
                JListItems[i] = this.usersRelationsCsv.usersRelationsArr[i].User2.Username+" - "+this.usersRelationsCsv.usersRelationsArr[i].RelationTime.toString()+" dias";
            }
            if(this.usersRelationsCsv.usersRelationsArr[i].User2ID.equals(UserData.ID)) {
                JListItems[i] = this.usersRelationsCsv.usersRelationsArr[i].User1.Username+" - "+this.usersRelationsCsv.usersRelationsArr[i].RelationTime.toString()+" dias";
            }
        }
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
