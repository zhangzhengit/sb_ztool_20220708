package com.votool.apidoc;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 扫描所有的RestController和 Controller来生成API文档
 *
 * @author zhangzhen
 * @date 2022年8月14日
 *
 */
@Component
public class APIScanner  implements ApplicationContextAware {

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "APIScanner.setApplicationContext()");

		final String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
		System.out.println("beanDefinitionNames.length = " + beanDefinitionNames.length);
		for (final String beanName : beanDefinitionNames) {


			// FIXME 2022年8月14日 下午4:17:49 zhanghen: 删除下面if，方便测试用的
			if(!beanName.toLowerCase().equals("TestAPIDoc".toLowerCase())) {
				continue;
			}

			final Object bean = applicationContext.getBean(beanName);
			final Class<? extends Object> cls = bean.getClass();
			final RestController restController = cls.getAnnotation(RestController.class);
			final Controller controller = cls.getAnnotation(Controller.class);
			// 是api
			if (restController != null || controller != null) {
				System.out.println("beanName = " + beanName);

				final Method[] declaredMethods = cls.getDeclaredMethods();

				final APIClassInfo aci = new APIClassInfo();
				aci.setClassName(beanName);
				for (final Method m : declaredMethods) {

					final int modifiers = m.getModifiers();
					final String string = Modifier.toString(modifiers);
					final Class<?> returnType = m.getReturnType();
//					System.out.println("\t" + string);
//					System.out.println("\t" + returnType.getSimpleName());
//					System.out.println("\t" + m.getName());

					// Method post ? get ? ...
					final String requestMethod = getRequestMethod(m);
					if (StrUtil.isEmpty(requestMethod)) {
						continue;
					}

					System.out.println("\t" + requestMethod);

					System.out.println("\t" + m);



					final APIInfo aaaa = new APIInfo();
					aaaa.setClassName(beanName);
					aaaa.setMethod(m);
					aaaa.setRequestMethod(getRequestMethod(m));
					aaaa.setRequestValues(getRequestValues(m));
					aaaa.setModifier(Modifier.toString(modifiers));
					aaaa.setReturnType(m.getReturnType().getSimpleName());
					final String returnTypeT = getReturnTypeT(m);
					aaaa.setReturnTypeT(returnTypeT);
					aaaa.setName(m.getName());
					aaaa.setParamList(getParameters(m));
					final ApiReturnTypeClassInfo returnTypeJSONBody = getReturnTypeJSONBody(m);
					aaaa.setApiReturnTypeClassInfo(returnTypeJSONBody);

					aci.addApi(aaaa);
				}
				APIScanner.apiClassList.add(aci);
			}


		}

		System.out.println("------------------------------------------------------------------------");
		for (final APIClassInfo info : apiClassList) {
			System.out.println(info);
		}
		System.out.println("------------------------------------------------------------------------");
		System.out.println("beanDefinitionNames.length = " + beanDefinitionNames.length);

	}

	public static final List<APIClassInfo> apiClassList = Lists.newArrayList();

	public static String getReturnTypeT(final Method method) {
		final Type genericReturnType = method.getGenericReturnType();
		return genericReturnType.toString();
	}


	public static ApiReturnTypeClassInfo getReturnTypeJSONBody(final Method method) {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "APIScanner.getReturnTypeClassBody()");

		final ApiReturnTypeClassInfo methodReturnType = getMethodReturnType(method);
		System.out.println("methodReturnType = " + methodReturnType);

		final ApiReturnTypeTClassInfo methodReturnTypeT = getMethodReturnTypeT(method);
		if (methodReturnTypeT != null) {
			methodReturnType.setT(methodReturnTypeT);
			System.out.println("methodReturnTypeT = " + methodReturnTypeT);
			System.out.println("AmethodReturnType = " + methodReturnType);

		}

		return methodReturnType;
	}

	private static ApiReturnTypeClassInfo getMethodReturnType(final Method method) {
		final Class<?> cls = method.getReturnType();
		System.out.println("cls = " + cls);
		if ("void".equals(cls.getCanonicalName())) {
			return null;
		}
		try {
			if(cls.getName().equals(int.class.getName())) {
				System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
			}

			final Object newInstance = cls.newInstance();
			final String json = JSON.toJSONString(newInstance, SerializerFeature.WRITE_MAP_NULL_FEATURES,
					SerializerFeature.PrettyFormat);
			System.out.println("json = " + json);

			final ApiReturnTypeClassInfo info = new ApiReturnTypeClassInfo();

			info.setClassName(cls.getSimpleName());
			info.setJson(json);
			return info;

		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static ApiReturnTypeTClassInfo getMethodReturnTypeT(final Method method) {
		final Type genericReturnType = method.getGenericReturnType();
		System.out.println("genericReturnType = " + genericReturnType);
		System.out.println("genericReturnType.toString = " + genericReturnType.toString());

		if (genericReturnType instanceof ParameterizedType) {
			// 如果要使用ParameterizedType中的方法，必须先强制向下转型
			final ParameterizedType type = (ParameterizedType) genericReturnType;
			// 获取返回值类型中的泛型类型，因为可能有多个泛型类型，所以返回一个数组
			final Type[] actualTypeArguments = type.getActualTypeArguments();
			// 循环数组，遍历每一个泛型类型
			for (final Type actualTypeArgument : actualTypeArguments) {
				final Class typeArgClass = (Class) actualTypeArgument;
//				System.out.println("成员方法返回值的泛型信息：" + typeArgClass);

				try {
					final String jsonT = JSON.toJSONString(typeArgClass.newInstance(),
							SerializerFeature.WRITE_MAP_NULL_FEATURES, SerializerFeature.PrettyFormat);
					System.out.println("jsonT = " + jsonT);
					final ApiReturnTypeTClassInfo info = new ApiReturnTypeTClassInfo();

					info.setClassName(typeArgClass.getSimpleName());
					info.setJson(jsonT);
					return info;
				} catch (InstantiationException | IllegalAccessException e) {
					// FIXME 2022年8月14日 下午6:41:36 zhangzhen: 记得处理这里 TODO Auto-generated catch block
					e.printStackTrace();
				}

				return null;
			}
		}
		return null;
	}

	public static List<ApiParamInfo> getParameters(final Method method) {

		final Parameter[] p = method.getParameters();
		if (ArrayUtil.isEmpty(p)) {
			return Collections.emptyList();
		}

		final List<ApiParamInfo> pppppp = Lists.newArrayList();

		for (final Parameter parameter : p) {
			final Annotation[] annotations = parameter.getAnnotations();
			final ArrayList<Annotation> al = Lists.newArrayList(annotations);
			final String c = al.stream().map(an -> an.annotationType().getSimpleName())
					.collect(Collectors.joining(" ", "@", ""));
			final String anno = StrUtil.isEmpty(c) ? "" : c;

			final Class<?> type = parameter.getType();

			final String string = anno.toString();
			System.out.println("string = " + string);
			System.out.println("\t\t" + anno + " " + type.getSimpleName() + " " + parameter.getName());



			final ApiParamInfo pp = new ApiParamInfo();
			pp.setType(type.getSimpleName());
			pp.setName(parameter.getName());
			pp.setAnnoName(getParameterAnnotations(parameter));

			pppppp.add(pp);

		}

		return pppppp;
	}

	public static String getParameterAnnotations(final Parameter parameter) {

		final Annotation[] annotations = parameter.getAnnotations();
		if (ArrayUtil.isEmpty(annotations)) {
			return "";
		}

		final StringJoiner joiner = new StringJoiner(" ", "", "");
		for (final Annotation a : annotations) {
			final Class<? extends Annotation> t = a.annotationType();
			final String simpleName = t.getSimpleName();
			joiner.add("@" + simpleName);
			System.out.println("simpleName = " + simpleName);
		}

		return joiner.toString();
	}

	public static String[] getRequestValues(final Method method) {

		final Annotation[] annotations = method.getAnnotations();

		for (final Annotation annotation : annotations) {
			if (annotation.annotationType().equals(PostMapping.class)) {
				return ((PostMapping) annotation).value();
			}
			if (annotation.annotationType().equals(GetMapping.class)) {
				return ((GetMapping) annotation).value();
			}
			if (annotation.annotationType().equals(PutMapping.class)) {
				return ((PutMapping) annotation).value();
			}
			if (annotation.annotationType().equals(DeleteMapping.class)) {
				return ((DeleteMapping) annotation).value();
			}

			if (annotation.annotationType().equals(RequestMapping.class)) {
				final RequestMapping rm = (RequestMapping) annotation;
				final String[] value = rm.value();
				return value;
			}
		}

		return null;
	}

	public static String getRequestMethod(final Method method) {

		final Annotation[] annotations = method.getAnnotations();

		for (final Annotation annotation : annotations) {
			if (annotation.annotationType().equals(PostMapping.class)) {
				return "POST";
			}
			if (annotation.annotationType().equals(GetMapping.class)) {
				return "GET";
			}
			if (annotation.annotationType().equals(PutMapping.class)) {
				return "PUT";
			}
			if (annotation.annotationType().equals(DeleteMapping.class)) {
				return "DELETE";
			}

			if (annotation.annotationType().equals(RequestMapping.class)) {

				final RequestMapping rm = (RequestMapping) annotation;

				final RequestMethod[] method2 = rm.method();
				final StringJoiner joiner = new StringJoiner(",");
				for (final RequestMethod r : method2) {
					joiner.add(r.name());
				}

				return joiner.toString();
			}

		}


		return "";
	}

}
