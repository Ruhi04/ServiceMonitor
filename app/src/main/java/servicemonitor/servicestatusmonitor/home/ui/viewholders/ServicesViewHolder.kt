package servicemonitor.servicestatusmonitor.home.ui.viewholders

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import servicemonitor.servicestatusmonitor.R
import servicemonitor.servicestatusmonitor.databinding.ServiceItemBinding
import servicemonitor.servicestatusmonitor.home.data.models.ServiceToCheck
import vn.tiki.noadapter2.AbsViewHolder
import javax.inject.Inject


class ServicesViewHolder @Inject constructor(
        private val binding: ServiceItemBinding
) : AbsViewHolder(binding.root) {
    companion object {
        @JvmStatic
        fun create(parent: ViewGroup): AbsViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding: ServiceItemBinding = DataBindingUtil.inflate(
                    inflater, R.layout.service_item, parent, false
            )
            return ServicesViewHolder(binding)
        }
    }

    override fun bind(item: Any?) {
        super.bind(item)

        if (item is ServiceToCheck) {
            binding.title.text = item.serviceName
            binding.label.text = item.serviceUrl
            binding.status.text = item.status
            binding.time.text = item.lastChecked
        }

    }
}
