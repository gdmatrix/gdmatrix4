#GDMatrix conventions

##CDI naming conventions

-<module>ManagerPort: module interface
-<module>Manager: module implementation (@PerRequest)
-<module>ManagerEndpoint: RS/WS module endpoint (@Singleton)

-<object>ObjectBean: Faces object bean (@ViewScoped)
-<objectTab>TabBean: Faces object tab bean (@ViewScoped)
-<object>FinderBean: Faces object finder bean (@ViewScoped)
-<module>ModuleBean: Faces module bean (@ApplicationScoped)
-<name>Bean: Faces bean (@RequestScoped, @SessionScoped, @ApplicationBean)

-<name>Service: singleton service (@ApplicationScoped)
-<object>Producer: object producer (@ApplicationScoped)
-<object>Cache: object cache (@ApplicationScoped)
