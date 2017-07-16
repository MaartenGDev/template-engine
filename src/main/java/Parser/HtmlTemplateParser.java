package Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlTemplateParser implements ITemplateParser {

    @Override
    public String parse(String template, Map<String, String> parameters) {

        List<String> allMatches = new ArrayList<>();

        Matcher m = Pattern.compile("(\\{\\{[a-zA-Z]+}})")
                .matcher(template);

        while (m.find()) {
            allMatches.add(m.group());
        }

        for (String placeholder: allMatches) {
            template = template.replace(placeholder, parameters.get(getVariableName(placeholder)));
        }
        return template;
    }

    private String getVariableName(String placeholder){
        return placeholder.replace("{", "").replace("}", "");
    }
}
