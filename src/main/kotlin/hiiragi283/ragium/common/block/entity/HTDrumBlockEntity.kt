package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.enchantment.HTEnchantmentEntry
import hiiragi283.ragium.api.enchantment.HTEnchantmentHolder
import hiiragi283.ragium.api.extension.enchLookup
import hiiragi283.ragium.api.extension.getData
import hiiragi283.ragium.api.extension.putData
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.HTFluidTankHandler
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumComponentTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.resources.ResourceKey
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.fluids.SimpleFluidContent

abstract class HTDrumBlockEntity(
    capacity: Int,
    type: HTDeferredBlockEntityType<*>,
    pos: BlockPos,
    state: BlockState,
) : HTBlockEntity(type, pos, state),
    HTEnchantmentHolder,
    HTFluidTankHandler {
    private val tank: HTFluidTank = HTFluidTank.create("tank", this) {
        this.capacity = capacity
    }

    override fun onRightClickedWithItem(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): ItemInteractionResult = interactWith(level, player, hand)

    //    Save & Load    //

    override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        if (!itemEnchantments.isEmpty) {
            nbt.putData(RagiumConstantValues.ENCHANTMENT, itemEnchantments, ItemEnchantments.CODEC, registryOps)
        }
        tank.writeNbt(nbt, registryOps)
    }

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        nbt
            .getData(RagiumConstantValues.ENCHANTMENT, ItemEnchantments.CODEC, registryOps)
            .result()
            .orElse(ItemEnchantments.EMPTY)
            .let(::loadEnchantment)
        tank.readNbt(nbt, registryOps)
    }

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        loadEnchantment(componentInput.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY))
        tank.replace(
            componentInput.getOrDefault(RagiumComponentTypes.FLUID_CONTENT, SimpleFluidContent.EMPTY).copy(),
            true,
        )
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        components.set(DataComponents.ENCHANTMENTS, itemEnchantments)
        components.set(RagiumComponentTypes.FLUID_CONTENT, SimpleFluidContent.copyOf(tank.stack))
    }

    //    HTEnchantmentHolder    //

    private var itemEnchantments: ItemEnchantments = ItemEnchantments.EMPTY

    private fun loadEnchantment(newEnchantments: ItemEnchantments) {
        this.itemEnchantments = newEnchantments
        tank.onUpdateEnchantment(newEnchantments)
    }

    override fun getEnchLevel(key: ResourceKey<Enchantment>): Int {
        val lookup: HolderLookup.RegistryLookup<Enchantment> = level?.registryAccess()?.enchLookup() ?: return 0
        return lookup.get(key).map(itemEnchantments::getLevel).orElse(0)
    }

    override fun getEnchEntries(): Iterable<HTEnchantmentEntry> = itemEnchantments.entrySet().map(::HTEnchantmentEntry)

    //    Fluid    //

    override fun getFluidIoFromSlot(tank: Int): HTStorageIO = HTStorageIO.GENERIC

    override fun getFluidTank(tank: Int): HTFluidTank = this.tank

    override fun getTanks(): Int = 1

    //    Impl    //

    class Small(pos: BlockPos, state: BlockState) :
        HTDrumBlockEntity(RagiumConstantValues.SMALL_DRUM, RagiumBlockEntityTypes.SMALL_DRUM, pos, state)

    class Medium(pos: BlockPos, state: BlockState) :
        HTDrumBlockEntity(RagiumConstantValues.MEDIUM_DRUM, RagiumBlockEntityTypes.MEDIUM_DRUM, pos, state)

    class Large(pos: BlockPos, state: BlockState) :
        HTDrumBlockEntity(RagiumConstantValues.LARGE_DRUM, RagiumBlockEntityTypes.LARGE_DRUM, pos, state)

    class Huge(pos: BlockPos, state: BlockState) :
        HTDrumBlockEntity(RagiumConstantValues.HUGE_DRUM, RagiumBlockEntityTypes.HUGE_DRUM, pos, state)
}
