/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.paint.Color;
import pimpmystatemachine.model.graphic.StateGraphicModel;
import pimpmystatemachine.model.graphic.StateMachineGraphicModel;
import pimpmystatemachine.model.graphic.TransitionGraphicModel;

/**
 *
 * @author Justin ROSSMANN
 */
public class JsonReader implements IReader {

    @Override
    public StateMachineGraphicModel readStateMachineGraphicModel(String str) {
        StateMachineGraphicModel sm = new StateMachineGraphicModel();

        //replace all white caracters
        str = str.replaceAll("\\p{Space}", "");

        Pattern pattern = Pattern.compile("\\{\"stateGraphicList\":\\[(.*)\\]\\}");
        Matcher matcher = pattern.matcher(str);
        matcher.find();
        str = matcher.group(1);

        //Extract the states
        Pattern pattern2 = Pattern.compile("(\\{\"sname(?:(?!sname).)*\\})");
        Matcher matcher2 = pattern2.matcher(str);

        while (matcher2.find()) {
            sm.addState(readStateGraphicModel(sm, matcher2.group(1)));
        }

        //update the transitions
        sm.getTransitionList().forEach(t -> {
            t.updateTransition(sm);
        });
        return sm;
    }

    @Override
    public StateGraphicModel readStateGraphicModel(StateMachineGraphicModel sm, String str) {
        //extract all parameters of a state
        String name = extractParameter(str, "sname");
        StateGraphicModel s = new StateGraphicModel(name);

        String isInitial = extractParameter(str, "isInitial");
        s.setInitial(Boolean.valueOf(isInitial));
        String isFinal = extractParameter(str, "isFinal");
        s.setFinal(Boolean.valueOf(isFinal));

        String enter = extractParameter(str, "enter");
        s.setEnter(enter);
        String leave = extractParameter(str, "leave");
        s.setLeave(leave);

        String comment = extractParameter(str, "comment");
        s.setLeave(comment);

        String size = extractParameter(str, "size");
        s.setSize(Double.parseDouble(size));
        String positionX = extractParameter(str, "positionX");
        s.setPositionX(Double.parseDouble(positionX));
        String positionY = extractParameter(str, "positionY");
        s.setPositionY(Double.parseDouble(positionY));
        s.setBorderColor(extractColor(str, "borderColor"));
        s.setInsideColor(extractColor(str, "insideColor"));

        ArrayList<String> strList = extractTransitions(str);
        strList.forEach(strg -> {
            sm.addTransition(readTransitionGraphicModel(sm, strg));
        });
        return s;
    }

    /**
     * Extract the value of a single parameter from the json string
     *
     * @param str
     * @param paramName
     * @return
     */
    private String extractParameter(String str, String paramName) {
        Pattern patt = Pattern.compile("\"" + paramName + "\":([^,\\}]*)");
        Matcher matcher = patt.matcher(str);
        matcher.find();
        return matcher.group(1).replaceAll("\"", "");
    }

    /**
     * Extract a color from its Json form
     *
     * @param str
     * @param param
     * @return
     */
    private Color extractColor(String str, String param) {
        Pattern patt = Pattern.compile("\"" + param + "\":\\[([^\\[]*)\\]");
        Matcher matcher = patt.matcher(str);
        matcher.find();
        String temp = matcher.group(1);

        //extract the red, green and blue parts
        Pattern patt2 = Pattern.compile("([^,]+)");
        Matcher matcher2 = patt2.matcher(temp);
        matcher2.find();
        String red = matcher2.group(0);
        matcher2.find();
        String green = matcher2.group(0);
        matcher2.find();
        String blue = matcher2.group(0);
        return Color.color(Double.parseDouble(red), Double.parseDouble(green), Double.parseDouble(blue));
    }

    /**
     * Extract all transitions from a State
     *
     * @param str
     * @return
     */
    private ArrayList<String> extractTransitions(String str) {
        Pattern patt = Pattern.compile("\"exitingList\":.*\\]");
        Matcher matcher = patt.matcher(str);
        matcher.find();
        String temp = matcher.group(0);

        ArrayList<String> strList = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\{[^\\}]*})");
        matcher = pattern.matcher(temp);
        while (matcher.find()) {
            strList.add(matcher.group(1));
        }
        return strList;
    }

    @Override
    public TransitionGraphicModel readTransitionGraphicModel(StateMachineGraphicModel sm, String str) {
        //Extract all parameters of a Transition
        TransitionGraphicModel t = new TransitionGraphicModel();
        String name = extractParameter(str, "tname");
        t.setName(name);
        String originName = extractParameter(str, "originName");
        t.setOriginName(originName);
        String destinationName = extractParameter(str, "destinationName");
        t.setDestinationName(destinationName);
        String event = extractParameter(str, "event");
        t.setEvent(event);
        String guard = extractParameter(str, "guard");
        t.setGuard(guard);
        String action = extractParameter(str, "action");
        t.setAction(action);
        String thickness = extractParameter(str, "thickness");
        t.setThickness(Double.parseDouble(thickness));
        String c1x = extractParameter(str, "C1X");
        t.setC1X(Double.parseDouble(c1x));
        String c1y = extractParameter(str, "C1Y");
        t.setC1Y(Double.parseDouble(c1y));
        String c2x = extractParameter(str, "C2X");
        t.setC2X(Double.parseDouble(c2x));
        String c2y = extractParameter(str, "C2Y");
        t.setC2Y(Double.parseDouble(c2y));
        t.setColor(extractColor(str, "transitionColor"));
        return t;
    }

    @Override
    public StateMachineGraphicModel read(File file) throws FileNotFoundException {
        String json = FileReaderWriter.read(file);
        StateMachineGraphicModel sm = readStateMachineGraphicModel(json);
        return sm;
    }

}
