package com.votool.apidoc;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年8月14日
 *
 */
@Controller
//@RestController
@RequestMapping(value = "/apidoc")
public class APIDocAPI {

	@GetMapping
	public String docAPiIndex(final Model model) {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "APIDocAPI.docAPiIndex()");

		final List<APIClassInfo> apiClassList = APIScanner.apiClassList;
//		apiClassList.get(0).getApiList().get(0).setReturnTypeT("<noescape><ABCD></noescape>");
		for (final APIClassInfo apiClassInfo : apiClassList) {
			final List<APIInfo> apiList = apiClassInfo.getApiList();
			for (final APIInfo a : apiList) {
				final String t = a.getReturnTypeT().replace("<", "&lt;").replace(">", "&gt;");
				a.setReturnTypeT(t);

				// FIXME 2022年8月14日 下午7:44:52 zhanghen:删除syso
				if(a.getApiReturnTypeClassInfo() != null) {
					System.out.println(a.getApiReturnTypeClassInfo().getJson());
				}

			}
		}

		System.out.println("apiClassList.size = " + apiClassList.size());
		model.addAttribute("apiClassList", apiClassList);

		return "apidoc_page";
	}

}
