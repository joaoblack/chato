package chat;

import java.io.File;

/**
 * @author João Leite 8140453
 */
public class FileProtocol {

    public FileProtocol() {
    }

    public void listFiles() {

        String dirPath = "chat_share";
        File dir = new File(dirPath);
        String[] files = dir.list();
        if (files.length == 0) {
            System.out.println("No files to share!");
        } else {
            for (String aFile : files) {
                System.out.println(aFile);
            }
        }
    }
    
    public void addFilesToList(){
        
    }
}
