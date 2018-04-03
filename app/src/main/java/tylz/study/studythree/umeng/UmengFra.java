package tylz.study.studythree.umeng;

import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.AdapterView;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;
import java.util.List;

import framework.simplecomm.ListViewFragment;
import framework.utils.LogUtils;
import tylz.study.R;

/**
 * @author cxw
 * @date 2018/3/28
 * @des TODO
 */

public class UmengFra extends ListViewFragment {
    public UmengFra(){
        initTitleBar("友盟");
    }
    @Override
    protected List<String> onCreateDataSource() {
        List<String> data = new ArrayList<>();
        data.add("友盟分享");
        data.add("友盟");
        return data;
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        super.onItemClick(parent, view, position, id);
        switch (position) {
            case 0:
                umengShare();
                break;
            case 1:
                startAcitvity(TestLFra.class);
                break;
            default:
                break;
        }
    }



    /**
     * 友盟分享
     */
    private void umengShare() {
        ShareAction shareAction = new ShareAction(getActivity());
        UMWeb umWeb = new UMWeb("http://www.baidu.com");
        umWeb.setTitle("title");
        umWeb.setDescription("描述");
        umWeb.setThumb(new UMImage(getActivity(), BitmapFactory.decodeResource(getResources(), R.mipmap.ic_home)));
        shareAction.withMedia(umWeb);
        shareAction.withText("分享测试：这里是Text")
                    .withSubject("分享测试：这里是Subject")
                    .withFollow("分享测试：这里是Follow")
                    .withExtra(new UMImage(getActivity(),BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher)))
                    .setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
                    .setCallback(new UMShareListener() {
                        /**
                         * @descrption 分享开始的回调
                         * @param share_media 平台类型
                         */
                        @Override
                        public void onStart(final SHARE_MEDIA share_media) {
                            LogUtils.debug("share onStart " + share_media.getName());
                        }
                        /**
                         * @descrption 分享成功的回调
                         * @param share_media 平台类型
                         */
                        @Override
                        public void onResult(final SHARE_MEDIA share_media) {
                            LogUtils.debug("share onResult " + share_media.getName());
                        }
                        /**
                         * @descrption 分享失败的回调
                         * @param share_media 平台类型
                         * @param throwable 错误原因
                         */
                        @Override
                        public void onError(final SHARE_MEDIA share_media, final Throwable throwable) {
                            LogUtils.debug("share onError " + share_media.getName() + " error = " + throwable.getLocalizedMessage());
                        }
                        /**
                         * @descrption 分享取消的回调
                         * @param share_media 平台类型
                         */
                        @Override
                        public void onCancel(final SHARE_MEDIA share_media) {
                            LogUtils.debug("share onCancel " + share_media.getName());
                        }
                    });
        shareAction.open();
    }
}
