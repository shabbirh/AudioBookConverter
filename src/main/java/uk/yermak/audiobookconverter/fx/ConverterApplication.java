package uk.yermak.audiobookconverter.fx;/**
 * Created by Yermak on 06-Jan-18.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import uk.yermak.audiobookconverter.ConversionContext;
import uk.yermak.audiobookconverter.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;

public class ConverterApplication extends Application {
    private static JfxEnv env;
    private static ConversionContext context = new ConversionContext();

    public static void main(String[] args) {
        launch(args);
    }
    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void start(Stage stage) {
        Parent root = null;

        logger.info("Initialising application");

        try {
            URL resource = ConverterApplication.class.getResource("/uk/yermak/audiobookconverter/fx/fxml_converter.fxml");
            root = FXMLLoader.load(resource);

            Scene scene = new Scene(root);
            stage.setTitle(Version.getVersionString());
            stage.setScene(scene);
            Screen primary = Screen.getPrimary();
            stage.setMinHeight(primary.getVisualBounds().getHeight() * 0.5);
            stage.setMinWidth(primary.getVisualBounds().getWidth() * 0.4);
            stage.show();
            env = new JfxEnv(scene, getHostServices());


            stage.setOnCloseRequest(event -> {
                logger.info("Closing application");
                ConverterApplication.getContext().stopConversions();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            });

        } catch (IOException e) {
            logger.error("Error initiating application", e);
            e.printStackTrace();
        }
    }

    public static ConversionContext getContext() {
        return context;
    }

    public static JfxEnv getEnv() {
        return env;
    }
}
