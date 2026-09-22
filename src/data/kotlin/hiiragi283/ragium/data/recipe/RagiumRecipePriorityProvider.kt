package hiiragi283.ragium.data.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConstants
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.RecipePrioritiesProvider
import java.util.concurrent.CompletableFuture

class RagiumRecipePriorityProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) :
    RecipePrioritiesProvider(output, registries, RagiumAPI.MOD_ID) {
    override fun start() {
        // Heat
        add("${RagiumConstants.MELTING}/molten_glass_from_panes", 100)
        // Electronics
        add("${RagiumConstants.RESOURCE_EXTRACTING}/water_at_ocean", 200)
        add("${RagiumConstants.RESOURCE_EXTRACTING}/water_at_river", 100)

        add("${RagiumConstants.RESOURCE_EXTRACTING}/crude_oil_at_soul_sand_valley", 100)
    }
}
