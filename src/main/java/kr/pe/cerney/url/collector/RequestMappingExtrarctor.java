package kr.pe.cerney.url.collector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controller 파일에 기술한 RequestMapping 키워드를 읽어서 그 안에 기록한 URI를 추출하는 class.
 *  - Class 범위 RequestMapping이 존재하면 모든 Method에 정의된 URI 앞에 class의 URI를 추가한다.
 */
public class RequestMappingExtrarctor {
    public static String[] parseController(File controller) {
        String uriRegex = "^\\s*@RequestMapping\\s*\\(\\s*(value)?\\s*=?\\s*\\{?\\s*\"([/a-zA-Z0-9\\-_\\.]+)\"";
        String classRegex = "^\\s*(public|private|protected)?\\s*class\\s+";
        Pattern uriPattern = Pattern.compile(uriRegex);
        Pattern classPattern = Pattern.compile(classRegex);
        
        int lastUrlLine = Integer.MAX_VALUE;
        boolean isPassClass = false;
        String url = null;
        String prefix = "";
        ArrayList<String> urlList = new ArrayList<>();
        
        BufferedReader br = null;
        
        try {
            br = new BufferedReader(new FileReader(controller));
            String line = null;
            int linenum = 0;
            
            while((line = br.readLine()) != null) {
                linenum++;
                Matcher uriMatcher = uriPattern.matcher(line);
                Matcher classMatcher = classPattern.matcher(line);
                
                if(uriMatcher.find()) {
                    url = uriMatcher.group(2);
                    lastUrlLine = linenum;
                    
                    if(isPassClass) {
                        urlList.add(prefix + url);
                    }
                }
                else if(classMatcher.find()) {
                    isPassClass = true;
                    
                    if(lastUrlLine < linenum) {
                        prefix = url;
                    }
                }
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch(IOException e) {
                ;
            }
        }
        
        urlList.trimToSize();
        String[] urlStringArray = new String[urlList.size()];
        return urlList.toArray(urlStringArray);
    }
}
