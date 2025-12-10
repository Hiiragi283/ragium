package hiiragi283.ragium.api.data

import net.minecraft.data.DataProvider

/**
 * [DataProvider]を登録するインターフェース
 * @see HTRootDataGenerator
 * @see HTDataPackGenerator
 */
interface HTDataGenerator {
    fun <DATA : DataProvider> addProvider(factory: DataProvider.Factory<DATA>): DATA

    fun <DATA : DataProvider> addProvider(factory: Factory<DATA>): DATA

    fun interface Factory<DATA : DataProvider> {
        fun create(context: HTDataGenContext): DATA
    }
}
