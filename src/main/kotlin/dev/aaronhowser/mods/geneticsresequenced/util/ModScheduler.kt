package dev.aaronhowser.mods.geneticsresequenced.util

import com.google.common.collect.HashMultimap
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced

object ModScheduler {

    var currentTick = 0
        set(value) {
            field = value
            handleScheduledTasks(value)
        }

    private val upcomingTasks: HashMultimap<Int, Runnable> = HashMultimap.create()

    fun scheduleTaskInTicks(ticksInFuture: Int, runnable: Runnable) {
        if (ticksInFuture > 0) {
            upcomingTasks.put(currentTick + ticksInFuture, runnable)
        } else {
            runnable.run()
        }
    }

    private fun handleScheduledTasks(tick: Int) {

        if (!upcomingTasks.containsKey(tick)) return

        val tasks = upcomingTasks[tick].iterator()

        while (tasks.hasNext()) {
            try {
                tasks.next().run()
            } catch (e: Exception) {
                GeneticsResequenced.LOGGER.error(e.toString())
            }

            tasks.remove()
        }

    }

}