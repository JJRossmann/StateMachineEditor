/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import pimpmystatemachine.model.graphic.StateGraphicModel;
import pimpmystatemachine.model.graphic.StateMachineGraphicModel;
import pimpmystatemachine.model.graphic.TransitionGraphicModel;
import pimpmystatemachine.model.semantics.StateModel;
import pimpmystatemachine.model.semantics.TransitionModel;

/**
 *
 * @author Justin ROSSMANN
 */
public class JsonWriter implements IWriter {

    /**
     * Convert a single parameter into json
     *
     * @param param
     * @param value
     * @param comma
     * @return
     */
    private String convertSimpleParameter(String param, Object value, boolean comma) {
        String str = "\t\"" + param + "\": ";

        if (value instanceof String) {
            str += "\"" + (String) value + "\"";
        } else if (value instanceof Double) {
            str += (double) value;
        } else if (value instanceof Boolean) {
            str += (boolean) value;
        }

        if (comma == true) {
            str += ",";
        }

        str += "\n";
        return str;
    }

    @Override
    public String convertStateMachine(StateMachineGraphicModel sm) {
        StringBuilder str = new StringBuilder("{\n");
        str.append("\"stateGraphicList\": [");
        ArrayList<StateModel> states = sm.getStateList();
        int lsize = states.size();
        for (int i = 0; i < lsize; i++) {
            str.append(convertState((StateGraphicModel) states.get(i)));
            if (i != lsize - 1) {
                str.append(",\n");
            }
        }
        str.append("]\n}");
        return str.toString();
    }

    /**
     * Convert a TransitionList into json
     *
     * @param list
     * @return
     */
    private String convertTransitionList(ArrayList<TransitionModel> list) {
        StringBuilder str = new StringBuilder();
        str.append("[");
        int lsize = list.size();
        for (int i = 0; i < lsize; i++) {
            str.append(convertTransition((TransitionGraphicModel) list.get(i)));
            if (i != lsize - 1) {
                str.append(",\n");
            }
        }
        str.append("]");
        return str.toString();
    }

    /**
     * Convert a Color to a json format
     *
     * @param c
     * @return
     */
    private String convertColor(Color c) {
        StringBuilder str = new StringBuilder();
        str.append("[");
        str.append(c.getRed());
        str.append(",");
        str.append(c.getGreen());
        str.append(",");
        str.append(c.getBlue());
        str.append("]");
        return str.toString();
    }

    @Override
    public String convertState(StateGraphicModel s) {
        StringBuilder str = new StringBuilder("{\n");
        //StateModel
        str.append(convertSimpleParameter("sname", s.getName(), true));
        str.append(convertSimpleParameter("isInitial", s.isInitial(), true));
        str.append(convertSimpleParameter("isFinal", s.isFinal(), true));
        str.append(convertSimpleParameter("enter", s.getEnter(), true));
        str.append(convertSimpleParameter("leave", s.getLeave(), true));

        //StateGraphicModel
        str.append(convertSimpleParameter("size", s.getSize(), true));
        str.append(convertSimpleParameter("positionX", s.getPositionX(), true));
        str.append(convertSimpleParameter("positionY", s.getPositionY(), true));
        str.append(convertSimpleParameter("comment", s.getComment(), true));

        str.append("\"borderColor\": ");
        str.append(convertColor(s.getBorderColor()));
        str.append(",\n");
        str.append("\"insideColor\": ");
        str.append(convertColor(s.getInsideColor()));
        str.append(",\n");

        str.append("\"enteringList\": ");
        str.append(convertTransitionList(s.getEnteringList()));
        str.append(",\n");
        str.append("\"exitingList\": ");
        str.append(convertTransitionList(s.getExitingList()));

        str.append("}");
        return str.toString().replaceAll("\n", "\n\t");
    }

    @Override
    public String convertTransition(TransitionGraphicModel t) {
        StringBuilder str = new StringBuilder("{\n");
        //TransitionModel
        str.append(convertSimpleParameter("tname", t.getName(), true));
        str.append(convertSimpleParameter("originName", t.getOrigin().getName(), true));
        str.append(convertSimpleParameter("destinationName", t.getDestination().getName(), true));
        str.append(convertSimpleParameter("event", t.getEvent(), true));
        str.append(convertSimpleParameter("guard", t.getGuard(), true));
        str.append(convertSimpleParameter("action", t.getAction(), true));
        //TransitionGraphicModel
        str.append(convertSimpleParameter("thickness", t.getThickness(), true));
        str.append(convertSimpleParameter("C1X", t.getC1X(), true));
        str.append(convertSimpleParameter("C1Y", t.getC1Y(), true));
        str.append(convertSimpleParameter("C2X", t.getC2X(), true));
        str.append(convertSimpleParameter("C2Y", t.getC2Y(), true));
        str.append("\"transitionColor\": ");
        str.append(convertColor(t.getColor()));
        str.append("\n");
        str.append("}");
        return str.toString().replaceAll("\n", "\n\t");
    }

    @Override
    public void write(StateMachineGraphicModel sm, File file) throws IOException {
        String str = convertStateMachine(sm);
        FileReaderWriter.write(file, str);
    }

}
