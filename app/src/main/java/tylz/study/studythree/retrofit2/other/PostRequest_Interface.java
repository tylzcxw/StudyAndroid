package tylz.study.studythree.retrofit2.other;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/11/13
 *  @描述：    TODO
 */
public interface PostRequest_Interface {
    /**
     * 采用@Post表示Post方法进行请求(传入部分url地址)
     * 采用@FormUrlEncoded注解的原因：API规定采用请求格式x-www-form-urlencoded,即表单形式
     * 需要配合@Field像服务器提交需要的字段
     */
    @POST("translate?doctype=json&jsonversion=&type=&keyfrom=&model=&mid=&imei=&vendor=&screen=&ssid=&network=&abtest=")
    @FormUrlEncoded
    Call<Translation1> getCall(@Field("i") String targetSentence);
    @POST("http://www.lejurobot.com/client/interface.php?func=fullLogin")
    @FormUrlEncoded
    Call<LejuRobotLoginResponse> getCall(@Field("username") String username,@Field("password") String password,@Field("platform") String platform);
    @POST("http://www.lejurobot.com/client/interface.php?func=fullLogin")
    @FormUrlEncoded
    Call<String> getLoginStringCall(@Field("username") String username,@Field("password") String password,@Field("platform") String platform);
}
