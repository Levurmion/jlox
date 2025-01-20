package com.lox;

import java.util.HashMap;

public class CommandLine {
    
    final private HashMap<String, Boolean> shortFlags = new HashMap<>();
    final private HashMap<String, Boolean> longFlags = new HashMap<>();
    final private HashMap<String, String> parameters = new HashMap<>();

    public CommandLine(String[] arguments) {
        int curr = 0;

        while (curr < arguments.length) {
            String arg = arguments[curr];

            if (isLongFlag(arg)) {
                if (curr < arguments.length - 1) {
                    String nextFlag = arguments[curr + 1];
                    if (!isShortFlag(nextFlag) && !isLongFlag(nextFlag)) {
                        parameters.put(arg.substring(2), nextFlag);
                        curr += 2;
                        continue;
                    }
                }
                longFlags.put(arg.substring(2), true);
            } else if (isShortFlag(arg)) {
                shortFlags.put(arg.substring(1), true);
            }

            curr++;
        }
    }
    
    private boolean isShortFlag (String arg) {
        return arg.startsWith("-");
    } 

    private boolean isLongFlag (String arg) {
        return arg.startsWith("--");
    }

    public boolean hasShortFlag (String flag) {
        return shortFlags.containsKey(flag);
    }

    public boolean hasLongFlag (String flag) {
        return longFlags.containsKey(flag);
    }

    public String getParameter (String flag) {
        return parameters.get(flag);
    }

}
