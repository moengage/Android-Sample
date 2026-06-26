package com.moengage.example.ordertracking.live

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

private val orderTrackingJob = SupervisorJob()

/** Application-wide scope for push handling and receiver I/O (off main thread). */
internal val orderTrackingScope = CoroutineScope(orderTrackingJob + Dispatchers.Default)
