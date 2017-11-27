package tylz.study.studythree.retrofit2.other;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/11/13
 *  @描述：    TODO
 */
public class LejuRobotLoginResponse {
    public String avatar;//	http://www.lejurobot.com/uploads/app/55820160806145737520.jpg
    public String id;//	99222775
    public String nickname;//	Devlnt
    public String phone;//	18067424555
    public String password;
    public String hobby;////爱好
    public String gender;// 性别
    public String birth;// 生日
    public String address;//地址
    public String qq;//qq
    public String mailbox;//邮箱
    public String selfInfo;//个人说明
    public String age;

    @Override
    public String toString() {
        return "LejuRobotLoginResponse{" +
                "avatar='" + avatar + '\'' +
                ", id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", hobby='" + hobby + '\'' +
                ", gender='" + gender + '\'' +
                ", birth='" + birth + '\'' +
                ", address='" + address + '\'' +
                ", qq='" + qq + '\'' +
                ", mailbox='" + mailbox + '\'' +
                ", selfInfo='" + selfInfo + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
