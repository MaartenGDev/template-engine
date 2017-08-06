# Template-Engine
This is a example Template Engine


## Setup
```java
public class Main {

    public static void main(String[] args) throws IOException, NoSuchFieldException {
        // The FileLocator use the following path as prefix.
        String basePath = Paths.get(getProjectRoot()).toString();

        // Locates the templates files. Searches in resources/templates by default.
        TemplateFileLocator templateLocator = new TemplateFileLocator(basePath);

        // Locates the resources files for data in the template files. Searches in resources/templateData by default.
        TemplateDataFileLocator templateDataFileLocator = new TemplateDataFileLocator(basePath);
        
        // Parser for the resources files with key value pairs.
        JsonResourceFileParser resourceFileParser = new JsonResourceFileParser();

        // Result of the JsonResourceFileParser.
        JsonObject parameters = resourceFileParser.parse(templateDataFileLocator.get("index.json"));
    
        // Create instance to parse template from TemplateFileLocator
        HtmlTemplateParser parser = new HtmlTemplateParser();
        // Parse template and fill template with data from resource file found by TemplateDataFileLocator.
        System.out.println(parser.parse(templateLocator.get("index.mct"), parameters));
    }
}
```

## Examples
```java
public class Demo{
    private HtmlTemplateParser templateParser;
    private JsonResourceFileParser resourceFileParser;
    
    public Demo(){
         templateParser = new HtmlTemplateParser();
         resourceFileParser = new JsonResourceFileParser();    
    }
    
    public void showTitle() throws NoSuchFieldException {
        JsonObject parameters = resourceFileParser.parse("{\"name\": \"helloWorld\"}");

        String output = templateParser.parse("<h1>{{name}}</h1>", parameters);

        //output: <h1>helloWorld</h1>
    }  
    
    public void showList() throws NoSuchFieldException {
        JsonObject parameters = resourceFileParser.parse("{\"users\": [{\"name\": \"First User\"}, {\"name\": \"Second User\"}]}");
        
        String output = templateParser.parse("<ul>{% for user in users %}<li>{{user.name}}</li>{% endfor %}</ul>", parameters);
        
        //output: <ul><li>First User</li><li>Second User</li></ul>
    }
}
```
## Syntax
### Basic Variable
#### Template
```html
<p>{{ myContent }}</p>
```
#### data
```json
{
  "myContent": "HelloWorld"
}
```
#### Result
```html
<p>HelloWorld</p>
```

### If Statement
#### Template
```html
{% if isAdmin %}
<p>Hello there Admin</p>
{% elseif isLoggedIn %}
<p>Hello, you are logged in but not an admin.</p>
{% else %}
<p>Hello</p>
{% endif %}
```
#### data
```json
{
  "isAdmin": false,
  "isLoggedIn": true
}
```
#### Result
```html
<p>Hello, you are logged in but not an admin.</p>
```

### For Loop
#### Template
```html
<ul>
    {% for user in users %}
    <li>{{ user.name }}</li>
    {% endfor %}
</ul>
```
#### data
```json
{
  "users": [
    {
      "name": "First User here"
    },
    {
      "name": "Second User"
    }
  ]
}
```
#### Result
```html
<ul><li>First User here</li><li>Second User</li></ul>
```