package org.minispm.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created with IntelliJ IDEA.
 * User: shibaoxu
 * Date: 12-11-10
 * Time: 下午10:32
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class HomePageController {

    @RequestMapping("/index")
    public String index(){
        return "homepage";
    }
}
