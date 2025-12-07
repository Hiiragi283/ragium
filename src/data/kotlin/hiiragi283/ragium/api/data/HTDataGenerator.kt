package hiiragi283.ragium.api.data

import net.minecraft.data.DataProvider

interface HTDataGenerator {
    fun <DATA : DataProvider> addProvider(run: Boolean, factory: DataProvider.Factory<DATA>): DATA

    fun <DATA : DataProvider> addProvider(run: Boolean, factory: Factory<DATA>): DATA

    fun interface Factory<DATA : DataProvider> {
        fun create(context: HTDataGenContext): DATA
    }
}
