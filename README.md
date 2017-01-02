# CompactNPCLib

[![Build Status](http://ci.inventivetalent.org/job/CompactNPCLib/badge/icon)](https://ci.inventivetalent.org/job/CompactNPCLib/)

Library to create NPCs.  

This is a "*compact*" (&amp; experimental) version of [NPCLib](https://github.com/InventivetalentDev/NPCLib), which uses Javassist and Reflection to create NPC classes for any Minecraft entity.  

Please check the [Wiki](https://github.com/InventivetalentDev/CompactNPCLib/wiki) or the [SpigotMC page](https://www.spigotmc.org/resources/api-npclib-1-7-1-8-1-9.5853/) for more information. 

## Maven
```xml
<repositories>
    <repository>
        <id>inventive-repo</id>
        <url>https://repo.inventivetalent.org/content/groups/public/</url>
    </repository>
</repositories>
<dependencies>
    <dependency>
        <groupId>org.inventivetalent.npc-lib</groupId>
        <artifactId>api</artifactId>
        <version>VERSION</version>
    </dependency>
</dependencies>
```

## Contributing
Please open a new pull request for any feature you think this API could need. 
Especially for methods you think should be directly available in the NPC classes, without having to call `#getBukkitEntiy` first.  
There are currently also many classes that are not fully documented yet, so feel free to complete those as well ;)

Please also create pull requests for entities currently not available/added in new Minecraft versions. Refer to the [**Creating custom NPC classes**](https://github.com/InventivetalentDev/CompactNPCLib/wiki/Creating-custom-NPC-classes) Wiki if you need help.
