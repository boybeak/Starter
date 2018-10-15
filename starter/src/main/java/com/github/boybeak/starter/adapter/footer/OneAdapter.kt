package com.github.boybeak.starter.adapter.footer

import android.content.Context
import android.view.View
import com.github.boybeak.adapter.extension.Footer
import com.github.boybeak.adapter.extension.FooterAdapter

class OneAdapter(context: Context,
                 actionText: String?,
                 actionListener: View.OnClickListener?) : FooterAdapter(context, FooterLayout(Footer())) {

    init {
        footerLayout.actionText = actionText
        footerLayout.actionListener = actionListener
    }

    constructor(context: Context): this(context, null, null)

    override fun getFooterLayout(): FooterLayout {
        return super.getFooterLayout() as FooterLayout
    }

}