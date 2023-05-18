package com.votool.page;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *	jpa .findAll后的org.springframework.data.domain.Page 对象取值
 *  接口PageRequest of 处理page参数时 ，写为 final int page = pn <= 0 ? 1 : pn - 1;
 *  因为jpa分页page从0开始
 *
  freemarker 分页代码

 		<p style="padding-left: 50px;font-size:20px">
			当前第${number}/${totalPages}页 |
			当前页有${numberOfElements}条 |
			每页显示${size}条 |
			总共${totalElements}条
		</p>

		<p style="padding-left: 50px;font-size:20px">

			<a href="/?pn=1&ps=${size}">首页 | </a>

			<#if number gt 1>
				<a href="/?pn=${number-1}&ps=${size}">上一页</a>
			<#else>
				(没有上一页了)
			</#if>

			<#if number lt totalPages>
				<a href="/?pn=${number+1}&ps=${size}">下一页</a>
			<#else>
				(没有下一页了)
			</#if>

			<a href="/?pn=${totalPages}&ps=${size}"> | 尾页</a>
		</p>

	springboot 分页代码

	@GetMapping
	public String indexPage(final Model model,
			@RequestParam(required = false, defaultValue = "10") final Integer ps,
			@RequestParam(required = false, defaultValue = "1") final Integer pn) {
		System.out.println(
				Thread.currentThread().getName() + "\t" + LocalDateTime.now() + "\t" + "ZController.indexPage()");

		final int p1 = pn <= 0 ? 1 : pn - 1;
		final PageRequest pageRequest = PageRequest.of(p1, ps, Sort.by("id").descending());

		final ExampleMatcher matcher = ExampleMatcher.matching();

		final ArticleEntity entity = new ArticleEntity();
		entity.setStatus(ArticleStatusEnum.NORMAL.getStatus());
		entity.setIsDelete(ArticleDeleteEnum.NO.getIsDelete());

		final Example<ArticleEntity> en = Example.of(entity, matcher);

		final Page<ArticleEntity> page = this.articleRepository.findAll(en, pageRequest);
		model.addAttribute("list", page.toList());
		model.addAttribute("page", page);

		final ZPR<ArticleEntity> zpr = new ZPR<>(page);
		System.out.println("zpr = " + zpr);
		ZMR.init(model, zpr);

		return "index";
	}

 * @author zhangzhen
 * @date 2022年5月6日
 *
 */
@Data
@AllArgsConstructor
public class ZPR<T> {

//	private final Page<T> page;

	/**
	 * 当前分页条件下，此条数据是第几页，从1开始，1 2 3 4 ....
	 */
	private final int number;

	/**
	 * 当前页的数据内容
	 */
	private List<? extends T> content;

	/**
	 * 每页条数
	 */
	private final int size;

	/**
	 * 总条数
	 */

	private final long totalElements;
	/**
	 * 总页数
	 */
	private final int totalPages;
	private final Boolean first;
	private final Boolean last;
	private final Boolean empty;
	private final Boolean hasPrevious;
	private final Boolean hasNext;
	private String apiUrl = "";

	/**
	 * 本页实际条数，正常情况等于size,在数据条数<每页条数时，此值会小于size
	 */
	private final int numberOfElements;


	public ZPR setApiUrl(final String apiUrl) {
		this.apiUrl = apiUrl;
		return this;
	}

	public static ZPR empty() {
		return new ZPR<>();
	}

	public ZPR() {
		this.number = -1;
		this.content = Collections.emptyList();
		this.size = 0;
		this.totalElements = 0L;
		this.totalPages = 0;
		this.first = true;
		this.last = true;
		this.empty = true;
		this.hasPrevious = false;
		this.hasNext = false;
		this.numberOfElements = 0;
	}

	public ZPR(final Page<T> page) {
//		this.page = page;
		this.number = page.getNumber();
		this.content = page.getContent();
		this.size = page.getSize();
		this.totalElements = page.getTotalElements();
		this.totalPages = page.getTotalPages();
		this.first = page.isFirst();
		this.last = page.isLast();
		this.empty = page.isEmpty();
		this.hasPrevious = page.hasPrevious();
		this.hasNext = page.hasNext();
		this.numberOfElements = page.getNumberOfElements();
	}

	@Override
	public String toString() {
		return "ZPR [number=" + this.number + ", size=" + this.size + ", totalElements=" + this.totalElements
				+ ", totalPages=" + this.totalPages + ", first=" + this.first + ", last=" + this.last + ", empty="
				+ this.empty + ", hasPrevious=" + this.hasPrevious + ", hasNext=" + this.hasNext + ", numberOfElements="
				+ this.numberOfElements + "]";
	}

}
