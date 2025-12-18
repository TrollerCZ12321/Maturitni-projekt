package sample;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class Controller {
    public AnchorPane menu,canvasPane;
    public String typ;
    public ToggleButton vybKruh,vybTrojuh,vybCtverec,vybCara,vybLom,vybElipsa;
    public ToggleGroup objektGroup;
    public Canvas canvas;
    public int x1,x2,y1,y2,temp,selected,editovany;
    public int poradi,i = 0;
    public ColorPicker barva,barva2;
    public double sirka;
    public double[] array1,array2;
    public ArrayList<ToggleButton> btn = new ArrayList<ToggleButton>();
    public ArrayList<Double> x = new ArrayList<Double>();
    public ArrayList<Double> y = new ArrayList<Double>();
    public ArrayList<Object[]> objekty = new ArrayList<Object[]>();
    public ScrollPane scroll;
    public VBox root;
    public GridPane main;
    public GraphicsContext gc ;
    public TextField xPoz,yPoz,s,v,x3,y3,width;
    public Text xLabel,yLabel,sLabel,vLabel,x3Label,y3Label;
    public Button smazBtn,editBtn;


    public void begin(MouseEvent mouseEvent) {
        x1 = (int)mouseEvent.getX();
        y1 = (int)mouseEvent.getY();
    }


    public void draw(MouseEvent mouseEvent) {
        x2 = (int) mouseEvent.getX();
        y2 = (int) mouseEvent.getY();

        gc = canvas.getGraphicsContext2D();
        Color lineBar =  barva.getValue();
        Color fillBar =  barva2.getValue();
        sirka = Double.parseDouble(width.getText());
        ToggleButton toggle = (ToggleButton) objektGroup.getSelectedToggle();
        String vyber = toggle.getId();
        switch (vyber){
            case "vybKruh":
                if (x1>x2){
                    temp = x1;
                    x1 = x2;
                    x2 = temp;
                }
                if (y1>y2){
                    temp = y1;
                    y1 = y2;
                    y2 = temp;
                }
                double r = (x2-x1+y2-y1)/2;
                vyber(new Object[]{"kruh",x1,y1,r,lineBar,fillBar,sirka});
                Kruh(x1,y1,r,lineBar,fillBar,gc,sirka);
                break;

            case "vybElipsa":
                if (x1>x2){
                    temp = x1;
                    x1 = x2;
                    x2 = temp;
                }
                if (y1>y2){
                    temp = y1;
                    y1 = y2;
                    y2 = temp;
                }
                double w =  (x2-x1);
                double h =  (y2-y1);
                vyber(new Object[]{"elipsa",x1,y1,w,h,lineBar,fillBar,sirka});
                Elipsa(x1,y1,w,h,lineBar,fillBar,gc,sirka);
                break;


            case "vybTrojuh":
                x.add((double)x1);
                y.add((double)y1);
                i++;
                if (i == 3){

                    double[] xArray = x.stream().mapToDouble(Double::doubleValue).toArray();
                    double[] yArray = y.stream().mapToDouble(Double::doubleValue).toArray();
                    vyber(new Object[]{"trojuhelnik",xArray,yArray,lineBar,fillBar,sirka});
                    Trojuh(xArray,yArray,lineBar,fillBar,gc,sirka);
                    x.clear();
                    y.clear();
                    i = 0;
                }

                break;

            case "vybPoly":
                switch (mouseEvent.getButton()) {
                    case PRIMARY:
                        x.add((double)x1);
                        y.add((double)y1);
                        break;
                    case SECONDARY:
                        double[] xArray = x.stream().mapToDouble(Double::doubleValue).toArray();
                        double[] yArray = y.stream().mapToDouble(Double::doubleValue).toArray();
                        vyber(new Object[]{"polygon",xArray,yArray,lineBar,fillBar,sirka});
                        Poly(xArray,yArray,lineBar,fillBar,gc,sirka);
                        x.clear();
                        y.clear();
                        break;
                }
                break;

            case "vybCtverec":
                if (x1>x2){
                    temp = x1;
                    x1 = x2;
                    x2 = temp;
                }
                if (y1>y2){
                    temp = y1;
                    y1 = y2;
                    y2 = temp;
                }
                vyber(new Object[]{"ctverec",x1,y1,x2-x1,y2-y1,lineBar,fillBar,sirka});
                Ctverec(x1,y1,x2-x1,y2-y1,lineBar,fillBar,gc,sirka);
                break;


            case "vybCara":
                vyber(new Object[]{"cara",x1,y1,x2,y2,lineBar,sirka});
                Cara(x1,y1,x2,y2,lineBar,gc,sirka);
                break;
            case "vybLom":
                x.add((double)x1);
                y.add((double)y1);
                i++;
                if (i == 3){
                    double[] xArray = x.stream().mapToDouble(Double::doubleValue).toArray();
                    double[] yArray = y.stream().mapToDouble(Double::doubleValue).toArray();
                    vyber(new Object[]{"lom",xArray,yArray,lineBar,sirka});
                    Lom(xArray,yArray,lineBar,gc,sirka);
                    x.clear();
                    y.clear();
                    i = 0;
                }
                break;
        }


    }
    public void vyber(Object[] x){

        objekty.add(x);
        ToggleButton test = new ToggleButton();
        Canvas tmpCan = new Canvas();
        GraphicsContext c = tmpCan.getGraphicsContext2D();
        tmpCan.setHeight(100);
        tmpCan.setWidth(150);
        tmpCan.setId("canvas"+poradi);
        test.setId("objekt"+poradi);
        test.setGraphic(tmpCan);
        test.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                System.out.println(event.getTarget());
                vyberEdit();
            }
        });
        root.getChildren().addAll(test);
        root.setSpacing(10);
        scroll.setContent(root);
        btn.add(test);
        switch ((String) x[0]){
            case "ctverec":
                Ctverec((Integer)x[1]/5,
                        (Integer)x[2]/5,
                        (Integer)x[3]/5,
                        (Integer)x[4]/5,
                        (Color) x[5],
                        (Color) x[6],
                        c,(Double)x[7]/5);
                break;
            case "polygon":
                array1 = (double[])x[1];
                array2 = (double[])x[2];
                for(int a = 0; a< array1.length; a++){
                    array1[a] = array1[a]/5;
                    array2[a] = array2[a]/5;
                }
                    Poly(array1,
                         array2,
                         (Color) x[3],
                         (Color) x[4],
                         c,(Double)x[5]/5);
                for(int a = 0; a< array1.length; a++){
                    array1[a] *= 5;
                    array2[a] *= 5;
                }
                break;
            case "kruh":
                Kruh((Integer)x[1]/5,
                        (Integer)x[2]/5,
                        (Double)x[3]/5,
                        (Color) x[4],
                        (Color) x[5],
                        c,(Double)x[6]/5);
                break;
            case "elipsa":

                Elipsa((Integer)x[1]/5,
                        (Integer)x[2]/5,
                        (Double) x[3]/5,
                        (Double)x[4]/5,
                        (Color) x[5],
                        (Color) x[6],
                        c,(Double)x[7]/5);
                break;
            case "cara":
                Cara(   (Integer)x[1]/5,
                        (Integer)x[2]/5,
                        (Integer)x[3]/5,
                        (Integer)x[4]/5,
                        (Color) x[5],
                        c,(Double)x[6]/5);
                break;
            case "lom":
                array1 = (double[])x[1];
                array2 = (double[])x[2];

                for(int a = 0; a< array1.length; a++){
                    array1[a] /= 5;
                    array2[a] /= 5;
                }
                Lom(array1,
                        array2,
                        (Color) x[3],
                        c,(Double)x[4]/5);
                for(int a = 0; a< array1.length; a++){
                    array1[a] *= 5;
                    array2[a] *= 5;
                }
                break;
            case "trojuhelnik":
                array1 = (double[])x[1];
                array2 = (double[])x[2];
                for(int a = 0; a< array1.length; a++){
                    array1[a] = array1[a]/5;
                    array2[a] = array2[a]/5;
                }
                Trojuh(array1,
                        array2,
                        (Color) x[3],
                        (Color) x[4],
                        c,(Double)x[5]/5);
                for(int a = 0; a< array1.length; a++){
                    array1[a] *= 5;
                    array2[a] *= 5;
                }
                break;
            default:
                break;

        }
        poradi++;
    }

    public void Kruh(double x,double y,double r,Color lineBar,Color fillBar,GraphicsContext g,Double sirka){
        g.setStroke(lineBar);
        g.setFill(fillBar);
        g.setLineWidth(sirka);
        g.strokeOval(x,y,r,r);
        g.fillOval(x,y,r,r);
    }

    public void Elipsa(double x,double y,double w,double h,Color lineBar,Color fillBar,GraphicsContext g,Double sirka){
        g.setStroke(lineBar);
        g.setFill(fillBar);
        g.setLineWidth(sirka);
        g.strokeOval(x,y,w,h);
        g.fillOval(x,y,w,h);
    }

    public void Ctverec(double x1,double y1,double x2,double y2,Color lineBar,Color fillBar,GraphicsContext g,Double sirka){
        g.setStroke(lineBar);
        g.setFill(fillBar);
        g.setLineWidth(sirka);
        g.strokeRect(x1,y1,x2,y2);
        g.fillRect(x1,y1,x2,y2);
    }

    public void Cara(double x1,double y1,double x2,double y2,Color lineBar,GraphicsContext g,Double sirka){
        g.setStroke(lineBar);
        g.setLineWidth(sirka);
        g.strokeLine(x1,y1,x2,y2);
    }

    public void Lom(double[] x,double[] y,Color lineBar,GraphicsContext g,Double sirka){
        g.setStroke(lineBar);
        g.setLineWidth(sirka);
        g.strokeLine(x[0],y[0],x[1],y[1]);
        g.strokeLine(x[1],y[1],x[2],y[2]);

    }

    public void Trojuh(double[] x,double[] y,Color lineBar,Color fillBar,GraphicsContext g,Double sirka) {
        g.setStroke(lineBar);
        g.setFill(fillBar);
        g.setLineWidth(sirka);
        g.strokePolygon(x, y, x.length);
        g.fillPolygon(x, y, x.length);
    }

    public void Poly(double[] x,double[] y,Color lineBar,Color fillBar,GraphicsContext g,Double sirka){
        g.setStroke(lineBar);
        g.setFill(fillBar);
        g.setLineWidth(sirka);
        g.strokePolygon(x, y, x.length);
        g.fillPolygon(x, y, x.length);
    }


    public void smazat() {
        int mazany = 0;
        int objekt = 0;
        for (ToggleButton i : btn) {
            ToggleButton temp = i;
            if (temp.isSelected()) {
                root.getChildren().remove(temp);
                temp.setSelected(false);
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                objekty.set(mazany,null);
            }
            mazany++;

        }
        for (Object i : objekty) {

            if (objekty.get(objekt)!=null) {
            typ = (String) objekty.get(objekt)[0];
            }
            else typ = "neni";
            switch (typ){
                case "ctverec":
                    Ctverec((Integer)objekty.get(objekt)[1],
                            (Integer)objekty.get(objekt)[2],
                            (Integer)objekty.get(objekt)[3],
                            (Integer)objekty.get(objekt)[4],
                            (Color) objekty.get(objekt)[5],
                            (Color) objekty.get(objekt)[6],
                            gc,(Double)objekty.get(objekt)[7]);
                    break;
                case "polygon":
                    Poly((double[])objekty.get(objekt)[1],
                            (double[])objekty.get(objekt)[2],
                            (Color) objekty.get(objekt)[3],
                            (Color) objekty.get(objekt)[4],
                            gc,(Double)objekty.get(objekt)[5]);
                    break;
                case "kruh":
                    Kruh((Integer)objekty.get(objekt)[1],
                            (Integer)objekty.get(objekt)[2],
                            (Double)objekty.get(objekt)[3],
                            (Color) objekty.get(objekt)[4],
                            (Color) objekty.get(objekt)[5],
                            gc,(Double)objekty.get(objekt)[6]);
                    break;
                case "elipsa":

                    Elipsa((Integer)objekty.get(objekt)[1],
                            (Integer)objekty.get(objekt)[2],
                            (Double) objekty.get(objekt)[3],
                            (Double)objekty.get(objekt)[4],
                            (Color) objekty.get(objekt)[5],
                            (Color) objekty.get(objekt)[6],
                            gc,(Double)objekty.get(objekt)[7]);
                    break;
                case "cara":
                    Cara(   (Integer)objekty.get(objekt)[1],
                            (Integer)objekty.get(objekt)[2],
                            (Integer)objekty.get(objekt)[3],
                            (Integer)objekty.get(objekt)[4],
                            (Color) objekty.get(objekt)[5],
                            gc,(Double)objekty.get(objekt)[6]);
                    break;
                case "lom":
                    Lom((double[])objekty.get(objekt)[1],
                            (double[])objekty.get(objekt)[2],
                            (Color) objekty.get(objekt)[3],
                            gc,(Double)objekty.get(objekt)[4]);
                    break;
                case "trojuhelnik":
                    Trojuh((double[])objekty.get(objekt)[1],
                            (double[])objekty.get(objekt)[2],
                            (Color) objekty.get(objekt)[3],
                            (Color) objekty.get(objekt)[4],
                            gc,(Double)objekty.get(objekt)[5]);
                    break;
                default:
                    break;

            }

            objekt++;

        }
        smazBtn.setDisable(true);
        editBtn.setDisable(true);
        menu.getChildren().removeAll(xPoz,yPoz,s,v,xLabel,yLabel,sLabel,vLabel,x3,y3,x3Label,y3Label);
    }

    public void vyberEdit() {

        menu.getChildren().removeAll(xPoz,yPoz,s,v,xLabel,yLabel,sLabel,vLabel,x3,y3,x3Label,y3Label);

        selected = 0;
        int objekt = 0;

        for (ToggleButton i : btn) {
            ToggleButton temp = i;
            if (temp.isSelected()) {
               selected++;

                if(selected == 1){
                    editovany = objekt;

                }

            }

            objekt++;
        }
        if(selected != 1) {
            editovany = -1;
            editBtn.setDisable(true);
        }
        System.out.println(selected);

        if(selected == 1){

            typ = (String) objekty.get(editovany)[0];
            editBtn.setDisable(false);
            smazBtn.setDisable(false);
                    xLabel = new Text();
                    yLabel = new Text();
                    sLabel = new Text();
                    vLabel = new Text();

                    xPoz = new TextField();
                    yPoz = new TextField();
                    s = new TextField();
                    v = new TextField();


                    xPoz.setLayoutX(597);
                    xPoz.setLayoutY(45);
                    xPoz.setPrefWidth(43);
                    xPoz.setPrefHeight(25);
                    xLabel.setLayoutX(582);
                    xLabel.setLayoutY(45+16);
                    xLabel.setText("x");

                    yPoz.setLayoutX(597);
                    yPoz.setLayoutY(97);
                    yPoz.setPrefWidth(43);
                    yPoz.setPrefHeight(25);
                    yLabel.setLayoutX(582);
                    yLabel.setLayoutY(97+16);
                    yLabel.setText("y");

                    s.setLayoutX(682);
                    s.setLayoutY(45);
                    s.setPrefWidth(43);
                    s.setPrefHeight(25);
                    sLabel.setLayoutX(672-5);
                    sLabel.setLayoutY(45+16);
                    sLabel.setText("w");

                    v.setLayoutX(682);
                    v.setLayoutY(97);
                    v.setPrefWidth(43);
                    v.setPrefHeight(25);
                    vLabel.setLayoutX(672-5);
                    vLabel.setLayoutY(97+16);
                    vLabel.setText("h");
            switch(typ) {

                case "lom","polygon":
                    menu.getChildren().removeAll(xPoz,yPoz,s,v,xLabel,yLabel,sLabel,vLabel,x3,y3,x3Label,y3Label);
                    editBtn.setDisable(true);
                    break;
                case "kruh":
                    xPoz.setText("" + objekty.get(editovany)[1]);
                    yPoz.setText("" + objekty.get(editovany)[2]);
                    s.setText("" + objekty.get(editovany)[3]);
                    sLabel.setText("r");
                    vLabel.setText("");
                    v.setDisable(true);
                    try {
                        menu.getChildren().addAll(xPoz,yPoz,s,v,xLabel,yLabel,sLabel,vLabel);
                    }
                    catch (NullPointerException e)
                    {}
                    break;
                case "cara":
                    xLabel.setText("x1");
                    yLabel.setText("x2");
                    sLabel.setText("y1");
                    vLabel.setText("y2");

                    xPoz.setText("" + objekty.get(editovany)[1]);
                    yPoz.setText("" + objekty.get(editovany)[3]);
                    s.setText("" + objekty.get(editovany)[2]);
                    v.setText("" + objekty.get(editovany)[4]);
                    try {
                        menu.getChildren().addAll(xPoz,yPoz,s,v,xLabel,yLabel,sLabel,vLabel);
                    }
                    catch (NullPointerException e)
                    {}
                    break;
                case "trojuhelnik":
                    xLabel.setText("x1");
                    yLabel.setText("x2");
                    sLabel.setText("y1");
                    vLabel.setText("y2");
                     x3 = new TextField();
                     y3 = new TextField();
                     x3Label = new Text();
                     y3Label = new Text();
                    x3.setLayoutX(597);
                    x3.setLayoutY(149);

                    x3.setPrefWidth(43);
                    x3.setPrefHeight(25);

                    x3Label.setLayoutX(582);
                    x3Label.setLayoutY(149+16);
                    x3Label.setText("x3");
                    y3.setLayoutX(682);
                    y3.setLayoutY(149);

                    y3.setPrefWidth(43);
                    y3.setPrefHeight(25);

                    y3Label.setLayoutX(672-5);
                    y3Label.setLayoutY(149+16);
                    y3Label.setText("y3");

                    array1 =(double[])objekty.get(editovany)[1];
                    array2 =(double[])objekty.get(editovany)[2];
                    xPoz.setText("" + array1[0]);
                    yPoz.setText("" + array1[1]);
                    x3.setText("" +   array1[2]);
                    s.setText("" +    array2[0]);
                    v.setText("" +    array2[1]);
                    y3.setText("" +   array2[2]);


                    menu.getChildren().addAll(x3,y3,x3Label,y3Label);
                    try {
                        menu.getChildren().addAll(xPoz,yPoz,s,v,xLabel,yLabel,sLabel,vLabel);
                    }
                    catch (NullPointerException e)
                    {}
                    break;
                default:
                    xPoz.setText("" + objekty.get(editovany)[1]);
                    yPoz.setText("" + objekty.get(editovany)[2]);
                    s.setText("" + objekty.get(editovany)[3]);
                    v.setText("" + objekty.get(editovany)[4]);
                    try {
                        menu.getChildren().addAll(xPoz,yPoz,s,v,xLabel,yLabel,sLabel,vLabel);
                    }
                    catch (NullPointerException e)
                    {}
                    break;
            }

        }

        if(selected == 0){
            smazBtn.setDisable(true);
        }

    }
    public void edit() {
        Color lineBar =  barva.getValue();
        Color fillBar =  barva2.getValue();
        sirka = Double.parseDouble(width.getText());

        typ = (String) objekty.get(editovany)[0];
        try {
            switch (typ) {


                case "ctverec":
                    vyber(new Object[]{"ctverec", Integer.parseInt(xPoz.getText()), Integer.parseInt(yPoz.getText()), Integer.parseInt(s.getText()), Integer.parseInt(v.getText()), lineBar, fillBar,sirka});
                    break;
                case "elipsa":
                    vyber(new Object[]{"elipsa", Integer.parseInt(xPoz.getText()), Integer.parseInt(yPoz.getText()), Double.parseDouble(s.getText()), Double.parseDouble(v.getText()), lineBar, fillBar,sirka});
                    break;
                case "kruh":
                    vyber(new Object[]{"kruh", Integer.parseInt(xPoz.getText()), Integer.parseInt(yPoz.getText()), Double.parseDouble(s.getText()), lineBar, fillBar,sirka});
                    break;
                case "cara":
                    vyber(new Object[]{"cara", Integer.parseInt(xPoz.getText()), Integer.parseInt(s.getText()), Integer.parseInt(yPoz.getText()), Integer.parseInt(v.getText()), lineBar,sirka});
                    break;
                case "trojuhelnik":
                    array1 =(double[])objekty.get(editovany)[1];
                    array2 =(double[])objekty.get(editovany)[2];
                    array1[0] = Double.parseDouble(xPoz.getText());
                    array1[1] = Double.parseDouble(yPoz.getText());
                    array1[2] = Double.parseDouble(x3.getText());
                    array2[0] = Double.parseDouble(s.getText());
                    array2[1] = Double.parseDouble(v.getText());
                    array2[2] = Double.parseDouble(y3.getText());
                    vyber(new Object[]{"trojuhelnik", array1,array2, lineBar,fillBar,sirka});
                    break;
            }

        }
        catch (NumberFormatException e){}
        selected = 0;
        smazat();
        menu.getChildren().removeAll(xPoz,yPoz,s,v,xLabel,yLabel,sLabel,vLabel,x3,y3,x3Label,y3Label);
        editBtn.setDisable(true);
        smazBtn.setDisable(true);

    }

    public void export(ActionEvent actionEvent) throws Exception {
        int sirka = (int)(canvas.localToScreen(canvas.getLayoutBounds()).getWidth());
        int vyska = (int)(canvas.localToScreen(canvas.getLayoutBounds()).getHeight());
        int xLayout = (int)(canvas.localToScreen(canvas.getLayoutBounds()).getMinX());
        int yLayout = (int)(canvas.localToScreen(canvas.getLayoutBounds()).getMinY());

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyyMMdd_HH-mm-ss");
        String formattedDate = myDateObj.format(myFormatObj);

        Rectangle screenRect = new Rectangle(xLayout,yLayout,sirka,vyska);
        BufferedImage capture = new Robot().createScreenCapture(screenRect);

        FileChooser fc = new FileChooser();
        fc.setTitle("Uložit jako PNG");
        fc.setInitialFileName(formattedDate+".png");
        File file = fc.showSaveDialog(null);

        ImageIO.write(capture, "png", file);
    }
}
