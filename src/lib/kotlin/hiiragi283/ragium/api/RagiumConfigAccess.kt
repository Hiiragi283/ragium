package hiiragi283.ragium.api

import net.minecraft.resources.Identifier

/**
 * Ragiumのコンフィグへにアクセスするインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.10
 */
interface RagiumConfigAccess {
    companion object {
        /**
         * [RagiumConfigAccess]のインスタンス
         */
        @JvmField
        val INSTANCE: RagiumConfigAccess = RagiumAPI.getService()
    }

    val modIdComparator: Comparator<Identifier>
}
