package view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Crop;
import model.Market;
import model.Player;

import java.util.HashMap;

public class MarketScreen {
    private int width;
    private int height;
    private Player player;
    private Market market;
    private Button buyPotatoBtn;
    private Button buyCornBtn;
    private Button buyRiceBtn;
    private Button sellPotatoSeedBtn;
    private Button sellCornSeedBtn;
    private Button sellRiceSeedBtn;
    private Button sellPotatoBtn;
    private Button sellCornBtn;
    private Button sellRiceBtn;
    private Button backBtn;

    private MarketScreen() { }

    public MarketScreen(int w, int h, Player p, Market m) {
        width = w;
        height = h;
        player = p;
        market = m;
        buyPotatoBtn = new Button("Buy");
        buyCornBtn = new Button("Buy");
        buyRiceBtn = new Button("Buy");
        sellPotatoSeedBtn = new Button("Sell for " + m.sell(new Crop(1, Crop.Type.Potato), 1));
        sellCornSeedBtn = new Button("Sell for " + m.sell(new Crop(1, Crop.Type.Corn), 1));
        sellRiceSeedBtn = new Button("Sell for " + m.sell(new Crop(1, Crop.Type.Rice), 1));
        sellPotatoBtn = new Button("Sell for " + m.sell(new Crop(3, Crop.Type.Potato), 1));
        sellCornBtn = new Button("Sell for " + m.sell(new Crop(3, Crop.Type.Corn), 1));
        sellRiceBtn = new Button("Sell for " + m.sell(new Crop(3, Crop.Type.Rice), 1));
        backBtn = new Button("Back");
    }

    public Button getBuyPotatoBtn() {
        return buyPotatoBtn;
    }
    public Button getBuyCornBtn() {
        return buyCornBtn;
    }
    public Button getBuyRiceBtn() {
        return buyRiceBtn;
    }
    public Button getSellPotatoSeedBtn() {
        return sellPotatoSeedBtn;
    }
    public Button getSellCornSeedBtn() {
        return sellCornSeedBtn;
    }
    public Button getSellRiceSeedBtn() {
        return sellRiceSeedBtn;
    }
    public Button getSellPotatoBtn() {
        return sellPotatoBtn;
    }
    public Button getSellCornBtn() {
        return sellCornBtn;
    }
    public Button getSellRiceBtn() {
        return sellRiceBtn;
    }
    public Button getBackBtn() {
        return backBtn;
    }

    public Scene getScene() {
        Text marketTitle = new Text("Market");
        Text playerMoney = new Text("Player Money: " + player.getMoney());

        //inventory
        VBox inventoryDisplay = new VBox();
        inventoryDisplay.getChildren().add(new Text("Inventory"));
        HashMap<Object, Integer> inventory = player.getInventory();

        for (Object key : inventory.keySet()) {
            String cropName;
            Text crop;
            HBox sellDisplay = new HBox();
            if (key.getClass() == Crop.class) {
                Crop c = (Crop) key;
                if (c.getStage() == 3) {
                    cropName = c.getSpecies().getName();
                    crop = new Text(cropName + ": " + inventory.get(key));
                    if (cropName.equals("Corn")) {
                        sellDisplay.getChildren().addAll(crop, getSellCornBtn());
                    } else if (cropName.equals("Potato")) {
                        sellDisplay.getChildren().addAll(crop, getSellPotatoBtn());
                    } else if (cropName.equals("Rice")){
                        sellDisplay.getChildren().addAll(crop, getSellRiceBtn());
                    }
                } else {
                    cropName = c.getSpecies().getName() + " Seed";
                    crop = new Text(cropName + ": " + inventory.get(key));
                    if (cropName.equals("Corn Seed")) {
                        sellDisplay.getChildren().addAll(crop, getSellCornSeedBtn());
                    } else if (cropName.equals("Potato Seed")) {
                        sellDisplay.getChildren().addAll(crop, getSellPotatoSeedBtn());
                    } else if (cropName.equals("Rice Seed")){
                        sellDisplay.getChildren().addAll(crop, getSellRiceSeedBtn());
                    }
                }
                inventoryDisplay.getChildren().add(sellDisplay);
            }
        }

        //Buy section for crops
        VBox buyBox = new VBox(new Text("Buy Here"));
        HashMap<Crop, Integer> stock = market.getStock();
        for (Crop key : stock.keySet()) {
            String cropName = key.getSpecies().getName();
            Text cropLabel = new Text(cropName + " Cost: " + stock.get(key));

            HBox crop;
            if (key.getStage() == 3) {
                if (cropName.equals("Potato")) {
                    crop = new HBox(buyPotatoBtn, cropLabel);
                } else if (cropName.equals("Corn")) {
                    crop = new HBox(buyCornBtn, cropLabel);
                } else {
                    crop = new HBox(buyRiceBtn, cropLabel);
                }
                buyBox.getChildren().add(crop);
            }
        }

        VBox market = new VBox(marketTitle, playerMoney, buyBox, inventoryDisplay, backBtn);
        market.setSpacing(15);
        return new Scene(market, width, height);
    }
}
