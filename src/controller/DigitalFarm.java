package controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.*;
import view.MarketScreen;
import view.StartScreen;
import view.ConfigScreen;
import view.InitialUIScreen;

import java.util.HashMap;

public class DigitalFarm extends Application {
    private Stage mainWindow;
    private Player player;
    private Farm farm;
    private final int width = 1200;
    private final int height = 1000;
    private int difficulty;
    private Crop startCrop;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        mainWindow = primaryStage;
        primaryStage.setTitle("Digital Farm");
        showStartScreen();
    }

    private void showStartScreen() {
        StartScreen startScreen = new StartScreen(width, height);
        Button startButton = startScreen.getStartButton();
        startButton.setOnAction(e -> {
            goToConfigScreen();
        });
        Scene scene = startScreen.getScene();
        mainWindow.setScene(scene);
        mainWindow.show();
    }

    private void goToConfigScreen() {
        ConfigScreen configScreen = new ConfigScreen(width, height);
        TextField nameField = configScreen.getNameField();


        Button easyBtn = configScreen.getEasyBtn();
        easyBtn.setOnAction(e -> {
            difficulty = 1;
        });

        Button mediumBtn = configScreen.getMediumBtn();
        mediumBtn.setOnAction(e -> {
            difficulty = 2;
        });

        Button difficultBtn = configScreen.getDifficultBtn();
        difficultBtn.setOnAction(e -> {
            difficulty = 3;
        });

        Button riceBtn = configScreen.getRiceBtn();
        riceBtn.setOnAction(e -> {
            startCrop = new Crop(1, Crop.Type.Rice);
        });

        Button cornBtn = configScreen.getCornBtn();
        cornBtn.setOnAction(e -> {
            startCrop = new Crop(1, Crop.Type.Corn);
        });

        Button potatoBtn = configScreen.getPotatoBtn();
        potatoBtn.setOnAction(e -> {
            startCrop = new Crop(1, Crop.Type.Potato);
        });

        Button springBtn = configScreen.getSpringBtn();
        springBtn.setOnAction(e -> {

        });

        Button summerBtn = configScreen.getSummerBtn();
        summerBtn.setOnAction(e -> {

        });

        Button fallBtn = configScreen.getFallBtn();
        fallBtn.setOnAction(e -> {

        });

        Button winterBtn = configScreen.getWinterBtn();
        winterBtn.setOnAction(e -> {

        });

        Button nextBtn = configScreen.getNextBtn();
        nextBtn.setOnAction(e -> {
            String inputName = nameField.getText();
            //unfinished, account for season
            if (inputName != null && !inputName.trim().equals("") && difficulty != 0
                    && startCrop != null) {
                player = new Player(inputName, 0, difficulty);
                player.addItem(startCrop, 1);
                player.addItem(new Crop(1, Crop.Type.Corn), 0);
                player.addItem(new Crop(1, Crop.Type.Potato), 0);
                player.addItem(new Crop(1, Crop.Type.Rice), 0);
                player.addItem(new Item(Item.MarketItem.Pesticides), 0);
                player.addItem(new Item(Item.MarketItem.Fertilizer), 0);
                farm = new Farm(difficulty);
                goToInitialUIScreen();
            }
        });

        Scene scene = configScreen.getScene();
        mainWindow.setScene(scene);
        mainWindow.show();
    }
    private void goToInitialUIScreen() {
        InitialUIScreen initUIScreen = new InitialUIScreen(width, height, player.getMoney(),
                farm.getDay(), player.getInventory());
        Button marketBtn = initUIScreen.getMarketBtn();
        Button plantCornBtn = initUIScreen.getPlantCornBtn();
        Button plantRiceBtn = initUIScreen.getPlantRiceBtn();
        Button plantPotatoBtn = initUIScreen.getPlantPotatoBtn();
        marketBtn.setOnAction(e -> {
            goToMarketScreen();
        });

        Button[] plotsBtn = initUIScreen.getPlotsBtn();
        Button[] waterBtns = initUIScreen.getWaterBtns();
        Button[] pestBtns = initUIScreen.getPestBtns();
        Button[] fertBtns = initUIScreen.getFertBtns();
        for (int i = 0; i < plotsBtn.length; i++) {
            plotsBtn[i] = new Button();
            waterBtns[i] = new Button();
            pestBtns[i] = new Button();
            fertBtns[i] = new Button();
        }

        Button timeBtn = initUIScreen.getTimeBtn();
        timeBtn.setOnAction(e -> {
            farm.nextDay();
            refreshPlots(initUIScreen, plotsBtn, waterBtns, fertBtns, pestBtns);
            Alert event = new Alert(Alert.AlertType.INFORMATION);
            event.setTitle("Event");
            switch (farm.getEventGenerated().name()) {
            case "Rain":
                event.setHeaderText("It has rained!");
                event.setGraphic(new ImageView(new Image("rain.jpg")));
                event.show();
                break;
            case "Locust":
                event.setHeaderText("Locusts have attacked your crops!");
                event.setGraphic(new ImageView(new Image("locust.jpg")));
                event.show();
                break;
            case "Drought":
                event.setHeaderText("A drought has occurred!");
                event.setGraphic(new ImageView(new Image("drought.jpg")));
                event.show();
                break;
            default:
                break;
            }
            initUIScreen.incrementDay();
            refreshPlots(initUIScreen, plotsBtn, waterBtns, fertBtns, pestBtns);
            mainWindow.setScene(initUIScreen.getScene());
        });

        plantCornBtn.setOnAction(e -> {
            Crop cornSeed = new Crop(1, Crop.Type.Corn);
            if (farm.plant(cornSeed, player.getInventory().get(cornSeed))) {
                player.subtractItem(cornSeed, 1);
                refreshPlots(initUIScreen, plotsBtn, waterBtns, fertBtns, pestBtns);
            }
        });

        plantRiceBtn.setOnAction(e -> {
            Crop riceSeed = new Crop(1, Crop.Type.Rice);
            if (farm.plant(riceSeed, player.getInventory().get(riceSeed))) {
                player.subtractItem(riceSeed, 1);
                refreshPlots(initUIScreen, plotsBtn, waterBtns, fertBtns, pestBtns);
            }
        });

        plantPotatoBtn.setOnAction(e -> {
            Crop potatoSeed = new Crop(1, Crop.Type.Potato);
            if (farm.plant(potatoSeed, player.getInventory().get(potatoSeed))) {
                player.subtractItem(potatoSeed, 1);
                refreshPlots(initUIScreen, plotsBtn, waterBtns, fertBtns, pestBtns);
            }
        });
        refreshPlots(initUIScreen, plotsBtn, waterBtns, fertBtns, pestBtns);
        for (int i = 0; i < plotsBtn.length; i++) {
            int finalI = i;
            waterBtns[i].setOnAction(e -> {
                if (farm.getCropArray()[finalI] != null) {
                    farm.getCropArray()[finalI].water();
                    refreshPlots(initUIScreen, plotsBtn, waterBtns, fertBtns, pestBtns);
                }
            });

            fertBtns[i].setOnAction(e -> {
                Item fertilizer = new Item(Item.MarketItem.Fertilizer);
                if (farm.getCropArray()[finalI] != null) {
                    if (player.getInventory().get(fertilizer) > 0) {
                        if (farm.getCropArray()[finalI].fertilize()) {
                            player.subtractItem(fertilizer, 1);
                            refreshPlots(initUIScreen, plotsBtn, waterBtns, fertBtns, pestBtns);
                        }
                    }
                }
            });

            pestBtns[i].setOnAction(e -> {
                Item pesticide = new Item(Item.MarketItem.Pesticides);
                if (farm.getCropArray()[finalI] != null) {
                    if (player.getInventory().get(pesticide) > 0) {
                        if (!farm.getCropArray()[finalI].isPesticides()) {
                            farm.getCropArray()[finalI].addPesticide();
                            player.subtractItem(pesticide, 1);
                            refreshPlots(initUIScreen, plotsBtn, waterBtns, fertBtns, pestBtns);
                        }
                    }
                }
            });

            plotsBtn[i].setOnAction(e -> {
                clickOnPlotAction(initUIScreen, plotsBtn, waterBtns, pestBtns, fertBtns, finalI);
            });

            Scene scene = initUIScreen.getScene();
            mainWindow.setScene(scene);
            mainWindow.show();
        }
    }

    private void clickOnPlotAction(InitialUIScreen initUIScreen, Button[] plotsBtn,
                                   Button[] waterBtns, Button[] pestBtns, Button[] fertBtns,
                                   int finalI) {
        switch (plotsBtn[finalI].getText()) {
        case "Potato":
            if (player.addItem(new Crop(7, Crop.Type.Potato), 1)) {
                initUIScreen.setDirt(plotsBtn[finalI]);
                farm.setCropArray(null, finalI);
                refreshPlots(initUIScreen, plotsBtn, waterBtns, fertBtns, pestBtns);
            }
            break;
        case "Pesticided Potato":
            if (player.addItem(new Crop(7, Crop.Type.PesticidedPotato), 1)) {
                initUIScreen.setDirt(plotsBtn[finalI]);
                farm.setCropArray(null, finalI);
                refreshPlots(initUIScreen, plotsBtn, waterBtns, fertBtns, pestBtns);
            }
            break;
        case "Corn":
            if (player.addItem(new Crop(7, Crop.Type.Corn), 1)) {
                initUIScreen.setDirt(plotsBtn[finalI]);
                farm.setCropArray(null, finalI);
                refreshPlots(initUIScreen, plotsBtn, waterBtns, fertBtns, pestBtns);
            }
            break;
        case "Pesticided Corn":
            if (player.addItem(new Crop(7, Crop.Type.PesticidedCorn), 1)) {
                initUIScreen.setDirt(plotsBtn[finalI]);
                farm.setCropArray(null, finalI);
                refreshPlots(initUIScreen, plotsBtn, waterBtns, fertBtns, pestBtns);
            }
            break;
        case "Rice":
            if (player.addItem(new Crop(7, Crop.Type.Rice), 1)) {
                initUIScreen.setDirt(plotsBtn[finalI]);
                farm.setCropArray(null, finalI);
                refreshPlots(initUIScreen, plotsBtn, waterBtns, fertBtns, pestBtns);
            }
            break;
        case "Pesticided Rice":
            if (player.addItem(new Crop(7, Crop.Type.PesticidedRice), 1)) {
                initUIScreen.setDirt(plotsBtn[finalI]);
                farm.setCropArray(null, finalI);
                refreshPlots(initUIScreen, plotsBtn, waterBtns, fertBtns, pestBtns);
            }
            break;
        case "Dead Plant":
            initUIScreen.setDirt(plotsBtn[finalI]);
            farm.setCropArray(null, finalI);
            refreshPlots(initUIScreen, plotsBtn, waterBtns, fertBtns, pestBtns);
            break;
        default:
            break;
        }
    }

    private void refreshPlots(InitialUIScreen initUIScreen, Button[] plotsBtn,
                              Button[] waterBtns, Button[] fertBtns, Button[] pestBtns) {
        for (int i = 0; i < plotsBtn.length; i++) {
            if (farm.getCropArray()[i] != null) {
                initUIScreen.setPlant(plotsBtn[i], farm.getCropArray()[i]);
                initUIScreen.setWater(waterBtns[i], farm.getCropArray()[i]);
                initUIScreen.setFert(fertBtns[i], farm.getCropArray()[i]);
                initUIScreen.setPest(pestBtns[i], farm.getCropArray()[i]);
            } else {
                initUIScreen.setDirt(plotsBtn[i]);
                initUIScreen.setEmptyWater(waterBtns[i]);
                initUIScreen.setEmptyFert(fertBtns[i]);
                initUIScreen.setEmptyPest(pestBtns[i]);
            }
        }
        Scene scene = initUIScreen.getScene();
        mainWindow.setScene(scene);
        mainWindow.show();
    }

    private void goToMarketScreen() {
        Market market = new Market(difficulty);
        HashMap<Object, Integer> stock = market.getStock();
        MarketScreen marketScreen = new MarketScreen(width, height, player, market);

        Button backBtn = marketScreen.getBackBtn();
        backBtn.setOnAction(e -> {
            goToInitialUIScreen();
        });

        for (Object key : stock.keySet()) {
            if (key instanceof Item) {
                createAndHandleItemBtns(market, stock, marketScreen, (Item) key);
            }
            if (key instanceof Crop) {
                Crop crop = (Crop) key;
                if (crop.getSpecies().getName().equals("Potato") && crop.getStage() == 1) {
                    Button buyPotatoBtn = marketScreen.getBuyPotatoBtn();
                    buyPotatoBtn.setOnAction(e -> {
                        if (market.buy(player.getMoney(), crop, 1, player.getMaxInventorySpace()
                                - player.getInventoryCount())) {
                            player.setMoney(player.getMoney() - stock.get(crop));
                            player.addItem(crop, 1);
                            mainWindow.setScene(marketScreen.getScene());
                        }
                    });
                    Button sellPotatoSeedBtn = marketScreen.getSellPotatoSeedBtn();
                    sellPotatoSeedBtn.setOnAction(e -> {
                        if (player.getInventory().get(crop) > 0) {
                            player.setMoney(player.getMoney() + market.sell(crop, 1));
                            player.subtractItem(crop, 1);
                            mainWindow.setScene(marketScreen.getScene());
                        }
                    });
                } else if (crop.getSpecies().getName().equals("Potato") && crop.getStage() == 7) {
                    Button sellPotatoBtn = marketScreen.getSellPotatoBtn();
                    sellPotatoBtn.setOnAction(e -> {
                        if (player.getInventory().get(crop) > 0) {
                            player.setMoney(player.getMoney() + market.sell(crop, 1));
                            player.subtractItem(crop, 1);
                            mainWindow.setScene(marketScreen.getScene());
                        }
                    });
                } else if (crop.getSpecies().getName().equals("Corn") && crop.getStage() == 1) {
                    Button buyCornBtn = marketScreen.getBuyCornBtn();
                    buyCornBtn.setOnAction(e -> {
                        if (market.buy(player.getMoney(), crop, 1, player.getMaxInventorySpace()
                                - player.getInventoryCount())) {
                            player.setMoney(player.getMoney() - stock.get(crop));
                            player.addItem(crop, 1);
                            mainWindow.setScene(marketScreen.getScene());
                        }
                    });
                    Button sellCornSeedBtn = marketScreen.getSellCornSeedBtn();
                    sellCornSeedBtn.setOnAction(e -> {
                        if (player.getInventory().get(crop) > 0) {
                            player.setMoney(player.getMoney() + market.sell(crop, 1));
                            player.subtractItem(crop, 1);
                            mainWindow.setScene(marketScreen.getScene());
                        }
                    });
                } else if (crop.getSpecies().getName().equals("Corn") && crop.getStage() == 7) {
                    Button sellCornBtn = marketScreen.getSellCornBtn();
                    sellCornBtn.setOnAction(e -> {
                        if (player.getInventory().get(crop) > 0) {
                            player.setMoney(player.getMoney() + market.sell(crop, 1));
                            player.subtractItem(crop, 1);
                            mainWindow.setScene(marketScreen.getScene());
                        }
                    });
                } else if (crop.getSpecies().getName().equals("Rice") && crop.getStage() == 1) {
                    Button buyRiceBtn = marketScreen.getBuyRiceBtn();
                    buyRiceBtn.setOnAction(e -> {
                        if (market.buy(player.getMoney(), crop, 1, player.getMaxInventorySpace()
                                - player.getInventoryCount())) {
                            player.setMoney(player.getMoney() - stock.get(crop));
                            player.addItem(crop, 1);
                            mainWindow.setScene(marketScreen.getScene());
                        }
                    });
                    Button sellRiceSeedBtn = marketScreen.getSellRiceSeedBtn();
                    sellRiceSeedBtn.setOnAction(e -> {
                        if (player.getInventory().get(crop) > 0) {
                            player.setMoney(player.getMoney() + market.sell(crop, 1));
                            player.subtractItem(crop, 1);
                            mainWindow.setScene(marketScreen.getScene());
                        }
                    });
                } else if (crop.getSpecies().getName().equals("Rice") && crop.getStage() == 7) {
                    Button sellRiceBtn = marketScreen.getSellRiceBtn();
                    sellRiceBtn.setOnAction(e -> {
                        if (player.getInventory().get(crop) > 0) {
                            player.setMoney(player.getMoney() + market.sell(crop, 1));
                            player.subtractItem(crop, 1);
                            mainWindow.setScene(marketScreen.getScene());
                        }
                    });
                } else if (crop.getSpecies().getName().equals("Pesticided Rice")
                        && crop.getStage() == 7) {
                    Button sellPestRiceBtn = marketScreen.getSellPestRiceBtn();
                    sellPestRiceBtn.setOnAction(e -> {
                        if (player.getInventory().get(crop) > 0) {
                            player.setMoney(player.getMoney() + market.sell(crop, 1));
                            player.subtractItem(crop, 1);
                            mainWindow.setScene(marketScreen.getScene());
                        }
                    });
                } else if (crop.getSpecies().getName().equals("Pesticided Corn")
                        && crop.getStage() == 7) {
                    Button sellPestCornBtn = marketScreen.getSellPestCornBtn();
                    sellPestCornBtn.setOnAction(e -> {
                        if (player.getInventory().get(crop) > 0) {
                            player.setMoney(player.getMoney() + market.sell(crop, 1));
                            player.subtractItem(crop, 1);
                            mainWindow.setScene(marketScreen.getScene());
                        }
                    });
                } else if (crop.getSpecies().getName().equals("Pesticided Potato")
                        && crop.getStage() == 7) {
                    Button sellPestPotatoBtn = marketScreen.getSellPestPotatoBtn();
                    sellPestPotatoBtn.setOnAction(e -> {
                        if (player.getInventory().get(crop) > 0) {
                            player.setMoney(player.getMoney() + market.sell(crop, 1));
                            player.subtractItem(crop, 1);
                            mainWindow.setScene(marketScreen.getScene());
                        }
                    });
                }
            }
        }

        Scene scene = marketScreen.getScene();
        mainWindow.setScene(scene);
        mainWindow.show();
    }

    private void createAndHandleItemBtns(Market market, HashMap<Object, Integer> stock,
                                         MarketScreen marketScreen, Item key) {
        Item item = key;
        Button buyFertBtn = marketScreen.getBuyFertBtn();
        Button buyPestBtn = marketScreen.getBuyPestBtn();
        if (item.getName().equals("Fertilizer")) {
            buyFertBtn.setOnAction(e -> {
                if (market.buy(player.getMoney(), item, 1,
                        player.getMaxInventorySpace() - player.getInventoryCount())) {
                    player.setMoney(player.getMoney() - stock.get(item));
                    player.addItem(item, 1);
                    mainWindow.setScene(marketScreen.getScene());
                }
            });
        }

        if (item.getName().equals("Pesticide")) {
            buyPestBtn.setOnAction(e -> {
                if (market.buy(player.getMoney(), item, 1,
                        player.getMaxInventorySpace() - player.getInventoryCount())) {
                    player.setMoney(player.getMoney() - stock.get(item));
                    player.addItem(item, 1);
                    mainWindow.setScene(marketScreen.getScene());
                }
            });
        }
    }

}
