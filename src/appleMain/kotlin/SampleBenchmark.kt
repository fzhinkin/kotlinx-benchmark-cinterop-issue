package org.example

import kotlinx.benchmark.*

@State(Scope.Benchmark)
open class SampleBenchmark {
    @Benchmark
    @OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
    fun cinterop(): Int = org.example.native.nativeCall()
}
