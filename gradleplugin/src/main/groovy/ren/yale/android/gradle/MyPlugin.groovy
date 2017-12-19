package ren.yale.android.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
//http://xuyushi.github.io/2017/06/03/gradle%20%E6%8F%92%E4%BB%B6%E5%BC%80%E5%8F%91/
public class MyPlugin implements Plugin<Project>{

    @Override
    void apply(Project project) {
        project.extensions.create('apkdistconf', ApkDistExtension)
        project.task("apkdist") <<{


            def closure = project['apkdistconf'].nameMap
            println closure('wow!');
            println project['apkdistconf'].destDir

            println "hello ,world"
        }

    }
}
