package org.minispm.sale.web;

import org.minispm.sale.entity.Action;
import org.minispm.sale.entity.LeadsBase;
import org.minispm.sale.service.ActionService;
import org.minispm.sale.service.ActionTypeService;
import org.minispm.sale.service.LeadsBaseService;
import org.minispm.security.entity.User;
import org.minispm.security.service.UserService;
import org.minispm.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: shibaoxu
 * Date: 12-12-30
 * Time: 下午8:57
 */
@Controller
public class ActionController {

    private LeadsBaseService leadsBaseService;
    private ActionService actionService;
    private ActionTypeService actionTypeService;
    private UserService userService;

    @RequestMapping("/sale/{leadsBaseid}/action/index")
    public String list(@PathVariable String leadsBaseid, Model model){
        model.addAttribute("actions", actionService.findAll(leadsBaseid));
        model.addAttribute("leadsBase", leadsBaseService.findById(leadsBaseid));
        return "/sale/actionList";
    }

    @RequestMapping(value = "/sale/{leadsBaseid}/action/new", method = RequestMethod.GET)
    public String add(Model model, @PathVariable String leadsBaseid){
        Action action = new Action();
        LeadsBase leadsBase = new LeadsBase();
        leadsBase.setId(leadsBaseid);
        action.setLeadsBase(leadsBase);
        User owner = new User();
        owner.setId(SecurityUtils.getCurrentShiroUser().getId());
        action.setOwner(owner);
        model.addAttribute("action", action);
        model.addAttribute("operation", "add");
        this.initDictionary(model, leadsBaseid);
        return "/sale/action";
    }

    @RequestMapping(value = "/sale/{leadsBaseid}/action/view/{actionId}", method = RequestMethod.GET)
    public String view(Model model, @PathVariable String leadsBaseid, @PathVariable String actionId){
        model.addAttribute("action", actionService.findByIdView(actionId));
        model.addAttribute("operation", "view");
        this.initDictionary(model, leadsBaseid);
        return "/sale/action";
    }


    @RequestMapping(value = "/sale/{leadsId}/action/edit/{actionId}", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable String leadsId, @PathVariable String actionId){
        model.addAttribute("action", actionService.findByIdEdit(actionId));
        model.addAttribute("operation", "edit");
        this.initDictionary(model, leadsId);
        return "/sale/action";
    }

    @RequestMapping(value = {"/sale/{leadsBaseId}/action/edit/{actionId}", "/sale/{leadsBaseId}/action/new"}, method = RequestMethod.POST)
    public String save(@ModelAttribute Action action, @PathVariable String leadsBaseId, Model model){
        actionService.save(action);
        return list(leadsBaseId, model);
    }

    private void initDictionary(Model model, String leadsId){
        model.addAttribute("actionTypes", actionTypeService.findAll());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("leads", leadsBaseService.findById(leadsId));
    }

    @InitBinder
    private void dateBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
        binder.registerCustomEditor(Date.class, editor);
    }
    @ExceptionHandler
    public ModelAndView exceptionHandler(Exception ex){
        ModelAndView mv = new ModelAndView();
        mv.getModel().put("message", ex.getMessage());
        mv.setViewName("exception");
        return mv;
    }
    public ActionService getActionService() {
        return actionService;
    }

    @Autowired
    public void setActionService(ActionService actionService) {
        this.actionService = actionService;
    }
    @Autowired
    public void setActionTypeService(ActionTypeService actionTypeService) {
        this.actionTypeService = actionTypeService;
    }

    public UserService getUserService() {
        return userService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setLeadsBaseService(LeadsBaseService leadsBaseService) {
        this.leadsBaseService = leadsBaseService;
    }
}