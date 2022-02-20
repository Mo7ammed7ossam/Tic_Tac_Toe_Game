
package validpkg;

import java.util.regex.Pattern;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;

/**
 *
 * @author Abdeladl
 */
public class Validation {
    public static boolean isEmpty(String text)
    {
        if(text.equals(""))
            return true;
        else
            return false;
    }
    public static boolean isNumber(String text)
    {
        return Character.isDigit(text.charAt(0));           
    }
    public static boolean ValidNumberOfCharacter(String text,int numberOfCharacter)
    {
        if(text.length() < numberOfCharacter)
            return false;
        else
            return true;  
    }
    private static boolean matchEmailPatter(String text)
    {
        Pattern emailPattern = Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}");
        if(emailPattern.matcher(text).matches())
            return true;
        else 
            return false;       
    }
    public  static void alertMessageError(String title,String errorMessage)
    {
           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.initStyle(StageStyle.UNDECORATED);
           alert.setTitle(title);
           alert.getDialogPane().setStyle("-fx-background-color:#f4f4f4;");
           alert.setHeaderText(null);
           alert.setContentText(errorMessage);
           alert.showAndWait();
    }
    public static boolean isEmail(String text)
    {
        if(isEmpty(text))
        {
            alertMessageError("Email Field", "Email field is required !");
            return false;
        }
        else
        {
            if(matchEmailPatter(text))
            {
                return true;
            }

            else
            {
                alertMessageError("Email Field", "Please Enter a Valid Email !");
                return false;
            }
                
        }
    }
    public static boolean isName(String text)
    {
        if(isEmpty(text))
        {
            alertMessageError("Name Field", "Name field is required");
            return false;
        }
        else
        {
            if(!ValidNumberOfCharacter(text, 3))
            {
                alertMessageError("Name Field", "Name Field Must be at least 3 Character !");
                return false;
            }
            else
            {
                if(isNumber(text))
                {
                    alertMessageError("Name Field", "Name Field Mustn't Start With Numeric Value");
                    return false;
                }
                else
                    return true;
            }
        }
    }
    
    public static boolean isPassword(String text)
    {
        if(isEmpty(text))
        {
            alertMessageError("Password Field", "password field is required");
            return false;
        }
        else
        {
            if(!ValidNumberOfCharacter(text, 8))
            {
                alertMessageError("Password Field", "password Field Must be at least 8 Character !");
                return false;
            }
            else
            {
                return true;
            }
        }
    }
    public static boolean isEqual(String text1 , String text2)
    {
        if(text1.equals(text2))
            return true;
        else
        {
            alertMessageError("Password Field", "password Doesn't match retype Password !");
            return false;
        }
    }
}
