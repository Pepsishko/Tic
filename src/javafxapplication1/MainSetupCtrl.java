package javafxapplication1;

import java.io.File;
import java.io.IOException;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.DragEvent;
import javafx.scene.input.InputEvent;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafxapplication1.data.GameConfig;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafxapplication1.bots.DefaultBot;
import javafxapplication1.bots.RandomBot;
import javafxapplication1.bots.TurnListProvider;
import javafxapplication1.data.GameState;
import javafxapplication1.data.GameState.FinishAction;
import javafxapplication1.data.GomokuBoard;
import javafxapplication1.data.players.BotPlayer;
import javafxapplication1.data.players.LocalPlayer;
/**
 * Класс контроллер.
 */
public class MainSetupCtrl implements Initializable {
    
    @FXML private Label label;    
    @FXML private Label labll;
    @FXML private Label labfl;
    @FXML private ChoiceBox gametype;
    @FXML private ChoiceBox botChoice;
    @FXML private Slider winsl;
    @FXML private Slider fieldsl;
    @FXML private CheckBox chTurn1;
    @FXML private CheckBox chUnlim;
    @FXML private TextField txtIp;
    @FXML private TextField txtPort;
    @FXML private GridPane imgPane;
    @FXML private TextField txtImgX;
    @FXML private TextField txtImgO;
    /**
     * Процедура определения производителя
     * @param nodes - узлы
     * @param val - tru-активное игровое поле; false-неактивное
     */
    private void setNodesVisible(Node[] nodes, boolean val) {
        for (Node n : nodes) {
            n.setVisible(val);
        }
    }


    /**
     * Класс для считывания настроек игры
     */
    private class GameTypeListener implements EventHandler<ActionEvent> {
        /**
         * Метод для отслеживания изменений в настройках игры
         * @param t - событие внесения изменений
         */
        @Override
        public void handle(ActionEvent t) {            
            if (gametype.getValue().equals(BT_BOT)) {

                setNodesVisible(new Node[]{winsl,fieldsl,chTurn1,chUnlim,botChoice}, true);
            } else {
                botChoice.setVisible(false);
                setNodesVisible(new Node[]{winsl,fieldsl,chTurn1,chUnlim}, true);
            }
        }
    }
    
    final String BT_LOC = "Локальный игрок", BT_BOT = "Бот";
    final String[] GAME_TYPES = new String[] {BT_LOC,BT_BOT};
    
    final String BT_RAN = "Легкий бот", BT_TH = "Обычный бот";
    String[] BOT_TYPES = new String[] {BT_RAN, BT_TH};


    /**
     * Метод установки настроек пользователя
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gametype.getItems().addAll(GAME_TYPES);
        gametype.setValue(BT_LOC);
        gametype.addEventHandler(ActionEvent.ACTION, new GameTypeListener());
        botChoice.getItems().addAll(BOT_TYPES);
        botChoice.setValue(BT_RAN);

        botChoice.setVisible(false);
    } 
    
    private boolean verifyOpts() {
        return true;
    }
    
    private Stage window;
    /**
     * Метод создания нового stage
     */
    private void launchGameWin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameBoard.fxml"));        
        javafx.scene.Parent root1 = (Parent) fxmlLoader.load();
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);        
        window.setTitle("Крестики Нолики");
        window.setScene(new Scene(root1));
        window.getIcons().add(new Image("javafxapplication1/data/X.png"));
        window.show();
    }
    /**
     * Обработчик нажатия кнопки
     * @param event - событие нажатия на кнопку
     */
    @FXML private void startBtn(ActionEvent event) {
        try {
            GameConfig conf = buildCfg();
            GomokuBoard board = buidBoard(conf);
            GameState game = new GameState(conf, board, buildFinishAction());
            GameState.singleton = game;
            game.startGame();            
            launchGameWin();
        }
        catch (IOException ex) {
            System.err.println(ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(MainSetupCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    @FXML private void exitBtn(ActionEvent event) {
    }
    /**
     * Метод открытия файл диалога
     * @param tf - textfield в который записывается путь к файлу
     */
    private void loadFileDlg(TextField tf) {
        FileChooser fc = new FileChooser();
        File sel = fc.showOpenDialog(null);            
        tf.setText(sel.toURI().toString());
    }
    /**
     * Вызов метода по выбору файла у первого
     */
    @FXML private void xImgBtn(ActionEvent event) {
        loadFileDlg(txtImgX);        
    }
    /**
     * Вызов метода по выбору файла у второго игрока
     */
    @FXML private void yImgBtn(ActionEvent event) {
        loadFileDlg(txtImgO);
    }
    /**
     * Класс для вывода информации о победителе
     */
    class FinishRunner implements Runnable {
                    
        private Boolean winp;
        private String msg;

        public FinishRunner(Boolean winp, String msg) {
            this.winp = winp;
            this.msg = msg;
        }

        public FinishRunner(Boolean winp) {
            this.winp = winp;
        }

        public FinishRunner(String msg) {
            this.msg = msg;
        }
        /**
         * Метод открытия конечного stage с информацией о победителе
         */
        @Override
        public void run() {
            final Stage dialogStage = new Stage();
            dialogStage.setTitle("Конец игры");
            dialogStage.getIcons().add(new Image("javafxapplication1/data/X.png"));
            dialogStage.setMinWidth(250);
            dialogStage.setMinHeight(140);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            VBox vbox = new VBox();
            String t;
            if (winp != null) {
                String p = winp ? "1" : "2";
                t = "Победил игрок " + p;
            } else {
                t = msg;
            }
            vbox.getChildren().add(new Text(t));
            Button btn1 = new Button("OK");
            btn1.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    dialogStage.close();
                    window.close();
                }
            });
            vbox.getChildren().add(btn1);                
            vbox.setAlignment(Pos.CENTER);
            vbox.setPadding(new Insets(15));
            dialogStage.setScene(new Scene(vbox));
            dialogStage.show();                                
        }
    }


    /**
     * Класс открытия конечного результата в новом потоке
     * @return результат в новом потоке
     */
    public FinishAction buildFinishAction() {
        return new FinishAction() {
            @Override
            public void action(Boolean winPlayer) {
                Platform.runLater(new FinishRunner(winPlayer));
            }
        };
    }
    /**
     * Создание "Бесконечного поля"
     * @param con - игровые настройки
     * @return обновленное игровое поле
     */
    public GomokuBoard buidBoard(GameConfig con) {        
        GomokuBoard b = new GomokuBoard(con.getMin(), con.getMax());        
        return b;
    }
    /**
     * Метод определения игровых настроек и определения игрового поля на "Бесконечном поле"
     * @return обновленные игровые конфигурации
     */
    public GameConfig buildCfg() {
        GameConfig n = new GameConfig();
        Object gt = gametype.getValue();
        n.p1 = new LocalPlayer(Boolean.TRUE);
        if (gt.equals(BT_LOC)) {                
            n.p2 = new LocalPlayer(Boolean.FALSE);
        } else {
            Object btype = botChoice.getValue();
            BotPlayer bp = null;
            TurnListProvider tp = new javafxapplication1.bots.AnyCellProvider(1);
            if (btype.equals(BT_RAN)) {
                bp = new RandomBot(Boolean.FALSE, tp);
            } else {
                bp = new DefaultBot(Boolean.FALSE, tp);
            }
            n.p2 = bp;
        }        
        int line = (int)winsl.getValue();
        n.setLineLength(line);        
        if (chUnlim.isSelected()) {
            n.setBoardLength(0);
        } else {
            int bounds = (int)fieldsl.getValue();
            n.setBoardLength(bounds);
        }        
        n.setFirstPlayerFirstTurn(chTurn1.isSelected());
        String file1 = txtImgX.getText();
        if (Utils.emptyStr(file1)==false) n.imTrue = new Image(file1);
        file1 = txtImgO.getText();
        if (Utils.emptyStr(file1)==false) n.imFalse = new Image(file1);
        return n;
    }
    
}
