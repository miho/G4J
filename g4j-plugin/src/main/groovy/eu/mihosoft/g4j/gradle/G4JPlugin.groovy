package eu.mihosoft.g4j.gradle;
 
import org.gradle.api.*;
import java.nio.file.Paths;
import java.nio.file.Path;
 
class G4JPlugin implements Plugin {
    def void apply(Object project) {
        
        project.extensions.create("g4j", G4JPluginExtension)
        
        project.task('g4j') << {
            println ">> G4JPlugin: processing files"
            project.sourceSets.all {
                allJava.getSrcDirs().each() {
                    dir->
                    if (dir.exists()) {
                       
                        eu.mihosoft.g4j.lang.G4JUtil.processDir(
                            Paths.get(dir.getAbsolutePath()),
//                            Paths.get(dir.getAbsolutePath()+"/" + project.g4j.g4jPackageName.replace(".","/")+"/g4j_tmp.java"),
                            Paths.get(dir.getAbsolutePath()+"/" + project.g4j.g4jPackageName.replace(".","/")),
                            project.g4j.g4jPackageName);
                        
                    }
                }
            }
        }
    }
}

class G4JPluginExtension {
    def g4jPackageName = 
         "g4j";
}
