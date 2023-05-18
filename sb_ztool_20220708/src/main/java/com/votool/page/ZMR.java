package com.votool.page;

import java.util.StringJoiner;

import org.springframework.ui.Model;

/**
 *	分页组件，freemarker中使用${page}即可生成分页代码
 *
 * @author zhangzhen
 * @date 2022年5月6日
 *
 */
public class ZMR {

//	private static final int MAX_PAGE = 5;
	private static final int MAX_PAGE = 11;

	public static void init(final Model model, final ZPR zpr) {

		model.addAttribute("number", zpr.getNumber() + 1);
		model.addAttribute("content", zpr.getContent());
		model.addAttribute("size", zpr.getSize());
		model.addAttribute("totalElements", zpr.getTotalElements());
		model.addAttribute("totalPages", zpr.getTotalPages());
		model.addAttribute("first", zpr.getFirst());
		model.addAttribute("last", zpr.getLast());
		model.addAttribute("empty", zpr.getEmpty());
		model.addAttribute("previous", zpr.getHasPrevious());
		model.addAttribute("next", zpr.getHasNext());
		model.addAttribute("numberOfElements", zpr.getNumberOfElements());

		model.addAttribute("maxPage", MAX_PAGE);


		final String page = gP1(zpr) + gP2(zpr);
		model.addAttribute("page", page);

	}

	private static String gP2(final ZPR zpr) {

		final StringBuilder builder = new StringBuilder();
		builder.append("<p style=\"padding-left: 50px; font-size: 30px\">");
		builder.append("<a href=\"" + zpr.getApiUrl() + "?pn=1&ps=" + zpr.getSize() + "\">首页 </a>");

		if (zpr.getNumber() + 1 > 1) {
			builder.append(" <a href=\"" + zpr.getApiUrl() + "?pn=" + (zpr.getNumber() + 1 - 1) + "&ps=" + zpr.getSize()
					+ "\">上一页</a>");
		} else {
			builder.append(" <span style=\"color:yellow; font-size: 30px;\">(没有上一页了)</span>");
		}

		// 1 2 3 4 5...代码，总页数<MAX_PAGE，全部生成
		if (zpr.getTotalPages() <= MAX_PAGE) {
			final StringJoiner aJoiner = new StringJoiner("&nbsp&nbsp");
			for (int i = 1; i <= zpr.getTotalPages(); i++) {
				if (zpr.getNumber() + 1 == i) {
					final String a = "<a href=\"" + zpr.getApiUrl() + "?pn=" + i + "&ps=" + zpr.getSize() + "\" style=\"color:yellow; font-size: 30px;\">" + i
							+ "</a>";
					aJoiner.add(a);
				} else {
					final String a = "<a href=\"" + zpr.getApiUrl() + "?pn=" + i + "&ps=" + zpr.getSize() + "\">" + i
							+ "</a>";
					aJoiner.add(a);
				}
			}
			builder.append(aJoiner.toString());
		} else {

			// FIXME 2022年11月2日 下午10:31:31 zhanghen: from还是有问题，点击尾页的时候，a标签数量不足
			final int from =
					(zpr.getTotalPages() - (zpr.getNumber() + 1)) >= (MAX_PAGE)
					? (zpr.getNumber() + 1)
					: (
						(zpr.getNumber() + 1) < (MAX_PAGE / 2)
						? 1
						: zpr.getNumber() + 1 - (MAX_PAGE / 2)
					)
					;

			final int to = (from + MAX_PAGE) > zpr.getTotalPages() ? zpr.getTotalPages() : from + MAX_PAGE;
			final StringJoiner aJoiner = new StringJoiner("&nbsp&nbsp");
			for (int i = from; i <= to; i++) {
				if (zpr.getNumber() + 1 == i) {
					final String a = "<a href=\"" + zpr.getApiUrl() + "?pn=" + i + "&ps=" + zpr.getSize() + "\" style=\"color:yellow; font-size: 30px;\">" + i
							+ "</a>";
					aJoiner.add(a);
				} else {
					final String a = "<a href=\"" + zpr.getApiUrl() + "?pn=" + i + "&ps=" + zpr.getSize() + "\">" + i
							+ "</a>";
					aJoiner.add(a);
				}
			}
			builder.append(aJoiner.toString());
		}

		if (zpr.getNumber() + 1< zpr.getTotalPages()) {
			builder.append(" <a href=\"" + zpr.getApiUrl() + "?pn=" + (zpr.getNumber() + 1 + 1) + "&ps=" + zpr.getSize()
					+ "\">下一页</a> ");
		} else {
			builder.append(" <span style=\"color:yellow; font-size: 30px;\">(没有下一页了)</span>");
		}

		builder.append(
				"<a href=\"" + zpr.getApiUrl() + "?pn=" + zpr.getTotalPages() + "&ps=" + zpr.getSize() + "\"> 尾页</a>");
		builder.append("</p>");
		final String sss = builder.toString();

		final String p2 = sss;

		 return p2;
	}

	private static String gP1(final ZPR zpr) {
		final String p1 =
							"<p style=\"padding-left: 50px; font-size: 20px\">\r\n"
				+ "				当前第${number}/${totalPages}页 | 当前页有${numberOfElements}条 | 每页显示${size}条\r\n"
				+ "				| 总共${totalElements}条\r\n"
				+ "			</p>";

		String replace = p1.replace("${number}", String.valueOf(zpr.getNumber() + 1));
		replace = replace.replace("${totalPages}", String.valueOf(zpr.getTotalPages()));
		replace = replace.replace("${numberOfElements}", String.valueOf(zpr.getNumberOfElements()));
		replace = replace.replace("${size}", String.valueOf(zpr.getSize()));
		replace = replace.replace("${totalElements}", String.valueOf(zpr.getTotalElements()));

		return replace;
	}

}
