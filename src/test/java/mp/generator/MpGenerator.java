package mp.generator;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ly_demo中的代码生成，是内网资源库上的jar包，对mybatis-plus进行了封装、整合，可以生成详细的Controller代码，原生的是不能生成的
 * 目前项目中的pom比较乱，无法直接引入。
 */
public class MpGenerator {

    public static void main(String[] args) {

//        String[] tablePrefix = new String[]{"ybg_"};
        String[] tablePrefix = new String[]{};
        String[] includeTables = new String[]{"user"};//需要生成的表
        String[] excludeTables = new String[]{};//排除生成的表

        String moduleName = "user";//模块名
        String author = "shizy";
        final String outputDir = System.getProperty("user.dir") + "\\src\\main\\java";//在项目内生成

        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(outputDir);
        gc.setFileOverride(true);//是否覆盖已有文件
        gc.setActiveRecord(false);// 不需要ActiveRecord特性的请改为false
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(true);// XML columList
        // .setKotlin(true) 是否生成 kotlin 代码
        gc.setAuthor(author);

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
//        gc.setMapperName(className+"Mapper");
//        gc.setXmlName(className+"Mapper");
        gc.setServiceName("%sService");//业务层命名
//        gc.setServiceImplName(className+"ServiceImpl");
//        gc.setControllerName(className+"Controller");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
//        dsc.setDbType(DbType.ORACLE);
        dsc.setDbType(DbType.MYSQL);

        /*dsc.setTypeConvert(new OracleTypeConvert(){//重写转换规则时需保证数据库类型一致
            // 自定义数据库表字段类型转换【可选】
            @Override
            public DbColumnType processTypeConvert(String fieldType) {
                System.out.println("转换类型：" + fieldType);
                // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
                String t = fieldType.toUpperCase();
                if (t.contains("NUMBER")) {
                    return DbColumnType.BIG_DECIMAL;
                }
                if (t.contains("CHAR(1)")) {
                    return DbColumnType.INTEGER;
                }
                return super.processTypeConvert(fieldType);
            }
        });*/

//        dsc.setDriverName("oracle.jdbc.OracleDriver");
//        dsc.setUsername("ly_cloud_xg_zhcp");
//        dsc.setPassword("ly_cloud_xg_zhcp");
//        dsc.setUrl("jdbc:oracle:thin:@192.168.6.217:1521:orcl");

        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        dsc.setUrl("jdbc:mysql://127.0.0.1:3306/test");

        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        strategy.setTablePrefix(tablePrefix);// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        //strategy.setNaming(NamingStrategy.database_annotation);// 表名生成策略
        strategy.setRestControllerStyle(true);
        strategy.setInclude(includeTables); // 需要生成的表
        strategy.setExclude(excludeTables); // 排除生成的表
        // 自定义实体父类
        // strategy.setSuperEntityClass("com.baomidou.demo.TestEntity");
        // 自定义实体，公共字段
        // strategy.setSuperEntityColumns(new String[] { "test_id", "age" });
        // 自定义 mapper 父类
        // strategy.setSuperMapperClass("com.baomidou.demo.TestMapper");
        // 自定义 service 父类
        // strategy.setSuperServiceClass("com.baomidou.demo.TestService");
        // 自定义 service 实现类父类
        // strategy.setSuperServiceImplClass("com.baomidou.demo.TestServiceImpl");
        // 自定义 controller 父类
        // strategy.setSuperControllerClass("com.baomidou.demo.TestController");
        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        // strategy.setEntityColumnConstant(true);
        // 【实体】是否为构建者模型（默认 false）
        // public UserPo setName(String name) {this.name = name; return this;}
        // strategy.setEntityBuilderModel(true);
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.gentest");
        pc.setModuleName(moduleName);
        pc.setEntity("entity");//实体生成包名，注意与配置文件中的别名路径相同
        pc.setController("controller");
        mpg.setPackageInfo(pc);

        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);
            }
        };

        // 自定义 xxList.jsp 生成
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
//        focList.add(new FileOutConfig("/template/list.jsp.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                // 自定义输入文件名称
//                return "D://my_" + tableInfo.getEntityName() + ".jsp";
//            }
//        });
//        cfg.setFileOutConfigList(focList);
//        mpg.setCfg(cfg);

        // 调整 xml 生成目录演示
        focList.add(new FileOutConfig("/mp/generator/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return outputDir.replace("java", "resources") + "\\mapper\\" + tableInfo.getEntityName() + "Mapper.xml";
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 关闭默认 xml 生成，调整生成 至 根目录
        TemplateConfig tc = new TemplateConfig();
        tc.setXml(null);
        mpg.setTemplate(tc);

        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/templates 下面内容修改，
        // 放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也可以自定义模板名称
        // TemplateConfig tc = new TemplateConfig();
        // tc.setController("...");
        // tc.setEntity("...");
        // tc.setMapper("...");
        // tc.setXml("...");
        // tc.setService("...");
        // tc.setServiceImpl("...");
        // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
        // mpg.setTemplate(tc);

        // 执行生成
        mpg.execute();

        // 打印注入设置【可无】
        System.err.println(mpg.getCfg().getMap().get("abc"));
    }
}