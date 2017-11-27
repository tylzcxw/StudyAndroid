package tylz.study.studythree.ormlite.ormlite1;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/9/29
 *  @描述：    TODO
 */
@DatabaseTable(tableName = "tb_person")
public class PersonBean {
    @DatabaseField(generatedId = true)
    private int    id;
    @DatabaseField(columnName = "person_name")
    private String name;
    @DatabaseField(columnName = "person_desc")
    private String desc;
    public PersonBean() {
    }

    public PersonBean(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "PersonBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
