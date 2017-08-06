package Parser;

import FileLocator.TemplateFileLocator;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.codemodel.internal.JArray;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlTemplateParser implements ITemplateParser {

    private TemplateFileLocator templateLocator;

    public HtmlTemplateParser(TemplateFileLocator templateLocator) {
        this.templateLocator = templateLocator;
    }

    @Override
    public String parse(String template, Map<String, Object> parameters) throws NoSuchFieldException, IOException {

        template = includeParentTemplate(template);
        template = replaceForLoops(template, parameters);
        template = replaceIfStatements(template, parameters);
        template = replacePlaceholders(template, parameters);

        return template;
    }

    private String includeParentTemplate(String template) throws IOException {
        Matcher m = Pattern.compile("(\\{% extends \"([\\w/]+)\" %})")
                .matcher(template);

        while (m.find()) {
            String extendsBlock = m.group(1);
            String templatePath = getTemplateName(m.group(2));

            String parentTemplate = this.templateLocator.get(templatePath);

            Map<String, ContentBlock> parentBlocks = getContentBlocks(parentTemplate);

            Map<String, ContentBlock> childrenBlocks = getContentBlocks(template);

            for(String key : parentBlocks.keySet()){
                template = parentTemplate.replace(parentBlocks.get(key).wrapper, childrenBlocks.get(key).content);

            }

            template = template.replace(extendsBlock, "");
        }

        return template;
    }

    private Map<String, ContentBlock> getContentBlocks(String template){
        Matcher m = Pattern.compile("(\\{% block ([\\w]+) %}(.*)\\{% endblock %})")
                .matcher(template);

        Map<String, ContentBlock> blocks = new HashMap<>();

        while(m.find()){
            String blockWrapper = m.group(1);
            String blockKey = m.group(2);
            String blockContent = m.group(3);

            ContentBlock contentBlock = new ContentBlock();

            contentBlock.wrapper = blockWrapper;
            contentBlock.content = blockContent;

            blocks.put(blockKey, contentBlock);
        }

        return blocks;
    }


    private String replacePlaceholders(String template, Map<String, Object> parameters) {
        Matcher m = Pattern.compile("(\\{\\{ *([a-zA-Z.]+) *}})")
                .matcher(template);

        while (m.find()) {
            String placeholder = m.group(1);
            String variableKey = m.group(2);

            template = template.replace(placeholder, (String) parameters.get(variableKey));
        }

        return template;
    }

    private String replaceForLoops(String template, Map<String, Object> parameters) throws NoSuchFieldException {
        Matcher m = Pattern.compile("(\\{% for (\\w+) in (\\w+) %}(.*)\\{% endfor %})")
                .matcher(template);

        while (m.find()) {
            String matchedForLoop = m.group(1);
            String fieldName = m.group(2);
            String collectionName = m.group(3);
            String output = m.group(4).trim();

            StringBuilder forItemBuilder = new StringBuilder();

            List<HashMap<String, Object>> forItems = (List<HashMap<String, Object>>) parameters.get(collectionName);


            for (HashMap<String, Object> currentElement : forItems) {
                Map<String, Object> forKeyValuePairs = new HashMap<>();

                for (String key : currentElement.keySet()) {
                    String value = (String) currentElement.get(key);

                    forKeyValuePairs.put(fieldName + "." + key, value);
                }

                forItemBuilder.append(replacePlaceholders(output, forKeyValuePairs));
            }

            template = template.replace(matchedForLoop, forItemBuilder);
        }

        return template;
    }

    private String replaceIfStatements(String template, Map<String, Object> parameters) {
        Matcher ifStatements = Pattern.compile("(\\{% if (?:not )?(?:\\w+) %}.*\\{% endif %})")
                .matcher(template);


        while (ifStatements.find()) {
            String ifStatement = ifStatements.group();

            Matcher ifGroups = Pattern.compile("(\\{% ((?:else)?(?:if)?) (not )?(?:(\\w+) )?%})([^{%]+)")
                    .matcher(template);

            boolean hasFoundTruthyIfStatement = false;

            while (ifGroups.find()) {
                String ifType = ifGroups.group(2);
                boolean isNegatedIf = ifGroups.group(3) != null;
                String ifVariableKey = ifGroups.group(4);
                String output = ifGroups.group(5);
                boolean ifTypeIsElse = ifType.equals("else");

                boolean ifRequirementIsTruthy = ifTypeIsElse || (Boolean) parameters.get(ifVariableKey);

                boolean evaluatesToTrue = ifTypeIsElse || isNegatedIf != ifRequirementIsTruthy;

                if (evaluatesToTrue) {
                    hasFoundTruthyIfStatement = true;
                    template = template.replace(ifStatement, replacePlaceholders(output, parameters));

                    break;
                }
            }

            if (!hasFoundTruthyIfStatement) {
                template = template.replace(ifStatement, "");
            }
        }

        return template;
    }

    private String getTemplateName(String name){
        return name.contains(".mct") ? name : name.concat(".mct");
    }
}
