package hiiragi283.ragium.api.data

import net.minecraft.core.HolderLookup
import net.minecraft.data.DataGenerator
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

data class HTDataPackGenerator(
    private val generator: DataGenerator.PackGenerator,
    val registries: CompletableFuture<HolderLookup.Provider>,
    val fileHelper: ExistingFileHelper,
) : HTDataGenerator {
    override fun <DATA : DataProvider> addProvider(run: Boolean, factory: DataProvider.Factory<DATA>): DATA = generator.addProvider(factory)

    override fun <DATA : DataProvider> addProvider(run: Boolean, factory: HTDataGenerator.Factory<DATA>): DATA =
        addProvider(run) { output: PackOutput -> factory.create(HTDataGenContext(output, registries, fileHelper)) }
}
