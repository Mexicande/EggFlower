package star.jiuji.egg_flower.armour.net;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.Map;

import star.jiuji.egg_flower.R;
import star.jiuji.egg_flower.base.App;


/**
 * Created by apple on 2018/4/13.
 */

public class ApiService {
    /**
     * @param params
     * @param listener
     * banner
     */
    public static void GET_SERVICE(String url, Map<String,String> params, final OnRequestDataListener listener) {
        newExcuteJsonPost(url,params,listener);
    }

    private static void newExcuteJsonPost(String url, Map<String,String> params, final OnRequestDataListener listener){
        final String netError = App.getApplication().getString(R.string.net_error);
        OkGo.<String>post(url)
                .tag(App.getApplication())
                .params(params,false)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if(response.body()!=null && !TextUtils.isEmpty(response.body())){
                            try {
                                JSONObject jsonObject= JSON.parseObject(response.body());
                                int code = jsonObject.getIntValue("error_code");
                                if(code==0){
                                    listener.requestSuccess(code, jsonObject);
                                }else {
                                    listener.requestFailure(code, jsonObject.getString("error_message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            listener.requestFailure(-1, netError);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        listener.requestFailure(-1, netError);

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        listener.onFinish();
                    }
                });


    }
    public static void apply(String productId, String token) {
        OkGo.<String>post(Api.APPLY)
                .params("id", productId)
                .params("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                    }
                });
    }

}
