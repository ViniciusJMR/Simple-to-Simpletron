package intermediatecode.commands;

import core.Symbol;
import core.Token;
import intermediatecode.ACommand;
import intermediatecode.CommandType;
import intermediatecode.TemporarySymbol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Arithmetics extends ACommand {
    public Arithmetics(List<Token> tokens) {
        super(tokens);
    }

    @Override
    public String getCodeLines(int index, Map<Integer, TemporarySymbol> symbols, Map<Integer, String> reversedSymbolMap) {
        List<Token> tProcess = new ArrayList<>();

        for(int i = 0; i < tokens.size(); i++){
            var token = tokens.get(i);
            if(token.getType() == Symbol.ASSIGNMENT){
                List<Integer> list = Arrays.asList(i-1, i+1, i+2, i+3);
                for (int idx : list){
                    try {
                        tProcess.add(tokens.get(idx));
                    }catch (IndexOutOfBoundsException e){
                    }
                }
            }
        }

        String code = "";

        switch (tProcess.size()) {
            case 2: { // EX: let a = 1
                Token targetVariableT = tProcess.get(0);
                Token aVariableT = tProcess.get(1);

                code = CommandType.LOAD + ACommand.getPlaceholder(aVariableT, symbols, reversedSymbolMap) + "\n";
                code += CommandType.STORE + ACommand.getPlaceholder(targetVariableT, symbols, reversedSymbolMap);
                System.out.println(code);
                break;
            }
            case 3: { // EX: let a = -1
                Token targetVariableT = tProcess.get(0);
                Token aVariableT = tProcess.get(2);

                code = CommandType.LOAD + ACommand.getPlaceholder(aVariableT, symbols, reversedSymbolMap) + "\n";
                code += CommandType.STORE + ACommand.getPlaceholder(targetVariableT, symbols, reversedSymbolMap);
                System.out.println(code);
                break;
            }
            case 4: { // EX: let a = b + 1
                Token targetVariableT = tProcess.get(0);
                Token aVariableT = tProcess.get(1);
                Token operationT = tProcess.get(2);
                Token bVariableT = tProcess.get(3);

                String bVariablePlaceholder = ACommand.getPlaceholder(bVariableT, symbols, reversedSymbolMap);
                code = CommandType.LOAD + ACommand.getPlaceholder(aVariableT, symbols, reversedSymbolMap) + "\n";

                switch (operationT.getType()) {
                    case DIVIDE:
                        code += CommandType.DIVIDE + bVariablePlaceholder;
                        break;
                    case ADD:
                        code += CommandType.ADD + bVariablePlaceholder;
                        break;
                    case SUBTRACT:
                        code += CommandType.SUBTRACT + bVariablePlaceholder;
                        break;
                    case MULTIPLY:
                        code += CommandType.MULTIPLY + bVariablePlaceholder;
                        break;
                    case MODULO:
                        code += CommandType.MODULE + bVariablePlaceholder;
                        break;
                }

                code += "\n" + CommandType.STORE + ACommand.getPlaceholder(targetVariableT, symbols, reversedSymbolMap);
                System.out.println(code);
                break;
            }
        }

        return code;
    }
}
