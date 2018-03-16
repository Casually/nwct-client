package cc.casually.nwct.project;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping()
@Controller
public class Body {

    @RequestMapping("/{url}")
    @ResponseBody
    public String body(@PathVariable("url") String url){
        System.out.println(url);
        return "<html><head><title>测试</title>" +
                "<link rel='stylesheet' type='text/css' href='/ss.css'>" +
                "</head><script src='/ss.js'></script>" +
                "<script src=\"https://cdn.bootcss.com/jquery/3.3.1/jquery.js\"></script>" +
                "<script>$.post('/ss.ajax',{},function(data){alert(data)})</script>" +
                "<body><h1 id='cc'>测试</h1></body></html>";
    }

    @RequestMapping("/*.js")
    @ResponseBody()
    public String js(){
        return "alert(123)";
    }

    @RequestMapping("/*.css")
    @ResponseBody()
    public String css(){
        return "#cc{color:red;}";
    }

    @RequestMapping("/*.ajax")
    @ResponseBody()
    public String ajax(){
        return "{data:'hahah'}";
    }
}
