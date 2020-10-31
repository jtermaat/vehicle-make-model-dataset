import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.interactions.internal.MouseAction;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

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
            wait(10);
        }
    }

//    private void scrollToBottom(WebClient webClient) {
//        webClient.getOptions().setCssEnabled(false);
//        webClient.getOptions().setJavaScriptEnabled(true);
//        webClient.setJavaScriptTimeout(99999);
//        webClient.waitForBackgroundJavaScript(99999);
//        webClient.getOptions().setThrowExceptionOnScriptError(false);
//        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
//
//        try {
//            Window window = webClient.getCurrentWindow();
//
//            HtmlPage productPage = (HtmlPage)webClient.getPage("http://detail.1688.com/offer/1228752603.html");
//            HtmlElement dealElement = (HtmlElement) productPage.getFirstByXPath("//*[@id=\"mod-detail-otabs\"]/ul/li[2]/a/span");
//            HtmlElement commentElement = (HtmlElement) productPage.getFirstByXPath("//*[@id=\"commentbody\"]/dl[1]/dd/div[2]");
//
//            if(dealElement!=null)
//            {
//                System.out.println(dealElement.asXml());
//            }
//            if(commentElement!=null)
//            {
//                System.out.println(commentElement.asXml());
//            }
//            System.out.println(productPage.asText());
//        } catch (Exception e) {
//            System.out.println("Exception scrolling");
//        }
//    }

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

//        WebElement lement = driver.findElement(By.linkText("Linux"));
//        WebElement
//        final DomNodeList lis = currentPage.querySelectorAll("li.ld");
//        for (Object li : lis) {
//            DomNode domLi = (DomNode)li;
//            final HtmlImage img = (HtmlImage)domLi.querySelector("img");
//            File folderFile = new File(folder);
//            File makeDir = new File(folder + "\\" + v.getMake());
//            File modelDir = new File(folder + "\\" + v.getMake() + "\\" + v.getModel());
//            if (!makeDir.exists()) {
//                makeDir.mkdir();
//            }
//            if (!modelDir.exists()) {
//                modelDir.mkdir();
//            }
//            img.saveAs(modelDir);

        //        String googleUrl = "https://www.google.com/search?q="
//                + searchString
//                + "&sxsrf=ALeKk01HE1OqDciXDZaw4h9_rRRTiZO7Iw:1604078061333&source=lnms&tbm=isch&sa=X&ved=2ahUKEwigtaW46NzsAhUEH6wKHTkoCr4Q_AUoAXoECCEQAw&biw=1777&bih=876";


//        public class HelloSelenium {
//
//            public static void main(String[] args) {
//                WebDriver driver = new FirefoxDriver();
//                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//                try {
//                    driver.get("https://google.com/ncr");
//                    driver.findElement(By.name("q")).sendKeys("cheese" + Keys.ENTER);
//                    WebElement firstResult = wait.until(presenceOfElementLocated(By.cssSelector("h3>div")));
//                    System.out.println(firstResult.getAttribute("textContent"));
//                } finally {
//                    driver.quit();
//                }


//        try {
//            WebClient webClient = new WebClient();
////            HtmlPage currentPage = (HtmlPage) webClient.getPage(new URL(googleUrl));
//            HtmlPage currentPage = (HtmlPage) webClient.getPage(new URL(yahooUrl));

//            WebDriver driver = new HtmlUnitDriver();
//
//
//            webClient.getCurrentWindow().setInnerHeight(Integer.MAX_VALUE);
////            webClient.getCurrentWindow().
//            webClient.waitForBackgroundJavaScript(10000);
//            ScriptResult sr = currentPage.executeJavaScript("window.scrollBy(0,60000)");
//            String s2 = "window.scrollY=5001;";
//            ScriptResult sr4 = currentPage.executeJavaScript(s2);
//            webClient.waitForBackgroundJavaScript(10000);
//            System.out.println("Set inner height to 600k");

//            for (int i = 0;i<5;i++) {
//                webClient.getCurrentWindow().setInnerHeight(Integer.MAX_VALUE);
//                HtmlButton moreButton = currentPage.querySelector("button.more_res");
//                moreButton.click();
//                System.out.println("Pressed more button.");
//                webClient.waitForBackgroundJavaScript(5000);
//            }
//            try {
//                for (int i = 0; i < 5; i++) {
//                    HtmlButtonInput moreButton = currentPage.querySelector("input.mye4qd");
//                    moreButton.click();
//                    System.out.println("Pressed more button");
//                    webClient.waitForBackgroundJavaScript(10000);
//                    webClient.getCurrentWindow().setInnerHeight(Integer.MAX_VALUE);
//                    String s = "window.scrollY=5001;window.onscroll();";
//                    ScriptResult sr3 = currentPage.executeJavaScript(s);
////                    currentPage = (HtmlPage) sr3.getJavaScriptResult();
////                    currentPage = currentPage.
//                    ScriptResult sr2 = currentPage.executeJavaScript("window.scrollBy(0,60000)");
//                    webClient.waitForBackgroundJavaScript(10000);
//                }
//            }
//            catch (Exception e) {
//                System.out.println("Done scrolling.");
//            }
//            final DomNodeList lis = currentPage.querySelectorAll("li.ld");
//            for (Object li : lis) {
//                DomNode domLi = (DomNode)li;
//                final HtmlImage img = (HtmlImage)domLi.querySelector("img");
//                File folderFile = new File(folder);
//                File makeDir = new File(folder + "\\" + v.getMake());
//                File modelDir = new File(folder + "\\" + v.getMake() + "\\" + v.getModel());
//                if (!makeDir.exists()) {
//                    makeDir.mkdir();
//                }
//                if (!modelDir.exists()) {
//                    modelDir.mkdir();
//                }
//                img.saveAs(modelDir);
//            }
//            final DomNodeList images = currentPage.querySelectorAll("img.rg_i");
//            System.out.println("images.size() : " + images.size());
//            final List<HtmlImage> imageList = new ArrayList<HtmlImage>();
//            for (Object image : images) {
//                HtmlImage htmlImage = (HtmlImage)image;
//                imageList.add(htmlImage);
//            }
//            final List<?> images = currentPage.getByXPath("//img");
//            int numThreads = 10;
//            ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
//            for (int i = 0;i<images.size();i+=10) {
//
//                    for (int j = i; j < i+10 && j < images.size(); j++) {
//                        try {
//                            final HtmlImage img = (HtmlImage) imageList.get(j);
//                            final int countSavedFinal = countSaved;
//                            final Vehicle vFinal = v;
//                            executorService.execute(new Runnable() {
//                                public void run() {
//                                    try {
//                                        HtmlImage image = (HtmlImage) img;
//                                        String iUrl = image.getAttribute("data-src");
//                                        System.out.println(iUrl);
//                                        saveImage(vFinal, iUrl, countSavedFinal);
//                                    } catch (Exception eee) {
//                                        System.out.println("exception1.");
//                                    }
//                                }
//                            });
//                            countSaved++;
//
//                        } catch (Exception e) {
////                    e.printStackTrace();
//                            System.out.println("Exception2");
//                        }
//                    }
//            }
//            executorService.shutdown();


//                    Document doc = Jsoup.connect(googleUrl).get();
//            System.out.println("Loaded google search results.");
//            Elements targetImgs = doc.select("img.rg_i");
//
//            System.out.println("Images found: " + targetImgs.size());
//            for (Element e : targetImgs) {
//                try {
//                    if (e.hasAttr("data-src")) {
//                        String imageUrl = e.attr("data-src");
//                        System.out.println(imageUrl);
//                        System.out.println("Found image for Vehicle " + v + "... saving image...");
//                        saveImage(v, imageUrl, countSaved);
//                        countSaved++;
//                    }
//                } catch(Exception e2) {
//                    e2.printStackTrace();
//                    System.err.println("Proceeding with scraping activities...");
//                }
//            }
//        } catch(IOException ie) {
//            System.out.println("Exception3");
////            ie.printStackTrace();
//        }
    }

    private void wait(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch(InterruptedException ie) {
            ie.printStackTrace();
        }
    }


    private void saveImage(Vehicle v, String imageUrl, Integer countSaved) {
        Image image = null;
        try {
            URL url = new URL(imageUrl);
            image = ImageIO.read(url);
            File folderFile = new File(folder);
            File makeDir = new File(folder + "\\" + v.getMake());
            File modelDir = new File(folder + "\\" + v.getMake() + "\\" + v.getModel());
            if (!makeDir.exists()) {
                makeDir.mkdir();
            }
            if (!modelDir.exists()) {
                modelDir.mkdir();
            }
            FileOutputStream fos = new FileOutputStream(folder + "\\" + v.getMake() + "\\" + v.getModel() + "\\" + countSaved + ".jpg");
            ImageIO.write((RenderedImage) image, "jpeg", fos);
        } catch (IOException e) {
//            e.printStackTrace();
        }
        wait(3);
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
