Hi 

Here a few comments about this solution to Ghost word game http://en.wikipedia.org/wiki/(Ghost_game)

1) It's a very bare-boned web application based on jQuery and the MVC Ajax support offered by Spring 3. 

2) There is no configuration involved. You can access the application using this url as an entry point: 
http://127.0.0.1:8080/optimalghost 

3) The algorithm of game resolution is based on the construction of a tree. First this tree is post-order traversed to find which nodes are goals for every player and then it is pre-order traversed again to prune it. All the nodes not logical from a computer player point of view are removed. These nodes would never be played

4) All application users share the same tree. They just have different pointers to different nodes of that game tree. 

5) Simplicity has been one of the main goals. There is little layering in my solution, in fact no services are injected in the Spring MVCcontroller which is accessing directly the game object. It seems to me kind of overarchitecting such a small application 

6) Things yet to be done? A lot. For instance i18n, ability to update/reload dictionaries from external sources, maybe trying to apply a flyweight pattern based on Character replacing String storage in memory to reduce memory footprint etc

7) And finally, the page design (or more properly speaking, the absolute lack of it) reflects my minimalistic aesthetic tastes ;-). Well, to be honest web,designing is far away from being one my talents

Best regards 
