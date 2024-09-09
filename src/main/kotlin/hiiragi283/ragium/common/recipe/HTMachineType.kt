package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.block.HTMultiMachineBlock
import hiiragi283.ragium.common.block.HTSingleMachineBlock
import hiiragi283.ragium.common.block.entity.HTMultiMachineBlockEntity
import hiiragi283.ragium.common.block.entity.HTSingleMachineBlockEntity
import hiiragi283.ragium.common.energy.HTRagiPower
import hiiragi283.ragium.common.screen.HTMachineScreenHandler
import io.netty.buffer.ByteBuf
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.resource.featuretoggle.FeatureFlags
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.function.ValueLists
import java.util.function.IntFunction

enum class HTMachineType(
    val tier: HTRagiPower,
    private val variant: Variant = Variant.SINGLE,
) : RecipeType<HTMachineRecipe>, ItemConvertible, StringIdentifiable {
    GRINDER(HTRagiPower.STEAM),
    ALLOY_FURNACE(HTRagiPower.STEAM),
    ;

    enum class Variant(
        val blockFactory: (HTMachineType) -> Block,
        blockEntityFactory: BlockEntityType.BlockEntityFactory<*>,
    ) {
        SINGLE(::HTSingleMachineBlock, ::HTSingleMachineBlockEntity),
        MULTI(::HTMultiMachineBlock, ::HTMultiMachineBlockEntity),
        ;

        val blockEntityType: BlockEntityType<*> = BlockEntityType.Builder.create(blockEntityFactory).build()
    }

    companion object {
        @JvmField
        val CODEC: StringIdentifiable.EnumCodec<HTMachineType> = StringIdentifiable.createCodec(HTMachineType::values)

        @JvmField
        val INT_FUNCTION: IntFunction<HTMachineType> = ValueLists.createIdToValueFunction(
            HTMachineType::ordinal, entries.toTypedArray(), ValueLists.OutOfBoundsHandling.WRAP
        )

        @JvmField
        val PACKET_CODEC: PacketCodec<ByteBuf, HTMachineType> =
            PacketCodecs.indexed(INT_FUNCTION, HTMachineType::ordinal)

        @JvmField
        val SCREEN_HANDLER_TYPE: ScreenHandlerType<HTMachineScreenHandler> =
            ScreenHandlerType({ syncId: Int, playerInventory: PlayerInventory ->
                HTMachineScreenHandler(syncId, playerInventory, ScreenHandlerContext.EMPTY)
            }, FeatureFlags.VANILLA_FEATURES)

        @JvmStatic
        fun init() {
            // ScreenHandlerType
            Registry.register(Registries.SCREEN_HANDLER, Ragium.id("generic"), SCREEN_HANDLER_TYPE)
            // BlockEntityType
            Variant.entries.forEach { variant ->
                Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    Ragium.id(variant.name.lowercase()),
                    variant.blockEntityType
                )
            }
            HTMachineType.entries.forEach { type: HTMachineType ->
                val id: Identifier = type.id
                val block: Block = type.block
                // Machine Block
                Registry.register(Registries.BLOCK, id, block)
                // BlockItem
                Registry.register(Registries.ITEM, id, BlockItem(block, Item.Settings()))
                // RecipeType
                Registry.register(Registries.RECIPE_TYPE, id, type)
                // Bind block with BET
                type.variant.blockEntityType.addSupportedBlock(block)
            }
        }
    }

    val id: Identifier = Ragium.id(asString())

    val block: Block = variant.blockFactory(this)
    val blockEntityType: BlockEntityType<*> by variant::blockEntityType

    val translationKey = "machine_type.${name.lowercase()}"
    val text: Text = Text.translatable(translationKey, name)

    //    ItemConvertible    //

    override fun asItem(): Item = block.asItem()

    //    StringIdentifiable    //

    override fun asString(): String = name.lowercase()
}