package com.jamesward.akkafun;

import static akka.dispatch.Futures.future;
import static akka.dispatch.Futures.sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.jamesward.akkafun.PrintResult;
import com.jamesward.akkafun.RandomPause;
import scala.concurrent.Await;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.dispatch.ExecutionContexts;
import akka.dispatch.Mapper;

public class SimpleFutures {
    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(4);
        ExecutionContext ec = ExecutionContexts.fromExecutorService(executor);

        List<Future<Long>> futures = new ArrayList<Future<Long>>();

        System.out.println("Akka Futures says: Adding futures for two random length pauses");

        futures.add(future(new RandomPause(), ec));
        futures.add(future(new RandomPause(), ec));

        System.out.println("Akka Futures says: There are " + futures.size()
                + " RandomPause's currently running");

        // compose a sequence of the futures
        Future<Iterable<Long>> futuresSequence = sequence(futures, ec);

        // Find the sum of the odd numbers
        Future<Long> futureSum = futuresSequence.map(
                new Mapper<Iterable<Long>, Long>() {
                    public Long apply(Iterable<Long> ints) {
                        long sum = 0;
                        for (Long i : ints)
                            sum += i;
                        return sum;
                    }
                }, ec);

        // block until the futures come back
        futureSum.onSuccess(new PrintResult<Long>(), ec);

        try {
            System.out.println("Result :" + Await.result(futureSum, Duration.apply(5, TimeUnit.SECONDS)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        executor.shutdown();
    }

}

