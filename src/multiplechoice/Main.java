package multiplechoice;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import io.models.AnswerModel;
import io.models.MultipleChoiceModel;

import java.util.Collections;
import java.util.List;

public class Main extends Application {
    private static final int WIDTH = 100;
    private static final int HEIGHT = 300;

    private List<MultipleChoiceModel> questions;
    private Stage stage;
    private int currentPos = 0;

    @Override
    public void start(Stage stage) {
        this.questions = io.FileReader.readFile("biology.txt").getQuestions();
        Collections.shuffle(this.questions);

        this.stage = stage;

        this.drawScene(this.buildQuestionsBody(this.questions.get(this.currentPos)));
    }

    private void drawScene(VBox questionBody) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Pane rootPane = new Pane();

        rootPane.getChildren().addAll(canvas, questionBody, this.buildControlsBody());
        Scene scene = new Scene(rootPane);
        this.stage.setScene(scene);
        this.stage.setTitle("Multiple Choice Dude");
        this.stage.show();
    }

    private VBox buildQuestionsBody(MultipleChoiceModel question) {
        VBox answersContainer = new VBox();
        VBox labelContainer = new VBox();

        Label label = new Label();
        label.setText((this.currentPos + 1) + ". " + question.title);
        labelContainer.getChildren().add(label);
        answersContainer.getChildren().add(labelContainer);

        for (int i = 0; i < question.answers.size(); i++) {
            AnswerModel mcm = question.answers.get(i);
            MultipleChoiceCheckBox checkBox = new MultipleChoiceCheckBox(mcm.key, mcm.correct);
            answersContainer.getChildren().add(checkBox);
        }

        return answersContainer;
    }

    private VBox buildSolutionsBody(MultipleChoiceModel question) {
        VBox solutionsContainer = new VBox();
        VBox labelContainer = new VBox();

        Label label = new Label();
        label.setText((this.currentPos + 1) + ". " + question.title);
        labelContainer.getChildren().add(label);
        solutionsContainer.getChildren().add(labelContainer);

        for (int i = 0; i < question.answers.size(); i++) {
            AnswerModel mcm = question.answers.get(i);
            MultipleChoiceCheckBox checkBox = new MultipleChoiceCheckBox(mcm.key, mcm.correct);
            if (checkBox.correct == true) {
                checkBox.setStyle("-fx-background-color: #00FF00;");
            } else {
                checkBox.setStyle("-fx-background-color: #FF0000;");
            }
            solutionsContainer.getChildren().add(checkBox);
        }

        return solutionsContainer;
    }

    private HBox buildControlsBody() {
        HBox controlsContainer = new HBox();
        controlsContainer.setLayoutY(HEIGHT);

        Button previousButton = new Button("Previous");
        previousButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                goPrevious();
            }
        });

        Button checkAnswersButton = new Button("Check");
        checkAnswersButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                checkAnswers();
            }
        });

        Button againButton = new Button("Again");
        againButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                goAgain();
            }
        });

        Button nextButton = new Button("Next");
        nextButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                goNext();
            }
        });

        controlsContainer.getChildren().addAll(previousButton, checkAnswersButton, againButton, nextButton);
        return controlsContainer;
    }

    private void goPrevious() {
        if (this.currentPos > 0) {
            this.currentPos--;
            this.drawScene(buildQuestionsBody(this.questions.get(this.currentPos)));
        }
    }

    private void checkAnswers() {
        this.drawScene(buildSolutionsBody(this.questions.get(this.currentPos)));
    }

    private void goAgain() {
        Collections.shuffle(this.questions.get(this.currentPos).answers);
        this.drawScene(this.buildQuestionsBody(this.questions.get(this.currentPos)));
    }

    private void goNext() {
        if (this.currentPos == this.questions.size() - 1) {
            for (MultipleChoiceModel question : this.questions) {
                Collections.shuffle(question.answers);
            }
            Collections.shuffle(this.questions);
            this.currentPos = 0;
        } else {
            this.currentPos++;
        }
        this.drawScene(buildQuestionsBody(this.questions.get(this.currentPos)));
    }

    public static void main(String[] args) {
        launch();
    }
}
