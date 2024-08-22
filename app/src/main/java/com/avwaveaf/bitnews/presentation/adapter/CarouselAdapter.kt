import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.avwaveaf.bitnews.R
import com.avwaveaf.bitnews.data.models.Article
import com.avwaveaf.bitnews.databinding.CarouselItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


class CarouselAdapter(private val carouselItems: List<Article>) :
    RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    inner class CarouselViewHolder(val binding: CarouselItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding =
            CarouselItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val item = carouselItems[position]
        holder.binding.apply {
            Glide.with(carouselBackground.context)
                .load(item.urlToImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.absurd_design___chapter_1___34) // Add a placeholder image
                .error(R.drawable.absurd_design___chapter_1___34) // Add an error image (fallback)
                .into(carouselBackground)

            carouselNewsTitle.text = item.title
        }
    }

    override fun getItemCount(): Int = carouselItems.size
}

data class CarouselItem(val imageResId: Int, val title: String)