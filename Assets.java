
package grafos;
public class Assets {
    public String getFileNameExtension(String fullName) {
        int dotIndex = fullName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fullName.substring(dotIndex + 1);
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
}
