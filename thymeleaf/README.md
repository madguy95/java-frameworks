# Setting Hot-Swap for spring thymeleaf

In order to have it working We had to:

1. Add to the application.propreties file:

```java
    spring.thymeleaf.cache=false
```

2. Run the app in Debug mode

With eclipse and external tomcat, we need more:

- setting tomcat in Overview > Publishing tab with Automatically publish when resources change.

- setting tomcat in tab Modules -> change Auto Reload to Disabled

Recommend:

- In Overview > Server location don't use Tomcat Installation  (this's modify tomcat installation)
