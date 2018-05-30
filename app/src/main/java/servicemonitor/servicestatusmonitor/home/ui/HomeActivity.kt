package servicemonitor.servicestatusmonitor.home.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.home_activity.*
import servicemonitor.servicestatusmonitor.R
import servicemonitor.servicestatusmonitor.databinding.AddServiceViewBinding
import servicemonitor.servicestatusmonitor.databinding.HomeActivityBinding
import servicemonitor.servicestatusmonitor.extentions.hideKeyboard
import servicemonitor.servicestatusmonitor.home.data.PollingService
import servicemonitor.servicestatusmonitor.home.ui.viewholders.ServicesViewHolder
import vn.tiki.noadapter2.OnlyAdapter
import java.util.*
import javax.inject.Inject


class HomeActivity @Inject constructor() : AppCompatActivity() {

    private lateinit var binding: HomeActivityBinding
    private lateinit var serviceAdapter: OnlyAdapter
    private lateinit var viewModel: HomeViewModel
    private var timer: Timer = Timer()

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, factory)[HomeViewModel::class.java]
        binding = DataBindingUtil.setContentView(this, R.layout.home_activity)

        setUpViews()

        viewModel.getServicesList().observe(this, Observer {
            it?.let {
                serviceAdapter.setItems(it)
                serviceAdapter.notifyDataSetChanged()
            }
        })

        // Runs Service
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runService()
            }
        }, 0, 1 * 60 * 1000)

        fab.setOnClickListener { _ ->
            showAddNewServiceDialog()
        }
    }

    private fun setUpViews() {
        setUpAdapter()
        binding.recyclerView.adapter = serviceAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.isNestedScrollingEnabled = true

    }

    private fun setUpAdapter() {
        serviceAdapter = OnlyAdapter.Builder()
                .viewHolderFactory { parent, _ -> ServicesViewHolder.create(parent) }
                .build()
    }

    private fun showAddNewServiceDialog() {
        val dialogBinding = AddServiceViewBinding.inflate(layoutInflater)
        dialogBinding.viewModel = viewModel

        val dialog = AlertDialog.Builder(this, R.style.AlertDialogTheme)
                .setTitle(getString(R.string.dialogue_title))
                .setView(dialogBinding.root)
                .setPositiveButton(R.string.done) { _, _ ->
                }
                .setCancelable(false)
                .setNegativeButton(R.string.cancel) { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }
                .create()
        dialog.setOnShowListener {
            val button = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            button.setOnClickListener {
                viewModel.addService()
                dialog.hideKeyboard()
                dialog.dismiss()
            }
        }
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun runService() {
        startService(Intent(this, PollingService::class.java))
    }

    override fun onPause() {
        stopService(Intent(this, PollingService::class.java))
        timer.cancel()
        super.onPause()
    }

    /*  ///////////////////////////////////// Delete Handler

    private inner class TouchHelperCallback : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            //viewModel.deleteItemAsync(viewHolder.itemId)
        }

        override fun isLongPressDragEnabled(): Boolean {
            return true
        }
    }*/
}
