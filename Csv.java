
package grafos;
import java.util.*; 
import java.io.File;
import javax.swing.JFileChooser;
import java.io.BufferedReader;
import java.io.FileReader;
import grafos.User;
import grafos.UsersRelation;
/**
 *
 * @author yoita5
 */
public class Csv {
    FileReader file;
    String filePath;
    BufferedReader reader;
    User[] usersArr = {};
    Integer[] usersIDList = {};
    UsersRelation[] usersRelationsArr = {};
    String status = "success";
    String message = "";
    
    public void FileSelection(String file_type) {
        File selectedFile;
        JFileChooser selectFile;
        selectFile = new JFileChooser();
        Assets assets = new Assets();
        Csv csv;
        
        selectFile.showOpenDialog(null);
        selectedFile = selectFile.getSelectedFile();
        
        if(selectedFile == null) {
            //show dialog modal
            this.message = "Debe seleccionar un archivo";
            this.status = "wrong";
        } else {
            String fileExtension = assets.getFileNameExtension(selectedFile.getName());
            if(!fileExtension.equals("txt")) {
                //show dialog modal
                this.message = "Debe seleccionar un archivo con la extensión .txt";
                this.status = "wrong";
                return;
            }
            this.processFile(selectedFile.getAbsolutePath(), file_type);
        }
    }
    
    public void processFile(String filePath, String type) {
        try{
            this.filePath = filePath;
            this.file = new FileReader(filePath);
            if(this.file.ready()) {
                this.reader = new BufferedReader(file);
                String fileLine;
                while((fileLine = this.reader.readLine()) != null) {
                    boolean checkCommas = checkCommas(fileLine);
                    String columns[];
                    
                    if(!checkCommas) {
                        this.message = "El archivo CSV debe estar delimitado por comas (,)";
                        this.status = "wrong";
                    }
                    columns = fileLine.split(",");
                    if(type == "users") {
                        processUserLine(columns);
                    }
                    if(type == "users_relations") {
                        processUsersRelationLine(columns);
                    }
                    
                    
                }
            } else {
                this.message = "El archivo no está listo para ser leído";
                this.status = "wrong";
            }
        } catch(Exception e) {
            System.out.println("Error:"+e.getMessage());
            this.message = "Error inesperado.";
            this.status = "wrong";
        }
        System.out.println(Arrays.toString(this.usersArr));
        System.out.println(Arrays.toString(this.usersRelationsArr));
    }
    
    private void processUsersRelationLine(String[] columns) {
        if(columns.length != 3 ) {
            this.message = "Cada fila debe tener tres columnas (ni mas ni menos).";
            this.status = "wrong";
            return;
        }
        if(!isInteger(columns[0])) {
            this.message = "Los datos de la primera columna deben ser de tipo entero";
            this.status = "wrong";
            return;
        }
        if(!isInteger(columns[1])) {
            this.message = "Los datos de la segunda columna deben ser de tipo entero";
            this.status = "wrong";
            return;
        }
        if(!isInteger(columns[2])) {
            this.message = "Los datos de la tercera columna deben ser de tipo entero";
            this.status = "wrong";
            return;
        }
        UsersRelation newUsersRelation = new UsersRelation();
        newUsersRelation.User1ID = Integer.parseInt(columns[0]);
        newUsersRelation.User2ID = Integer.parseInt(columns[1]);
        newUsersRelation.RelationTime = Integer.parseInt(columns[2]);
        addNewUsersRelation(newUsersRelation); 
    }

    private void addNewUsersRelation(UsersRelation newUsersRelation) {
        int n = this.usersRelationsArr.length;  
        if(newUsersRelation.User1ID.equals(newUsersRelation.User2ID)) {
            this.message = "Un usuario no puede estar relacionado con sigo mismo. ID: "+newUsersRelation.User1ID.toString();
            this.status = "wrong";
            return;
        }
        if(!Arrays.stream(this.usersArr).anyMatch(x -> x.ID.equals(newUsersRelation.User1ID))){
            this.message = "El usuario de ID: "+newUsersRelation.User1ID.toString()+" no existe en la lista de usuarios.";
            this.status = "wrong";
            return;
        }
        if(!Arrays.stream(this.usersArr).anyMatch(x -> x.ID.equals(newUsersRelation.User2ID))){
            this.message = "El usuario de ID: "+newUsersRelation.User2ID.toString()+" no existe en la lista de usuarios.";
            this.status = "wrong";
            return;
        }
        if(Arrays.stream(this.usersRelationsArr).anyMatch(
                x -> x.User1ID.equals(newUsersRelation.User1ID) && x.User2ID.equals(newUsersRelation.User2ID))
        ) {
            this.message = "La relación entre los usuarios de ID : "+newUsersRelation.User1ID.toString()+" y ID: "+newUsersRelation.User2ID.toString()+" está repetida en la lista de relaciones.";
            this.status = "wrong";
            return;
        }
        if(Arrays.stream(this.usersRelationsArr).anyMatch(
                x -> x.User2ID.equals(newUsersRelation.User1ID) && x.User1ID.equals(newUsersRelation.User2ID)
        )) {
            this.message = "La relación entre los usuarios de ID : "+newUsersRelation.User2ID.toString()+" y ID: "+newUsersRelation.User1ID.toString()+" está repetida en la lista de relaciones.";
            this.status = "wrong";
            return;
        }
        
        UsersRelation newUsersRelationsArr[] = new UsersRelation[n+1];  
        for(int i = 0; i<n; i++) {  
            newUsersRelationsArr[i] = this.usersRelationsArr[i];  
        }  
        
        for(int u = 0; u < this.usersArr.length; u++) {
            if(this.usersArr[u].ID.equals(newUsersRelation.User1ID)) {
                newUsersRelation.User1 = this.usersArr[u];
            }
            if(this.usersArr[u].ID.equals(newUsersRelation.User2ID)) {
                newUsersRelation.User2 = this.usersArr[u];
            }
        }
        newUsersRelationsArr[n] = newUsersRelation;
        this.usersRelationsArr = newUsersRelationsArr;
    }
    
    private void processUserLine(String[] columns) {
        if(columns.length != 2 ) {
            this.message = "Cada fila debe tener dos columnas (ni mas ni menos).";
            this.status = "wrong";
            return;
        }
        if(!isInteger(columns[0])) {
            this.message = "Los datos de la primera columna deben ser de tipo entero";
            this.status = "wrong";
            return;
        }
        User newUser = new User();
        newUser.ID = Integer.parseInt(columns[0]);
        newUser.Username = columns[1];
        addNewUser(newUser); 
    }
    
    private boolean checkCommas(String Textline) {
        int dotIndex = Textline.lastIndexOf(',');
        return (dotIndex == -1) ? false : true;
    }
    
    public static boolean isInteger(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int result = Integer.parseInt(strNum);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public void addNewUser(User newUser) {  
        int n = this.usersArr.length;  
        Integer[] newUserIDSList = new Integer[this.usersIDList.length+1];
        if(Arrays.stream(this.usersIDList).anyMatch(x -> x == newUser.ID)){
            this.message = "El usuario de ID: "+newUser.ID.toString()+" está repetido en la lista de usuarios.";
            this.status = "wrong";
            return;
        }
        User newUsersArr[] = new User[n+1];  
        for(int i = 0; i<n; i++) {  
            newUsersArr[i] = this.usersArr[i];  
            newUserIDSList[i] = this.usersIDList[i];
        }  
        newUsersArr[n] = newUser;  
        newUserIDSList[n] = newUser.ID;  
        this.usersArr = newUsersArr;
        this.usersIDList = newUserIDSList;
    }  
}
