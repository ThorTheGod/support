package com.xidian.support.util;

import com.xidian.support.pojo.Plan;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author thornoir
 * @date 2022/1/24
 * @apiNote
 */
public class PythonUtil {

    static DecimalFormat df = new DecimalFormat("0.00");
    /**
     * 调用python代码：根据相似度计算出各方案的总相似度，并返回相似度前三名
     * @param planIds
     */
    public static List<Map<String,Object>> calTotalSim(double[] planIds){
//        }D:\IDEA\IdeaProject\support\src\main\resources\output
        String[] arguments = new String[]{"python",System.getProperty("user.dir") +
                "/output" +
                "/cal" +
                ".py",
                Arrays.toString(planIds)};
        List<Map<String,Object>> res = new ArrayList<>();
        try{
            Process process = Runtime.getRuntime().exec(arguments);
            process.waitFor();
            //获取python文件执行后的输出(print的内容)
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(),
                    "GB2312"));
            String line = null;
            int i = 0;
            //从输出的dataframe结构中生成list用于前端显示
            while ((line=in.readLine())!=null){
                System.out.println(line);
                Map<String, Object> results = new HashMap<>();
                line = line.replace("[","").replace("]","").replace("'","").trim();
                String[] result = line.split("\\s+");
//               Math.round(Double.parseDouble(result[1])*100)/100
                results.put("sim",((double)(int)(Double.parseDouble(result[1])*1000))/1000);
                results.put("planId",(int)Double.parseDouble(result[0]));
                res.add(results);
                i++;
            }
            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }



}
