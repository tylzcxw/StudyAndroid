package tylz.study.studythree.okhttp;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.protocol.LoginRobotRequest;
import com.example.protocol.LoginRobotResponse;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import framework.app.BaseFragment;
import framework.async.ThreadManager;
import framework.config.HttpUrl;
import framework.transaction2.RunUiThreadResultListener2;
import framework.transaction2.TransactionRequest2;
import framework.ui.LoadingDialog;
import framework.utils.LogUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tylz.study.R;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/9/22
 *  @描述：    TODO
 */
public class OkHttp1Fra extends BaseFragment implements View.OnClickListener {
    private String mUrl = "http://www.baidu.com";
    private Button   mBtnSimpleGet;
    private Button   mBtnSimpleSyncGet;
    private Button   mBtnSimplePost;
    private Button   mBtnSimpleSyncPost;
    private Button   mBtnLogin;
    private Button   mBtnLogin1;
    private TextView mTvResponseContent;

    public OkHttp1Fra() {
        initTitleBar("OkHttp学习", true, false);
    }

    @Override
    protected int onCreateRootView() {
        return R.layout.fra_okhttp_study;
    }

    @Override
    protected void initView() {
        super.initView();
        mBtnSimpleGet = findViewById(R.id.btn_get_okhttp);
        mBtnSimpleSyncGet = findViewById(R.id.btn_get_sync_okhttp);
        mBtnSimplePost = findViewById(R.id.btn_post_okhttp);
        mBtnSimpleSyncPost = findViewById(R.id.btn_post_sync_okhttp);
        mTvResponseContent = findViewById(R.id.tv_content);
        mBtnLogin = findViewById(R.id.btn_login);
        mBtnLogin1 = findViewById(R.id.btn_login1);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBtnSimpleGet.setOnClickListener(this);
        mBtnSimpleSyncGet.setOnClickListener(this);
        mBtnSimplePost.setOnClickListener(this);
        mBtnSimpleSyncPost.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mBtnLogin1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnSimpleGet) {
            sendSimpleGet();
        } else if (v == mBtnSimpleSyncGet) {
            sendSimpleSyncGet();
        } else if (v == mBtnSimplePost) {
            sendSimplePost();
        } else if (v == mBtnSimpleSyncPost) {
            sendSimpleSyncPost();
        } else if (v == mBtnLogin) {
            doLogin();
        }else if(v == mBtnLogin1){
            doLogin1();
        }
    }

    private void doLogin1() {
        Map<String,String> params = new HashMap<>();
        params.put("username","13652304622");
        params.put("password","123456");
        LoginRobotRequest request = new LoginRobotRequest();
        request.setProtocolName("fullLogin");
        request.setParams(params);
        request.setMethod(TransactionRequest2.Method.POST);
        request.setParseJavaBean(false);
        request.send(new RunUiThreadResultListener2<LoginRobotResponse>(getActivity()) {

            @Override
            protected void onResult(String result) {
                super.onResult(result);
                mTvResponseContent.setText(result.toString());
            }
        });
    }

    private void doLogin() {
        //id = 99222817
        String           username = "13652304622";
        String           password = "123456";
        OkHttpClient     client   = new OkHttpClient();
        FormBody.Builder body     = new FormBody.Builder();
        body.add("username", username);
        body.add("password", password);
        body.add("platform", "android");
        LoadingDialog.getInstance().showLoading();
        final Request request = new Request.Builder().url(HttpUrl.BASE + "fullLogin")
                                                     .post(body.build())
                                                     .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    showResponse(response);
                }
            }
        });
    }

    private void sendSimpleSyncPost() {
        LoadingDialog.getInstance().showLoading();
        ThreadManager.getNormalPool().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient     client   = new OkHttpClient();
                    FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
                    formBody.add("username", "zhangsan");//传递键值对参数

                    Request request = new Request.Builder().url(mUrl)
                                                           .post(formBody.build())
                                                           .build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        showResponse(response);
                    } else {
                        showResponse(null);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void sendSimplePost() {
        LoadingDialog.getInstance().showLoading();
        OkHttpClient     client   = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("username", "zhangsan");//传递键值对参数

        Request request = new Request.Builder().url(mUrl).post(formBody.build()) //传递请求体
                                               .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                showResponse(response);
            }
        });
        // TODO: 2017/9/22  使用RequestBody传递Json或File对象 示例
        MediaType JSON    = MediaType.parse("application/json;charset=utf-8");
        String    jsonStr = "{\"username\":\"lisi\",\"nickname\":\"李四\"}";//json数据.
        RequestBody.create(JSON, jsonStr);

        MediaType   fileType = MediaType.parse("File/*");
        File        file     = new File("fileName");//file对象
        RequestBody body     = RequestBody.create(fileType, file);
        // TODO: 2017/9/22  使用MultipartBody同时传递键值对参数和File对象
        MultipartBody multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                                                 .addFormDataPart("key1", "value1")
                                                                 .addFormDataPart("key2", "value2")
                                                                 .addFormDataPart("key3", file
                                                                         .getName(), body)
                                                                 .build();
    }

    /**
     * 发送异步get请求
     * 不用再次开启子线程，但回调方法是执行在子线程中，所以在更新UI时还要跳转到UI线程中。
     */
    private void sendSimpleGet() {
        LoadingDialog.getInstance().showLoading();
        OkHttpClient client  = new OkHttpClient();
        Request      request = new Request.Builder().url(mUrl).build();
        /**
         * 1，回调接口的onFailure方法和onResponse执行在子线程。
         2，response.body().string()方法也必须放在子线程中。当执行这行代码得到结果后，再跳转到UI线程修改UI。
         */
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                showResponse(response);
            }
        });
    }

    /**
     * 发送同步get请求
     */
    private void sendSimpleSyncGet() {
        LoadingDialog.getInstance().showLoading();
        ThreadManager.getNormalPool().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //创建okhttpclient对象
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(mUrl) //请求接口，如果需要传参凭借到接口后面
                                                           .build();//创建Request对象
                    Response response = null;
                    response = client.newCall(request).execute();//得到Response对象
                    if (response.isSuccessful()) {
                        showResponse(response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showResponse(Response response) throws IOException {
        StringBuffer sb = new StringBuffer();
        if (response == null) {
            setContent("fial send request");
        } else {
            sb.append("response.code() = " + response.code() + "\n");
            sb.append("response.message() = " + response.message() + "\n");
            String content = response.body().string();
            sb.append("respnseContent = " + content + "\n");
            LogUtils.debug("response.code() = " + response.code());
            LogUtils.debug("response.message() = " + response.message());
            LogUtils.debug("respnseContent = " + content);
            setContent(sb.toString());
        }

    }

    private void setContent(final String content) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoadingDialog.getInstance().dismissLoading();
                mTvResponseContent.setText(content);
            }
        });
    }
}
