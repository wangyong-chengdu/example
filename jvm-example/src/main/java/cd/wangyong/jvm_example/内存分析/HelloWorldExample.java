package cd.wangyong.jvm_example.内存分析;

/**
 * -XX:NativeMemoryTracking=summary -XX:+UnlockDiagnosticVMOptions -XX:+PrintNMTStatistics
 * @author andy
 * @since 2021/1/22
 */
public class HelloWorldExample {

    public static void main(String[] args) {
        System.out.println("hello, world.");
    }
}

//hello, world.
//
//        Native Memory Tracking:
//
//        Total: reserved=4489463KB, committed=332455KB
//        -                 Java Heap (reserved=3039232KB, committed=190464KB)
//        (mmap: reserved=3039232KB, committed=190464KB)
//
//        -                     Class (reserved=1062015KB, committed=10111KB)
//        (classes #531)
//        (malloc=5247KB #173)
//        (mmap: reserved=1056768KB, committed=4864KB)
//
//        -                    Thread (reserved=14510KB, committed=14510KB)
//        (thread #14)
//        (stack: reserved=14352KB, committed=14352KB)
//        (malloc=44KB #76)
//        (arena=113KB #29)
//
//        -                      Code (reserved=249633KB, committed=2569KB)
//        (malloc=33KB #317)
//        (mmap: reserved=249600KB, committed=2536KB)
//
//        -                        GC (reserved=116815KB, committed=107543KB)
//        (malloc=5771KB #119)
//        (mmap: reserved=111044KB, committed=101772KB)
//
//        -                  Compiler (reserved=166KB, committed=166KB)
//        (malloc=2KB #27)
//        (arena=164KB #4)
//
//        -                  Internal (reserved=5362KB, committed=5362KB)
//        (malloc=5330KB #1516)
//        (mmap: reserved=32KB, committed=32KB)
//
//        -                    Symbol (reserved=1512KB, committed=1512KB)
//        (malloc=960KB #157)
//        (arena=552KB #1)
//
//        -    Native Memory Tracking (reserved=42KB, committed=42KB)
//        (malloc=3KB #39)
//        (tracking overhead=39KB)
//
//        -               Arena Chunk (reserved=177KB, committed=177KB)
//        (malloc=177KB)