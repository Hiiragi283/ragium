package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.extension.blockLookup
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.DataMapProvider
import java.util.concurrent.CompletableFuture

@Suppress("DEPRECATION")
class RagiumDataMapProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>) :
    DataMapProvider(output, provider) {
    private lateinit var blockLookup: HolderGetter<Block>

    override fun gather(provider: HolderLookup.Provider) {
        blockLookup = provider.blockLookup()
    }
}
