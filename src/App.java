import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class App extends Application {

    // labels for top sections (day, month, year, date)
    private Label dayLabel, monthLabel, yearLabel, dateLabel;

    // array of labels for each individual character in time (hh:mm:ss)
    private Label[] timeLabels = new Label[8];

    // store all date/time formatters in one place for easy access
    private final DateTimeFormatter[] formats = {
            DateTimeFormatter.ofPattern("EEEE"),       // full day (e.g., monday)
            DateTimeFormatter.ofPattern("MMMM"),       // full month (e.g., april)
            DateTimeFormatter.ofPattern("yyyy"),       // year
            DateTimeFormatter.ofPattern("M/dd/yyyy"),  // full date
            DateTimeFormatter.ofPattern("HH:mm:ss")    // time (24-hour)
    };

    @Override
    public void start(Stage stage) {

        // main container for everything in the scene
        Pane root = new Pane();

        // load the background clock image from resources folder
        Image img = new Image(getClass().getResource("/resources/clock.png").toExternalForm());

        // display the image
        root.getChildren().add(new ImageView(img));

        // create text boxes for each top section and grab their labels
        // each box centers text automatically using stackpane
        dayLabel   = getLabel(createBox(root, 122, 21, 257, 51, 35));
        monthLabel = getLabel(createBox(root, 133, 86, 78, 41, 14));
        yearLabel  = getLabel(createBox(root, 287, 86, 78, 41, 14));
        dateLabel  = getLabel(createBox(root, 122, 135, 257, 51, 35));

        // base positioning for time digit boxes (bottom row)
        double x = 127;  // starting x position
        double y = 245;  // vertical position
        double w = 30;   // width of each digit box
        double h = 42;   // height of each digit box

        // create one box per character in "HH:mm:ss"
        for (int i = 0; i < timeLabels.length; i++) {

            // position each box side-by-side
            timeLabels[i] = createTimeBox(x + i * w, y, w, h, 26);

            // add to screen
            root.getChildren().add(timeLabels[i]);
        }

        // match window size exactly to image size (removes extra whitespace)
        root.setPrefSize(img.getWidth(), img.getHeight());

        // create scene and attach to stage (window)
        stage.setScene(new Scene(root));
        stage.setTitle("Stardew Clock");
        stage.sizeToScene(); // auto-fit window
        stage.show();

        // start background thread for updating time
        startClockThread();
    }

    // creates a centered box (used for day, month, year, date)
    private StackPane createBox(Pane root, double x, double y, double w, double h, int size) {

        // create label inside the box
        Label label = createLabel(size);

        // stackpane centers label automatically
        StackPane box = new StackPane(label);

        // position and size of the box
        box.setLayoutX(x);
        box.setLayoutY(y);
        box.setPrefSize(w, h);

        // ensure text is centered inside the box
        box.setAlignment(Pos.CENTER);

        // add box to root container
        root.getChildren().add(box);

        return box;
    }

    // helper to extract label from a stackpane
    private Label getLabel(StackPane box) {
        return (Label) box.getChildren().get(0);
    }

    // reusable label creator (keeps font/style consistent)
    private Label createLabel(int size) {
        Label l = new Label();

        // set font and appearance
        l.setFont(Font.font("Consolas", size));
        l.setStyle("-fx-text-fill: black;");

        return l;
    }

    // creates a single box for each time character (digits + :)
    private Label createTimeBox(double x, double y, double w, double h, int size) {

        Label l = createLabel(size);

        // manually position each digit box
        l.setLayoutX(x);
        l.setLayoutY(y);

        // size of the digit box
        l.setPrefSize(w, h);

        // center the character inside the box
        l.setAlignment(Pos.CENTER);

        return l;
    }

    // background thread that updates time every second
    private void startClockThread() {

        Thread t = new Thread(() -> {

            // infinite loop to keep clock running
            while (true) {

                // get current system time
                LocalDateTime now = LocalDateTime.now();

                // format each part of the date/time
                String day   = now.format(formats[0]);
                String month = now.format(formats[1]);
                String year  = now.format(formats[2]);
                String date  = now.format(formats[3]);
                String time  = now.format(formats[4]);

                // update console on a single line (prevents spam)
                System.out.print("\r" + String.format("%-25s", time + " " + date));

                // update javafx ui safely on application thread
                Platform.runLater(() -> {

                    // update top sections
                    dayLabel.setText(day);
                    monthLabel.setText(month);
                    yearLabel.setText(year);
                    dateLabel.setText(date);

                    // update each individual character in time
                    for (int i = 0; i < time.length(); i++) {
                        timeLabels[i].setText(String.valueOf(time.charAt(i)));
                    }
                });

                try {
                    Thread.sleep(1000); // wait 1 second between updates
                } catch (InterruptedException e) {
                    break; // exit loop if thread is interrupted
                }
            }
        });

        // run thread in background so it doesn’t block ui
        t.setDaemon(true);

        // give higher priority to keep time updates smooth
        t.setPriority(Thread.MAX_PRIORITY);

        t.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}