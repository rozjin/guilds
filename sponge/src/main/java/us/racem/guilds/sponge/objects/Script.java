package us.racem.guilds.sponge.objects;

import com.whl.quickjs.wrapper.JSObject;
import com.whl.quickjs.wrapper.QuickJSContext;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "scripts")
public class Script {
    @Id
    public int id;

    @OneToOne
    private Law act;

    public String content;

    @Transient private Guild juris;
    @Transient private QuickJSContext js;
    @Transient private byte[] code;
    @Transient private JSObject ctx;
    @Transient private List<String> log;

    public Script() {
        this.juris = act.juris;
        this.log = new ArrayList<>();

        this.js = QuickJSContext.create();
        this.code = js.compile(content);
        this.ctx = js.createNewJSObject();

        this.bind();
    }

    private void destroy() {
        QuickJSContext.destroy(js);
        QuickJSContext.destroyRuntime(js);
    }

    @Override
    protected void finalize() throws Throwable {
        destroy();

        super.finalize();
    }

    private void bind() {
        js.getGlobalObject().setProperty("nativeLog", args -> {
            if (args.length == 2) {
                String level = (String) args[0];
                String info = (String) args[1];

                switch (level) {
                    case "info": log.add("[INFO] [CTX: " + id + "] " + info); break;
                    case "warn": log.add("[WARN] [CTX: " + id + "] " + info); break;
                    case "error": log.add("[FAIL] [CTX: " + id + "] " + info); break;

                    case "log":
                    case "debug": log.add("[DBG] [CTX: " + id + "] " + info); break;
                }
            }

            return null;
        });

        js.evaluate("const console = {\n" +
                "    log: (...args) => printLog(\"log\", ...args),\n" +
                "    debug: (...args) => printLog(\"debug\", ...args),\n" +
                "    info: (...args) => printLog(\"info\", ...args),\n" +
                "    warn: (...args) => printLog(\"warn\", ...args),\n" +
                "    error: (...args) => printLog(\"error\", ...args)\n" +
                "};\n" +
                "\n" +
                "const printLog = (level, ...args) => {\n" +
                "    let arg = '';\n" +
                "    if (args.length == 1) {\n" +
                "        let m = args[0];\n" +
                "        arg = __format_string(m);\n" +
                "    } else if (args.length > 1) {\n" +
                "        for (let i = 0; i < args.length; i++) {\n" +
                "            if (i > 0) {\n" +
                "                arg = arg.concat(', ');\n" +
                "            }\n" +
                "            let m = args[i];\n" +
                "            arg = arg.concat(__format_string(m));\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    nativeLog(level, arg);\n" +
                "};");

        js.getGlobalObject().setProperty("guild", ctx);
    }
}
