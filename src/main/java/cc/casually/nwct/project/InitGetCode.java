package cc.casually.nwct.project;


import cc.casually.htmlParse.http.HttpClient;
import cc.casually.htmlParse.http.Request;
import cc.casually.htmlParse.http.Response;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component//被spring容器管理
@Order(1)//如果多个自定义ApplicationRunner，用来标明执行顺序
public class InitGetCode implements ApplicationRunner{


    @Override
    public void run(ApplicationArguments args) throws Exception {
        GainLotteryData gd = new GainLotteryData();
        Map<String,String> map = gd.getCode();
        Request request = new Request();
        request.setUri("");
        request.addBody("taskName","临时获取数据");
        request.addBody("taskContext","临时获取北京pk10的出奖号码数据");
        request.addBody("taskUrl","http://localhost/getcode");
        request.addBody("executeDateTime",map.get("last_time"));
        request.addBody("createUser","casually");
        request.setUri("http://localhost:8080/ttm/app/timingTask/addTemporaryTask.html");
        Response response = HttpClient.post(request);
        System.out.println(response.getBodyStr());
    }
}
