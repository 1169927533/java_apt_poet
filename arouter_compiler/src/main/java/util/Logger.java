package util;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

public class Logger {
    private Messager msg;

    public Logger(Messager messager) {
        this.msg = messager;
    }

    public void info(CharSequence info) {
        msg.printMessage(Diagnostic.Kind.NOTE, info);
    }
}
