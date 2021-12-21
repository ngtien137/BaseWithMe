package com.base.baselibrary.views.custom.swipe_reveal

import android.os.Bundle
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet


class ViewBinderHelper {
    // region Const and Fields
    private var mapStates = Collections.synchronizedMap(HashMap<String, Int>())
    private val mapLayouts  = Collections.synchronizedMap(HashMap<String, SwipeRevealLayout>())
    private val lockedSwipeSet = Collections.synchronizedSet(HashSet<String>())

    @Volatile var openOnlyOne = false
    private val stateChangeLock = Object()
    // endregion

    // region public method
    fun bind(swipeLayout: SwipeRevealLayout, id: String){
        if (swipeLayout.shouldRequestLayout()){
            swipeLayout.requestLayout()
        }

        mapLayouts.values.remove(swipeLayout)
        mapLayouts[id] = swipeLayout

        swipeLayout.abort()
        swipeLayout.setDragStateChangeListener(object: DragStateChangeListener {
            override fun onDragStateChanged(state: Int) {
                mapStates[id] = state

                if (openOnlyOne){
                    closeOthers(id, swipeLayout)
                }
            }
        })

        // first time binding.
        if (!mapStates.containsKey(id)) {
            mapStates[id] = SwipeRevealLayout.STATE_CLOSE
            swipeLayout.close(false)
        }
        // not the first time, then close or open depends on the current state.
        else {
            val state = mapStates[id]

            if (state == SwipeRevealLayout.STATE_CLOSE || state == SwipeRevealLayout.STATE_CLOSING ||
                state == SwipeRevealLayout.STATE_DRAGGING
            ) {
                swipeLayout.close(false)
            } else {
                swipeLayout.open(false)
            }
        }

        // set lock swipe
        swipeLayout.setLockDrag(lockedSwipeSet.contains(id))
    }

    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in [android.app.Activity.onSaveInstanceState]
     */
    fun saveStates(outState: Bundle?) {
        if (outState == null) return
        val statesBundle = Bundle()
        for ((key, value) in mapStates) {
            statesBundle.putInt(key, value!!)
        }
        outState.putBundle(BUNDLE_MAP_KEY, statesBundle)
    }

    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in [android.app.Activity.onRestoreInstanceState]
     */
    fun restoreStates(inState: Bundle?) {
        if (inState == null) return
        if (inState.containsKey(BUNDLE_MAP_KEY)) {
            val restoredMap: HashMap<String, Int> = HashMap()
            val statesBundle = inState.getBundle(BUNDLE_MAP_KEY)
            val keySet = statesBundle!!.keySet()
            if (keySet != null) {
                for (key in keySet) {
                    restoredMap[key] = statesBundle.getInt(key)
                }
            }
            mapStates = restoredMap
        }
    }

    /**
     * Lock swipe for some layouts.
     * @param id a string that uniquely defines the data object.
     */
    fun lockSwipe(vararg id: String?) {
        setLockSwipe(true, id)
    }

    /**
     * Unlock swipe for some layouts.
     * @param id a string that uniquely defines the data object.
     */
    fun unlockSwipe(vararg id: String?) {
        setLockSwipe(false, id)
    }

    /**
     * Open a specific layout.
     * @param id unique id which identifies the data object which is bind to the layout.
     */
    fun openLayout(id: String){
        synchronized(stateChangeLock){
            mapStates[id] = SwipeRevealLayout.STATE_OPEN;

            when {
                mapLayouts.containsKey(id) -> {
                    val layout = mapLayouts[id]
                    layout?.open(true)
                }
                openOnlyOne -> {
                    closeOthers(id, mapLayouts[id])
                }
                else -> { }
            }
        }
    }

    /**
     * Close a specific layout.
     * @param id unique id which identifies the data object which is bind to the layout.
     */
    fun closeLayout(id: String?) {
        synchronized(stateChangeLock) {
            mapStates[id] = SwipeRevealLayout.STATE_CLOSE
            if (mapLayouts.containsKey(id)) {
                val layout = mapLayouts[id]
                layout!!.close(true)
            }
        }
    }

    /**
     * Close others swipe layout.
     * @param id layout which bind with this data object id will be excluded.
     * @param swipeLayout will be excluded.
     */
    private fun closeOthers(id: String, swipeLayout: SwipeRevealLayout?) {
        synchronized(stateChangeLock) {
            // close other rows if openOnlyOne is true.
            if (getOpenCount() > 1) {
                for (entry in mapStates.entries) {
                    if (entry.key != id) {
                        entry.setValue(SwipeRevealLayout.STATE_CLOSE)
                    }
                }
                for (layout in mapLayouts.values) {
                    if (layout != swipeLayout) {
                        layout.close(true)
                    }
                }
            }
        }
    }

    private fun setLockSwipe(lock: Boolean,id: Array<out String?>?) {
        if (id == null || id.isEmpty()) return
        if (lock) lockedSwipeSet.addAll(id.toList()) else lockedSwipeSet.removeAll(
            id.toList()
        )
        for (s in id) {
            val layout = mapLayouts[s]
            layout?.setLockDrag(lock)
        }
    }

    private fun getOpenCount(): Int {
        var total = 0
        for (state in mapStates.values) {
            if (state == SwipeRevealLayout.STATE_OPEN || state == SwipeRevealLayout.STATE_OPENING) {
                total++
            }
        }
        return total
    }
    // endregion

    companion object {
        private const val BUNDLE_MAP_KEY = "ViewBinderHelper"
    }
}