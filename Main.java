package entranceJsonFilePackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Main extends Application{
	
    Stage searchStage = null;
    Stage tableStage = null;
    Stage detailStage = null;
    
    String SearchByName = null;
    String SearchByCity = null;
    String SearchByHouse = null;
    String SearchByYear = null;
    
	String name;
	String city;
	String house;
	String year; 
	
	String nameDetail;
	String cityDetail;
	String houseDetail;
	String yearDetail; 
	String URL = "http://mysafeinfo.com/api/data?list=englishmonarchs&format=json";
	      
    
	public Scene createSearchScene() {
    	GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));   	
        Text scenetitle = new Text("Json Search");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label name = new Label("Name:");
        grid.add(name, 0, 1);
        final TextField userNameBox = new TextField();
        grid.add(userNameBox, 1, 1);

        Label city = new Label("City:");
        grid.add(city, 0, 2);
        final TextField userCityBox = new TextField();
        grid.add(userCityBox, 1, 2);
        
        Label house = new Label("House:");
        grid.add(house, 0, 3);
        final TextField userHouseBox = new TextField();
        grid.add(userHouseBox, 1, 3);
        
        Label year = new Label("Year:");
        grid.add(year, 0, 4);
        final TextField userYearBox = new TextField();
        grid.add(userYearBox, 1, 4);
        
        final Button button = new Button("OK");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(button);
        grid.add(hbBtn, 1, 5);
        
        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);
        button.setOnAction(new EventHandler<ActionEvent>() {     	 
            public void handle(ActionEvent t) {
                SearchByName = userNameBox.getText();
                SearchByCity = userCityBox.getText();
                SearchByHouse = userNameBox.getText();
                SearchByYear = userYearBox.getText();
                prepareList();
                tableStage = new Stage();
                tableStage.setScene(createTableScene());
                searchStage.close();
                tableStage.show();
            }
        });
        Scene sc = new Scene(grid, 300, 275);
        return sc;
    }
    
    
    public String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    
    public JSONArray readJsonFromUrl(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        JSONArray json = null;
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            json = new JSONArray(jsonText);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            is.close();
        }
        return json;
    }
    
    
    public void start(Stage stage) throws Exception {
        this.searchStage = stage;
        stage.setScene(createSearchScene());
        stage.show();     
    }
    

    public static void main(String[] args) {
        launch(args); 
    }
    
       
    public void prepareList(){
        try {
            JSONArray json = readJsonFromUrl(URL);
            //for(int i=0; i<5; i++){
                JSONObject obj = (JSONObject)json.get(Integer.valueOf(SearchByName));
                //JSONObject obj = (JSONObject)json.get(i);
                name = obj.getString("nm");
                city = obj.getString("cty");
                house = obj.getString("hse"); 
                year = obj.getString("yrs"); 
                data.add(new Person(name, city, house, year));            	
            //}
            
            System.out.println(name);
            System.out.println(city);
            System.out.println(house); 
            System.out.println(year); 
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }    	
    }
    
	
    public TableView<Person> table = new TableView<Person>();
    public final ObservableList<Person> data = FXCollections.observableArrayList();
    
    
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Scene createTableScene() { 	
		Scene scene = new Scene(new Group());
		tableStage.setTitle("Table View");
		tableStage.setWidth(350);
		tableStage.setHeight(525);
        final Label label = new Label("Search Result");
        label.setFont(new Font("Arial", 20));
        table.setEditable(true);      
        Callback<TableColumn, TableCell> stringCellFactory =
                new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                MyStringTableCell cell = new MyStringTableCell();
                cell.addEventFilter(MouseEvent.MOUSE_CLICKED ,  new MyEventHandler());
                return cell;
            }
        };
        TableColumn Name = new TableColumn("Name");
        Name.setMinWidth(150);
        Name.setCellValueFactory(
                new PropertyValueFactory<Person, String>("Name"));
        Name.setCellFactory(stringCellFactory);
        TableColumn House = new TableColumn("House");
        House.setMinWidth(150);
        House.setCellValueFactory(
                new PropertyValueFactory<Person, String>("House"));
        House.setCellFactory(stringCellFactory);
        table.setItems(data);
        table.getColumns().addAll(Name, House);      
        final Button button = new Button("OK");

        button.setOnAction(new EventHandler<ActionEvent>() {   	 
            public void handle(ActionEvent t) {
            	tableStage.close();
            }
        });
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, button);
        vbox.setAlignment(Pos.CENTER);
        ((Group) scene.getRoot()).getChildren().addAll(vbox); 
        return scene;
    }
	
	
    class MyStringTableCell extends TableCell<Person, String> {
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty ? null : getString());
            setGraphic(null);
        }
        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
    
    
    class MyEventHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent t) {
            @SuppressWarnings("rawtypes")
			TableCell c = (TableCell) t.getSource();
            int index = c.getIndex();
            System.out.println("Name = " + data.get(index).getName());
            System.out.println("City = " + data.get(index).getCity());
            System.out.println("House = " + data.get(index).getHouse());
            System.out.println("Year = " + data.get(index).getYear());         
            nameDetail = data.get(index).getName();
            cityDetail = data.get(index).getCity();
            houseDetail = data.get(index).getHouse();
            yearDetail = data.get(index).getYear();           
            detailStage = new Stage();
            detailStage.setScene(createSceneDetails());
            detailStage.show();
        }
    }
	
    
    private Scene createSceneDetails() {
    	GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));   
        Text scenetitle = new Text("Details:");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);
        Label names = new Label(nameDetail);
        grid.add(names, 0, 1);
        Label citys = new Label(cityDetail);
        grid.add(citys, 0, 2);
        Label houses = new Label(houseDetail);
        grid.add(houses, 0, 3);
        Label years = new Label(yearDetail);
        grid.add(years, 0, 4);
        final Button button = new Button("OK");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(button);
        grid.add(hbBtn, 1, 5);        
        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);
        button.setOnAction(new EventHandler<ActionEvent>() {   	 
            public void handle(ActionEvent t) {
            	detailStage.close();
            }
        });
        Scene sc = new Scene(grid, 225, 225);
        return sc;
    }
	
    
	public static class Person {	 
        private final SimpleStringProperty Name;
        private final SimpleStringProperty City;
        private final SimpleStringProperty House;
        private final SimpleStringProperty Year;
 
        private Person(String name, String city, String house, String year ) {
            this.Name = new SimpleStringProperty(name);
            this.City = new SimpleStringProperty(city);
            this.House = new SimpleStringProperty(house);
            this.Year = new SimpleStringProperty(year);
        }
 
        public String getName() {
            return Name.get();
        }
 
        public void setName(String name) {
            Name.set(name);
        }
 
        public String getCity() {
            return City.get();
        }
 
        public void setCity(String city) {
            City.set(city);
        }
 
        public String getHouse() {
            return House.get();
        }
 
        public void setHouse(String house) {
            House.set(house);
        }
        
        public String getYear() {
            return Year.get();
        }
 
        public void setYear(String year) {
            Year.set(year);
        }
    }
    
}

"# Read-Json-from-web-display-on-table" 
