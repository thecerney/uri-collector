package kr.pe.cerney.url.collector;

import java.io.File;
import java.util.Objects;
import java.util.Optional;

public class EntryPoint {
    public static void main(String args[]) {
        if(!Optional.ofNullable(args).isPresent()) {
            throw new NullPointerException("A full path to the root directory of the controller is required.");
        }
        
        System.out.println(args[0]);
        findControllers(args[0]);
    }
    
    public static void findControllers(String dir) {
        final String LAST_FILENAME = "Controller.java";
        File directoriesInfo = new File(dir);
        
        if(!directoriesInfo.isDirectory()) {
            throw new RuntimeException("Wrong Arguments!! It's not a directory.");
        }
        
        for(File currentDir : Objects.requireNonNull(directoriesInfo.listFiles())) {
            File[] controllerFiles = null;
            
            if(currentDir.isDirectory()) {
                findControllers(currentDir.getAbsolutePath());
                
                controllerFiles = currentDir.listFiles((pathname) -> !pathname.isDirectory() && pathname.getName().endsWith(LAST_FILENAME));
                
                for(File aController : Objects.requireNonNull(controllerFiles)) {

                    String[] urlList = RequestMappingExtrarctor.parseController(aController);
                    for(String url : urlList) {
                        System.out.println(url);
                    }
                }
            }
            
        }
    }
}
