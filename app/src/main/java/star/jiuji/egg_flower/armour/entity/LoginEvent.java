package star.jiuji.egg_flower.armour.entity;

import java.io.Serializable;

/**
 * Created by apple on 2018/8/15.
 */

public class LoginEvent implements Serializable{
    public final String msg;
    public LoginEvent(String phone) {
        this.msg = phone;
    }
}
