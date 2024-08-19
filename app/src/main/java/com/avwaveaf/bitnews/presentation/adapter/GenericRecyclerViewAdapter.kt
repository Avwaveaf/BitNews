package com.avwaveaf.bitnews.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * A generic RecyclerView adapter that supports multiple view types, allowing for different layouts
 * for the first item and the rest of the items in the list.
 *
 * @param VB The primary ViewBinding type used for most items.
 * @param VBAlt The alternative ViewBinding type used for the first item.
 * @param T The type of data being displayed in the RecyclerView.
 * @param inflatePrimary A lambda function that inflates the primary layout for regular items.
 * @param inflateAlt A lambda function that inflates the alternate layout for the first item.
 * @param bindPrimary A lambda function that binds data to the primary layout.
 * @param bindAlt A lambda function that binds data to the alternate layout.
 * @param areItemsTheSameCondition A lambda function to check if two items are the same.
 * @param areContentsTheSameCondition A lambda function to check if the contents of two items are the same.
 */
class GenericRecyclerViewAdapter<T, VB : ViewBinding, VBAlt : ViewBinding>(
    private val inflatePrimary: (LayoutInflater, ViewGroup, Boolean) -> VB,
    private val inflateAlt: (LayoutInflater, ViewGroup, Boolean) -> VBAlt,
    private val bindPrimary: (VB, T) -> Unit,
    private val bindAlt: (VBAlt, T) -> Unit,
    private val areItemsTheSameCondition: (T, T) -> Boolean,
    private val areContentsTheSameCondition: (T, T) -> Boolean
) : ListAdapter<T, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<T>() {

    /**
     * Determines if two items are the same based on the provided condition.
     *
     * @param oldItem The old item.
     * @param newItem The new item.
     * @return True if the items are the same, false otherwise.
     */
    override fun areItemsTheSame(oldItem: T & Any, newItem: T & Any): Boolean {
        return areItemsTheSameCondition(oldItem, newItem)
    }

    /**
     * Determines if the contents of two items are the same based on the provided condition.
     *
     * @param oldItem The old item.
     * @param newItem The new item.
     * @return True if the contents are the same, false otherwise.
     */
    override fun areContentsTheSame(oldItem: T & Any, newItem: T & Any): Boolean {
        return areContentsTheSameCondition(oldItem, newItem)
    }
}) {

    /**
    * Hold click listener for each item
    * */
    private var onItemClickListener: ((T) -> Unit)? = null

    /**
     * Setting the listener variable
     * */
    fun setOnItemClickListener(clickListener: ((T) -> Unit)) {
        onItemClickListener = clickListener
    }

    companion object {
        private const val VIEW_TYPE_FIRST = 0
        private const val VIEW_TYPE_OTHER = 1
    }

    /**
     * Returns the view type for the item at the given position. The first item uses a different
     * view type than the rest.
     *
     * @param position The position of the item.
     * @return An integer representing the view type.
     */
    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_FIRST else VIEW_TYPE_OTHER
    }

    /**
     * Creates a new ViewHolder based on the view type.
     *
     * @param parent The parent ViewGroup.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder instance.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_FIRST -> {
                val binding = inflateAlt(layoutInflater, parent, false)
                AltViewHolder(binding)
            }

            else -> {
                val binding = inflatePrimary(layoutInflater, parent, false)
                PrimaryViewHolder(binding)
            }
        }
    }

    /**
     * Binds the data to the ViewHolder based on its type.
     *
     * @param holder The ViewHolder to bind data to.
     * @param position The position of the item in the list.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is GenericRecyclerViewAdapter<*, *, *>.PrimaryViewHolder -> {
                bindPrimary(holder.binding as VB, item)
                holder.binding.root.setOnClickListener {
                    onItemClickListener?.invoke(item)
                }
            }
            is GenericRecyclerViewAdapter<*, *, *>.AltViewHolder -> {
                bindAlt(holder.binding as VBAlt, item)
                holder.binding.root.setOnClickListener {
                    onItemClickListener?.invoke(item)
                }
            }
        }
    }

    /**
     * ViewHolder for items using the primary layout.
     *
     * @param binding The ViewBinding for the primary layout.
     */
    inner class PrimaryViewHolder(val binding: VB) : RecyclerView.ViewHolder(binding.root)

    /**
     * ViewHolder for the first item, using an alternate layout.
     *
     * @param binding The ViewBinding for the alternate layout.
     */
    inner class AltViewHolder(val binding: VBAlt) : RecyclerView.ViewHolder(binding.root)
}
