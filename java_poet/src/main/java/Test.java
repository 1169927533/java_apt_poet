import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        buildAjavaFile();
    }

    static void buildAjavaFile() throws IOException {
        MethodSpec methodSpec = MethodSpec.methodBuilder("test")
                .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                .returns(void.class)
                .addParameter(Integer.class,"loop")
                .addCode("System.out.print(\"生成的代码\");\n")
                .addCode("$T a = $L", Student.class,"new Student()")
                .build();
        TypeSpec typeSpec = TypeSpec.classBuilder("TestCode")
                .addModifiers(Modifier.PUBLIC,Modifier.FINAL)
                .addMethod(methodSpec)
                .build();
        JavaFile javaFile = JavaFile.builder("com.haha.hah",typeSpec)
                .build();
        // 将java文件内容写入文件中
        File file = new File("java_poet/build_result");
        javaFile.writeTo(file);
    }
}
