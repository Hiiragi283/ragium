package hiiragi283.ragium.common.item

import com.mojang.serialization.Codec
import hiiragi283.ragium.common.Ragium
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.screen.*
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.TypedActionResult
import net.minecraft.util.function.ValueLists
import net.minecraft.world.World
import java.util.function.IntFunction

private fun openEnderChest(syncId: Int, playerInventory: PlayerInventory): ScreenHandler = GenericContainerScreenHandler.createGeneric9x3(
    syncId,
    playerInventory,
    playerInventory.player.enderChestInventory,
)

enum class HTPortableScreenType(private val factory: (Int, PlayerInventory) -> ScreenHandler, translationKey: String) :
    ItemConvertible,
    StringIdentifiable {
    ENDER_CHEST(::openEnderChest, "container.enderchest"),
    CRAFTING(::CraftingScreenHandler, "container.crafting"),
    GRINDER(::GrindstoneScreenHandler, "container.grindstone_title"),
    LOOM(::LoomScreenHandler, "container.loom"),
    SMITHING(::SmithingScreenHandler, "container.upgrade"),
    STONE_CUTTING(::StonecutterScreenHandler, "container.stonecutter"),
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTPortableScreenType> =
            StringIdentifiable.createCodec(HTPortableScreenType::values)

        @JvmField
        val INT_FUNCTION: IntFunction<HTPortableScreenType> =
            ValueLists.createIdToValueFunction(
                HTPortableScreenType::ordinal,
                HTPortableScreenType.entries.toTypedArray(),
                ValueLists.OutOfBoundsHandling.WRAP,
            )

        @JvmField
        val PACKET_CODEC: PacketCodec<ByteBuf, HTPortableScreenType> =
            PacketCodecs.indexed(INT_FUNCTION, HTPortableScreenType::ordinal)

        @JvmStatic
        fun init() {
            HTPortableScreenType.entries.forEach { type: HTPortableScreenType ->
                Registry.register(
                    Registries.ITEM,
                    Ragium.id("portable_${type.asString()}"),
                    type.asItem(),
                )
            }
        }
    }

    private val text: Text = Text.translatable(translationKey)
    private val screenHandlerFactory =
        SimpleNamedScreenHandlerFactory({ syncId: Int, playerInventory: PlayerInventory, _: PlayerEntity ->
            factory(syncId, playerInventory)
        }, text)

    //    ItemConvertible    //

    private val item = PortableScreenItem(this)

    override fun asItem(): Item = item

    //    StringIdentifiable    //

    override fun asString(): String = name.lowercase()

    //    PortableScreenItem    //

    private class PortableScreenItem(private val type: HTPortableScreenType) : Item(Settings()) {
        override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
            if (!world.isClient) {
                user.openHandledScreen(type.screenHandlerFactory)
            }
            return TypedActionResult.success(user.getStackInHand(hand), world.isClient)
        }
    }
}
