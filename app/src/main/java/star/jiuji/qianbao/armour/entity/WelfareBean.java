package star.jiuji.qianbao.armour.entity;

import java.io.Serializable;

/**
 * Created by apple on 2017/7/16.
 */

public class WelfareBean implements Serializable {


    private int id;
    private String link;
    private String image;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
