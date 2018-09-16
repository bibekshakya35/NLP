package util;

/**
 * @author bibek on 12/22/17
 * @project compare&search
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils{
    public static String lowerCaseWithNoWhiteSpace(String str){
        if(StringUtils.contains(str,"/")){
            String[] splitter = str.split("/");
            StringBuilder stringBuilder = new StringBuilder();
            for (String split : splitter){
                stringBuilder.append(split);
            }
            return stringBuilder.toString().toLowerCase().replaceAll("[\\s|\\u00A0]+", "");
        }
        return str.toLowerCase().replaceAll("[\\s|\\u00A0]+", "");
    }
}
