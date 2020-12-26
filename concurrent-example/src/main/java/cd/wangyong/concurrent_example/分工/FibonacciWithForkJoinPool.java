package cd.wangyong.concurrent_example.分工;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * @author andy
 * @since 2020/12/25
 */
public class FibonacciWithForkJoinPool {
    static class Fibonacci extends RecursiveTask<Integer> {
        private final int n;

        public Fibonacci(int n) {
            this.n = n;
        }

        @Override
        protected Integer compute() {
            if (n <= 1) return n;

            Fibonacci f1 = new Fibonacci(n - 1);
            f1.fork();

            Fibonacci f2 = new Fibonacci(n - 2);
            return f2.compute() + f1.join();
        }
    }

    public static void main(String[] args) {
        ForkJoinPool fjp = new ForkJoinPool(4);
        Fibonacci fibonacci = new Fibonacci(30);
        Integer res = fjp.invoke(fibonacci);
        System.out.println(res);
    }


    /**
     * 自顶向下，递归、备忘录解法
     */
    public int fib(int N) {
        if (N <= 0) return 0;
        int[] memory = new int[N + 1];
        return help(N, memory);
    }

    private int help(int n, int[] memory) {
        if (n == 1 || n == 2) return 1;
        if (memory[n] != 0) return memory[n];
        else memory[n] = help(n - 1, memory) + help(n - 2, memory);
        return memory[n];
    }
}
