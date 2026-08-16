package hiiragi283.lib.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.block.entity.BlockEntity

/**
 * [SE][SoundEvent]を再生可能な[BlockEntity]に実装するインターフェースです。
 *
 * 参考 : [Mekanism - ITileSound](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/common/tile/interfaces/ITileSound.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTSoundPlayerBlockEntity : HTAbstractBlockEntity {
    /**
     * SEを鳴らす座標を取得します。
     */
    fun getSoundPos(): BlockPos = getBlockPos()

    /**
     * SEの音源の種類を指定します。
     */
    fun getSoundSource(): SoundSource = SoundSource.BLOCKS

    /**
     * SEを鳴らします。
     * @param sound SEの種類
     * @param volume SEの音量
     * @param pitch SEの高低
     */
    fun playSound(sound: SoundEvent, volume: Float = 1f, pitch: Float = 1f) {
        getLevelResult().onRight { it.playSound(null, getSoundPos(), sound, getSoundSource(), volume, pitch) }
    }
}
