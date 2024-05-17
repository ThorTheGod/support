package com.xidian.support.util;

import com.xidian.support.pojo.Plan;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author thornoir
 * @date 2022/3/18
 * @apiNote
 */
public class WordUtil {

        public static void genWord(Plan plan){
            try {
                Map<String,String> dataMap = new HashMap<>();
                dataMap.put("planId", plan.getPlanId()+"");
                dataMap.put("planProvince", plan.getPlanProvince());
                dataMap.put("planCity", plan.getPlanCity());
                dataMap.put("planTime", plan.getPlanTime()+"");
                dataMap.put("temp1", plan.getTemp1()+"");
                dataMap.put("temp2", plan.getTemp2()+"");
                dataMap.put("humidity1", plan.getHumidity1()+"");
                dataMap.put("humidity2", plan.getHumidity2()+"");
                dataMap.put("no2Content", plan.getNo2Content()+"");
                dataMap.put("weather", plan.getWeather());
                dataMap.put("newCases", plan.getNewCases()+"");
                dataMap.put("confirmedCases", plan.getConfirmedCases()+"");
                dataMap.put("closeContact", plan.getCloseContact()+"");
                dataMap.put("asymptomatic", plan.getAsymptomatic()+"");
                dataMap.put("medical", plan.getMedical());
                dataMap.put("transport", plan.getTransport());
                dataMap.put("livelihood", plan.getLivelihood());
                dataMap.put("epidemic", plan.getEpidemic());
                dataMap.put("epidemicScore", plan.getEpidemicScore()+"");
                dataMap.put("economic", plan.getEconomic());
                dataMap.put("economicScore", plan.getEconomicScore()+"");
                dataMap.put("social", plan.getSocial());
                dataMap.put("socialScore", plan.getSocialScore()+"");

                Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
                configuration.setDefaultEncoding("utf-8");
                //.xml 模板文件所在目录
                configuration.setDirectoryForTemplateLoading(new File(System.getProperty("user.dir") +
                        "/output"));
                // 输出文档路径及名称
                File outFile = new File(System.getProperty("user.dir") + "/output/" +
                        "plan.doc");
                //以utf-8的编码读取模板文件
                Template t =  configuration.getTemplate("template.xml","utf-8");
                Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"),10240);
                t.process(dataMap, out);
                out.close();
                System.out.println("生成成功");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("生成失败");
            }
        }

}
