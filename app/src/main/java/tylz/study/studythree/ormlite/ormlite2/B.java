package tylz.study.studythree.ormlite.ormlite2;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/10/9
 *  @描述：    表b删除字段age
 */
@DatabaseTable(tableName = "tb_b")
public class B {
    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField
    public String name;
    @DatabaseField
    public String age;
    @DatabaseField
    public String sex;
    @Override
    public String toString() {
        return "B{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
