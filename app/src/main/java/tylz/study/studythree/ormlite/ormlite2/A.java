package tylz.study.studythree.ormlite.ormlite2;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/10/9
 *  @描述：    数据库升级，表a增加age字段
 */
@DatabaseTable(tableName = "tb_a")
public class A {
    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField
    public String name;
    @DatabaseField
    public String age;

    @Override
    public String toString() {
        return "A{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
