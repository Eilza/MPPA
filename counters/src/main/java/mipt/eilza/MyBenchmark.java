/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package mipt.eilza;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;
import java.util.concurrent.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static junit.framework.TestCase.assertEquals;

public class MyBenchmark {

    private static int incrementCallsCount = 1000000;
    private static int nThreads = 4;

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }


    @Benchmark
    @BenchmarkMode(Mode.All)
    @Fork(value = 2)
    @Warmup(iterations = 2)
    @Measurement(iterations = 3)
    @Group("testCounterSynchronized")
    @GroupThreads(1)
//    @OperationsPerInvocation()
    public void testCounterSynchronized(Blackhole blackhole) throws ExecutionException, InterruptedException {
        CounterSynchronized counterSynchronized = new CounterSynchronized();
        long count = checkCounter(incrementCallsCount, nThreads, counterSynchronized);
        blackhole.consume(count);
    }

    @Benchmark
    @BenchmarkMode(Mode.All)
    @Fork(value = 2)
    @Warmup(iterations = 2)
    @Measurement(iterations = 3)
    @Group("testCounterLock")
    @GroupThreads(1)
    public void testReentrantLock(Blackhole blackhole) throws ExecutionException, InterruptedException {
        CounterReentrantLock counterReentrantLock = new CounterReentrantLock();
        long count = checkCounter(incrementCallsCount, nThreads, counterReentrantLock);
        blackhole.consume(count);
    }

    @Benchmark
    @BenchmarkMode(Mode.All)
    @Fork(value = 2)
    @Warmup(iterations = 2)
    @Measurement(iterations = 3)
    @Group("testCounterAtomicLong")
    @GroupThreads(1)
    public void testCounterAtomicLong(Blackhole blackhole) throws ExecutionException, InterruptedException {
        CounterAtomicLong counterAtomicLong = new CounterAtomicLong();
        long count = checkCounter(incrementCallsCount, nThreads, counterAtomicLong);
        blackhole.consume(count);
    }


    @Benchmark
    @BenchmarkMode(Mode.All)
    @Fork(value = 2)
    @Warmup(iterations = 2)
    @Measurement(iterations = 3)
    @Group("testCounterSequential")
    @GroupThreads(1)
    public void testCounterSequential(Blackhole blackhole) throws ExecutionException, InterruptedException {
        CounterSequential counterSequential = new CounterSequential();
        for(int i = 0; i < incrementCallsCount; i++) {
            counterSequential.increment();
            blackhole.consume(counterSequential.getValue());
        }
    }

    private static long checkCounter(int incrementCallsCount, int nThreads, Counter counter) throws ExecutionException, InterruptedException {

        ExecutorService executors = Executors.newFixedThreadPool(nThreads);

        List<Future> futures = range(0, incrementCallsCount)
                .mapToObj(i -> executors.submit(incrementRunnable(counter)))
                .collect(toList());
        for (Future future : futures) {
            future.get();
        }
        assertEquals("Oops! Smth is wrong!", incrementCallsCount, counter.getValue());
        executors.shutdownNow();
        return counter.getValue();
    }
    private static Runnable incrementRunnable(Counter counter) {
        return counter::increment;
    }

}
