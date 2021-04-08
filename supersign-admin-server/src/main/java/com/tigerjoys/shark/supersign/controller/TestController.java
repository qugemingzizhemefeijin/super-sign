package com.tigerjoys.shark.supersign.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.tigerjoys.nbs.common.ActionResult;
import com.tigerjoys.nbs.web.BaseController;

/**
 * 测试模版
 *
 */
@Controller
@RequestMapping("/test")
public class TestController extends BaseController {
	
	@RequestMapping("/index")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/json")
	public @ResponseBody ActionResult testJson() {
		return ActionResult.fail();
	}
	
	@RequestMapping("/get")
	public String getModel(Model model) {
		model.addAttribute("msg", "错误信息");
		model.addAttribute("title", "设置标题");
		model.addAttribute("bb", 1);
		
		List<String> list = Lists.newArrayList("你","好","北","京");
		model.addAttribute("list", list);
		
		return "test/get";
	}

}
