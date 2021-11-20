package club.smartsheep.panelcraftcore.DAO.Mappers;

import club.smartsheep.panelcraftcore.DAO.Entity.ActivitiesEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

@Mapper
public interface ActivitiesMapper extends BaseMapper<ActivitiesEntity> {

    @Select("SELECT * FROM Accounts WHERE create_time=#{createTime}")
    ActivitiesEntity selectByCreateTime(Date createTime);
}
