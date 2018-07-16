package com.github.boybeak.starter.app

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.github.boybeak.permission.Callback
import com.github.boybeak.permission.PH
import com.github.boybeak.selector.Operator
import com.github.boybeak.selector.Path
import com.github.boybeak.starter.adapter.Converter
import com.github.boybeak.starter.adapter.DataBindingAdapter
import com.github.boybeak.starter.app.adapter.PermissionImpl
import kotlinx.android.synthetic.main.activity_permission.*

class PermissionActivity : AppCompatActivity() {

    private var adapter: DataBindingAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        val permissionList = listOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)

        adapter = DataBindingAdapter(this)
        recycler_view.adapter = adapter
        adapter!!.addAll(permissionList, object : Converter<String, PermissionImpl> {
            override fun convert(data: String?, adapter: DataBindingAdapter?): PermissionImpl {
                return PermissionImpl(data)
            }

        }).autoNotify()

    }

    fun checkPermission(view: View) {
        val plist = adapter!!.dataSelector(PermissionImpl::class.java)
                .where(Path.with(PermissionImpl::class.java, Boolean::class.java).methodWith("isChecked"), Operator.OPERATOR_EQUAL, true)
                .findAll()
        if (plist.isEmpty()) {
            Toast.makeText(this, "check some permission first", Toast.LENGTH_SHORT).show()
            return
        }
        val parray = Array(plist.size){
            plist[it].source
        }

        PH.ask(*parray).go(this, object : Callback {
            override fun onGranted(permissions: MutableList<String>) {

            }

            override fun onDenied(permission: String) {

            }

        })
    }
}
