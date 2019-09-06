package com.sakuqi.recyclerviewlibrary.utils

import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType

object PinYinUtil {
    fun getPinYinAllFirstString(string: String):String{
        if (string.isEmpty()) {
            return ""
        }
        val stringBuffer = StringBuffer()
        val defaultFormat = HanyuPinyinOutputFormat()
        defaultFormat.caseType = HanyuPinyinCaseType.UPPERCASE
        defaultFormat.toneType = HanyuPinyinToneType.WITHOUT_TONE
        string.forEach {
            if(it.toInt()>128){
                try {
                    stringBuffer.append(
                        PinyinHelper.toHanyuPinyinStringArray(
                            it,
                            defaultFormat
                        ).first().first()
                    )
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }else{
                stringBuffer.append(it.toUpperCase())
            }
        }
        return stringBuffer.toString()
    }

    fun getPinYinFirstString(string: String):String{
        if (string.isEmpty()) {
            return ""
        }
        val defaultFormat = HanyuPinyinOutputFormat()
        defaultFormat.caseType = HanyuPinyinCaseType.UPPERCASE
        defaultFormat.toneType = HanyuPinyinToneType.WITHOUT_TONE
        val pinyinArray = PinyinHelper.toHanyuPinyinStringArray(string.first(),defaultFormat)
        if(pinyinArray.isNullOrEmpty()){
            return string.first().toString().toUpperCase()
        }else{
            return pinyinArray.first().first().toString()
        }
    }

}
