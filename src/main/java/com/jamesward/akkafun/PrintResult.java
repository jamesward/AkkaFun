package com.jamesward.akkafun;

import akka.dispatch.OnSuccess;

public final class PrintResult<T> extends OnSuccess<T> {

    @Override
    public final void onSuccess(T t) {

        System.out.println("PrintResults says: Total pause was for " + ((Long) t)
                + " milliseconds");
    }
}
