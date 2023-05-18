package com.votool;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.aspectj.weaver.patterns.HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.votool.random.ZR;
import com.votool.ze.AbstractZETask;
import com.votool.ze.ZE;
import com.votool.ze.ZERunnable;
import com.votool.ze.ZES;
import com.votool.ze.ZETaskResult;
import com.votool.ze.ZETaskStep;

import cn.hutool.core.lang.UUID;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年12月17日
 *
 */
@SpringBootTest
public class ZETest {

	public static void assertTrue(final boolean ok) {
		if(!ok) {
			throw new IllegalStateException("@Test出错了");
		}

	}

	@Test
	public void test_test_subAngGet_12() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZETest.test_test_subAngGet_12()");

		final int n = 11232;
		final ZE ze = ZES.newZE(12);

		final ArrayList<AbstractZETask<String>> taskList = Lists.newArrayList();
		for (int i = 1; i <= n; i++) {
			final AbstractZETask<String> abstractZETask = new AbstractZETask<String>() {

				@Override
				public String call() {
					final int ms = ZR.nextInt(5);
					sleepMS(ms);
					return java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t" + "OK" + "\t"
							+ "ms = " + ms;
				}
			};

			taskList.add(abstractZETask);
		}

		System.out.println("开始派发，任务个数 = " + taskList.size());

		final List<String> rList = ze.submitInQueueAndGet(taskList);

		ze.executeInQueue(() -> {

			System.out.println("rList.size = " + rList.size());
//				for (final String string : rList) {
//				System.out.println("\t" + string);
////			}
			System.out.println("rList.size = " + rList.size());

			final long count = rList.stream().filter(s -> s.contains("OK")).count();
			assertTrue(count == n);
		});

		ze.executeInQueue(() -> {

			final String v1 = ze.submitInQueueAndGet(new AbstractZETask<String>() {

				@Override
				public String call() {
					return "v1";
				}
			});

			System.out.println(
					java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t" + "v1 = " + v1);

			assertTrue("v1" .equals(v1));
		});

	}

	@Test
	public  void test_subAngGet_1() {

		final ZE ze = ZES.newZE(55);

		ze.executeImmediately(() -> {

			final String v1 = ze.submitImmediatelyAndGet(new AbstractZETask<String>() {

				@Override
				public String call() {

					sleepMS(1020);
					return "v1";
				}
			});

			System.out.println(
					java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t" + "v1 = " + v1);
		});

		ze.executeImmediately(() -> {

			final String v2 = ze.submitImmediatelyAndGet(new AbstractZETask<String>() {

				@Override
				public String call() {

					sleepMS(1020);
					return "v2";
				}
			});

			System.out.println(
					java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t" + "v2 = " + v2);
		});
	}

	@Test
	public void test_byName_reassign_2() {

		final ExecutorService service = Executors.newFixedThreadPool(10);
		final ZE ze = ZES.newZE(500);
		final int n = 50000;

		final ArrayList<ZERunnable<String>> lll = Lists.newArrayList();
		final AtomicInteger x = new AtomicInteger();

		final long t1 = System.currentTimeMillis();

		for (int i = 1; i <= n; i++) {

			final ZERunnable<String> r = new ZERunnable<String>() {

				@Override
				public void run() {
//						try {
//							Thread.sleep(5);
//						} catch (final InterruptedException e) {
//							e.printStackTrace();
//						}

					x.incrementAndGet();
//						System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName());
				}
			};

			ze.executeByNameInASpecificThread(String.valueOf(UUID.randomUUID()), r);
		}

		while (x.get() < n) {

		}
		final long t2 = System.currentTimeMillis();

		assertTrue(x.get() == n);
		System.out.println("n = " + n + "\t" + "ms = " + (t2 - t1));
	}

	@Test
	public void test_byName_reassign_1() {

		final ExecutorService service = Executors.newFixedThreadPool(10);
		final ZE ze = ZES.newZE(2);
		final int n = 10;

		final ArrayList<ZERunnable<String>> lll = Lists.newArrayList();
		final AtomicInteger x = new AtomicInteger();

		final long t1 = System.currentTimeMillis();

		for (int i = 1; i <= n; i++) {
			lll.add(new ZERunnable<String>() {

				@Override
				public void run() {
					try {
						Thread.sleep(1000);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}

					x.incrementAndGet();
					System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName());
				}
			});
		}

		ze.executeAllByNameInASpecificThread(String.valueOf(UUID.randomUUID()), lll);

		while (x.get() < n) {

		}
		final long t2 = System.currentTimeMillis();
		assertTrue(x.get() == n);
		System.out.println("n = " + n + "\t" + "ms = " + (t2 - t1));

	}

	@Test
	public  void test_Executors_AND_ZE_2() {

		final ZE ze = ZES.newZE(1011);
		final int n = 1000;

		final ArrayList<ZERunnable<String>> lll = Lists.newArrayList();
		final AtomicInteger x = new AtomicInteger();

		final long t1 = System.currentTimeMillis();

		for (int i = 1; i <= n; i++) {
			lll.add(new ZERunnable<String>() {

				@Override
				public void run() {
					try {
						Thread.sleep(3);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}

					x.incrementAndGet();
				}
			});
		}

		ze.executeInQueue(lll);

		while (x.get() < n) {

		}
		final long t2 = System.currentTimeMillis();

		System.out.println("n = " + n + "\t" + "ms = " + (t2 - t1));

	}

	public static void test_Executors_AND_ZE_1() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZETest_1.test_Executors_AND_ZE_1()");

		final ExecutorService service = Executors.newFixedThreadPool(10);
		final ZE ze = ZES.newZE(10);
		final int n = 1000;

		final AtomicInteger x = new AtomicInteger();
		final long t1 = System.currentTimeMillis();
		for (int i = 1; i <= n; i++) {
//				service.execute(new Runnable() {
//					@Override
//					public void run() {
//						try {
//							Thread.sleep(3);
//						} catch (final InterruptedException e) {
//							e.printStackTrace();
//						}
//						x.incrementAndGet();
//						System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName());
//					}
//				});

			ze.executeInQueue(new ZERunnable<String>() {
				@Override
				public void run() {
//						try {
//							Thread.sleep(3);
//						} catch (final InterruptedException e) {
//							e.printStackTrace();
//						}
					x.incrementAndGet();
//						System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName());
				}
			});

		}

		while (x.get() < n) {

		}
		final long t2 = System.currentTimeMillis();

		System.out.println("n = " + n + "\t" + "ms = " + (t2 - t1));

	}

	public static void test_byName1() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZETest_1.test_byName1()");

		final ZE ze = ZES.newZE(1);

		ze.executeByNameInASpecificThread("A", () -> {
			sleepMS(100);
			System.out.println("A");
		});

		ze.executeByNameInASpecificThread("B", () -> {

			System.out.println("B");
		});
		ze.executeByNameInASpecificThreadPriority("C", () -> {

			System.out.println("C");
		});

	}

	public static void test_reassign_1() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZETest_1.test_reassign_1()");

		final ZE ze = ZES.newZE(2);

		final int n = 5;
		for (int i = 1; i <= n; i++) {

			final Integer k = i;
//				ze.executeByNameInASpecificThread("1", () -> {
			//
//					sleepMS(500);
//					System.out.println(Thread.currentThread().getName() + "\t" + "execute -500ms- " + k);
			//
//				});
			ze.executeInQueue(() -> {

				sleepMS(500);
				System.out.println(Thread.currentThread().getName() + "\t" + "execute -500ms- " + k);

			});
		}

		final int n2 = 5;
		for (int i = 1; i <= n2; i++) {

			final Integer k = i;
//				ze.executeByNameInASpecificThread("2",() -> {
			ze.executeInQueue(() -> {

				sleepMS(100);
				System.out.println(Thread.currentThread().getName() + "\t" + "execute -100ms - " + k);

			});

		}

		System.out.println("n1 n2 都派发完成");

	}

	public static void test_arrange1() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZETest_1.test_arrange1()");

		final ZE ze = ZES.newZE();

		final ZETaskStep step1 = new ZETaskStep(Lists.newArrayList(() -> System.out.println("1")));

		final ZETaskStep step2 = new ZETaskStep(
				Lists.newArrayList(() -> System.out.println("2"), () -> System.out.println("3")));

		final ZETaskStep step3 = new ZETaskStep(Lists.newArrayList(() -> System.out.println("4")));

		ze.arrange(step1, step2, step3);

	}

	public static void test_submitImmediatelyAndGet2() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZETest_1.test_submitImmediatelyAndGet2()");

		final ZE ze = ZES.newZE(1);

		final AbstractZETask<String> task = new AbstractZETask<String>() {

			@Override
			public String call() {
//					final int n = 20 / 0;
				return "A" + "\t" + Thread.currentThread().getName();
			}
		};
		final String v = ze.submitInQueuePriorityAndGet(task);

		System.out.println("vvv = " + v);
	}

	public static void test_submitImmediatelyAndGetList1() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZETest_1.test_submitImmediatelyAndGetList1()");

		final ZE ze = ZES.newZE(50);
		final int n = 200;
		final List<AbstractZETask<String>> tl = Lists.newArrayList();
		final long t1 = System.currentTimeMillis();
		for (int i = 1; i <= n; i++) {
			final AbstractZETask<String> task = new AbstractZETask<String>() {

				@Override
				public String call() {
					final int ms = ZR.nextInt(100);
					try {
						Thread.sleep(ms);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}
					return ms + "\t" + "A" + "\t" + Thread.currentThread().getName();
				}
			};

			tl.add(task);
		}

//			final List<String> vl = ze.submitInQueueAndGet(tl);
//			final List<String> vl = ze.submitImmediatelyAndGet(tl);
		final List<String> vl = ze.submitInQueuePriorityAndGet(tl);

		final long t2 = System.currentTimeMillis();
		System.out.println("vl.size = " + vl.size());
		for (final String string : vl) {
			System.out.println("\t\t" + string);
		}
		System.out.println("vl.size = " + vl.size());

		System.out.println("zongms = " + (t2 - t1));

	}

	public static void test_submitImmediatelyAndGet1() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZETest_1.test_submitImmediatelyAndGet1()");

		final ZE ze = ZES.newZE(1);

		final AbstractZETask<String> task = new AbstractZETask<String>() {

			@Override
			public String call() {
				return "A" + "\t" + Thread.currentThread().getName();
			}
		};

//			final String v = ze.submitInQueueAndGet(task);
//			final String v = ze.submitInQueuePriorityAndGet(task);
		final String v = ze.submitImmediatelyAndGet(task);

		System.out.println("v = " + v);

	}

	public static void test_submitList1() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZETest_1.test_submitList1()");

		final ZE ze = ZES.newZE(1);

		final int i = 20;

		final List<AbstractZETask<String>> list = Lists.newArrayList();
		for (int k = 1; k <= i; k++) {
			final AbstractZETask<String> task = new AbstractZETask<String>() {

				@Override
				public String call() {
					final int ms = ZR.nextInt(1000);
					try {
						Thread.sleep(ms);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}
					return ms + "\t" + "A" + "\t" + Thread.currentThread().getName();
				}
			};

			list.add(task);
		}

		final List<ZETaskResult<String>> rList = ze.submitInQueue(list);
		System.out.println("rList.size = " + rList.size());

		for (final ZETaskResult<String> zeTaskResult : rList) {
			final String string = zeTaskResult.get();
			System.out.println("string = " + string);
		}

		System.out.println("------------------list.get ok");

	}

	public static void test_ZET_daoqu_1() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZETest_1.test_ZET_daoqu_1()");

		final ZE ze = ZES.newZE(2);

		ze.executeInQueue(() -> {
			sleepMS(10);
			System.out.println(Thread.currentThread().getName() + "\t" + "100");
		});

		ze.executeInQueue(() -> {
			sleepMS(500);
			System.out.println(Thread.currentThread().getName() + "\t" + "500");
		});

		ze.executeInQueue(() -> {
			System.out.println(Thread.currentThread().getName() + "\t" + "20");
		});

	}

	public static void test_groouName_1() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZETest_1.test_groouName_1()");

		final ZE ze = ZES.newZE();

		ze.executeInQueue(() -> {

			sleepMS(1000);

			System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
					+ "ZETest_1.test_groouName_1()");
			System.out.println("A");

		});

		ze.executeInQueue(() -> {
			System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
					+ "ZETest_1.test_groouName_1()");
			System.out.println("B");

		});

	}

	public static void test_submitTaskImmediately() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZETest_1.test_submitTaskImmediately()");

		final ZE ze = ZES.newZE(2);

		final ZETaskResult<String> result = ze.submitImmediately(new AbstractZETask<String>() {

			@Override
			public String call() {
				try {
					Thread.sleep(2000);
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
				return "OK-from-absTask.call";
			}
		});

		final ZETaskResult<String> result2 = ze.submitImmediately(new AbstractZETask<String>() {

			@Override
			public String call() {
				return "OK-from-absTask.call222222222222";
			}
		});

		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t" + "result2 = "
				+ result2);
		System.out.println(
				java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t" + "result = " + result);

		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "result2.get = " + result2.get());
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "result.get = " + result.get());

		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t" + "result2 = "
				+ result2);
		System.out.println(
				java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t" + "result = " + result);

	}

	public static void test_test1() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZETest_1.test_test1()");

		final ZE ze = ZES.newZE(22);

		ze.executeInQueue(() -> {

		});

		ze.executeInQueue(() -> System.out.println(LocalDateTime.now()));
		ze.executeInQueue(() -> {
			System.out.println(LocalDateTime.now());
		});
	}

	public static void test_Priority_2() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZETest_1.test_Priority_2()");

		final ZE ze = ZES.newZE(22);

		final String name = "name";
		ze.submitByNameInASpecificThread(name, new AbstractZETask<String>() {

			@Override
			public String call() {
				sleepMS(100);
				System.out.println("A");
				return null;
			}
		});
		ze.submitByNameInASpecificThreadPriority(name, new AbstractZETask<String>() {

			@Override
			public String call() {
				sleepMS(100);
				System.out.println("B");
				return null;
			}
		});

		ze.submitByNameInASpecificThreadPriority(name, new AbstractZETask<String>() {

			@Override
			public String call() {
				sleepMS(100);
				System.out.println("C");
				return null;
			}
		});

	}

	public static void test_Priority_1() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZETest_1.test_Priority_1()");

		final ZE ze = ZES.newZE(22);

		ze.executeInQueue(() -> {
			sleepMS(50);
			final String name = Thread.currentThread().getName();

			System.out.println(name + "\t" + "A");

		});
		ze.executeInQueue(() -> {
			sleepMS(20);
			final String name = Thread.currentThread().getName();

			System.out.println(name + "\t" + "B");

		});
		ze.executeInQueuePriority(() -> {
			sleepMS(1101);
			final String name = Thread.currentThread().getName();

			System.out.println(name + "\t" + "C");

		});
		ze.executeInQueue(() -> {
//				sleepMS(110);
			final String name = Thread.currentThread().getName();

			System.out.println(name + "\t" + "D");

		});

	}

	private static void sleepMS(final int ms) {
		if (ms <= 0) {
			return;
		}

		try {
			Thread.sleep(ms);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static int xxx = 0;

	public static void test_inqueue() throws InterruptedException {

		final ZE ze = ZES.newZE(5000);

		final int n = 10000 * 20;

		int aaa = 0;
		final int sleepMS = 0;
		final Set<Object> ssss = Sets.newConcurrentHashSet();
		for (int i = 1; i <= n; i++) {

			final int k = i;
			final boolean execute = ze.executeInQueue(() -> {

				try {
					Thread.sleep(1232232);
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}

				final String name = Thread.currentThread().getName();
//					System.out.println("k = " + k + "\t" + "此线程忙完了-kname = " + name);
				ssss.add(k);
//					System.out.println("已执行任务个数 = " + ssss.size());
			});

			aaa++;
			System.out.println("派发结果 " + "i = " + i + "\t" + "execute = " + execute);
		}

//			Thread.sleep((n * sleepMS) + 200);

		System.out.println("派发结束");
		System.out.println("派发.count = " + aaa);
		System.out.println("ze.taskQueueSize() = " + ze.taskQueueSize());
		System.out.println("taskQueue.poll.count = " + xxx);

	}

	public static void test_executeByNameInASpecificThread() {

		final ZE ze = ZES.newZE(2);

		final int n = 5;

		for (int i = 1; i <= n; i++) {

			final int k = i;
			final ZETaskResult<String> taskResult = ze.submitByNameInASpecificThread("lisi",
					new AbstractZETask<String>() {

						@Override
						public String call() {
							try {
								Thread.sleep(1000);
							} catch (final InterruptedException e) {
								e.printStackTrace();
							}
							System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName()
									+ "\t" + "lisi");

							return null;
						}

					});

			System.out.println("派发lisi  " + "i = " + i + "\t" + "execute = " + taskResult.isExecuted());
		}

		final int n2 = 5;
		for (int i = 1; i <= n2; i++) {

			final int k = i;
			final ZETaskResult<String> taskResult = ze.submitByNameInASpecificThread("wangwu",
					new AbstractZETask<String>() {

						@Override
						public String call() {
							try {
								Thread.sleep(500);
							} catch (final InterruptedException e) {
								e.printStackTrace();
							}
							System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName()
									+ "\t" + "wangwu");

							return null;
						}
					});
			System.out.println("派发wangwu i = " + i + "\t" + "execute = " + taskResult.isExecuted());
		}

		final int n3 = 5;
		for (int i = 1; i <= n2; i++) {

			final int k = i;
			final ZETaskResult<String> taskResult = ze.submitByNameInASpecificThread("zhang",
					new AbstractZETask<String>() {

						@Override
						public String call() {
							System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName()
									+ "\t" + "zhang");

							return null;
						}
					});
			System.out.println("派发zhang i = " + i + "\t" + "execute = " + taskResult.isExecuted());
		}

	}

	public static void test_ZETask_1() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZETest_1.test_ZETask_1()");

		final ZE ze = ZES.newZE(343);

		ze.executeInQueue(() -> System.out.println("zerunnable-ok"));

		ze.submitImmediately(new AbstractZETask<String>() {

			@Override
			public String call() {
				System.out.println("AbstractZETask-ok");
				return null;
			}
		});

	}

	public static void test_ZERunnable1() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZETest_1.test_ZERunnable1()");

		final ZE ze = ZES.newZE(1);
		final int n = 10000;
		for (int i = 1; i <= n; i++) {

			final int k = i;
			final boolean execute = ze
					.executeInQueue(() -> System.out.println(Thread.currentThread().getName() + "\t" + k));
			System.out.println("i = " + i + "\t" + "execute = " + execute);
		}

		System.out.println("派发结束,n = " + n);

	}

	public static void test_Exception_interface_NOT_ERROR_1() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZETest_1.test_Exception_interface_NOT_ERROR_1()");

		final ZE ze = ZES.newZE(1200);

		ze.executeInQueue(() -> {
			final int n = 20 / 0;
		});

		ze.submitInQueue(new AbstractZETask<String>() {

			@Override
			public String call() {
				final int n = 20 / 0;
				return "A";
			}
		});

	}

	public static void test_Exception1() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZETest_1.test_Exception1()");

		final ZE ze = ZES.newZE(1200);

		final AbstractZETask<String> zeTask = new AbstractZETask<String>() {

			@Override
			public String call() {
				final int n = 20 / 0;
//					final int n = 20;
				return "A";
			}

		};

		ze.submitImmediately(zeTask);

	}

	public static void test_name_putIntQueue1() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZETest_1.test_name_putIntQueue1()");

		final ZE ze = ZES.newZE(1);

		final int n = 1000;
		for (int i = 1; i <= n; i++) {
			final ZETaskResult<String> taskResult = ze.submitByNameInASpecificThread("zhang",
					new AbstractZETask<String>() {

						@Override
						public String call() {
							try {
								Thread.sleep(5);
							} catch (final InterruptedException e) {
								e.printStackTrace();
							}
							final String name = Thread.currentThread().getName();
							System.out.println("name = " + name + "\t" + "执行了...");
							return "OK";
						}
					});

			System.out.println("i = " + i + "\t" + "execute = " + taskResult.isExecuted());
		}

		System.out.println("派发结束,n = " + n);

	}

	public static void putINtQueue_1() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZETest_1.putINtQueue_1()");

		final ZE ze = ZES.newZE(1);

		final int n = 10000;
		for (int i = 1; i <= n; i++) {

			final ZETaskResult<String> result = ze.submitImmediately(new AbstractZETask<String>() {

				@Override
				public String call() {
//						try {
//							Thread.sleep(1);
//						} catch (final InterruptedException e) {
//							e.printStackTrace();
//						}
					System.out.println("vvvvv");
					return null;
				}
			});

			System.out.println("i = " + i + "\t" + "execute = " + result.isArranged());
		}

		System.out.println("派发结束,n = " + n);

//			ze.execute(new ZETask<String>() {
		//
//				@Override
//				public String call() {
//					System.out.println(222);
//					return null;
//				}
//			},true);

	}

	public static void test_timeoutMS1() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZETest_1.test_timeoutMS1()");

		final ZE ze = ZES.newZE(10);

		final int n = 5;
//			final int n= 10000 * 200;
		for (int i = 1; i <= n; i++) {

			final AbstractZETask<String> task = new AbstractZETask<String>() {

				@Override
				public String call() {
					final int millis = 1222;
					try {
						Thread.sleep(millis);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}
					return "OK";
				}

				@Override
				public String onSuccess(final String v) {
					final String name = Thread.currentThread().getName();
					System.out.println(name + "\t" + LocalDateTime.now() + "\t" + "执行完了, onSuccess-v = " + v);
					return v;
				}

			};

			final ZETaskResult<String> result = ze.submitImmediately(task);
			System.out.println("i = " + i + "\t" + "execute = " + result.isArranged());

//				System.out.println(LocalDateTime.now() + "\t" + "execute");
//				final String v = task.get(500);
//				System.out.println(LocalDateTime.now());
//				System.out.println(LocalDateTime.now() + "\t" + "v = " + v);

		}

		System.out.println("派发结束,n = " + n);
	}

	public static <T> void test_callable1() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZETest_1.test_callable1()");

		final ZE ze = ZES.newZE();
		final int n = 2000;
		for (int i = 1; i <= n; i++) {

			final int k = i;
			final ZETaskResult<String> result = ze.submitImmediately(new AbstractZETask<String>() {

				@Override
				public String call() {
					sleepMS(111);
					return String.valueOf(k);
				}

				@Override
				public String onSuccess(final String v) {
					System.out.println("onSuccess.v = " + v);
					return v;
				}
			});

			System.out.println("i = " + i + "\t" + "execute = " + result.isArranged());
		}

	}

	@Test
	public void test_zerunnnable_1() {

		final ZE ze = ZES.newZE(4);

		ze.executeImmediately(() -> System.out.println("A"));
		ze.executeImmediately(() -> System.out.println("B"));
		ze.executeImmediately(() -> System.out.println("C"));
		ze.executeImmediately(() -> System.out.println("D"));

	}

}
