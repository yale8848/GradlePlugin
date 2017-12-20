public class MyInject {

    public static void injectDir(String path, String packageName) {

        File dir = new File(path)
        if (dir.isDirectory()) {
            dir.eachFileRecurse { File file ->

                String filePath = file.absolutePath
                println "------MyInject1----- "+filePath

            }
        }
    }
}
