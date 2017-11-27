package tylz.study.studythree.retrofit2;

import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import framework.app.BaseFragment;
import framework.utils.LogUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import tylz.study.R;
import tylz.study.studythree.retrofit2.other.GetRequest_Interface;
import tylz.study.studythree.retrofit2.other.PostRequest_Interface;
import tylz.study.studythree.retrofit2.other.Translation;
import tylz.study.studythree.retrofit2.other.Translation1;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/11/13
 *  @描述：    TODO
 */
public class Retrofit2Fra extends BaseFragment {
    @BindView(R.id.btn_request1)
    Button mBtnRequest1;
    @BindView(R.id.btn_request2)
    Button mBtnRequest2;
    @BindView(R.id.tv_response)
    TextView mTvResponse;
    @BindView(R.id.btn_request3)
    Button mBtnRequest3;
    @BindView(R.id.btn_request4)
    Button mBtnRequest4;

    public Retrofit2Fra() {
        initTitleBar("Retrofit2学习运用");
    }

    @Override
    protected int onCreateRootView() {
        return R.layout.fra_retrofit2;
    }


    @OnClick(R.id.btn_request1)
    public void onViewClicked() {
        // 创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fy.iciba.com/")
                .addConverterFactory(GsonConverterFactory.create())//设置Gson解析
                .build();
        //创建 网络请求接口 的实例
        GetRequest_Interface request = retrofit.create(GetRequest_Interface.class);
        //对发送请求进行封装
        Call<Translation> call = request.getCall();
        //发送网络请求(异步)
        call.enqueue(new Callback<Translation>() {
            //请求成功时回调
            @Override
            public void onResponse(final Call<Translation> call, final Response<Translation> response) {
                //处理返回的数据结果
                response.body().show();
                mTvResponse.setText(response.body().toString());
            }

            @Override
            public void onFailure(final Call<Translation> call, final Throwable throwable) {
                LogUtils.debug("连接失败" + throwable.getMessage());
            }
        });
    }

    @OnClick(R.id.btn_request2)
    public void onView2Clicked() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fanyi.youdao.com/")
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                //增加返回值为Oservable<T>的支持
                //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        PostRequest_Interface request = retrofit.create(PostRequest_Interface.class);
        Call<Translation1> call = request.getCall("I love you");
        call.enqueue(new Callback<Translation1>() {
            @Override
            public void onResponse(final Call<Translation1> call, final Response<Translation1> response) {
                LogUtils.debug(response.body().getTranslateResult().get(0).get(0).getTgt());
                mTvResponse.setText(response.body().getTranslateResult().get(0).get(0).getTgt());
            }

            @Override
            public void onFailure(final Call<Translation1> call, final Throwable throwable) {
                LogUtils.debug("连接失败" + throwable.getMessage());
            }
        });
    }


    @OnClick(R.id.btn_request3)
    public void onBtnRequest3Clicked() {
        Retrofit retrofit = new Retrofit.Builder()
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())

                .baseUrl("http://www.lejurobot.com/")
                .build();
        PostRequest_Interface request = retrofit.create(PostRequest_Interface.class);

        Call<String> requestCall = request.getLoginStringCall("13652304622", "123456", "android");
        requestCall.enqueue(new Callback<String>(){

            @Override
            public void onResponse(final Call<String> call, final Response<String> response) {
                LogUtils.debug(response.toString());
                mTvResponse.setText(response.body().toString());
            }

            @Override
            public void onFailure(final Call<String> call, final Throwable throwable) {
                LogUtils.debug("连接失败" + throwable.getMessage());
            }
        });

    }

    @OnClick(R.id.btn_request4)
    public void onBtnRequest4Clicked() {
        /*
            初始化一个Observable
         */
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<Integer> observableEmitter) throws Exception {
                observableEmitter.onNext(1);
                observableEmitter.onNext(2);
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.computation());
        /**
         * 初始化一个Observer
         */
        Observer<Integer> observer = new Observer<Integer>() {
            private Disposable mDisposable;
            @Override
            public void onSubscribe(@NonNull final Disposable disposable) {
                mDisposable = disposable;
            }

            @Override
            public void onNext(@NonNull final Integer integer) {
                LogUtils.debug(integer.toString());
                if(integer > 3){// >3 时为异常数据，接触订阅
                    mDisposable.dispose();
                }
            }

            @Override
            public void onError(@NonNull final Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        };
        //建立订阅关系
        observable.subscribe(observer );
    }
}
