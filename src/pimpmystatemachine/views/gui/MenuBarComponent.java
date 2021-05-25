/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.views.gui;

import java.awt.image.RenderedImage;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import pimpmystatemachine.io.JavaReader;
import pimpmystatemachine.io.JavaWriter;
import pimpmystatemachine.io.JsonReader;
import pimpmystatemachine.io.JsonWriter;
import pimpmystatemachine.io.SmalaWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import static javafx.scene.layout.Region.USE_PREF_SIZE;
import pimpmystatemachine.model.graphic.StateMachineGraphicModel;

/**
 *
 * @author Fatiha OUARDI, Justin ROSSMANN
 */
public class MenuBarComponent extends MenuBar {

    private Stage stage;
    private CenterComponent center;

    //Icons
    private static final ImageView undoIcon = new ImageView("file:resources/menu/undo.png");
    private static final ImageView redoIcon = new ImageView("file:resources/menu/redo.png");
    private static final ImageView zoomInIcon = new ImageView("file:resources/menu/zoom_in.png");
    private static final ImageView zoomOutIcon = new ImageView("file:resources/menu/zoom_out.png");

    //Menus
    private static final Menu fileMenu = new Menu("File");
    private static final Menu editMenu = new Menu("Edit");
    private static final Menu viewMenu = new Menu("View");

    //Menu items
    private static final MenuItem menuItemNewFile = new MenuItem("New File");
    private static final MenuItem menuItemSaveFile = new MenuItem("Save File");
    private static final MenuItem menuItemOpenFile = new MenuItem("Open File");
    private static final SeparatorMenuItem separator1 = new SeparatorMenuItem();
    private static final Menu menuImportFile = new Menu("Import File");
    private static final MenuItem menuItemImportJava = new MenuItem("Import From Java");
    private static final MenuItem menuItemImportSmala = new MenuItem("Import From Smala");

    private static final Menu menuExportFile = new Menu("Export File");
    private static final MenuItem menuItemExportJava = new MenuItem("Export To Java");
    private static final MenuItem menuItemExportSmala = new MenuItem("Export To Smala");

    private static final SeparatorMenuItem separator2 = new SeparatorMenuItem();
    private static final MenuItem menuItemSnapshot = new MenuItem("Save to image");
    private static final SeparatorMenuItem separator3 = new SeparatorMenuItem();
    private static final MenuItem menuItemQuit = new MenuItem("Exit");

    private static final MenuItem menuItemUndo = new MenuItem("Undo", undoIcon);
    private static final MenuItem menuItemRedo = new MenuItem("Redo", redoIcon);
    private static final MenuItem menuItemRemoveAll = new MenuItem("Remove all");

    private static final MenuItem menuItemZoomIn = new MenuItem("Zoom In", zoomInIcon);
    private static final MenuItem menuItemZoomOut = new MenuItem("Zoom Out", zoomOutIcon);
    private static final MenuItem menuItemHelp = new MenuItem("Help");

    /**
     * Constructor for MenuBarComponent
     *
     * @param stage stage
     * @param center center
     */
    public MenuBarComponent(Stage stage, CenterComponent center) {
        super();
        this.stage = stage;
        this.center = center;

        //Icon Parameters
        undoIcon.setFitWidth(20);
        undoIcon.setFitHeight(20);
        redoIcon.setFitWidth(20);
        redoIcon.setFitHeight(20);
        zoomInIcon.setFitWidth(20);
        zoomInIcon.setFitHeight(20);
        zoomOutIcon.setFitWidth(20);
        zoomOutIcon.setFitHeight(20);

        //Construct the menu
        getMenus().addAll(fileMenu, editMenu, viewMenu);

        //fileMenu
        menuImportFile.getItems().addAll(menuItemImportJava, menuItemImportSmala);
        menuItemImportSmala.setDisable(true);
        menuExportFile.getItems().addAll(menuItemExportJava, menuItemExportSmala);
        fileMenu.getItems().addAll(menuItemNewFile, menuItemSaveFile, menuItemOpenFile, separator1, menuImportFile, menuExportFile, separator2, menuItemSnapshot, separator3, menuItemQuit);

        //editMenu
        editMenu.getItems().addAll(menuItemUndo, menuItemRedo, menuItemRemoveAll);

        //viewMenu
        viewMenu.getItems().addAll(menuItemZoomIn, menuItemZoomOut, menuItemHelp);

        //Add the actions
        menuItemNewFile.setOnAction(newStateMachineHandler);
        menuItemSaveFile.setOnAction(saveToJSONHandler);
        menuItemOpenFile.setOnAction(openFromJSONHandler);
        menuItemImportJava.setOnAction(importFromJavaHandler);
        menuItemExportJava.setOnAction(exportToJavaHandler);
        menuItemExportSmala.setOnAction(exportToSmalaHandler);
        menuItemSnapshot.setOnAction(snapshotHandler);
        menuItemQuit.setOnAction(quitHandler);
        menuItemUndo.setOnAction(undoHandler);
        menuItemRedo.setOnAction(redoHandler);
        menuItemHelp.setOnAction(helpHandler);
        menuItemZoomIn.setOnAction(zoomInHandler);
        menuItemZoomOut.setOnAction(zoomOutHandler);

        //Shortcut is interpreted as ctrl on windows and as command on Mac
        menuItemNewFile.setAccelerator(KeyCombination.keyCombination("Shortcut+N"));
        menuItemSaveFile.setAccelerator(KeyCombination.keyCombination("Shortcut+S"));
        menuItemOpenFile.setAccelerator(KeyCombination.keyCombination("Shortcut+O"));
        menuItemQuit.setAccelerator(KeyCombination.keyCombination("Shortcut+Q"));
        menuItemUndo.setAccelerator(KeyCombination.keyCombination("Shortcut+Z"));
        menuItemRedo.setAccelerator(KeyCombination.keyCombination("Shortcut+Y"));
        menuItemRemoveAll.setAccelerator(KeyCombination.keyCombination("Shortcut+D"));
        menuItemHelp.setAccelerator(KeyCombination.keyCombination("Shortcut+H"));
    }

    /**
     * Initialize a FileChooser
     *
     * @param title
     * @param extensionTitle
     * @param extension
     * @return
     */
    private FileChooser createFileChooser(String title, String extensionTitle, String extension) {
        FileChooser fc = new FileChooser();
        fc.setTitle(title);
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(extensionTitle, extension));
        fc.setInitialDirectory(new File(System.getProperty("user.home")));
        return fc;
    }

    /**
     *
     * @param title
     * @param extensionTitle
     * @param extension
     * @throws IOException
     */
    private void saveFile(String title, String extensionTitle, String extension) throws IOException {
        FileChooser fc = createFileChooser(title, extensionTitle, extension);
        File file = fc.showSaveDialog(stage);

        if (file != null) {
            switch (extension) {
                case "*.json": {
                    System.out.println("Writing StateMachine to Json File");
                    JsonWriter jw = new JsonWriter();
                    jw.write(center.getStateMachine(), file);
                    break;
                }
                case "*.java": {
                    System.out.println("Writing StateMachine to Java File");
                    JavaWriter jw = new JavaWriter();
                    jw.write(center.getStateMachine(), file);
                    break;
                }
                case "*.sma":
                    System.out.println("Writing StateMachine to Smala File");
                    SmalaWriter sw = new SmalaWriter();
                    sw.write(center.getStateMachine(), file);
                    break;
                default:
                    break;
            }
        }
    }

    private void openFile(String title, String extensionTitle, String extension) throws FileNotFoundException {
        StateMachineGraphicModel sm = null;
        FileChooser fc = createFileChooser(title, extensionTitle, extension);
        File file = fc.showOpenDialog(stage);

        if (file != null) {
            switch (extension) {
                case "*.json": {
                    System.out.println("Reading StateMachine from Json File");
                    JsonReader jw = new JsonReader();
                    sm = jw.read(file);
                    break;
                }
                case "*.java": {
                    System.out.println("Reading StateMachine from Java File");
                    JavaReader jw = new JavaReader();
                    sm = jw.read(file);
                    break;
                }
                case "*.sma":
                    //SmalaReader sw = new SmalaReader();
                    //sm = sw.read(file);
                    break;
                default:
                    break;
            }
        }
        if (sm == null) {
            showErrorPopup("Could not load from file", new Exception("StateMachineGraphicModel is null"));
        } else {
            center.setStateMachine(sm);
        }
    }

    /**
     * Alert used for confirmation when deleting things
     *
     * @param str
     * @return
     */
    private String losingAlert(String str) {
        String res = "";
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Continue ?");
        alert.setHeaderText(str);
        ButtonType continuer = new ButtonType("No, continue");
        ButtonType save = new ButtonType("Yes, save");
        ButtonType cancel = new ButtonType("Cancel");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(cancel, continuer, save);
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == save) {
            try {
                saveFile("Save to JSON", "JSON save", "*.json");
            } catch (IOException ex) {
                Logger.getLogger(MenuBarComponent.class.getName()).log(Level.SEVERE, null, ex);
                showErrorPopup("Could not save StateMachine", ex);
            }
        } else if (option.get() == cancel) {
            res = "cancel";
        } else if (option.get() == continuer) {
            res = "continue";
        }
        return res;
    }

    /**
     * Error popup giving the exception stacktrace
     *
     * @param str
     * @param ex
     */
    private void showErrorPopup(String str, Exception ex) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setContentText(str);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);
        Optional<ButtonType> option = alert.showAndWait();
    }

    private final EventHandler newStateMachineHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            String res = losingAlert("Do you want to save before new ?");
            if (res.equals("continue")) {
                center.deleteAll();
            }
        }
    };

    private final EventHandler saveToJSONHandler = (EventHandler<ActionEvent>) (ActionEvent event) -> {
        try {
            saveFile("Save to JSON", "JSON save", "*.json");
        } catch (IOException ex) {
            Logger.getLogger(MenuBarComponent.class.getName()).log(Level.SEVERE, null, ex);
            showErrorPopup("Could not save StateMachine to JSON file", ex);
        }
    };

    private final EventHandler openFromJSONHandler = (EventHandler<ActionEvent>) (ActionEvent event) -> {
        try {
            openFile("Open JSON Save", "JSON save", "*.json");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MenuBarComponent.class.getName()).log(Level.SEVERE, null, ex);
            showErrorPopup("Could not load StateMachine from JSON file", ex);
        }
    };

    private final EventHandler importFromJavaHandler = (EventHandler<ActionEvent>) (ActionEvent event) -> {
        try {
            openFile("Import Java StateMachine", "Java code", "*.java");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MenuBarComponent.class.getName()).log(Level.SEVERE, null, ex);
            showErrorPopup("Could not load StateMachine from Java file", ex);
        }
    };

    private final EventHandler exportToJavaHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            try {
                if (center.getStateMachine().checkAllTransitions()) {
                    saveFile("Import Java StateMachine", "Java code", "*.java");
                } else {
                    showErrorPopup("Can't save StateMachine", new Exception("Not all Transitions have Start and End States"));
                }
            } catch (IOException ex) {
                Logger.getLogger(MenuBarComponent.class.getName()).log(Level.SEVERE, null, ex);
                showErrorPopup("Could not save StateMachine to Java file", ex);
            }
        }
    };

    private final EventHandler importFromSmalaHandler = (EventHandler<ActionEvent>) (ActionEvent event) -> {
        //TODO import a StateMachine from a Smala file
        //openFile("Import Smala StateMachine", "Smala code", "*.sma");
    };

    private final EventHandler exportToSmalaHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            try {
                if (center.getStateMachine().checkAllTransitions()) {
                    saveFile("Export Smala StateMachine", "Smala code", "*.sma");
                } else {
                    showErrorPopup("Can't save StateMachine", new Exception("Not all Transitions have Start and End States"));
                }
            } catch (IOException ex) {
                Logger.getLogger(MenuBarComponent.class.getName()).log(Level.SEVERE, null, ex);
                showErrorPopup("Could not save StateMachine to Smala file", ex);
            }
        }
    };

    //TOREFINE
    private final EventHandler snapshotHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            FileChooser snapshotTaker = createFileChooser("Save Snapshot image", "PNG image", "*.png");
            File snapshotFile = snapshotTaker.showSaveDialog(stage);
            if (snapshotFile != null) {
                try {
                    center.setPadding(Insets.EMPTY);
                    center.setPrefSize(USE_PREF_SIZE, USE_PREF_SIZE);
                    WritableImage image = center.snapshot(new SnapshotParameters(), null);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(image, null);
                    ImageIO.write(renderedImage, "png", snapshotFile);
                    center.setPrefSize(3000, 3000);
                } catch (IOException e) {
                    showErrorPopup("Error while doing a snapshot", e);
                }
            }
        }
    };

    private final EventHandler quitHandler = (EventHandler<ActionEvent>) (ActionEvent event) -> {
        String res = losingAlert("Do you want to save before quitting ?");
        if (!res.equals("cancel")) {
            Platform.exit();
        }
    };

    private final EventHandler zoomInHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            center.appendScale(1.5, 2500, 2500);
        }
    };

    private final EventHandler zoomOutHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            center.appendScale(0.66, 2500, 2500);
        }
    };

    private final EventHandler undoHandler = (EventHandler<ActionEvent>) (ActionEvent event) -> {
        ((MainComponent) getParent()).getCenterPane().getSaveManager().loadPreviousSave();
    };

    private final EventHandler redoHandler = (EventHandler<ActionEvent>) (ActionEvent event) -> {
        ((MainComponent) getParent()).getCenterPane().getSaveManager().loadNextSave();
    };

    private final EventHandler helpHandler = (EventHandler<ActionEvent>) (ActionEvent event) -> {
        HelpWindow help = new HelpWindow();
    };
}
