package uk.yermak.audiobookconverter.fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.commons.io.FileUtils;
import uk.yermak.audiobookconverter.MediaInfo;
import uk.yermak.audiobookconverter.MediaLoader;
import uk.yermak.audiobookconverter.StateDispatcher;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Yermak on 04-Feb-18.
 */
public class FilesController {
    private final StateDispatcher stateDispatcher = StateDispatcher.getInstance();


    @FXML
    protected void addFiles(ActionEvent event) {
        Button node = (Button) event.getSource();
        Window window = node.getScene().getWindow();

        ContextMenu contextMenu = new ContextMenu();
        MenuItem item1 = new MenuItem("Files");
        item1.setOnAction(e -> showFileSelection(window));
        MenuItem item2 = new MenuItem("Folder");
        item2.setOnAction(e -> showDirectorySelection(window));

        contextMenu.getItems().addAll(item1, item2);
        contextMenu.show(node, Side.RIGHT, 0, 0);

        node.setContextMenu(contextMenu);
    }

    private void showDirectorySelection(Window window) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(window);
        if (selectedDirectory != null) {
            Collection<File> files = FileUtils.listFiles(selectedDirectory, new String[]{"mp3"}, true);
            processFiles(files);
        }
    }

    private void processFiles(Collection<File> files) {

        List<String> fileNames = new ArrayList<>();
        files.forEach(f -> fileNames.add(f.getPath()));
        List<MediaInfo> addedMedia = new MediaLoader(fileNames).loadMediaInfo();

//        addedMedia.forEach(m -> fileListControl.add(m.getFileName()));
        stateDispatcher.fileListChanged();

    }

    private void showFileSelection(Window window) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("mp3", "*.mp3")
        );
        List<File> files = fileChooser.showOpenMultipleDialog(window);
        processFiles(files);
    }
}