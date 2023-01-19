# Setting deploy tomcat as root path

Prerequisite:

- change ROOT folder in webapps fodler to another name

## Option 1:

- change file app-name.war deploy to ROOT.war

## Option 2:

- Add a file called ROOT.xml in <catalina_home>/conf/Catalina/localhost/

    This ROOT.xml will override the default settings for the root context of the tomcat installation for that engine and host (Catalina and localhost).

- Enter the following to the ROOT.xml file;

```xml
    <Context 
      docBase="<yourApp>" 
      path="" 
      reloadable="true" 
    />
```

-- Note: Here, <yourApp> is the name your app or name file war (abc.war)

-- And there you go, your application is now the default application and will show up on http://localhost:8080

However, there is one side effect; your application will be loaded twice. Once for localhost:8080 and once for localhost:8080/yourApp.

To fix this you can put your application OUTSIDE <catalina_home>/webapps and use a relative or absolute path in the ROOT.xml's docBase tag.
Something like this;

```xml
<Context 
  docBase="/opt/mywebapps/<yourApp>" 
  path="" 
  reloadable="true" 
/>
```

## Option 3:

- The second option is to set the context path of the application in the server.xml (which is located at $CATALINA_HOME\conf).

    We must insert the following inside the <Host> tag for that:

```xml
    <Context path="" docBase="ExampleApp"></Context>
```

- Note: defining the context path manually has the side effect that the application is deployed twice by default: at http://localhost:8080/ExampleApp/ as well as at http://localhost:8080/.

  To prevent this, we have to set autoDeploy="false" and deployOnStartup="false" in the <Host> tag:

```xml
    <Host name="localhost" appBase="webapps" unpackWARs="true"
      autoDeploy="false" deployOnStartup="false">
        <Context path="" docBase="ExampleApp"></Context>   
        <!-- Further settings for localhost -->
    </Host>
```

- Important: this option is not recommended anymore, since Tomcat 5: it makes context configurations more invasive, since the server.xml file cannot be reloaded without restarting Tomcat.
