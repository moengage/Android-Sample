package com.moengage.example.ordertracking

/**
 * Application-wide [CoroutineScope] for order-tracking work started from push callbacks and
 * broadcast receivers, so MoEngage and receiver threads are not blocked on disk I/O.
 */

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

private val orderTrackingJob = SupervisorJob()

/** Application-wide scope for order-tracking push handling and receiver I/O. */
internal val orderTrackingScope = CoroutineScope(orderTrackingJob + Dispatchers.Default)
