package com.votool.index;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年7月8日
 *
 */
@Controller
public class IndexC {

	@GetMapping(value = "/indexC")
	public String index() {
		return "index";
	}

}
