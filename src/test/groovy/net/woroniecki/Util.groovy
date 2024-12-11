package net.woroniecki

class Util {

    static String readFileToString(String filePath) {
        def resource = Util.class.getResource(filePath)
        if (resource == null) {
            throw new IllegalArgumentException("Resource not found: $filePath")
        }
        return resource.text
    }
}
