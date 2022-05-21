package com.example.vrg_soft_test_task.presentation.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vrg_soft_test_task.R
import com.example.vrg_soft_test_task.databinding.ItemPublicationBinding
import com.example.vrg_soft_test_task.domain.models.Child
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.*
import java.util.concurrent.TimeUnit

interface ClickOnTheImg {
    fun imgPress(imgUrl: String)
}

interface ClickOnTheSaveImg {
    fun saveImgPress(imgUrl: String)
}

class TopPublicationRvAdapter(
    val clickOnTheImg: ClickOnTheImg,
    val clickOnTheSaveImg: ClickOnTheSaveImg) :
    ListAdapter<Child, TopPublicationRvAdapter.ItemViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPublicationBinding.inflate(inflater, parent, false)

        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewHolder(private val binding: ItemPublicationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(publicationItem: Child) {

            with(binding) {
                val time = convertToGmt(publicationItem.data.createdUTC)
                val numComments = publicationItem.data.numComments

                authorNameTxt.text = publicationItem.data.authorFullName
                createdTimeTxt.text = time
                titleTxt.text = publicationItem.data.title

                Glide.with(imgPublication.context)
                    .load(publicationItem.data.thumbnail)
                    .error(R.drawable.empty_img)
                    .into(imgPublication)

                numCommentsTxt.text = "$numComments comments"

                imgPublication.setOnClickListener {
                    if (!publicationItem.data.thumbnail.isNullOrEmpty() && !publicationItem.data.url.isNullOrEmpty())
                        clickOnTheImg.imgPress(publicationItem.data.url)
                }

                saveImgBtn.setOnClickListener {
                    clickOnTheSaveImg.saveImgPress(publicationItem.data.url)
                }

            }
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Child>() {

    override fun areItemsTheSame(
        oldItem: Child,
        newItem: Child
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: Child,
        newItem: Child
    ): Boolean {
        return oldItem == newItem
    }
}

@SuppressLint("NewApi")
fun convertToGmt(dateTime: Double): String {
    val timeToLong = dateTime.toLong()
    val localTime = Instant.ofEpochSecond(timeToLong)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    val format = SimpleDateFormat("yyyy-MM-dd")
    val past = format.parse(localTime.toString())
    val now = Date()

    val minutesL = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
    val hoursL = TimeUnit.MILLISECONDS.toHours(now.time - past.time)

    val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time).toString() + " minutes ago"
    val hours = TimeUnit.MILLISECONDS.toHours(now.time - past.time).toString() + " hours ago"
    val days = TimeUnit.MILLISECONDS.toDays(now.time - past.time).toString() + " days ago"

    return if (minutesL < 59 && hoursL == 0L)
        minutes
    else if (minutesL > 59 && hoursL < 24)
        hours
    else
        days
}

