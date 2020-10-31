
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VehicleScraper {
    List<Vehicle> vehicleList;

    public static String folder = "C:\\Users\\John\\Documents\\Vehicle Dataset";

    public static void main(String[] args) {
        VehicleScraper scraper = new VehicleScraper();
        scraper.populateVehicleList();
        scraper.loadDataset();
    }

    public VehicleScraper() {
        vehicleList = new ArrayList<Vehicle>();
    }

    public void loadDataset() {
        for (Vehicle v : vehicleList) {
            harvestImages(v);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }


    public void harvestImages(Vehicle v) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\John\\Downloads\\chromedriver_win32\\chromedriver.exe");
        String searchString = "2020 " + v.getMake() + "+" + v.getModel();
        System.out.println("Using search string " + searchString);
        String yahooUrl = "https://images.search.yahoo.com/search/images;_ylt=AwrExl8RhJ1fkzQAkoyLuLkF;_ylc=X1MDOTYwNTc0ODMEX3IDMgRmcgMEZ3ByaWQDeUFPSWJ4a2hTU3kxcklDMW1Idm93QQRuX3N1Z2cDMTAEb3JpZ2luA2ltYWdlcy5zZWFyY2gueWFob28uY29tBHBvcwMwBHBxc3RyAwRwcXN0cmwDBHFzdHJsAzkEcXVlcnkDYXVkaSUyMGEzBHRfc3RtcAMxNjA0MTU4NDg1?fr2=sb-top-images.search&p="
                + searchString
                + "&ei=UTF-8&iscqry=&fr=sfp&guce_referrer=aHR0cHM6Ly9pbWFnZXMuc2VhcmNoLnlhaG9vLmNvbS8_Z3VjZV9yZWZlcnJlcj1hSFIwY0hNNkx5OTNkM2N1WjI5dloyeGxMbU52YlM4Jmd1Y2VfcmVmZXJyZXJfc2lnPUFRQUFBRnBHLTR6WjZkU2xpQzQzZDhQSmFSMXlMVzFmb2NZWi1iVkVNMmQ3aVZ0STF1c2t0X3hFZnZuNW5JOWlOZDlLeFpJSnVtRDZ4Xy13Z29sSTRYYmtCcldGejRSOXp5azFjcmt3UGdlR2x3NUNSMnNXUDFsNjVwMVR3cnJKNkM3cUVOVUJaSm1uU2wtby1BblZMMzdOUk51WVZWX3pyZnlZNWtXb015UklETmZmJl9ndWNfY29uc2VudF9za2lwPTE2MDQxNTg1MTE&guce_referrer_sig=AQAAADiijTDTceJrTU114sc1-razaqduQbZcxkUAAr7YQXQc75KHqMz0fEzDatjYmjo9a2pWIbcbmittgRouYXju3pfLgRMwmcuaAFZR3ZVR1FkUYbDdlqkKk4dBiyVZ6byPxnlRuNMopaMBmTOY9jBFHiCeqUbRCNH9jeMl4up31PI6&_guc_consent_skip=1604158515";
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 10000);
        try {
            driver.get(yahooUrl);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0,10000)");
            Thread.sleep(2000);
            for (int i = 0;i<5;i++) {
                WebElement moreBtn = driver.findElement(By.cssSelector("button.ygbt"));
                moreBtn.click();
                Thread.sleep(2000);
                js.executeScript("window.scrollBy(0,10000)");
                Thread.sleep(2000);
            }
            List<WebElement> lis = driver.findElements(By.cssSelector("li.ld"));
            int count = 0;
            for (WebElement li : lis) {
                File folderFile = new File(folder);
                File makeDir = new File(folder + "\\" + v.getMake());
                File modelDir = new File(folder + "\\" + v.getMake() + "\\" + v.getModel());
                if (!makeDir.exists()) {
                    makeDir.mkdir();
                }
                if (!modelDir.exists()) {
                    modelDir.mkdir();
                }
                WebElement img = li.findElement(By.tagName("img"));
                String src = img.getAttribute("src");
                System.out.println("Image URL: " + src);
                try {
                    BufferedImage bufferedImage = ImageIO.read(new URL(src));
                    File outputfile = new File(modelDir + "\\" + count + ".png");
                    count++;
                    ImageIO.write(bufferedImage, "png", outputfile);
                } catch (MalformedURLException me) {
                    System.out.println("Bad image URL");
                }
            }
        } catch(Exception e) {
            System.out.println("Exception.........");
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    public void populateVehicleList() {
        String vehicleListUrl = "https://www.kbb.com/car-make-model-list/new/view-all/?s=model";
        try {
            Document doc = Jsoup.connect(vehicleListUrl).get();
            Element e = doc.select("table.make-model-table").first();
            Elements tags = e.select("tr.modelnamesandmakes_item");
            for (Element tag : tags) {
                Elements tds = tag.getElementsByTag("td");
                Element modelTd = tds.get(0);
                Element makeTd = tds.get(1);
                Vehicle v = new Vehicle();
                v.setMake(makeTd.text().trim());
                v.setModel(modelTd.text().trim());
                vehicleList.add(v);
            }

            Collections.sort(vehicleList);

            for (Vehicle v : vehicleList) {
                System.out.println(v.getMake() + " " + v.getModel());
            }

        } catch(IOException ie) {
            ie.printStackTrace();
        }
    }

}
