package com.jamesward.akkafun;

import java.util.ArrayList;
import java.util.List;

import akka.dispatch.Future;
import static akka.dispatch.Futures.future;
import static akka.dispatch.Futures.sequence;

public class SimpleFutures
{
    public static void main(String[] args)
    {
        List<Future<Long>> futures = new ArrayList<Future<Long>>();

        System.out.println("Adding futures for two random length pauses");

        futures.add(future(new RandomPause()));
        futures.add(future(new RandomPause()));

        System.out.println("There are " + futures.size() + " RandomPause's currently running");

        // compose a sequence of the futures
        Future<Iterable<Long>> futuresSequence = sequence(futures);

        // block until the futures come back
        Iterable<Long> results = futuresSequence.get();

        System.out.println("All RandomPause's are complete");

        Long totalPause = 0L;
        for (Long result : results)
        {
            System.out.println("One pause was for " + result + " milliseconds");
            totalPause += result;
        }

        System.out.println("Total pause was for " + totalPause + " milliseconds");
    }
}