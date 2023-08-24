package ru.ragefalcon.tutatores.adapter.deprecated

import java.util.*

/**
class RasxodAdapter(val typeF: FinanceType, val listener: (ItemRasxod) -> Unit) :
    RecyclerView.Adapter<RasxodAdapter.RasxodAdapterViewHolder>() {

   var items: List<ItemRasxod> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RasxodAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (typeF) {
            FinanceType.RASXOD -> RasxodAdapterViewHolder(
                convertView = inflater.inflate(
                    R.layout.item_rasxod,
                    parent,
                    false
                )
            )
            FinanceType.DOXOD -> RasxodAdapterViewHolder(
                convertView = inflater.inflate(
                    R.layout.item_doxod,
                    parent,
                    false
                )
            )
            FinanceType.SCHET -> RasxodAdapterViewHolder(
                convertView = inflater.inflate(
                    R.layout.item_schet,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RasxodAdapterViewHolder, position: Int) {
        Log.d("M_ChatAdapter", "onBindViewHolder $position")
        holder.bind(items[position], listener)
    }

    fun updateData(data: List<ItemRasxod>) {
        Log.d(
            "M_ChatAdapter", "update data adapter - new data ${data.size} hash : ${data.hashCode()}" +
                    "old data ${items.size} hash : ${items.hashCode()}"
        )

        val diffCallback = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean = items[oldPos].id == data[newPos].id

            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean =
                items[oldPos].hashCode() == data[newPos].hashCode()

            override fun getOldListSize(): Int = items.size

            override fun getNewListSize(): Int = data.size

        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items = data
        diffResult.dispatchUpdatesTo(this)
//        notifyDataSetChanged()
    }

//    abstract inner class RasxodAdapterViewHolder(convertView: View) :
//        RecyclerView.ViewHolder(convertView),
//        LayoutContainer {
//        override val containerView: View?
//            get() = itemView
//
//        abstract fun bind(item: ItemRasxod, listener: (ItemRasxod) -> Unit)
//
//    }


    class RasxodAdapterViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView),
        LayoutContainer { //ChatItemViewHolder(convertView),ItemTouchViewHolder
        override val containerView: View?
            get() = itemView

//        override fun onItemSelected() {
//            itemView.setBackgroundColor(Color.LTGRAY)
//        }
//
//        override fun onItemCleared() {
//            itemView.setBackgroundColor(Color.WHITE)
//        }

        /* override*/
        fun bind(item: ItemRasxod, listener: (ItemRasxod) -> Unit) { //Unit то же что void
            text_name.text = item.name
            text_type.text = item.type
            text_schet.text = item.schet
            text_summa.text = item.summa.toString()
            text_data.text = Date(item.data).format("dd MMM yyyy")
            itemView.setOnClickListener {
                listener.invoke(item)
            }
        }
    }
}*/