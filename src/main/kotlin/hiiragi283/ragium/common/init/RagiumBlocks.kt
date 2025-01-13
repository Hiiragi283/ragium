package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.extension.blockProperty
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineTierProvider
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.TransparentBlock
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumBlocks {
    //    Components    //

    enum class StorageBlocks(override val material: HTMaterialKey) : HTBlockContent.Material {
        RAGI_ALLOY(RagiumMaterialKeys.RAGI_ALLOY),
        RAGI_STEEL(RagiumMaterialKeys.RAGI_STEEL),
        ALUMINUM(RagiumMaterialKeys.ALUMINUM),
        FLUORITE(RagiumMaterialKeys.FLUORITE),
        STEEL(RagiumMaterialKeys.STEEL),
        RAGI_CRYSTAL(RagiumMaterialKeys.RAGI_CRYSTAL),
        REFINED_RAGI_STEEL(RagiumMaterialKeys.REFINED_RAGI_STEEL),
        CRYOLITE(RagiumMaterialKeys.CRYOLITE),
        DEEP_STEEL(RagiumMaterialKeys.DEEP_STEEL),
        RAGIUM(RagiumMaterialKeys.RAGIUM),
        ;

        override val holder: DeferredHolder<Block, Block> = HTContent.blockHolder("${name.lowercase()}_block")
        override val tagPrefix: HTTagPrefix = HTTagPrefix.STORAGE_BLOCK
    }

    enum class Grates(override val tier: HTMachineTier) :
        HTBlockContent,
        HTMachineTierProvider {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val holder: DeferredHolder<Block, Block> = HTContent.blockHolder("${name.lowercase()}_grate")
    }

    enum class Casings(override val tier: HTMachineTier) :
        HTBlockContent,
        HTMachineTierProvider {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val holder: DeferredHolder<Block, Block> = HTContent.blockHolder("${name.lowercase()}_casing")
    }

    enum class Hulls(override val tier: HTMachineTier) :
        HTBlockContent,
        HTMachineTierProvider {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val holder: DeferredHolder<Block, Block> = HTContent.blockHolder("${name.lowercase()}_hull")
    }

    enum class Coils(override val tier: HTMachineTier) :
        HTBlockContent,
        HTMachineTierProvider {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val holder: DeferredHolder<Block, Block> = HTContent.blockHolder("${name.lowercase()}_coil")
    }

    //    Register    //

    @JvmField
    val REGISTER: DeferredRegister.Blocks = DeferredRegister.createBlocks(RagiumAPI.MOD_ID)

    @JvmStatic
    internal fun register(bus: IEventBus) {
        // storage block
        StorageBlocks.entries.forEach { storage: StorageBlocks ->
            storage.registerBlock(REGISTER, blockProperty(Blocks.IRON_BLOCK))
            storage.registerBlockItem(RagiumItems.REGISTER)
        }
        // grate
        Grates.entries.forEach { grate: Grates ->
            grate.registerBlock(REGISTER, blockProperty(Blocks.COPPER_GRATE), ::TransparentBlock)
            grate.registerBlockItem(RagiumItems.REGISTER)
        }
        // casing
        Casings.entries.forEach { casings: Casings ->
            casings.registerBlock(REGISTER, blockProperty(Blocks.SMOOTH_STONE))
            casings.registerBlockItem(RagiumItems.REGISTER)
        }
        // hull
        Hulls.entries.forEach { hull: Hulls ->
            hull.registerBlock(REGISTER, blockProperty(Blocks.SMOOTH_STONE), ::TransparentBlock)
            hull.registerBlockItem(RagiumItems.REGISTER)
        }
        // coil
        Coils.entries.forEach { coil: Coils ->
            coil.registerBlock(REGISTER, blockProperty(Blocks.COPPER_BLOCK), ::RotatedPillarBlock)
            coil.registerBlockItem(RagiumItems.REGISTER)
        }

        REGISTER.register(bus)
    }
}
