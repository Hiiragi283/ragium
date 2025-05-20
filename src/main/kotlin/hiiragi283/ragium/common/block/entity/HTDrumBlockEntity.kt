package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.HTFluidTankHandler
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumComponentTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponentMap
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
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
        tank.writeNbt(nbt, registryOps)
    }

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        tank.readNbt(nbt, registryOps)
    }

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        super.applyImplicitComponents(componentInput)
        tank.replace(
            componentInput.getOrDefault(RagiumComponentTypes.FLUID_CONTENT, SimpleFluidContent.EMPTY).copy(),
            true,
        )
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        components.set(RagiumComponentTypes.FLUID_CONTENT, SimpleFluidContent.copyOf(tank.stack))
    }

    override fun loadEnchantment(newEnchantments: ItemEnchantments) {
        super.loadEnchantment(newEnchantments)
        tank.onUpdateEnchantment(newEnchantments)
    }

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
