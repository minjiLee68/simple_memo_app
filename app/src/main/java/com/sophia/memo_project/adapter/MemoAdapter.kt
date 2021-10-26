package com.sophia.memo_project.adapter

import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sophia.memo_project.R
import com.sophia.memo_project.R.color.colorLightBlack
import com.sophia.memo_project.databinding.ItemTodoBinding
import com.sophia.memo_project.entity.MemoEntity
import java.util.*

class MemoAdapter(
    private val listener: OnItemClickListener,
) : ListAdapter<MemoEntity, MemoViewHolder>(

    object : DiffUtil.ItemCallback<MemoEntity>() {
        override fun areItemsTheSame(oldItem: MemoEntity, newItem: MemoEntity): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MemoEntity, newItem: MemoEntity): Boolean =
            oldItem.title == newItem.title && oldItem.content == newItem.content
                    && oldItem.date == newItem.date
    }

) {

    interface OnItemClickListener {
        fun setOnClickListener(position: Int)
        fun setOnLongClickListener(position: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder =
        MemoViewHolder(
            ItemTodoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), listener
        )

    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
        holder.bind(getItem(position
        ))
    }

}

class MemoViewHolder(
    private val binding: ItemTodoBinding,
    listener: MemoAdapter.OnItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(memo: MemoEntity) {
        binding.run {
            tvTitle.text = memo.title
            tvDesc.text = memo.content
            tvDateTime.text = memo.date

            val random = Random()
            val color =
                Color.argb(
                    255, random.nextInt(256),
                    random.nextInt(256), random.nextInt(256)
                )
            ibColor.setBackgroundColor(color)
        }
    }

    init {

        binding.layoutNote.let {
            it.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.setOnClickListener(adapterPosition)
                }
            }
            it.setOnLongClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.setOnLongClickListener(adapterPosition)
                }
                return@setOnLongClickListener true

            }
        }
    }

}
