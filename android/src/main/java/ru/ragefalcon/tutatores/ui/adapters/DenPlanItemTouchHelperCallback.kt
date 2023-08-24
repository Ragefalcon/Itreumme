package ru.ragefalcon.tutatores.ui.adapters

/**
class DenPlanItemTouchHelperCallback<T: Id_class, K: RVMainAdapterViewHolder<T>>(
        val adapter: RVMainAdapter<T, K>,
        val swipeListener: (T) -> Unit
    ) : ItemTouchHelper.Callback() {
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            return makeFlag(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.ACTION_STATE_IDLE)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//            swipeListener.invoke(adapter.items[viewHolder.adapterPosition])
        }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
//        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE && viewHolder is RVMainAdapterViewHolder<*>) {
        Log.d("item","onSelectedChanged: ${viewHolder}")
        if (viewHolder is RVMainAdapterViewHolder<*>) {
//            viewHolder.onItemSelected()
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        Log.d("item","clearView: ${viewHolder}")
//        if (viewHolder is RVMainAdapterViewHolder<*>) viewHolder.onItemCleared()
    }

}
        */