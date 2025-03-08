package hiiragi283.ragium.data.server.advancement

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTAdvancementGenerator
import hiiragi283.ragium.api.data.HTDisplayInfoBuilder
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component

object HTTierThreeAdvancements : HTAdvancementGenerator("tier3/") {
    override fun createRoot(): AdvancementHolder = create("root") {
        display {
            setIcon(HTTagPrefix.INGOT, RagiumMaterials.DURALUMIN)
            title = Component.literal("Ragium - Tier 3")
            backGround = RagiumAPI.id("textures/block/aluminum_casing.png")
            showToast = false
            showChat = false
        }
        hasItem("has_frame", RagiumBlocks.PRECISION_MACHINE_FRAME)
    }

    override fun generate(registries: HolderLookup.Provider) {
        // Brewery
        val brewery: AdvancementHolder = createMachine(root, HTMachineType.BREWERY, HTDisplayInfoBuilder::setGoal)
        // Enchanter
        val enchanter: AdvancementHolder = createMachine(root, HTMachineType.ENCHANTER, HTDisplayInfoBuilder::setGoal)
        // Laser Assembly
        val laser: AdvancementHolder = createMachine(root, HTMachineType.LASER_ASSEMBLY)

        val redstoneLens: AdvancementHolder = createSimple(laser, RagiumItems.REDSTONE_LENS)
        val glowstoneLens: AdvancementHolder = createSimple(laser, RagiumItems.GLOWSTONE_LENS)
        val diamondLens: AdvancementHolder = createSimple(laser, RagiumItems.DIAMOND_LENS)
        val emeraldLens: AdvancementHolder = createSimple(laser, RagiumItems.EMERALD_LENS)
        val amethystLens: AdvancementHolder =
            createSimple(laser, RagiumItems.AMETHYST_LENS, HTDisplayInfoBuilder::setChallenge)

        val ragium: AdvancementHolder =
            createMaterial(amethystLens, HTTagPrefix.INGOT, RagiumMaterials.RAGIUM, HTDisplayInfoBuilder::setChallenge)
        // Multi Smelter
        val smelter: AdvancementHolder = createMachine(root, HTMachineType.MULTI_SMELTER, HTDisplayInfoBuilder::setGoal)
    }
}
