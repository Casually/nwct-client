package cc.casually.nwct.project;

import cc.casually.htmlParse.http.HttpClient;
import cc.casually.htmlParse.http.Request;
import cc.casually.htmlParse.http.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/getcode")
public class GainLotteryData {

    @RequestMapping()
    @ResponseBody
    public Object getCode(HttpServletRequest request){
        new getCodeThread(request.getHeader("callbackUrl"),request.getHeader("taskId")).start();
        return "success";
    }

    class getCodeThread extends Thread{
        private String callbackUrl = "";
        private String taskId = "";
        public getCodeThread(String callbackUrl,String taskId){
            this.callbackUrl = callbackUrl;
            this.taskId = taskId;
        }

        @Override
        public void run(){
            //此处添加处理逻辑
            Map<String,String> map = getCode();
            Request request = new Request();
            request.addParam("taskName","临时获取数据");
            request.addParam("taskContext","临时获取北京pk10的出奖号码数据");
            request.addParam("taskUrl","http://casually.free.ngrok.cc//getcode");
            request.addParam("executeDateTime",map.get("last_time"));
            request.addParam("createUser","casually");
            request.setUri("http://localhost:8080/ttm/app/timingTask/addTemporaryTask.html");
            HttpClient.post(request);
            //回复调度管理服务器
            HashMap result = new HashMap();
            result.put("taskId", taskId);
            result.put("msg", "调度无异常，执行完成");
            request.setUri(callbackUrl);
            request.setParams(result);
            HttpClient.post(request);
        }

    }

    /**
     * 获取数据
     * @return
     */
    public Map<String,String> getCode() {
        Request request = new Request();
        request.setUri("http://zh.wikipedia.org/wiki/人工智能");
        request.setUri("https://www.cdd456.com/index.php?c=api2&a=getLastData&cp=bjpk10&_=0.1809315721100997");
        request.addParam("c","api2");
        request.addParam("a","getLastData");
        request.addParam("cp","bjpk10");
        request.addParam("_",String.valueOf(new Random().nextDouble()));
        Response response = HttpClient.post(request);
        Map<String,String> bodyMap = response.getBodyMap();
        System.out.println("当前期数：" + bodyMap.get("c_t"));
        System.out.println("当前开奖时间：" + bodyMap.get("c_d"));
        System.out.println("当前开奖号码: " + bodyMap.get("c_r"));
        System.out.println("下次开奖时间：" + delayDate(bodyMap.get("c_d").toString(),5));
        System.out.println("格式话下次开奖时间：" + dateFomart(delayDate(bodyMap.get("c_d").toString(),6)));
        bodyMap.put("last_time",dateFomart(delayDate(bodyMap.get("c_d").toString(),6)));
        return bodyMap;
    }

    /**
     * 计算下次开奖时间
     * @param datatime
     * @param amount
     * @return
     */
    public static String delayDate(String datatime,int amount){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(simpleDateFormat.parse(datatime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.MINUTE, amount);
        Date result=calendar.getTime();
        return simpleDateFormat.format(result);
    }

    /**
     * 将时间格式为调度执行时间
     * @param dataTime
     * @return
     */
    public static String dateFomart(String dataTime){
        String data = dataTime.split(" ")[0];
        String time = dataTime.split(" ")[1];
        return time.split(":")[1] + " " + time.split(":")[0] + " "
                + data.split("-")[2] + " " + data.split("-")[1] + " " + data.split("-")[0] ;
    }
}
