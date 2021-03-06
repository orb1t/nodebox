package nodebox.client;

import nodebox.node.Node;
import nodebox.node.NodeLibrary;
import nodebox.node.Parameter;

import javax.swing.*;
import java.awt.*;

public class ExpressionWindow extends AbstractParameterEditor {

    private JTextArea expressionArea;
    private JTextArea errorArea;

    public ExpressionWindow(Parameter parameter) {
        super(parameter);
    }

    public Component getContentArea() {
        expressionArea = new JTextArea(getParameter().getExpression());
        expressionArea.setFont(Theme.EDITOR_FONT);
        expressionArea.setBorder(null);
        JScrollPane expressionScroll = new JScrollPane(expressionArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        expressionScroll.setBorder(BorderFactory.createEtchedBorder());

        errorArea = new JTextArea();
        errorArea.setFont(Theme.EDITOR_FONT);
        errorArea.setBorder(null);
        errorArea.setBackground(Theme.EXPRESSION_ERROR_BACKGROUND_COLOR);
        errorArea.setEditable(false);
        errorArea.setForeground(Theme.EXPRESSION_ERROR_FOREGROUND_COLOR);
        JScrollPane errorScroll = new JScrollPane(errorArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        errorScroll.setBorder(BorderFactory.createEtchedBorder());

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, expressionScroll, errorScroll);
        split.setBorder(null);
        split.setDividerLocation(150);
        split.setDividerSize(2);
        return split;
    }

    public void valueChanged(Parameter source) {
        // Don't change the expression area if the expression is the same.
        // This would cause an infinite loop of setExpression/valueChanged calls.
        if (expressionArea.getText().equals(getParameter().getExpression())) return;
        expressionArea.setText(getParameter().getExpression());
    }

    public boolean save() {
        expressionArea.requestFocus();
        getParameter().setExpression(expressionArea.getText());
        if (getParameter().hasExpressionError()) {
            errorArea.setText(getParameter().getExpressionError().getCause().toString());
            return false;
        } else {
            errorArea.setText("");
            return true;
        }
    }

    public static void main(String[] args) {
        NodeLibrary testLibrary = new NodeLibrary("test");
        Node node = Node.ROOT_NODE.newInstance(testLibrary, "test");
        Parameter pX = node.addParameter("x", Parameter.Type.FLOAT);
        AbstractParameterEditor win = new ExpressionWindow(pX);
        win.setVisible(true);
    }

}
