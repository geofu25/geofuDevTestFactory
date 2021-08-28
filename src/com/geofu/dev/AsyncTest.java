package com.geofu.dev;

import static org.junit.Assert.*;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.LogManager;
import java.util.stream.Collectors;

import org.junit.Test;

public class AsyncTest {
	
	final int THREAD_MIN_COUNT = 1;
	final int THREAD_MAX_COUNT = 10;
	
	@Test
	public void test() {
		
		// 처리 시간 측정을 위한 변수
		long start = 0L;
		long end = 0L;
		
		List<String> result = new ArrayList<String>();
		
		// 단일 쓰레드 
		start = System.currentTimeMillis();
		result = onAsync(THREAD_MIN_COUNT);
		result.stream().forEach((x)->System.out.println(x));
		end = System.currentTimeMillis();
		System.out.println( "단일 쓰레드 처리 실행 시간 : " + ( end - start )/1000.0 +"\n");
		
		// 멀티 쓰레드
		start = System.currentTimeMillis();
		result = onAsync(THREAD_MAX_COUNT);
		result.stream().forEach((x)->System.out.println(x));
		end = System.currentTimeMillis();
		System.out.println( "멀티 쓰레드 처리 실행 시간 : " + ( end - start )/1000.0 +"초");
	}
	
	// 비동기 처리 로직
	public List<String> onAsync(int tread_cnt) {
		
		ExecutorService executor = Executors.newFixedThreadPool(tread_cnt);
		
		// Thread Pool 개수 만큼 list 생성
		List<Integer> list = new ArrayList<Integer>();
		for(int i=0; i<THREAD_MAX_COUNT; i++)
			list.add(i+1);
		
		List<CompletableFuture<String>> completableFutureList = list.stream()
				.map((map -> CompletableFuture.supplyAsync(() -> {
					try {
						Thread.sleep(500);
					}catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return Thread.currentThread().getName() + ": running. ";
						
		}, executor))).collect(Collectors.toList());
		
		return completableFutureList.stream().map(CompletableFuture::join).collect(Collectors.toList());
	}
	
}
