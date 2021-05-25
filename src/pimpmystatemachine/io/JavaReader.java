/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pimpmystatemachine.model.graphic.StateGraphicModel;
import pimpmystatemachine.model.graphic.StateMachineGraphicModel;
import pimpmystatemachine.model.graphic.TransitionGraphicModel;

/**
 * Class for reading a Java file and transforming it into a StateMachineModel
 *
 * @author Justin ROSSMANN
 */
public class JavaReader implements IReader {

    /**
     * Convert the contents of a File to a StateMachineModel
     *
     * @param file the file to read from
     * @return a StateMachineModel
     */
    private final StateMachineAdditionnals smAdds = new StateMachineAdditionnals();

    @Override
    public StateMachineGraphicModel read(File file) throws FileNotFoundException {
        String str = FileReaderWriter.read(file);
        StateMachineGraphicModel sm = readStateMachineGraphicModel(str);
        return sm;
    }

    /**
     * Convert a String to a StateMachineModel
     *
     * @param str the string to read
     * @return a statemachine
     */
    @Override
    public StateMachineGraphicModel readStateMachineGraphicModel(String str) {
        StateMachineGraphicModel sm = new StateMachineGraphicModel();

        //remove the comments
        str = removeComments(str);

        //Remove all the useless parts first
        str = uselessTrashRemover(str);

        //Then, extract the states
        Pattern statePattern = Pattern.compile("(public State [^\\s]* = new(?:(?!new\\sState).)*\\s*\\};)");
        Matcher stateMatcher = statePattern.matcher(str);
        while (stateMatcher.find()) {
            sm.addState(readStateGraphicModel(sm, stateMatcher.group(1).trim()));
        }

        //Update the transitions
        sm.getTransitionList().forEach(t -> {
            t.updateTransition(sm);
        });

        //Remove all the states from the string
        String str2 = str.trim();
        str2 = str2.replaceAll(statePattern.toString(), "");
        //Find all the other attributes the State Machine has and add them to the additionnals
        Pattern attributePattern = Pattern.compile("private ([^\\s]*) ([^\\s]*);");
        Matcher attributeMatcher = attributePattern.matcher(str2);
        while (attributeMatcher.find()) {
            String additionnalAttribute = attributeMatcher.group(0);
            smAdds.storeAdditionnalSMString(additionnalAttribute);
        }

        //TODO Find all the other methods the StateMachine has and put them in the StateMachineAdditionals
        return sm;
    }

    /**
     * Remove all Java comments from a String
     *
     * @param str the string to read from
     * @return another string with no comments
     */
    public String removeComments(String str) {
        Pattern commentPattern = Pattern.compile("//[^\n]*\n");
        Matcher commentMatcher = commentPattern.matcher(str);
        while (commentMatcher.find()) {
            smAdds.storeAdditionnalSMString(commentMatcher.group(0));
        }
        str = str.replaceAll("//[^\n]*\n", "");
        str = str.replaceAll("/[^/]*/", "");
        return str;
    }

    /**
     *
     * @param str the string to read from
     * @return a string with less stuff
     */
    public String uselessTrashRemover(String str) {
        //Remove package
        String regPackage = "package [^\\s]*;";
        str = str.replaceAll(regPackage, "");

        //Remove imports
        String regImports = "import [^\\s]*;";
        str = str.replaceAll(regImports, "");

        //Remove class declaration and everything above its
        String regClass = ".*extends\\s*StateMachine\\s*";
        str = str.replaceAll(regClass, "");

        //Remove some useless parts
        str = str.trim();
        str = str.replaceAll("\n", "");
        str = str.replaceAll("\t", "");
        str = str.replaceAll("\\s{2,}", " ");
        //remove the { and } at the beginning and the end
        str = str.substring(1, str.length() - 1).trim();
        str = str.replaceAll("@Override", "");

        return str;
    }

    private String extract(String str, String pattern) {
        Pattern patt = Pattern.compile(pattern);
        Matcher matcher = patt.matcher(str);
        matcher.find();
        return matcher.group(1).trim();
    }

    /**
     * Add all states it finds with their names into the given StateMachineModel
     *
     * @param sm the statemachine
     * @param str the string to read from
     * @return a state
     */
    @Override
    public StateGraphicModel readStateGraphicModel(StateMachineGraphicModel sm, String str) {
        //First get the name
        String name = extract(str, "public State ([^(]*?)=");

        StateGraphicModel s = new StateGraphicModel(name);

        //Then, find the transitions
        Pattern transitionPattern = Pattern.compile("(Transition\\s*[^\\s]*\\s*=\\s*new(?:(?!\\};).)*?\\};)");

        TransitionGraphicModel t;
        Matcher transitionMatcher = transitionPattern.matcher(str);
        while (transitionMatcher.find()) {
            t = readTransitionGraphicModel(sm, transitionMatcher.group(1).trim());
            t.setOriginName(name);
            sm.addTransition(t);
        }

        //TODO find all other methods and attributes and put them in the StateMachineAdditionals
        return s;
    }

    /**
     * Convert a String to a TransitionModel
     *
     * @param sm the statemachine
     * @param str the string to read from
     * @return a transition
     */
    @Override
    public TransitionGraphicModel readTransitionGraphicModel(StateMachineGraphicModel sm, String str) {
        TransitionGraphicModel t = new TransitionGraphicModel();

        //Get the name
        String name = extract(str, "Transition\\s*([^\\s]*)\\s*");
        t.setName(name);

        //Get the event
        String event = extract(str, "Transition<\\s*([^\\s]*)\\s*>");
        t.setEvent(event);

        //Get all methods
        String guard = "guard";
        t.setGuard(guard);
        String action = "action";
        t.setAction(action);

        String method;
        String additionnalMethods = "";
        Pattern pattern = Pattern.compile("((public|private)[^\\}]*\\})");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            method = matcher.group(1).trim();
            if (method.startsWith("public boolean guard")) {

            } else if (method.startsWith("public void action")) {

            } else if (method.startsWith("public State goTo")) {
                String goTo = extract(str, "public\\s*State\\s*goTo\\s*\\(\\)\\s*\\{\\s*return\\s*([^\\s]*);");
                t.setDestinationName(goTo);
            } else {
                //The found method is not directly linked with our model, thus it was handwritten by someone
                additionnalMethods += (method + "\n");
            }
        }

        //Adding the additionnal methods if there were any found
        if (!additionnalMethods.equals("")) {
            String originName = t.getOrigin().getName();
            smAdds.storeAdditionnalStateTransitionString(originName, name, additionnalMethods);
        }

        return t;
    }
}
