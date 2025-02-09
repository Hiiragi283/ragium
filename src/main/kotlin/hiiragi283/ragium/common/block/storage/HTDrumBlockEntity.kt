package hiiragi283.ragium.common.block.storage

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.block.entity.HTBlockEntityHandlerProvider
import hiiragi283.ragium.api.block.entity.HTEnchantableBlockEntity
import hiiragi283.ragium.api.capability.HTHandlerSerializer
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.extension.getLevel
import hiiragi283.ragium.api.fluid.HTMachineFluidTank
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
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

class HTDrumBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntity(RagiumBlockEntityTypes.DRUM, pos, state),
    HTBlockEntityHandlerProvider,
    HTEnchantableBlockEntity {
    private val fluidTank: HTMachineFluidTank = RagiumAPI.getInstance().createTank(this::setChanged)
    private val serializer: HTHandlerSerializer = HTHandlerSerializer.ofFluid(listOf(fluidTank))

    override fun writeNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        serializer.writeNbt(nbt, dynamicOps)
        ItemEnchantments.CODEC
            .encodeStart(dynamicOps, enchantments)
            .ifSuccess { nbt.put(ENCH_KEY, it) }
    }

    override fun readNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        serializer.readNbt(nbt, dynamicOps)
        ItemEnchantments.CODEC
            .parse(dynamicOps, nbt.get(ENCH_KEY))
            .ifSuccess(::updateEnchantments)
    }

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        super.applyImplicitComponents(componentInput)
        // Fluid
        val content: SimpleFluidContent =
            componentInput.getOrDefault(RagiumComponentTypes.FLUID_CONTENT, SimpleFluidContent.EMPTY)
        fluidTank.fluid = content.copy()
        // Enchantment
        val enchantments: ItemEnchantments = componentInput.get(DataComponents.ENCHANTMENTS) ?: return
        updateEnchantments(enchantments)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        // Fluid
        components.set(RagiumComponentTypes.FLUID_CONTENT, SimpleFluidContent.copyOf(fluidTank.fluid))
        // Enchantment
        if (!enchantments.isEmpty) {
            components.set(DataComponents.ENCHANTMENTS, enchantments)
        }
    }

    override fun onRightClickedWithItem(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): ItemInteractionResult = when {
        fluidTank.interactWithFluidStorage(player, HTStorageIO.GENERIC) -> ItemInteractionResult.SUCCESS

        else -> super.onRightClickedWithItem(stack, state, level, pos, player, hand, hitResult)
    }

    //    HTBlockEntityHandlerProvider    //

    override fun getFluidHandler(direction: Direction?): HTMachineFluidTank = fluidTank

    //    HTEnchantableBlockEntity    //

    override var enchantments: ItemEnchantments = ItemEnchantments.EMPTY

    override fun getEnchantmentLevel(key: ResourceKey<Enchantment>): Int {
        val lookup: HolderLookup.RegistryLookup<Enchantment> =
            level?.registryAccess()?.lookupOrThrow(Registries.ENCHANTMENT) ?: return 0
        return enchantments.getLevel(lookup, key)
    }

    override fun updateEnchantments(newEnchantments: ItemEnchantments) {
        this.enchantments = newEnchantments
        fluidTank.updateCapacity(this)
    }
}
