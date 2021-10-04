# Use this doc to help install the java 8 jdk and set up the environment

## Windows Setup
> Follow these steps to install the java jdk for windows
1. Visit https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html and select 
'Accept Licence Agreement' and download the Windows x64 bit installer
1. Sign up for an account (*Need to check if this step is needed*) it shouldn't be.
1. Run the windows installer

> To configure IntelliJ follow these steps:
1. If you haven't installed IntelliJ visit this link https://www.jetbrains.com/idea/download/#section=windows and select
community edition (if you don't have a paid licence). It is possible to create an account with your UQ student email and
use the paid products.
1. When you have it installed, open IntelliJ and go through the configuration how you would like (can leave as default)
1. Once you have set the plugins, theme etc. you will see a box that allows you to import/open a project, create a new
one etc. DO NOT click those yet!
1. First, in the bottom right corner select the "configure" button and from the dropdown select "Structure for New
Projects".
1. Under "Project SDK" select 'New' -> 'JDK', find and select the jdk directory. THe path generally looks like
```C:\Program Files\Java\jdk.1.8.0_102```
1. Once you have selected the directory, click 'Apply' -> 'OK'
1. You can now open/import the deco project into IntelliJ. IntelliJ will automatically download and configure gradle
based on the config file already located in the project
> To set the ```JAVA_HOME``` variable needed to run the application properly follow these steps or go to
https://javatutorial.net/set-java-home-windows-10:
1. In the search bar type 'advanced system settings' and click the 'View advanced system settings' box
1. Click the box that says 'Environment Variables'
1. Under the "System Variables" click New and Enter ```JAVA_HOME``` as the variable name and the path to the jdk under
variable value i.e ```C:\Program Files\Java\jdk.1.8.0_102```. Then click Ok
1. Next, find the "Path" system variable and select 'Edit'. In the box that appears, select 'New' and enter
```%JAVA_HOME%\bin```
1. Restart your computer to ensure the changes take effect
1. To test that the jdk has been added to your path correctly, open up a terminal and type ```eco %JAVA_HOME%```. If it
prints out the directory with the jdk location then it has been applied correctly. If a blank line is printed the jdk was
not added correctly.
1. Additionally, you can type the command ```javac -version``` to test java is recognised globally

## Mac Setup
#### TODO: Fill in details for mac devices
