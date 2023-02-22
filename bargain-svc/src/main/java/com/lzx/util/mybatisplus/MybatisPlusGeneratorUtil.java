package com.lzx.util.mybatisplus;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


/**
 * mybatis +发电机跑龙套
 *
 * @author Bobby.zx.lin
 * @date 2023/02/22
 */
@Getter
@Setter
public class MybatisPlusGeneratorUtil {
    /**
     * 代码备注创建人
     */
    private String author;
    /**
     * 包路径
     */
    private String packageName;
    /**
     * 数据库地址
     */
    private String DBUrl;
    /**
     * 数据库用户名
     */
    private String DBUserName;
    /**
     * 数据库密码
     */
    private String DBPwd;

    /**
     * 是否开启Swagger
     */
    private Boolean enableSwagger;


    public MybatisPlusGeneratorUtil(String author, String packageName, String DBUrl, String DBUserName, String DBPwd) {
        this.author = author;
        this.packageName = packageName;
        this.DBUrl = DBUrl;
        this.DBUserName = DBUserName;
        this.DBPwd = DBPwd;
        this.setEnableSwagger(true);
    }

    /**
     * 生成代码
     *
     * @param tableNames
     * @return
     */
    public void startGenerator(String... tableNames) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        // 设置用户名
        gc.setAuthor(author);
        //生成完毕后是否打开文件夹
        gc.setOpen(false);
        // service 命名方式
        gc.setServiceName("%sService");
        // service impl 命名方式
        gc.setServiceImplName("%sServiceImpl");
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setFileOverride(true);
        gc.setActiveRecord(true);
        // XML 二级缓存
        gc.setEnableCache(false);
        // XML ResultMap
        gc.setBaseResultMap(true);
        // XML columList
        gc.setBaseColumnList(false);
        //时间类型对应策略
        gc.setDateType(DateType.TIME_PACK);
        //是否启用Swagger注解
        gc.setSwagger2(Boolean.TRUE.equals(enableSwagger));

        mpg.setGlobalConfig(gc);

        //  数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(DBUrl + "?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=UTC");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername(DBUserName);
        dsc.setPassword(DBPwd);
        mpg.setDataSource(dsc);

        //  包配置
        PackageConfig pc = new PackageConfig();
        //pc.setModuleName(scanner("模块名"));
        pc.setParent(packageName);
        pc.setEntity("entity");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        mpg.setPackageInfo(pc);

        // 自定义需要填充的字段
        List<TableFill> tableFillList = new ArrayList<>();
        //如 每张表都有一个创建时间、修改时间
        //而且这基本上就是通用的了，新增时，创建时间和修改时间同时修改
        //修改时，修改时间会修改，
        //虽然像Mysql数据库有自动更新几只，但像ORACLE的数据库就没有了，
        //使用公共字段填充功能，就可以实现，自动按场景更新了。
        //如下是配置
        //TableFill createField = new TableFill("gmt_create", FieldFill.INSERT);
        //TableFill modifiedField = new TableFill("gmt_modified", FieldFill.INSERT_UPDATE);
        //tableFillList.add(createField);
        //tableFillList.add(modifiedField);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return projectPath + "/src/main/resources/mapper/" + "/"
                        + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        mpg.setTemplate(new TemplateConfig().setXml(null));

        StrategyConfig strategy = getStrategyConfig(tableNames);
        mpg.setStrategy(strategy);
        // 选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
        System.out.println("代码生成完毕");
    }

    /**
     * 表策略
     *
     * @param tableNames
     * @return
     */
    public static StrategyConfig getStrategyConfig(String... tableNames) {
        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //@Data
        strategy.setEntityLombokModel(true);
        //生成getset
        strategy.setEntityBuilderModel(false);
        // 设置逻辑删除键
        strategy.setLogicDeleteFieldName("deleted");
        //   strategy.setSuperEntityColumns("id");
        // 指定生成的bean的数据库表名
        strategy.setInclude(tableNames);
        // 驼峰转连字符
        strategy.setControllerMappingHyphenStyle(true);
        // 去前缀
        strategy.setFieldPrefix("t_");
        return strategy;
    }

}
