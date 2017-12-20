package ren.yale.android.gradle

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.google.common.collect.ImmutableSet
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
/**
 * Created by yale on 2017/12/20.
 */

public class MyTransform extends Transform{

    Project mProject;
    private static ClassPool pool = ClassPool.getDefault();
    public MyTransform(Project project){
        mProject = project;
    }

    @Override
    public String getName() {
        return "MyTrans";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {

        return ImmutableSet.of(
                QualifiedContent.Scope.PROJECT,
                QualifiedContent.Scope.SUB_PROJECTS,
                QualifiedContent.Scope.PROJECT_LOCAL_DEPS,
                QualifiedContent.Scope.SUB_PROJECTS_LOCAL_DEPS,
                QualifiedContent.Scope.EXTERNAL_LIBRARIES
        )
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs, TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {

        inputs.each {TransformInput input ->

            input.directoryInputs.each {DirectoryInput directoryInput->

                MyInject.injectDir(directoryInput.file.absolutePath,"com\\hc\\hcplugin")

                def dest = outputProvider.getContentLocation(directoryInput.name,
                        directoryInput.contentTypes, directoryInput.scopes,
                        Format.DIRECTORY)
                println("----transform1---- directoryInput.file :"+directoryInput.file.absolutePath)
                println("----transform1---- directoryInput.dest :"+dest.absolutePath)
                FileUtils.copyDirectory(directoryInput.file, dest)
            }
            input.jarInputs.each {JarInput jarInput->
                def jarName = jarInput.name
                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                if(jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0,jarName.length()-4)
                }

                def dest = outputProvider.getContentLocation(jarName+md5Name,
                        jarInput.contentTypes, jarInput.scopes, Format.JAR)
                println("----transform---- jarInput:"+jarInput.file.absolutePath)
                println("----transform---- jarInput dest:"+dest.absolutePath)



                FileUtils.copyFile(jarInput.file, dest)

                if (jarInput.file.absolutePath.indexOf("com.squareup.retrofit2\\retrofit\\2.1.0")>0){

                    println("---------start work-------------")

                    pool.insertClassPath(dest.absolutePath);
                    CtClass cc1 = pool.get("retrofit2.Retrofit");
                    CtMethod method = cc1.getDeclaredMethod("create");
                    method.insertAt(138, "System.out.println(\"------------yale add------------\");");
                    byte[] b = cc1.toBytecode();
                    JarHandler.replaceJarFile(dest.absolutePath, b, "retrofit2/Retrofit.class");
                }
            }
        }
    }
}
