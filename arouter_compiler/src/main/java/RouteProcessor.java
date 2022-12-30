
import annotation.Route;
import api.ISyring;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import util.Consts;
import util.Logger;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.Set;

import static javax.lang.model.element.Modifier.PUBLIC;

@AutoService(Processor.class)
//标记支持的注解有哪些
@SupportedAnnotationTypes({Consts.ANNOTATION_TYPE_ROUTE, Consts.ANNOTATION_TYPE_AUTOWIRED})
public class RouteProcessor extends AbstractProcessor {
    Logger logger;
    Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        logger = new Logger(env.getMessager());
        mFiler = env.getFiler();
    }

    /**
     * @param annoations 只有当需要被处理的注解被用过了,才会存在他里面去.
     * @param env
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annoations, RoundEnvironment env) {
        Set<TypeElement> s = (Set<TypeElement>) env.getElementsAnnotatedWith(Route.class);
        try {
            if (s != null || !s.isEmpty()) {
                parseRoutes(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void parseRoutes(Set<? extends Element> routeElements) throws IOException {
        logger.info(">>> Found routes , size = " + routeElements.size() + "<<<");
        for (Element element : routeElements) {
            Route annotation = element.getAnnotation(Route.class);
            String routerPath = annotation.path();
            String fileName = element.getSimpleName() + "$$Arouter";
            TypeSpec.Builder helper = TypeSpec.classBuilder(fileName)
                    .addSuperinterface(ISyring.class)
                    .addModifiers(Modifier.PUBLIC);
            MethodSpec.Builder injectMethodBuilder = MethodSpec.methodBuilder("inject")
                    .addAnnotation(Override.class)
                    .addModifiers(PUBLIC)
                    .addParameter(Object.class, "target");
            injectMethodBuilder.addStatement("String path = $S", routerPath);
            helper.addMethod(injectMethodBuilder.build());
            JavaFile.builder(Consts.PACKAGE_OF_GENERATE_FILE, helper.build())
                    .build().writeTo(mFiler);
        }
    }
}