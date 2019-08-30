package com.sakuqi.viewpagerlibrary.holder

/**
 * 创建数据视图绑定的接口
 */
interface ViewPagerHolderCreator<VH : ViewPagerHolder<*>> {
    fun createViewHolder(): VH
}
