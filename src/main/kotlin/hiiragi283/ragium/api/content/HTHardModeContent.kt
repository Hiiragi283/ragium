package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.material.HTMaterialProvider

fun interface HTHardModeContent {
    fun getContent(hardMode: Boolean): HTMaterialProvider

    companion object {
        @JvmStatic
        fun of(normal: HTMaterialProvider, hard: HTMaterialProvider): HTHardModeContent = HTHardModeContent {
            when (it) {
                true -> hard
                false -> normal
            }
        }
    }
}
