package club.smartsheep.panelcraftcore.DAO.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("activities")
public class ActivitiesEntity {

    @TableId(type = IdType.AUTO)
    private long id;

    @TableField("content")
    private String content;

    @TableField("create_time")
    private Date createTime;
}
