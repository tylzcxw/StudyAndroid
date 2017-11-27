package tylz.study.studythree.ormlite.ormlite2;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/10/9
 *  @描述：    数据库版本3时增加age字段
 *  @描述：    数据库版本4时增加other字段
 *  @描述：    数据库版本5时otthe时删除字段
 */
@DatabaseTable(tableName = "tb_c")
public class C {
    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField
    public String name;
    @DatabaseField
    public String age;


    @Override
    public String toString() {
        return "C{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
