package club.smartsheep.panelcraftcore.Common.Configure;

import club.smartsheep.panelcraftcore.PanelCraft;
import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;
import lombok.SneakyThrows;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MybatisPlusConfigure {

    public static SqlSessionFactory SESSION_FACTORY = null;

    /**
     * This function will summon / reset the mybatis plus configure
     * Default configure will connect to the local sqlite for development and test
     * If you want, you can change the plugin config.yml database config
     *
     * Now Support InnerDatabase(SQLite), SQLite, MySQL
     * TODO Support custom driver
     */
    @SneakyThrows
    public static void saveDefaultMybatisPlusConfigure() {

        /*
         * How to create the default configure?
         *  1. Check the config is exits?
         *  2. Read the default configure from the resource
         *  3. Write it! and reboot mybatis plus
         */

        File ORMConfigureFile = new File(PanelCraft.getPlugin(PanelCraft.class).getDataFolder(), "mybatisplus.cfg.xml");
        if (!ORMConfigureFile.exists()) {
            PanelCraft.LOGGER.info("Couldn't found mybatisplus.cfg.xml use default setting and summon it");

            ORMConfigureFile.getParentFile().mkdirs();
            PanelCraft.getPlugin(PanelCraft.class).saveResource("mybatisplus.cfg.xml", false);

            // Load and write it
            String originConfigure = new String(
                    PanelCraft.class.getClassLoader().getResourceAsStream("examples/mybatisplus.cfg.xml").readAllBytes(),
                    StandardCharsets.UTF_8
            );

            FileWriter originWriter = new FileWriter(ORMConfigureFile);
            originWriter.write(originConfigure);
            originWriter.close();
        }
    }

    @SneakyThrows
    public static void setupMybatisPlus(boolean innerDatabase) {
        InputStream configure;

        if(innerDatabase) {
            configure = Resources.getResourceAsStream("mybatisplus.cfg.xml");
        } else {
            saveDefaultMybatisPlusConfigure();
            configure = new FileInputStream(new File(PanelCraft.getPlugin(PanelCraft.class).getDataFolder(), "mybatisplus.cfg.xml"));
        }

        MybatisSqlSessionFactoryBuilder mybatisSessionFactoryBuilder = new MybatisSqlSessionFactoryBuilder();
        SESSION_FACTORY = mybatisSessionFactoryBuilder.build(configure);

        PanelCraft.LOGGER.info("Successfully connect to the database and start MybatisPlus!");
    }
}
