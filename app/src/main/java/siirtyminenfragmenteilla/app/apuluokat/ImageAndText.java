package siirtyminenfragmenteilla.app.apuluokat;

/**
 * Created by k1101374 on 2.10.2014.
 */
public class ImageAndText {
    private String imagePath;
    private String text;

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ImageAndText() {

    }

    public ImageAndText(String imagePath, String text) {
        this.imagePath = imagePath;
        this.text = text;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getText() {
        return text;
    }

}
