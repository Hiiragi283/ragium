package hiiragi283.ragium.data.server.advancement

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTAdvancementGenerator
import hiiragi283.ragium.api.data.HTDisplayInfoBuilder
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component

object HTTierOneAdvancements : HTAdvancementGenerator("tier1/") {
    override fun createRoot(): AdvancementHolder = create("root") {
        display {
            setIcon(HTTagPrefix.INGOT, CommonMaterials.STEEL)
            title = Component.literal("Ragium - Tier 1")
            backGround = RagiumAPI.id("textures/block/ragi_alloy_casing.png")
            showToast = false
            showChat = false
        }
        hasItem("has_frame", RagiumBlocks.MACHINE_FRAME)
    }

    override fun generate(registries: HolderLookup.Provider) {
        // Alloy Furnace
        val alloyFurnace: AdvancementHolder = createMachine(root, HTMachineType.ALLOY_FURNACE)

        val deepSteel: AdvancementHolder = createMaterial(alloyFurnace, HTTagPrefix.INGOT, RagiumMaterials.DEEP_STEEL)
        val frame: AdvancementHolder =
            createSimple(deepSteel, RagiumBlocks.CHEMICAL_MACHINE_FRAME, HTDisplayInfoBuilder::setChallenge)

        val emberAlloy: AdvancementHolder = createMaterial(alloyFurnace, HTTagPrefix.INGOT, RagiumMaterials.EMBER_ALLOY)

        val ragiCrystal: AdvancementHolder =
            createMaterial(alloyFurnace, HTTagPrefix.GEM, RagiumMaterials.RAGI_CRYSTAL, HTDisplayInfoBuilder::setGoal)
        val ragiLantern: AdvancementHolder = createSimpleConsume(ragiCrystal, RagiumItems.RAGI_LANTERN)
        // Assembler
        val assembler: AdvancementHolder = createMachine(root, HTMachineType.ASSEMBLER)

        val basicCircuit: AdvancementHolder = createSimple(assembler, RagiumItems.BASIC_CIRCUIT)
        val advancedCircuit: AdvancementHolder = createSimple(basicCircuit, RagiumItems.ADVANCED_CIRCUIT)
        val eliteCircuit: AdvancementHolder =
            createSimple(advancedCircuit, RagiumItems.ELITE_CIRCUIT, HTDisplayInfoBuilder::setGoal)
        // Auto Chisel
        val autoChisel: AdvancementHolder = createMachine(root, HTMachineType.AUTO_CHISEL)
        // Compressor
        val compressor: AdvancementHolder = createMachine(root, HTMachineType.COMPRESSOR)

        val meatIngot: AdvancementHolder = createSimple(compressor, RagiumItems.MEAT_INGOT)
        val cannedMeat: AdvancementHolder = createSimple(meatIngot, RagiumItems.CANNED_COOKED_MEAT)
        val meatSandwich: AdvancementHolder = createSimple(meatIngot, RagiumItems.MEAT_SANDWICH)
        // Fisher
        val fisher: AdvancementHolder = createMachine(root, HTMachineType.FISHER)
        // Grinder
        val grinder: AdvancementHolder = createMachine(root, HTMachineType.GRINDER)

        val flour: AdvancementHolder = createSimple(grinder, RagiumItems.FLOUR)
        val spongeCake: AdvancementHolder = createSimple(flour, RagiumBlocks.SPONGE_CAKE)

        val warpedWart: AdvancementHolder = createSimple(grinder, RagiumItems.WARPED_WART)
    }
}
