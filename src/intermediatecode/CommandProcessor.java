package intermediatecode;

import core.Symbol;
import core.Token;
import intermediatecode.commands.GoTo;
import intermediatecode.commands.If;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CommandProcessor {

    private static class MappedLines {
        private Map<Integer, Integer> lines = new HashMap<>();
    }

    private static class Symbols {
        private final Map<Integer, TemporarySymbol> symbols = new HashMap<>();
    }

    public static String process(List<ACommand> commands, Map<Integer, String> reversedSymbolMap, boolean withDescription) {
        List<Token> allTokens = commands.stream().flatMap(c -> c.getTokens().stream()).collect(Collectors.toList());
//        Token[] allTokens = Arrays.stream(commands)
//                .flatMap(c -> c.getTokens().stream())
//                .toArray(Token[]::new);


        Symbols symbols = new Symbols();
        MappedLines mappedLines = new MappedLines();
        prepare(allTokens, symbols, reversedSymbolMap, mappedLines);
        String code = generateCode(commands, symbols, reversedSymbolMap, mappedLines);
        System.out.println("SYMBOLS");
        System.out.println(symbols.symbols);
        System.out.println("MAPPED LINES");
        System.out.println(mappedLines.lines);
        code = replaceGotoLines(commands, reversedSymbolMap, code, mappedLines);
        code = replaceTemporaryMarkers(symbols, code, reversedSymbolMap);

//        if (withDescription) {
//            AtomicInteger counter = new AtomicInteger(0);
//
//            code = Arrays.stream(code.split("\n"))
//                    .map(p -> {
//                        if (p.trim().isEmpty()) {
//                            return "";
//                        }
//                        String command = p.substring(1, 3);
//                        String variable = p.substring(3);
//                        String commandName = CommandType(command);
//                        int lineNumber = counter.incrementAndGet();
//
//                        if (!command.equals("00")) {
//                            String memoryType = "VARIABLE";
//                            if (CommandType.BRANCH_NEG.toString().contains(command)
//                                    || CommandType.BRANCH.toString().contains(command)
//                                    || CommandType.BRANCH_ZERO.toString().contains(command)
//                            ) {
//                                memoryType = " GT LINE";
//                            }
//                            if (CommandType.HALT.toString().contains(command)) {
//                                return p + "    #" + (lineNumber < 10 ? "0" + lineNumber : lineNumber) + " - COMMAND: " + commandName;
//                            }
//                            return p + "    #" + (lineNumber < 10 ? "0" + lineNumber : lineNumber) + " - COMMAND: " + String.format("%1$-11s", commandName) + " - " + memoryType + ": " + variable;
//                        } else {
//                            return p + "    #" + (lineNumber < 10 ? "0" + lineNumber : lineNumber) + " - VARIABLE";
//                        }
//                    })
//                    .collect(Collectors.joining("\n"));
//        }
        return code;
    }

    private static String replaceTemporaryMarkers(Symbols symbols, String code, Map<Integer, String> reversedSymbolMap) {
        List<TemporarySymbol> symbolsToDeclare = new ArrayList<>(symbols.symbols.values());
        int index = code.split("\n").length;
        for (TemporarySymbol symbol : symbolsToDeclare) {
            code += symbol.getDeclaration(reversedSymbolMap, ++index) + "\n";
        }

        for (TemporarySymbol symbol : symbolsToDeclare) {
            code = code.replaceAll(symbol.getPlaceHolder(), symbol.getReplacement());
        }

        return code;
    }

    private static String generateCode(List<ACommand> commands, Symbols symbols, Map<Integer, String> reversedSymbolMap, MappedLines mappedLines) {
        int index = 0;
        StringBuilder code = new StringBuilder();
        for (ACommand command : commands) {
            String codeLines = command.getCodeLines(index, symbols.symbols, reversedSymbolMap);
            String[] parts = codeLines.split("\n");
            int line = Integer.parseInt(command.getLine(reversedSymbolMap));
            mappedLines.lines.put(line, index + 1);

            index += parts.length;
            code.append(codeLines).append("\n");
        }
        return code.append(CommandType.HALT.getUid()).append("\n").toString();
    }

    private static void prepare(List<Token> allTokens, Symbols symbols, Map<Integer, String> reversedSymbolMap, MappedLines mappedLines) {
        for (int i = 0; i < allTokens.size(); i++) {
            Token current = allTokens.get(i);
            Token previous = i - 1 >= 0 ? allTokens.get(i - 1) : null;
            if (current.getType().equals(Symbol.VARIABLE) || current.getType().equals(Symbol.INTEGER)) {
                String symbol = reversedSymbolMap.get(current.getAddress());
                if (previous == null || !previous.getType().equals(Symbol.GOTO)) {
                    symbols.symbols.put(current.getAddress(), new TemporarySymbol(current, i, current.getLine(), previous));
                } else {
                    int desiredLine = Integer.parseInt(symbol);
                    mappedLines.lines.put(desiredLine, -1);
                }
            }
        }
    }

    private static String replaceGotoLines(List<ACommand> commands, Map<Integer, String> reversedSymbolMap, String code, MappedLines mappedLines) {
        for (ACommand current : commands) {
            if ( !(current instanceof GoTo || current instanceof If ) )
                continue;

            int targetLine = 0;
            String placeholder = "";
            if (current instanceof GoTo) {
                targetLine = ((GoTo) current).getTargetLine(reversedSymbolMap);
                placeholder = ((GoTo) current).getPlaceholder();
            } else if (current instanceof If) {
                targetLine = ((If) current).getTargetLine(reversedSymbolMap);
                placeholder = ((If) current).getPlaceholder();
            }

            System.out.println("TARGET LINE: " + targetLine);
            System.out.println("PLACEHOLDER: " + placeholder);
            int value = mappedLines.lines.get(targetLine);
            String valueStr = Integer.toString(value);
            if (value < 10) {
                valueStr = "0" + value;
            }

            int index = code.indexOf(placeholder);
            while (index != -1) {
                code = code.substring(0, index) + valueStr + code.substring(index + placeholder.length());
                index = code.indexOf(placeholder, index + valueStr.length());
            }
        }
        return code;
    }

}
