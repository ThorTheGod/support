package com.xidian.support.util;

import com.xidian.support.pojo.Plan;
import com.xidian.support.pojo.User;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author thornoir
 * @date 2022/1/23
 * @apiNote
 */
public class ExcelUtil {

    private static final String path = System.getProperty("user.dir") + "/output/sim.xls";
    /**
     * 计算属性相似度
     * @param data
     * @param newPlan
     */
    public static void export(List<Plan> data,Plan newPlan) {
        //获取对象的所有属性名
        String[] fieldNames = fieldName(Plan.class);
        HSSFWorkbook wb = new HSSFWorkbook();
        //计算每个案例各属性的相似度并写入第二个表格
        //1.预测原始目标案例的预测数据
        predictPlan(newPlan);
        System.out.println(newPlan);
        //2.将预测的案例数据加入方案list中用于计算
        data.add(newPlan);
        //创建相似度表
        HSSFSheet sim_sheet = wb.createSheet("sim");
        //获取温度，湿度...的最值
        double maxTemp=0.0,minTemp=0.0,
               maxHumid=0.0,minHumid=0.0,
               maxN02=0.0,minNO2=0.0,
               maxNew=0.0,minNew=0.0,
               maxConfirm=0.0,minConfirm=0.0,
               maxClose=0.0,minClose=0.0,
               maxAsymp=0.0,minAsymp=0.0;
        for (Plan plan:data
             ) {
            if(plan.getTemp2()>maxTemp){
                maxTemp = plan.getTemp2();
            }
            if(plan.getTemp1()<minTemp){
                minTemp = plan.getTemp1();
            }
            if(plan.getHumidity2()>maxHumid){
                maxHumid = (double)plan.getHumidity2()/100;
            }
            if(plan.getHumidity1()<minHumid){
                minHumid = (double)plan.getHumidity1()/100;
            }
            if(plan.getNo2Content()>maxN02){
                maxN02 = (double)plan.getNo2Content();
            }
            if(plan.getNo2Content()<minNO2){
                minNO2 = (double)plan.getNo2Content();
            }
            if(plan.getNewCases()<minNew){
                minNew = (double)plan.getNewCases();
            }
            if(plan.getNewCases()>maxNew){
                maxNew = (double)plan.getNewCases();
            }
            if(plan.getConfirmedCases()<minConfirm){
                minConfirm = (double)plan.getConfirmedCases();
            }
            if(plan.getConfirmedCases()>maxConfirm){
                maxConfirm = (double)plan.getConfirmedCases();
            }
            if(plan.getCloseContact()<minClose){
                minClose = (double)plan.getCloseContact();
            }
            if(plan.getCloseContact()>maxClose){
                maxClose = (double)plan.getCloseContact();
            }
            if(plan.getAsymptomatic()<minAsymp){
                minAsymp = (double)plan.getAsymptomatic();
            }
            if(plan.getAsymptomatic()>maxAsymp){
                maxAsymp = (double)plan.getAsymptomatic();
            }
        }
        //测试用
//        maxTemp = 37;
//        minTemp = 2;
//        maxHumid = 1.0;
//        minHumid = 0.16;
//        maxN02 = 38;
//        minNO2 = 4;
//        maxNew = 323;
//        minNew = 0;
//        maxConfirm = 958;
//        minConfirm = 0;
//        maxClose = 4904;
//        minClose = 254;
//        maxAsymp = 1041;
//        minAsymp = 0;
        try{
            //遍历数据库的案例数据，对每行案例数据(最后一行预测目标案例除外)
            for (int x = 0; x < data.size()-1; x++) {
                //在sim表中新建x行
                HSSFRow rowNew = sim_sheet.createRow(x);
                //获取第x个案例
                Plan plan= data.get(x);
                //计算8列属性
                for (int i = 0; i < 8; i++) {
                    if(i==0){   //0.温度
                        rowNew.createCell(0).setCellValue(calTempAndHumid(plan.getTemp1(),plan.getTemp2(),
                                newPlan.getTemp1(),newPlan.getTemp2(),minTemp,maxTemp));
                    }else if(i==1){//1.湿度
                        rowNew.createCell(1).setCellValue(calTempAndHumid((double)plan.getHumidity1()/100,
                                (double)plan.getHumidity2()/100,
                                (double)newPlan.getHumidity1()/100,(double)newPlan.getHumidity2()/100,minHumid,maxHumid));
                    }else if(i==2){//2.N02
                        rowNew.createCell(2).setCellValue(calN02NewConfirmedCloseAsymp(plan.getNo2Content()
                                ,newPlan.getNo2Content(),minNO2,maxN02));
                    }else if(i==3){//3.天气
                        rowNew.createCell(3).setCellValue(calWeather(plan.getWeather(), newPlan.getWeather()));
                    }else if(i==4){//4.新增确诊
                        rowNew.createCell(4).setCellValue(calN02NewConfirmedCloseAsymp(plan.getNewCases(),
                                newPlan.getNewCases(),minNew,maxNew));
                    }else if(i==5){//5.现有确诊
                        rowNew.createCell(5).setCellValue(calN02NewConfirmedCloseAsymp(plan.getConfirmedCases(),
                                newPlan.getConfirmedCases(),minConfirm,maxConfirm));
                    }else if(i==6){//6.密接
                        rowNew.createCell(6).setCellValue(calN02NewConfirmedCloseAsymp(plan.getCloseContact(),
                                newPlan.getCloseContact(),minClose,maxClose));
                    }else if(i==7){//7.现有无症状
                        rowNew.createCell(7).setCellValue(calN02NewConfirmedCloseAsymp(plan.getAsymptomatic(),
                                newPlan.getAsymptomatic(),minAsymp,maxAsymp));
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            FileOutputStream fout = new FileOutputStream(path);
            wb.write(fout);
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String[] fieldName(Class clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        String[] fieldNames = new String[declaredFields.length];
        for (int i = 0; i < declaredFields.length; i++) {
            fieldNames[i] = declaredFields[i].getName(); //通过反射获取属性名
        }
        return fieldNames;
    }

    /**
     * 获得根据相似度排序后相似度最高的前三个方案id
     * @return
     */
//    public static int[] getTop3(){
//        int[] planIds = new int[3];
//        try {
//            FileInputStream fileIn = new FileInputStream(path);
//            //根据指定文件输入流导入excel，产生workbook对象
//            Workbook wb = new HSSFWorkbook(fileIn);
//            Sheet sheet = wb.getSheetAt(0);
//            Row row;
//            for (int i = 0; i < 3; i++) {
//                row = sheet.getRow(i+1);
//                planIds[i] = Integer.parseInt(row.getCell(0).getStringCellValue());
//            }
//            fileIn.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return planIds;
//    }

    /**
     *计算温度和湿度相似度
     * @param a 历史案例数据左取值
     * @param b 历史案例数据右取值
     * @param c 目标案例数据左取值
     * @param d 目标案例数据右取值
     * @param min  区间最小值
     * @param max  区间最大值
     * @return 相似度
     */
    public static double calTempAndHumid(double a,double b,double c,double d,double min,
                                         double max){
        double sim = 0.0;
        if(b<=c){
            sim = 1-(d+c-b-a)/(2.0*(max-min));
        }else if(b<=d){
            sim =
                    1-((c-a)*(d-a))/(2.0*(b-a)*(max-min))-((b-c)*(b-c)*(b-c)+(b-d)*(b-d)*(b-d)+(d-c)*(d-c)*
                    (d-c))/(6.0*(b-a)*(d-c)*(max-min));
        }else{
            sim = 1-(d*d+c*c+c*d)/(3.0*(b-a)*(max-min))-(b*b+a*a-(a+b)*(c+d))/(2.0*(b-a)*(max-min));
        }
        return sim<0?0:sim;
    }

    /**
     * 计算NO2、新增确诊、现有确诊、密接、现有无症状的相似度
     * @param his 历史数据
     * @param tar 目标数据
     * @param min 最小值
     * @param max 最大值
     * @return 相似度
     */
    public static double calN02NewConfirmedCloseAsymp(double his,double tar,double min,double max){
        double sim = 0.0;
        double distance = his - tar;
        if(distance<0){
            distance = distance*(-1.0);
        }
        sim = 1 - distance/(max-min);
        return sim;
    }

    /**
     * 计算天气属性相似度
     * @param planWeather 历史案例天气
     * @param newWeather  目标案例天气
     * @return 天气属性相似度
     */
    public static double calWeather(String planWeather,String newWeather){
        double planWea = 0.0,newWea = 0.0,wea=0.0;

        switch (planWeather){
            case "晴": planWea = 1.0;break;
            case "多云": planWea = 2.0;break;
            case "阴": planWea = 3.0;break;
            case "雨": planWea = 4.0;break;
            case "雪": planWea = 5.0;break;
        }
        switch (newWeather){
            case "晴": newWea = 1.0;break;
            case "多云": newWea = 2.0;break;
            case "阴": newWea = 3.0;break;
            case "雨": newWea = 4.0;break;
            case "雪": newWea = 5.0;break;
        }
        wea = planWea-newWea;
        if(wea<0){
            wea = wea*(-1.0);
        }
        wea = 1-wea/5;
        return wea;
    }

    /**
     * 根据输入目标案例信息，预测未来案例信息
     * @param newPlan 目标案例
     * @return 预测案例
     */
    public static Plan predictPlan(Plan newPlan){
        //原确诊I(t) 26
        int confirmedCases = newPlan.getConfirmedCases();
        //原无症状E(t) 33
        int asymptomatic = newPlan.getAsymptomatic();
        //预测确诊:I(t+1) 33
        double preConfirmedCases = confirmedCases-0.07142*confirmedCases+0.25*asymptomatic;
        //十分位不为0则进1，为0则舍去所有小数
        if(preConfirmedCases*10%10!=0){ //42.05 ->420.5 ->0; 42.31 ->423.1 ->3
            preConfirmedCases = preConfirmedCases+1;
        }
        newPlan.setConfirmedCases((int)preConfirmedCases);
        //预测新增确诊I(t+1)-I(t)
        newPlan.setNewCases((int)preConfirmedCases-confirmedCases);
        //易感者S(t)=N-E-I-R，其中N城市总人口用epidemicScore承载，R治愈死亡数用economicScore承载
        int S = newPlan.getEpidemicScore()-asymptomatic-confirmedCases- newPlan.getEconomicScore();
        //预测无症状E(t+1)
        double preAsymptomatic =
                asymptomatic+0.045*10*confirmedCases*S/ newPlan.getEpidemicScore()+0.017*10*asymptomatic*S/ newPlan.getEpidemicScore()-0.25*asymptomatic;

        if(preAsymptomatic*10%10>=1){ //42.05 ->420.5 ->0; 42.31 ->423.1 ->3
            preAsymptomatic = preAsymptomatic+1;
        }
        newPlan.setAsymptomatic((int)preAsymptomatic);
        //预测密接
        double preClose =
                newPlan.getCloseContact()+10*(newPlan.getNewCases()/0.25+preAsymptomatic-asymptomatic);
        newPlan.setCloseContact((int)preClose);
        return newPlan;
    }

}
