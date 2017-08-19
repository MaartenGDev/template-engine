# Template-Engine
This is a example Template Engine


## Example
```java
public class Main {

    public static void main(String[] args) throws IOException, NoSuchFieldException {
        String basePath = "/path/to/template/files";

        FileLocator templateLocator = new FileLocator(basePath);

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("isLoggedIn", true);
        List<Map<String, Object>> posts = new ArrayList<>();

        HashMap<String, Object> firstPost = new HashMap<>();
        firstPost.put("title", "Hello world");
        firstPost.put("user", "Java Template engine");
        firstPost.put("date", "2017-12-13");
        firstPost.put("body", "my post");

        HashMap<String, Object> secondPost = new HashMap<>();
        secondPost.put("title", "Second First");
        secondPost.put("user", "Admin");
        secondPost.put("date", "2017-11-13");
        secondPost.put("body", "second post");

        posts.add(firstPost);
        posts.add(secondPost);

        parameters.put("posts", posts);

        HtmlTemplateParser parser = new HtmlTemplateParser(templateLocator);

        System.out.println(parser.parse(templateLocator.get("blog.mct"), parameters));
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
```java
HashMap<String, Object> parameters = new HashMap<>();
parameters.put("myContent", "Content");
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
HashMap<String, Object> parameters = new HashMap<>();
parameters.put("isAdmin", false);
parameters.put("isLoggedIn", true);
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
```java
Map<String, Object> parameters = new HashMap<>();
List<Map<String, Object>> posts = new ArrayList<>();

HashMap<String, Object> firstPost = new HashMap<>();
firstPost.put("title", "Hello world");
firstPost.put("date", "2017-12-13");

HashMap<String, Object> secondPost = new HashMap<>();
secondPost.put("title", "Second First");
secondPost.put("date", "2017-11-13");

posts.add(firstPost);
posts.add(secondPost);

parameters.put("posts", posts);
```
#### Result
```html
<ul><li>First User here</li><li>Second User</li></ul>
```