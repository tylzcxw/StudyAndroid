package tylz.study.studythree.retrofit2.other;

import framework.utils.LogUtils;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/11/13
 *  @描述：    TODO
 */
public class Translation {
    private String status;

    private Content content;
    private class Content {
        private String from;
        private String to;
        private String vendor;
        private String out;
        private String errNo;

        @Override
        public String toString() {
            return "Content{" +
                    "from='" + from + '\'' +
                    ", to='" + to + '\'' +
                    ", vendor='" + vendor + '\'' +
                    ", out='" + out + '\'' +
                    ", errNo='" + errNo + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Translation{" +
                "status='" + status + '\'' +
                ", content=" + content +
                '}';
    }

    public void show(){
        LogUtils.debug(status);
        LogUtils.debug(content.from);
        LogUtils.debug(content.to);
        LogUtils.debug(content.vendor);
        LogUtils.debug(content.out);
        LogUtils.debug(content.errNo);
        LogUtils.debug(toString());
    }
}
